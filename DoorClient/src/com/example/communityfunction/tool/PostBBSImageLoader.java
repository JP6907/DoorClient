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
 * ͼƬ������
 */
public class PostBBSImageLoader {

    public static PostBBSImageLoader mInstance;

    /**
     * ͼƬ����ĺ��Ķ���
     */
    private LruCache<String, Bitmap> mLruCache;
    /**
     * �̳߳�
     */
    private ExecutorService mThreadPool;
    private static final int DEAFULT_THREAD_COUNT = 1;

    private Type mType = Type.LIFO;

    /**
     * �������
     * ����������
     */
    private LinkedList<Runnable> mTaskQuequ;

    /**
     * ��̨��ѯ�߳�
     */
    private Thread mPoolThread;
    private Handler mPoolThreadHandler;

    /**
     * UI�߳��е�Handler
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
     * ��ʼ��
     * @param mThreadCount
     * @param type
     */
    @SuppressLint("HandlerLeak")
    private void init(int threadCount, Type type) {
        //��̨��ѯ�߳�
        mPoolThread = new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                mPoolThreadHandler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        //ͨ���̳߳�ȡ������ȥִ��
                        mThreadPool.execute(getTask());

                        try {
                            mSemaphoreThreadPool.acquire();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                };
                //�ͷ�һ���ź���
                mPoolThreadHandlerSemaphore.release();
                Looper.loop();
            }

        };
        mPoolThread.start();

        //��ȡӦ�õ��������ڴ� 
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheMemory = maxMemory/8;

        mLruCache = new LruCache<String,Bitmap>(cacheMemory){
            @Override
            protected int sizeOf(String key, Bitmap value) {//sizeOf�������Ǽ��㻺�����Ĵ�С
                return value.getRowBytes() * value.getHeight();
            }
        };

        //�����̳߳�
        mThreadPool = Executors.newFixedThreadPool(threadCount);
        mTaskQuequ = new LinkedList<Runnable>();
        mType = type;

        mSemaphoreThreadPool = new Semaphore(threadCount);
    }

    /**
     * ���������ȡ��һ������
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

    //����ģʽ
    public static PostBBSImageLoader getInstance(int threadCount,Type tpye) {
        //û����ͬ������,Ϊ�����Ч��
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
     * g����pathΪImageView����ͼƬ
     * @param path
     * @param imageView
     */
    @SuppressLint("HandlerLeak")
    public void loadImage(final String path,final ImageView imageView) {
        imageView.setTag(path);
        if (mUIHandler == null) {
            mUIHandler = new Handler() {
                public void handleMessage(Message msg) {
                    //��ȡ�õ���ͼƬ,Ϊimageview�ص�����ͼƬ
                	
                    ImgBeanHolder holder = (ImgBeanHolder) msg.obj;
                    Bitmap bm = holder.bitmap;
                    ImageView imageView = holder.imageView;
                    String path = holder.Path;

                    //��Path��getTag�洢·�����бȽ�,������ΪimageView�ĸ����´���
                    if (imageView.getTag().toString().equals(path)) {
                        imageView.setImageBitmap(bm);
                    }
                }
            };
        }

        //����Path�ڻ����л�ȡbitmap
        Bitmap bm = getBitmapFromLruCache(path);
        if (bm != null) {
            refreshBitmap(path, imageView, bm);
        } else {
            addTasks(new Runnable() {

                @Override
                public void run() {
                    // ����ͼƬ
                    // ͼƬ��ѹ��
                    // 1�����ͼƬ��Ҫ��ʾ�Ĵ�С
                    ImageSize imageSize = getImageViewSize(imageView);
                    // 2��ѹ��ͼƬ
                    Bitmap bm = decodeSampledBitmapFromPath(path, imageSize.width, imageSize.height);
                    // 3����ͼƬ���뵽����
                    addBitmapToLruCache(path, bm);

                    //�ص�
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
     *��ͼƬ����LruCache 
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
     * ����ͼƬ��Ҫ��ʾ�Ŀ�͸߶�ͼƬ����ѹ��
     * @param path
     * @param width
     * @param height
     * @return
     */
    protected Bitmap decodeSampledBitmapFromPath(String path, int width,
            int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//��ȡͼƬ�Ŀ�͸ߣ����ǲ������ڴ���
        /**
         * �������û�У��Ǿ�ȥ���ء�
         */
        BitmapFactory.decodeFile(path, options);//options�����ͼƬʵ�ʵĿ�͸�

        options.inSampleSize = caculateInSampleSize(options,width,height);

        //ʹ�û�ȡ����InSampleSize�ٴν���ͼƬ
        options.inJustDecodeBounds = false;//��ȡͼƬ�Ŀ�͸ߣ����������ڴ���
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);

        return bitmap;
    }

    /**
     * ��������Ŀ�͸��Լ�ʵ�ʵĿ�͸߼���SampleSize
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
     * ����ImageView����ʵ���ѹ���Ŀ�͸�
     * @param imageView
     * @return
     */
    @SuppressLint("NewApi")
    protected ImageSize getImageViewSize(ImageView imageView) {
        ImageSize imageSize = new ImageSize();

        //��Ļ�Ŀ��
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

    //���ж���
    private class ImgBeanHolder {
        Bitmap bitmap;
        ImageView imageView;
        String Path;
    }

    private Bitmap getBitmapFromLruCache(String path) {
        return mLruCache.get(path);
    }
}