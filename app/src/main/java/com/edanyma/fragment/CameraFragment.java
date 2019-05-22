package com.edanyma.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.edanyma.AppConstants;
import com.edanyma.R;
import com.edanyma.activity.PersonActivity;
import com.edanyma.manager.GlobalManager;
import com.edanyma.model.ApiResponse;
import com.edanyma.model.ClientAvatar;
import com.edanyma.owncomponent.AutoFitTextureView;
import com.edanyma.owncomponent.ModalMessage;
import com.edanyma.pixelshot.PixelShot;
import com.edanyma.rest.RestController;
import com.edanyma.utils.AppUtils;
import com.edanyma.utils.ConvertUtils;

import java.io.File;
import java.nio.ByteBuffer;

import me.aflak.ezcam.EZCam;
import me.aflak.ezcam.EZCamCallback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;


public class CameraFragment extends BaseFragment implements PixelShot.PixelShotListener {

    private static final String TAG = "CameraFragment";


    private static final int JPG_MAX_QUALITY = 100;


    private static final int AVATAR_SIZE = ( int ) ConvertUtils.convertDpToPixel( 256 );

    private OnCameraFragmentListener mListener;

    private AutoFitTextureView mCameraView;
    private EZCam mCamera;
    private Button mTakePhotoBtn;
    private Button mGoBackBtn;

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
        initializeCamera();
        mGoBackBtn = getView().findViewById( R.id.goBackCameraBtnId );
        mGoBackBtn.setOnClickListener( ( View view ) -> {
            AppUtils.clickAnimation( view );
            getActivity().onBackPressed();
        } );
        mTakePhotoBtn = getView().findViewById( R.id.takePhotoBtnId );
        mTakePhotoBtn.setOnClickListener( ( View view ) -> {
            AppUtils.clickAnimation( view );
            AppUtils.transitionAnimation( getView().findViewById( R.id.cameraViewId ),
                    getView().findViewById( R.id.pleaseWaitAvatarId ) );
            mCamera.takePicture();
        } );
    }

    private void initializeCamera() {
        mCameraView = getView().findViewById( R.id.cameraViewId );
        mCamera = new EZCam( getContext() );
        String id = mCamera.getCamerasList().get( CameraCharacteristics.LENS_FACING_FRONT );
        mCamera.selectCamera( id );
        mCamera.setCameraCallback( new EZCamCallback() {
            @Override
            public void onCameraReady() {
                mCamera.setCaptureSetting( CaptureRequest.COLOR_CORRECTION_ABERRATION_MODE,
                        CameraMetadata.COLOR_CORRECTION_ABERRATION_MODE_HIGH_QUALITY );
                mCamera.startPreview();
            }

            @Override
            public void onPicture( Image image ) {
                ByteBuffer buffer = image.getPlanes()[ 0 ].getBuffer();
                byte[] data = new byte[ buffer.capacity() ];
                buffer.get( data );
                takePhoto( data );
                image.close();
            }

            @Override
            public void onError( String message ) {
                AppUtils.transitionAnimation( getView().findViewById( R.id.pleaseWaitAvatarId ),
                        getView().findViewById( R.id.cameraViewId ) );
                ModalMessage.show( getActivity(), "Сообщение", new String[]{ "Ошибка инициализации камеры" } );
            }

            @Override
            public void onCameraDisconnected() {
            }
        } );
    }


    private void takePhoto( byte[] data ) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        float height = AVATAR_SIZE*1.225f;
        Bitmap bmp = Bitmap.createScaledBitmap( BitmapFactory.decodeByteArray( data, 0, data.length, options )
                , AVATAR_SIZE, (int) height, true );
        bmp = Bitmap.createBitmap(bmp, 0,0, AVATAR_SIZE, AVATAR_SIZE );
        mTakePhotoBtn.setEnabled( false );
        mGoBackBtn.setEnabled( false );
        new PixelShot.BitmapSaver( getActivity(), bmp, AppConstants.PICTURE_DIR, GlobalManager.getInstance().getClient().getUuid()
                , AppConstants.EXTENSION_JPG, JPG_MAX_QUALITY, this ).execute();
    }

    @Override
    public void onStart() {
        super.onStart();
        mCamera.open( CameraDevice.TEMPLATE_PREVIEW, mCameraView );
        ( ( PersonActivity ) getActivity() ).setHeaderFooterVisibilty( View.GONE );
    }

    @Override
    public void onStop() {
        ( ( PersonActivity ) getActivity() ).setHeaderFooterVisibilty( View.VISIBLE );
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCamera.close();
        mCamera = null;
    }

    @Override
    public void onRequestPermissionsResult( int requestCode, String[] permissions, int[] grantResults ) {
        super.onRequestPermissionsResult( requestCode, permissions, grantResults );
//        mCameraView.onRequestPermissionsResult( requestCode, permissions, grantResults );
    }

    @Override
    public void onAttach( Context context ) {
        super.onAttach( context );
        if ( context instanceof OnCameraFragmentListener ) {
            mListener = ( OnCameraFragmentListener ) context;
        } else {
            throw new RuntimeException( context.toString()
                    + " must implement OnCameraFragmentListener" );
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onPixelShotSuccess( String path ) {
        new UpdateClientAvatar().execute( GlobalManager.getInstance().getClient().getUuid() );
    }

    @Override
    public void onPixelShotFailed() {
        ModalMessage.show( getActivity(), "Ошибка !", new String[]{ "Не сохранено ! Попробуйте еще раз " } );
        AppUtils.transitionAnimation( getView().findViewById( R.id.pleaseWaitAvatarId ),
                getView().findViewById( R.id.cameraViewId ) );
    }


    public interface OnCameraFragmentListener {
        // TODO: Update argument type and name
        void onCameraAction( Uri uri );
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
                RequestBody fileReqBody = RequestBody.create( MediaType.parse( "multipart/form-data" ), file );
                MultipartBody.Part part = MultipartBody.Part.createFormData( "clientAvatar", file.getName(), fileReqBody );
                Call< ApiResponse > avatarCall = RestController.getInstance()
                        .getApi().updateClientAvatar( AppConstants.AUTH_BEARER
                                + GlobalManager.getInstance().getUserToken(), part, fileName );


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
                    getView().findViewById( R.id.cameraViewId ) );
            mTakePhotoBtn.setEnabled( true );
            mGoBackBtn.setEnabled( true );
            if ( result != null ) {
                GlobalManager.getInstance().getClient().setPhoto( result );
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
