package com.example.communityfunction.tool;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * 
 * @author Administrator
 * 图片加载类
 */
public class PostBBSImageLoader {

    public static PostBBSImageLoader mInstance;

    /**
     * 图片缓存的核心对象
     */
    private LruCache<String, Bitmap> mLruCache;
    /**
     * 线程池
     */
    private ExecutorService mThreadPool;
    private static final int DEAFULT_THREAD_COUNT = 1;

    private Type mType = Type.LIFO;

    /**
     * 任务队列
     * 里面是链表
     */
    private LinkedList<Runnable> mTaskQuequ;

    /**
     * 后台轮询线程
     */
    private Thread mPoolThread;
    private Handler mPoolThreadHandler;

    /**
     * UI线程中的Handler
     */
    private Handler mUIHandler;

    private Semaphore mPoolThreadHandlerSemaphore = new Semaphore(0);
    private Semaphore mSemaphoreThreadPool;
    public enum Type {
        FIFO,LIFO
    }
    public PostBBSImageLoader(int threadCount, Type type) {
        init(threadCount,type);
    }

    /**
     * 初始化
     * @param mThreadCount
     * @param type
     */
    @SuppressLint("HandlerLeak")
    private void init(int threadCount, Type type) {
        //后台轮询线程
        mPoolThread = new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                mPoolThreadHandler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        //通过线程池取出任务去执行
                        mThreadPool.execute(getTask());

                        try {
                            mSemaphoreThreadPool.acquire();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                };
                //释放一个信号量
                mPoolThreadHandlerSemaphore.release();
                Looper.loop();
            }

        };
        mPoolThread.start();

        //获取应用的最大可用内存 
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheMemory = maxMemory/8;

        mLruCache = new LruCache<String,Bitmap>(cacheMemory){
            @Override
            protected int sizeOf(String key, Bitmap value) {//sizeOf的作用是计算缓存对象的大小
                return value.getRowBytes() * value.getHeight();
            }
        };

        //创建线程池
        mThreadPool = Executors.newFixedThreadPool(threadCount);
        mTaskQuequ = new LinkedList<Runnable>();
        mType = type;

        mSemaphoreThreadPool = new Semaphore(threadCount);
    }

    /**
     * 从任务队列取出一个方法
     * @return
     */
    private Runnable getTask() {
        if (mType == Type.FIFO) {
            return mTaskQuequ.removeFirst();
        } else if (mType == Type.LIFO){
            return mTaskQuequ.removeLast();
        }
        return null;
    }

    //单例模式
    public static PostBBSImageLoader getInstance(int threadCount,Type tpye) {
        //没有做同步处理,为了提高效率
        if (mInstance == null) {
            synchronized (PostBBSImageLoader.class) {

                if (mInstance == null) {
                    mInstance = new PostBBSImageLoader(threadCount, tpye);
                }
            }
        }

        return mInstance;
    }

    /**
     * g根据path为ImageView设置图片
     * @param path
     * @param imageView
     */
    @SuppressLint("HandlerLeak")
    public void loadImage(final String path,final ImageView imageView) {
        imageView.setTag(path);
        if (mUIHandler == null) {
            mUIHandler = new Handler() {
                public void handleMessage(Message msg) {
                    //获取得到的图片,为imageview回调设置图片
                	
                    ImgBeanHolder holder = (ImgBeanHolder) msg.obj;
                    Bitmap bm = holder.bitmap;
                    ImageView imageView = holder.imageView;
                    String path = holder.Path;

                    //将Path与getTag存储路径进行比较,避免因为imageView的复导致错误。
                    if (imageView.getTag().toString().equals(path)) {
                        imageView.setImageBitmap(bm);
                    }
                }
            };
        }

        //根据Path在缓存中获取bitmap
        Bitmap bm = getBitmapFromLruCache(path);
        if (bm != null) {
            refreshBitmap(path, imageView, bm);
        } else {
            addTasks(new Runnable() {

                @Override
                public void run() {
                    // 加载图片
                    // 图片的压缩
                    // 1、获得图片需要显示的大小
                    ImageSize imageSize = getImageViewSize(imageView);
                    // 2、压缩图片
                    Bitmap bm = decodeSampledBitmapFromPath(path, imageSize.width, imageSize.height);
                    // 3、把图片加入到缓存
                    addBitmapToLruCache(path, bm);

                    //回调
                    refreshBitmap(path, imageView, bm);

                    mSemaphoreThreadPool.release();

                }
            });
        }
    }

    private void refreshBitmap(final String path, final ImageView imageView,
            Bitmap bm) {
        Message message = Message.obtain();
        ImgBeanHolder holder = new ImgBeanHolder();
        holder.bitmap = bm;
        holder.imageView = imageView;
        holder.Path = path;
        message.obj = holder;
        mUIHandler.sendMessage(message);
    }

    /**
     *将图片加入LruCache 
     * @param pathm
     * @param bm
     */
    protected void addBitmapToLruCache(String path, Bitmap bm) {
        if (getBitmapFromLruCache(path) == null) {
            if (bm != null) {
                mLruCache.put(path, bm);
            }
        }

    }

    /**
     * 根据图片需要显示的宽和高对图片进行压缩
     * @param path
     * @param width
     * @param height
     * @return
     */
    protected Bitmap decodeSampledBitmapFromPath(String path, int width,
            int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//获取图片的宽和高，但是不载入内存中
        /**
         * 如果本地没有，那就去下载。
         */
        BitmapFactory.decodeFile(path, options);//options获得了图片实际的宽和高

        options.inSampleSize = caculateInSampleSize(options,width,height);

        //使用获取到的InSampleSize再次解析图片
        options.inJustDecodeBounds = false;//获取图片的宽和高，但是载入内存中
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);

        return bitmap;
    }

    /**
     * 根据需求的宽和高以及实际的宽和高计算SampleSize
     * @param options
     * @param width
     * @param height
     * @return
     */
    private int caculateInSampleSize(Options options, int reqWidth, int reHheight) {
        int width = options.outWidth;
        int height = options.outHeight;

        int inSampleSize = 1;

        if(width > reqWidth || height > reHheight) {
            int widthRadio = Math.round(width * 1.0f / reqWidth);
            int heightRadio = Math.round(height * 1.0f / reHheight);
            inSampleSize = Math.max(widthRadio, heightRadio);
        }
        return inSampleSize;
    }

    private class ImageSize {
        int width;
        int height;
    }
    /**
     * 根据ImageView获得适当的压缩的宽和高
     * @param imageView
     * @return
     */
    @SuppressLint("NewApi")
    protected ImageSize getImageViewSize(ImageView imageView) {
        ImageSize imageSize = new ImageSize();

        //屏幕的宽度
        DisplayMetrics metrics = imageView.getContext().getResources().getDisplayMetrics();
        RelativeLayout.LayoutParams lp =  (android.widget.RelativeLayout.LayoutParams) imageView.getLayoutParams();

        int width = imageView.getWidth();
        if (width <= 0) {
            width = lp.width;
        }
        if (width <= 0) {
            width = imageView.getMaxWidth();
        }
        if (width <= 0) {
            width = metrics.widthPixels;
        }

        int height = imageView.getHeight();
        if (height <= 0) {
            height = lp.height;
        }
        if (height <= 0) {
            height = imageView.getMaxHeight();
        }
        if (height <= 0) {
            height = metrics.heightPixels;
        }

        imageSize.width = width;
        imageSize.height = height;
        return imageSize;
    }

    private synchronized void addTasks(Runnable runnable) {
        mTaskQuequ.add(runnable);

        // if (mPoolThreadHandler == null) wait();
        if (mPoolThreadHandler == null) {
            try {
                mPoolThreadHandlerSemaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        mPoolThreadHandler.sendEmptyMessage(0x110);

    }

    //持有对象
    private class ImgBeanHolder {
        Bitmap bitmap;
        ImageView imageView;
        String Path;
    }

    private Bitmap getBitmapFromLruCache(String path) {
        return mLruCache.get(path);
    }
}