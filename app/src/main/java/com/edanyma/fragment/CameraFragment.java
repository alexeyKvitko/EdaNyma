package com.edanyma.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Rational;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.camera.core.CameraX;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureConfig;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.core.PreviewConfig;
import androidx.lifecycle.LifecycleOwner;

import com.edanyma.AppConstants;
import com.edanyma.R;
import com.edanyma.activity.PersonActivity;
import com.edanyma.model.ApiResponse;
import com.edanyma.owncomponent.ModalMessage;
import com.edanyma.pixelshot.PixelShot;
import com.edanyma.rest.RestController;
import com.edanyma.utils.AppUtils;
import com.edanyma.utils.AutoFitPreviewBuilder;

import java.io.File;
import java.nio.ByteBuffer;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

import static com.edanyma.manager.GlobalManager.getClient;
import static com.edanyma.manager.GlobalManager.getUserToken;


public class CameraFragment extends BaseFragment implements PixelShot.PixelShotListener {

    private static final String TAG = "CameraFragment";

    private static final int JPG_MAX_QUALITY = 100;
    private static final int AVATAR_SIZE = 768;
    private static final int TARGET_HEIGHT = 1280;
    private static final int TARGET_WIDTH = 720;

    private Button mTakePhotoBtn;
    private Button mGoBackBtn;

    private TextureView mViewFinder;
    private ImageCapture mImageCapture;


    public CameraFragment() {
    }

