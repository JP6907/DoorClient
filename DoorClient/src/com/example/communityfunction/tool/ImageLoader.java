package com.example.communityfunction.tool;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.neo.huikaimen.R;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StatFs;
import android.os.Build.VERSION_CODES;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;
import neo.door.cache.DiskLruCache;
import neo.door.cache.DiskLruCache.Editor;
import neo.door.cache.DiskLruCache.Snapshot;

/**
 * 图片加载器
 */
public class ImageLoader {
	 private static final String TAG = "ImageLoader";
	 public static final int MESSAGE_POST_RESULT = 1;
	 private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();     // 处理器数
	 private static final int CORE_POOL_SIZE = CPU_COUNT + 1;                             // 线程池的最大核心线程数
	 private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;                      // 线程池的最大线程数
	 private static final long KEEP_ALIVE = 10L;                                          // 线程闲置超长时长

	 private static final int TAG_KEY_URI = R.id.imageloader_uri;
	 private static final long DISK_CACHE_SIZE = 1024 * 1024 * 10;                       // 磁盘缓存最大值10M
	 private static final int IO_BUFFER_SIZE = 8 * 1024;                                 // 8KB
	 private static final int DISK_CACHE_INDEX = 0;                                      // 因为磁盘缓存只设置了一个节点
	 private boolean mIsDiskLruCacheCreated = false;
	 
	 // 线程工厂
	 private static final ThreadFactory sThreadFactory = new ThreadFactory() {
	    private final AtomicInteger mCount = new AtomicInteger(1);

	    public Thread newThread(Runnable r) {
	        return new Thread(r, "ImageLoader#" + mCount.getAndIncrement());
	    }
	};
	// 线程池
	public static final Executor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(
	        CORE_POOL_SIZE, MAXIMUM_POOL_SIZE,
	        KEEP_ALIVE, TimeUnit.SECONDS,
	        new LinkedBlockingQueue<Runnable>(), sThreadFactory);
	    
	// !!!这样就可以更新ui
	private Handler mMainHandler = new Handler(Looper.getMainLooper()) {
	    @Override
	    public void handleMessage(Message msg) {
	    	LoaderResult result = (LoaderResult) msg.obj;
	        ImageView imageView = result.imageView;
	        String uri = (String) imageView.getTag(TAG_KEY_URI);            // 获得Tag
	        if (uri.equals(result.uri)) {
	            imageView.setImageBitmap(result.bitmap);
	        } else {
	            Log.w(TAG, "set image bitmap,but url has changed, ignored!");
	        }
	    };
	};

	private Context mContext;
	private ImageResizer mImageResizer = new ImageResizer();
	private LruCache<String, Bitmap> mMemoryCache;
	private DiskLruCache mDiskLruCache;

