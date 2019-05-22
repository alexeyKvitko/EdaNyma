package com.edanyma.pixelshot;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;

import com.edanyma.AppConstants;
import com.edanyma.utils.AppUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;

import static com.edanyma.AppConstants.EXTENSION_JPG;
import static com.edanyma.AppConstants.EXTENSION_NOMEDIA;
import static com.edanyma.AppConstants.EXTENSION_PNG;


public class PixelShot {

    private static final String TAG = PixelShot.class.getSimpleName();

    private static final int JPG_MAX_QUALITY = 100;
    private static final int JPG_MID_QUALITY = 80;

    private String path = AppConstants.PICTURE_DIR;
    private String filename = AppConstants.FILENAME_DISH;
    private String fileExtension = EXTENSION_JPG;
    private int jpgQuality = JPG_MID_QUALITY;

    private PixelShotListener listener;
    private View view;

    private PixelShot(@NonNull View view) {
        this.view = view;
    }

    public static PixelShot of(@NonNull View view) {
        return new PixelShot(view);
    }

    public PixelShot setFilename(String filename) {
        this.filename = filename;
        return this;
    }

    public PixelShot setPath(String path) {
        this.path = path;
        return this;
    }

    public PixelShot setResultListener(PixelShotListener listener) {
        this.listener = listener;
        return this;
    }

    public PixelShot toJPG() {
        jpgQuality = JPG_MAX_QUALITY;
        setFileExtension(EXTENSION_JPG);
        return this;
    }

    public PixelShot toJPG(int jpgQuality) {
        this.jpgQuality = jpgQuality;
        setFileExtension(EXTENSION_JPG);
        return this;
    }

    public PixelShot toPNG() {
        setFileExtension(EXTENSION_PNG);
        return this;
    }

    public PixelShot toNomedia() {
        setFileExtension(EXTENSION_NOMEDIA);
        return this;
    }


    public void save() throws NullPointerException {
        if (!AppUtils.isStorageReady()) {
            throw new IllegalStateException("Storage was not ready for use");
        }

        if (view instanceof SurfaceView ) {
            PixelCopyHelper.getSurfaceBitmap((SurfaceView) view, new PixelCopyHelper.PixelCopyListener() {
                @Override
                public void onSurfaceBitmapReady(Bitmap surfaceBitmap) {
                    new BitmapSaver(getAppContext(), surfaceBitmap, path, filename, fileExtension, jpgQuality, listener).execute();
                }

                @Override
                public void onSurfaceBitmapError() {
                    Log.d(TAG, "Couldn't create a bitmap of the SurfaceView");
                    if (listener != null) {
                        listener.onPixelShotFailed();
                    }
                }
            });
        } else {
            new BitmapSaver(getAppContext(), getViewBitmap(), path, filename, fileExtension, jpgQuality, listener).execute();
        }
    }


    private void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    private Context getAppContext() {
        if (view == null) {
            throw new NullPointerException("The provided View was null");
        } else {
            return view.getContext().getApplicationContext();
        }
    }

    private Bitmap getViewBitmap() {
        Bitmap bitmap;
        if (view instanceof TextureView ) {
            bitmap = ((TextureView) view).getBitmap();
            Canvas canvas = new Canvas(bitmap);
            view.draw(canvas);
            canvas.setBitmap(null);
            return bitmap;
        } else {
            bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            view.draw(canvas);
            canvas.setBitmap(null);
            return bitmap;
        }
    }


    public interface PixelShotListener {
        void onPixelShotSuccess(String path);

        void onPixelShotFailed();
    }



   public static class BitmapSaver extends AsyncTask<Void, Void, Boolean> {
//            implements MediaScannerConnection.OnScanCompletedListener {

        private final WeakReference<Context> weakContext;
        private Handler handler = new Handler( Looper.getMainLooper());
        private Bitmap bitmap;
        private String path;
        private String filename;
        private String fileExtension;
        private int jpgQuality;
        private PixelShotListener listener;
        private File file;

        public BitmapSaver( Context context, Bitmap bitmap, String path, String filename, String fileExtension, int jpgQuality, PixelShotListener listener ) {
            this.weakContext = new WeakReference<>(context);
            this.bitmap = bitmap;
            this.path = path;
            this.filename = filename;
            this.fileExtension = fileExtension;
            this.jpgQuality = jpgQuality;
            this.listener = listener;
        }

//        private void cancelTask() {
//            cancel(true);
//            if (listener != null) {
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        listener.onPixelShotFailed();
//                    }
//                });
//            }
//        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean result = true;
            File directory = new File(Environment.getExternalStorageDirectory(), path);
            if (!directory.exists() && !directory.mkdirs()) {
//                cancelTask();
                return false;
            }

            file = new File(directory, filename + fileExtension);
            try ( OutputStream out = new BufferedOutputStream(new FileOutputStream(file))) {
                switch (fileExtension) {
                    case EXTENSION_JPG:
                        bitmap.compress(Bitmap.CompressFormat.JPEG, jpgQuality, out);
                        break;
                    case EXTENSION_PNG:
                        bitmap.compress(Bitmap.CompressFormat.PNG, 0, out);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                result = false;
//                cancelTask();
            }

            bitmap.recycle();
            bitmap = null;
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute( result );
//            MediaScannerConnection.scanFile(weakContext.get(), new String[]{file.getPath()}, null, this);
            weakContext.clear();
            if( result ){
                listener.onPixelShotSuccess(path);
            } else {
                listener.onPixelShotFailed();
            }
        }

//        @Override
//        public void onScanCompleted(final String path, final Uri uri) {
//            if (listener != null) {
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (uri != null) {
//                            Log.i(TAG, "Saved image to path: " + path);
//                            Log.i(TAG, "Saved image to URI: " + uri);
//                            listener.onPixelShotSuccess(path);
//                        } else {
//                            listener.onPixelShotFailed();
//                        }
//                    }
//                });
//            }
//        }
    }
}