    public static CameraFragment newInstance() {
        CameraFragment fragment = new CameraFragment();
        return fragment;
    }

    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState ) {
        return inflater.inflate( R.layout.fragment_camera, container, false );
    }

    @Override
    public void onActivityCreated( @Nullable Bundle savedInstanceState ) {
        super.onActivityCreated( savedInstanceState );
        mViewFinder = getView().findViewById( R.id.cameraViewFinderId );
        mViewFinder.post( () -> {
            startCamera();
        } );
        mViewFinder.addOnLayoutChangeListener( ( View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7 ) -> {
            updateTransform();
        } );
    }

    private void startCamera() {
        CameraX.unbindAll();
        DisplayMetrics metrics = new DisplayMetrics();
        CameraX.LensFacing lensFacing = CameraX.LensFacing.FRONT;
        int rotation =  mViewFinder.getDisplay().getRotation();
        mViewFinder.getDisplay().getRealMetrics( metrics );
        Rational screenAspectRatio = new Rational(metrics.widthPixels, metrics.heightPixels);
        int targetHeight = screenAspectRatio.toString().equals( "6/13" )
                                            ? (int) (TARGET_HEIGHT*1.21875) : TARGET_HEIGHT;

        Size targetResolution = new Size( TARGET_WIDTH, targetHeight );
        PreviewConfig previewConfig = new PreviewConfig.Builder()
                .setLensFacing( lensFacing )
                .setTargetAspectRatio( screenAspectRatio )
                .setTargetRotation( rotation )
                .setTargetResolution( targetResolution )
                .build();

        Preview preview = new AutoFitPreviewBuilder( previewConfig, mViewFinder ).build();

        preview.setOnPreviewOutputUpdateListener( ( Preview.PreviewOutput output ) -> {
            ViewGroup parent = ( ViewGroup ) mViewFinder.getParent();
            parent.removeView( mViewFinder );
            parent.addView( mViewFinder, 0 );
            mViewFinder.setSurfaceTexture( output.getSurfaceTexture() );
            updateTransform();
        } );
        HandlerThread handlerThread = new HandlerThread("LuminosityAnalysis");
        handlerThread.start();

        ImageCaptureConfig imageCaptureConfig =
                new ImageCaptureConfig.Builder()
                        .setCaptureMode( ImageCapture.CaptureMode.MIN_LATENCY )
                        .setLensFacing( lensFacing )
                        .setTargetAspectRatio( screenAspectRatio )
                        .setTargetRotation( rotation )
                        .setTargetResolution( targetResolution )
                        .build();

        mImageCapture = new ImageCapture( imageCaptureConfig );

        CameraX.bindToLifecycle( ( LifecycleOwner ) this,  preview , mImageCapture);
        initialize();
    }

    private void updateTransform() {
        // TODO: Implement camera viewfinder transformations
    }


    private void initialize() {
        mGoBackBtn = getView().findViewById( R.id.goBackCameraBtnId );
        mGoBackBtn.setOnClickListener( ( View view ) -> {
            AppUtils.clickAnimation( view );
            getActivity().onBackPressed();
        } );
        mTakePhotoBtn = getView().findViewById( R.id.takePhotoBtnId );
        mTakePhotoBtn.setOnClickListener( ( View view ) -> {
            AppUtils.clickAnimation( view );
            AppUtils.transitionAnimation( getView().findViewById( R.id.cameraViewFinderId ),
                    getView().findViewById( R.id.pleaseWaitAvatarId ) );
            setCaptureClickListener();
        } );
    }

    private void setCaptureClickListener(){
        File directory = new File(Environment.getExternalStorageDirectory(), AppConstants.PICTURE_DIR);
        if (!directory.exists() && !directory.mkdirs()) {
            return;
        }
        File file = new File(directory, getClient().getUuid() + AppConstants.EXTENSION_JPG);
        mImageCapture.takePicture( new ImageCapture.OnImageCapturedListener() {
            @Override
            public void onCaptureSuccess( ImageProxy image, int rotationDegrees ) {
//                super.onCaptureSuccess( image, rotationDegrees );
                takePhoto( image );
            }

            @Override
            public void onError( ImageCapture.UseCaseError useCaseError, String message, @Nullable Throwable cause ) {
                super.onError( useCaseError, message, cause );
            }
        } );
    }

    private void takePhoto( final ImageProxy imageProxy ) {
        new Handler( ).post( ()->{
            Bitmap bmp = decodeToBitmap( imageProxy.getImage() );
            if ( bmp.getHeight() < bmp.getWidth() ){
                bmp = rotateImage( bmp, -90 );
            }
            int x = bmp.getWidth()/2-AVATAR_SIZE/2;
            int y = bmp.getHeight()/2-AVATAR_SIZE/2;;
            int sizeHeight  = bmp.getHeight() < AVATAR_SIZE ? bmp.getHeight() : AVATAR_SIZE;
            int sizeWidth = bmp.getWidth() < AVATAR_SIZE ? bmp.getWidth() : AVATAR_SIZE;
            int size = sizeHeight < sizeWidth ? sizeHeight : sizeWidth;
            bmp = Bitmap.createBitmap(bmp, x > 0 ? x : 0 ,y > 0 ? y : 0, size, size );

            mTakePhotoBtn.setEnabled( false );
            mGoBackBtn.setEnabled( false );
            new PixelShot.BitmapSaver( getActivity(), bmp, AppConstants.PICTURE_DIR, getClient().getUuid()
                    , AppConstants.EXTENSION_JPG, JPG_MAX_QUALITY, this ).execute();
            imageProxy.close();
        } );

    }

    private  Bitmap rotateImage(Bitmap bitmap, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        bitmap.recycle();
        return rotatedBitmap;
    }

     private Bitmap decodeToBitmap( final Image image) {
         if (image==null){
             return null;
         }
         byte[] data = null;
         if (image.getFormat() == ImageFormat.JPEG) {
             Image.Plane[] planes = image.getPlanes();
             ByteBuffer buffer = planes[0].getBuffer();
             data = new byte[buffer.capacity()];
             buffer.get(data);
         } else if (image.getFormat() == ImageFormat.YUV_420_888) {
             data = null;
         }
         BitmapFactory.Options options = new BitmapFactory.Options();
         options.inMutable = true;
        return BitmapFactory.decodeByteArray( data, 0, data.length, options );
    }



    @Override
    public void onStart() {
        super.onStart();
        ( (PersonActivity) getActivity() ).setHeaderFooterVisibilty( View.GONE );

    }

    @Override
    public void onStop() {
        ( ( PersonActivity ) getActivity() ).setHeaderFooterVisibilty( View.VISIBLE );
        super.onStop();
    }

    @Override
    public void onPixelShotSuccess( String path ) {
        new UpdateClientAvatar().execute( getClient().getUuid() );
    }

    @Override
    public void onPixelShotFailed() {
        ModalMessage.show( getActivity(), "Ошибка !", new String[]{ "Не сохранено ! Попробуйте еще раз " } );
        AppUtils.transitionAnimation( getView().findViewById( R.id.pleaseWaitAvatarId ),
                getView().findViewById( R.id.cameraViewFinderId ) );
    }


    class UpdateClientAvatar extends AsyncTask< String, Void, String > {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground( String... params ) {
            String result = null;
            try {
                String fileName = params[ 0 ];
                File directory = new File( Environment.getExternalStorageDirectory(), AppConstants.PICTURE_DIR );
                if ( !directory.exists() && !directory.mkdirs() ) {
                    return null;
                }
                File file = new File( directory, fileName + AppConstants.EXTENSION_JPG );
                Log.i( TAG, file.getAbsolutePath() );
                RequestBody fileReqBody = RequestBody.create( MediaType.parse( "multipart/form-data" ), file );
                MultipartBody.Part part = MultipartBody.Part.createFormData( "clientAvatar", file.getName(), fileReqBody );
                Call< ApiResponse > avatarCall = RestController
                        .getApi().updateClientAvatar( AppConstants.AUTH_BEARER
                                + getUserToken(), part, fileName );


                Response< ApiResponse > responseUpdate = avatarCall.execute();
                if ( responseUpdate.body() != null ) {
                    if ( responseUpdate.body().getStatus() == 200 ) {
                        result = responseUpdate.body().getMessage();
                    } else {
                        result = null;
                    }
                } else {
                    result = null;
                }
            } catch ( Exception e ) {
                result = null;
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute( String result ) {
            super.onPostExecute( result );
            AppUtils.transitionAnimation( getView().findViewById( R.id.pleaseWaitAvatarId ),
                    getView().findViewById( R.id.cameraViewFinderId ) );
            mTakePhotoBtn.setEnabled( true );
            mGoBackBtn.setEnabled( true );
            if ( result != null ) {
                getClient().setPhoto( result );
                new Handler().postDelayed( () -> {
                    getActivity().onBackPressed();
                }, 200 );
            } else {
                ModalMessage.show( getActivity(), getString( R.string.title_notifications ),
                        new String[]{ getResources().getString( R.string.internal_error ) }, 2000 );
            }
        }
    }


}