	private ImageLoader(Context context) {
	    mContext = context.getApplicationContext();
	    int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
	    int cacheSize = maxMemory / 8;
	    // 创建内存缓存
	    mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
	        @Override
	        protected int sizeOf(String key, Bitmap bitmap) {
	            return bitmap.getRowBytes() * bitmap.getHeight() / 1024;          // 计算缓存对象的大小
	        }
	    };
	    // 创建文件缓存
        File diskCacheDir = getDiskCacheDir(mContext, "bitmap");
        if (!diskCacheDir.exists()) {
            diskCacheDir.mkdirs();
        }
        if (getUsableSpace(diskCacheDir) > DISK_CACHE_SIZE) {
            try {
                mDiskLruCache = DiskLruCache.open(diskCacheDir, 1, 1, DISK_CACHE_SIZE);
                mIsDiskLruCacheCreated = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * build a new instance of ImageLoader
     * @param context
     * @return a new instance of ImageLoader
     */
    public static ImageLoader build(Context context) {
        return new ImageLoader(context);
    }
    /**
     * 把图片保存到内存缓存
     * @param key
     * @param bitmap
     */
    private void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }
    /**
     * 从内存缓存获取图片
     * @param key
     * @return
     */
    private Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

    /**
     * load bitmap from memory cache or disk cache or network async, then bind imageView and bitmap.
     * NOTE THAT: should run in UI Thread
     * @param uri http url
     * @param imageView bitmap's bind object
     */
    public void bindBitmap(final String uri, final ImageView imageView) {
        bindBitmap(uri, imageView, 0, 0);
    }

    public void bindBitmap(final String uri, final ImageView imageView,
            final int reqWidth, final int reqHeight) {
        imageView.setTag(TAG_KEY_URI, uri);                 // 设置TAG, 防止图片乱序
        Bitmap bitmap = loadBitmapFromMemCache(uri);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            return;
        }

        Runnable loadBitmapTask = new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = loadBitmap(uri, reqWidth, reqHeight);
                if (bitmap != null) {
                    LoaderResult result = new LoaderResult(imageView, uri, bitmap);
                    mMainHandler.obtainMessage(MESSAGE_POST_RESULT, result).sendToTarget();
                }
            }
        };
        THREAD_POOL_EXECUTOR.execute(loadBitmapTask);       // 提交给线程池处理
    }

    /**
     * load bitmap from memory cache or disk cache or network.
     * @param uri http url
     * @param reqWidth the width ImageView desired
     * @param reqHeight the height ImageView desired
     * @return bitmap, maybe null.
     */
    private Bitmap loadBitmap(String uri, int reqWidth, int reqHeight) {
        Bitmap bitmap = loadBitmapFromMemCache(uri);
        if (bitmap != null) {
            Log.d(TAG, "loadBitmapFromMemCache,url:" + uri);
            return bitmap;
        }

        try {
            bitmap = loadBitmapFromDiskCache(uri, reqWidth, reqHeight);
            if (bitmap != null) {
                Log.d(TAG, "loadBitmapFromDisk,url:" + uri);
                return bitmap;
            }
            bitmap = loadBitmapFromHttp(uri, reqWidth, reqHeight);
            Log.d(TAG, "loadBitmapFromHttp,url:" + uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 若上面的加载都不成功并且磁盘缓存不可用
        if (bitmap == null && !mIsDiskLruCacheCreated) {
            Log.w(TAG, "encounter error, DiskLruCache is not created.");
            bitmap = downloadBitmapFromUrl(uri);
        }

        return bitmap;
    }
    /**
     * 从内存缓存加载图片
     * @param url
     * @return
     */
    private Bitmap loadBitmapFromMemCache(String url) {
        final String key = hashKeyFormUrl(url);             // 根据url生成独一无二的key
        Bitmap bitmap = getBitmapFromMemCache(key);
        return bitmap;
    }
    /**
     * 从网上加载图片
     * @param url
     * @param reqWidth
     * @param reqHeight
     * @return
     * @throws IOException
     */
    private Bitmap loadBitmapFromHttp(String url, int reqWidth, int reqHeight)
            throws IOException {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new RuntimeException("can not visit network from UI Thread.");
        }
        if (mDiskLruCache == null) {
            return null;
        }
        
        String key = hashKeyFormUrl(url);
        DiskLruCache.Editor editor = mDiskLruCache.edit(key);
        if (editor != null) {
            OutputStream outputStream = editor.newOutputStream(DISK_CACHE_INDEX);  
            if (downloadUrlToStream(url, outputStream)) {	// 从网上下载并保存进磁盘缓存
                editor.commit();
            } else {
                editor.abort();
            }
            mDiskLruCache.flush();
        }
        return loadBitmapFromDiskCache(url, reqWidth, reqHeight);      // 从磁盘里下载
    }
    /**
     * 从磁盘缓存加载图片
     * @param url
     * @param reqWidth
     * @param reqHeight
     * @return
     * @throws IOException
     */
    private Bitmap loadBitmapFromDiskCache(String url, int reqWidth,
            int reqHeight) throws IOException {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            Log.w(TAG, "load bitmap from UI Thread, it's not recommended!");
        }
        if (mDiskLruCache == null) {
            return null;
        }

        Bitmap bitmap = null;
        String key = hashKeyFormUrl(url);
        DiskLruCache.Snapshot snapShot = mDiskLruCache.get(key);
        if (snapShot != null) {
            FileInputStream fileInputStream = (FileInputStream)snapShot.getInputStream(DISK_CACHE_INDEX);
            FileDescriptor fileDescriptor = fileInputStream.getFD();
            bitmap = mImageResizer.decodeSampledBitmapFromFileDescriptor(fileDescriptor,
                    reqWidth, reqHeight);
            if (bitmap != null) {
                addBitmapToMemoryCache(key, bitmap);             // 保存进内存缓存
            }
        }

        return bitmap;
    }
    /**
     * 直接从网上下载图片, 并写进磁盘缓存
     * @param urlString
     * @param outputStream
     * @return
     */
    public boolean downloadUrlToStream(String urlString,
            OutputStream outputStream) {
        HttpURLConnection urlConnection = null;
        BufferedOutputStream out = null;
        BufferedInputStream in = null;

        try {
            final URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream(), IO_BUFFER_SIZE);
            out = new BufferedOutputStream(outputStream, IO_BUFFER_SIZE);

            int b;
            while ((b = in.read()) != -1) {
                out.write(b);          // 写进磁盘缓存
            }
            return true;
        } catch (IOException e) {
            Log.e(TAG, "downloadBitmap failed." + e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            close(out);
            close(in);
        }
        return false;
    }
    /**
     * 直接从网上下载图片
     * @param urlString
     * @return
     */
    private Bitmap downloadBitmapFromUrl(String urlString) {
        Bitmap bitmap = null;
        HttpURLConnection urlConnection = null;
        BufferedInputStream in = null;

        try {
            final URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream(), IO_BUFFER_SIZE);
            bitmap = BitmapFactory.decodeStream(in);
        } catch (final IOException e) {
            Log.e(TAG, "Error in downloadBitmap: " + e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            close(in);
        }
        return bitmap;
    }

    private String hashKeyFormUrl(String url) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(url.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(url.hashCode());
        }
        return cacheKey;
    }

    private String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    public File getDiskCacheDir(Context context, String uniqueName) {
        boolean externalStorageAvailable = Environment
                	.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        final String cachePath;
        if (externalStorageAvailable) {
            cachePath = context.getExternalCacheDir().getPath();        // sd卡缓存路径
        } else {
            cachePath = context.getCacheDir().getPath();                // 内置缓存路径
        }
        return new File(cachePath + File.separator + uniqueName);
    }

    @SuppressWarnings("deprecation")
	@TargetApi(VERSION_CODES.GINGERBREAD)
    private long getUsableSpace(File path) {
        if (Build.VERSION.SDK_INT >= VERSION_CODES.GINGERBREAD) {
            return path.getUsableSpace();
        }
        final StatFs stats = new StatFs(path.getPath());
        return (long) stats.getBlockSize() * (long) stats.getAvailableBlocks();
    }

    private static class LoaderResult {
        public ImageView imageView;
        public String uri;
        public Bitmap bitmap;

        public LoaderResult(ImageView imageView, String uri, Bitmap bitmap) {
            this.imageView = imageView;
            this.uri = uri;
            this.bitmap = bitmap;
        }
    }
    /**
     * 关闭IO流
     * @param closeable
     */
    public static void close(Closeable closeable) {
		try {
	        if (closeable != null) {
	            closeable.close();
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
}
