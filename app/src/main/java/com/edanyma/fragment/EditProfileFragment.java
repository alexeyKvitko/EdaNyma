package com.edanyma.fragment;


import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edanyma.AppConstants;
import com.edanyma.R;
import com.edanyma.activity.PersonActivity;
import com.edanyma.model.ApiResponse;
import com.edanyma.model.OurClientModel;
import com.edanyma.owncomponent.ModalMessage;
import com.edanyma.rest.RestController;
import com.edanyma.utils.AppUtils;
import com.edanyma.utils.GlideClient;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Response;

import static com.edanyma.manager.GlobalManager.*;


public class EditProfileFragment extends BaseFragment implements View.OnClickListener {

    private OnEditFragmentActionListener mListener;

    private AppCompatEditText mProfileNickName;
    private AppCompatEditText mProfilePhone;
    private AppCompatEditText mProfileEmail;

    private TextView mProfilePhoneError;
    private TextView mProfileEmailError;

    private OurClientModel mClient;


    public EditProfileFragment() {}

    public static EditProfileFragment newInstance() {
        EditProfileFragment fragment = new EditProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState ) {
        // Inflate the layout for this fragment
        return inflater.inflate( R.layout.fragment_edit_profile, container, false );
    }

    @Override
    public void onActivityCreated( @Nullable Bundle savedInstanceState ) {
        super.onActivityCreated( savedInstanceState );
        mClient = getClient();
        updateAvatar();
        initTextInputLayout( R.id.profileNickNameLayoutId, AppConstants.ROBOTO_CONDENCED );
        initTextInputLayout( R.id.profilePhoneLayoutId, AppConstants.ROBOTO_CONDENCED );
        initTextInputLayout( R.id.profileEmailLayoutId, AppConstants.ROBOTO_CONDENCED );

        initTextView( R.id.profileNickNameErrorFieldId, AppConstants.ROBOTO_CONDENCED );
        mProfilePhoneError = initTextView( R.id.profilePhoneErrorId, AppConstants.ROBOTO_CONDENCED );
        mProfileEmailError = initTextView( R.id.profileEmailErrorId, AppConstants.ROBOTO_CONDENCED );

        mProfileNickName = initEditText( R.id.profileNickNameValueId, AppConstants.ROBOTO_CONDENCED );
        mProfilePhone = initEditText( R.id.profilePhoneValueId, AppConstants.ROBOTO_CONDENCED );
        mProfileEmail = initEditText( R.id.profileEmailValueId, AppConstants.ROBOTO_CONDENCED );
        mProfileNickName.setText( mClient.getNickName() );
        mProfilePhone.setText( mClient.getPhone() );
        mProfileEmail.setText( mClient.getEmail() );

        initTextView( R.id.editProfileTitleId, AppConstants.B52 );
        initButton( R.id.updateProfileBtnId, AppConstants.ROBOTO_CONDENCED );
        setThisOnClickListener( R.id.updateProfileBtnId, R.id.editProfileBackBtnId,
                R.id.signOutProfileBtnId, R.id.removeProfileBigBtnId, R.id.removeProfileBtnId, R.id.avatarCardViewId );
    }

    private void updateProfile(){
        AppUtils.hideKeyboardFrom( getActivity(), getView() );
        String nickName = mProfileNickName.getText().toString().trim();
        String phone = mProfilePhone.getText().toString().trim();
        String email = mProfileEmail.getText().toString().trim().toLowerCase();
        String errorMessage = null;
        if( phone.length() == 0 ){
            errorMessage = getResources().getString( R.string.error_required_field );
        }
        if( errorMessage == null && !AppUtils.validatePhone( phone )){
            errorMessage = getResources().getString( R.string.error_wrong_phone );
        }
        if ( errorMessage != null ){
            mProfilePhoneError.setText( errorMessage );
            showErrorMessage( mProfilePhoneError );
            return;
        }
        if( email.length() > 0 && !AppUtils.validateEmail( email ) ) {
            errorMessage = getResources().getString( R.string.error_wrong_email );
        }
        if( errorMessage != null ){
            mProfileEmailError.setText( errorMessage );
            showErrorMessage( mProfileEmailError );
            return;
        }
        mClient.setNickName( nickName );
        mClient.setPhone( phone );
        mClient.setEmail( email );

        AppUtils.transitionAnimation( getView().findViewById( R.id.editProfileScrollId ),
                getView().findViewById( R.id.pleaseWaitContainerId ) );
        new UpdateOurClientInfo().execute( mClient );
    }

    private void showErrorMessage( final View view ){
        view.setVisibility( View.VISIBLE );
        ( new Handler() ).postDelayed( () -> {
                view.setVisibility( View.GONE );
        }, 3000 );
    }

    @Override
    public void onAttach( Context context ) {
        super.onAttach( context );
        if ( context instanceof OnEditFragmentActionListener ) {
            mListener = ( OnEditFragmentActionListener ) context;
        } else {
            throw new RuntimeException( context.toString()
                    + " must implement OnEditFragmentActionListener" );
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        (( PersonActivity ) getActivity() ).getHeader().setVisibility( View.GONE );
    }

    @Override
    public void onPause() {
        super.onPause();
        (( PersonActivity ) getActivity() ).getHeader().setVisibility( View.VISIBLE );
    }

    private void updateAvatar(){
        int avatarLayoutVisibility = View.GONE;
        LinearLayout avatarLayout = getView().findViewById( R.id.editProfileLayoutAvatarId );
        TextView firstLatter = initTextView( R.id.editProfileFirstLetterId, AppConstants.ROBOTO_BLACK );
        if( AppUtils.nullOrEmpty( mClient.getPhoto() ) ){
            avatarLayoutVisibility = View.VISIBLE;
            int blueIndex = mClient.getNickName().hashCode() % 256;
            int color = Color.argb( 180, 83,91, blueIndex );
            avatarLayout.setBackgroundColor( color );
            firstLatter.setText( mClient.getNickName().substring( 0,1 ).toUpperCase() );
        } else {
            String url = mClient.getPhoto()+"?time="+( new Date() ).getTime();
            GlideClient.downloadImage( getActivity(), url,
                                    getView().findViewById( R.id.editProfileImageAvatarId ) );
        }
        avatarLayout.setVisibility( avatarLayoutVisibility );
    }

    @Override
    public void onClick( View view ) {
        AppUtils.clickAnimation( view );
        switch ( view.getId() ){
            case R.id.editProfileBackBtnId :
                getActivity().onBackPressed();
                break;
            case R.id.updateProfileBtnId:
                updateProfile();
                break;
            case R.id.signOutProfileBtnId:
                if ( mListener != null ) {
                    mListener.onSignOutFromEditAction();
                }
                break;
            case R.id.avatarCardViewId:
                if ( mListener != null ) {
                    mListener.onGetAvatarClick();
                }
                break;
            case R.id.removeProfileBtnId:
            case R.id.removeProfileBigBtnId:
                if ( mListener != null ) {
                    mListener.onRemoveClientFullyAction();
                }
                break;
        }
    }



    class UpdateOurClientInfo extends AsyncTask< OurClientModel, Void, String > {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground( OurClientModel... ourClient ) {
            String result = null;
            try {
                Call< ApiResponse> updateCall = RestController
                        .getApi().updateClientInfo( AppConstants.AUTH_BEARER
                                + getUserToken(), ourClient[ 0 ] );


                Response< ApiResponse > responseUpdate = updateCall.execute();
                if ( responseUpdate.body() != null ) {
                    if ( responseUpdate.body().getStatus() == 200 ) {
                        result = responseUpdate.body().getMessage();
                    } else {
                        result = responseUpdate.body().getMessage();
                    }
                } else {
                    result = getResources().getString( R.string.internal_error );
                }
            } catch ( Exception e ) {
                result = getResources().getString( R.string.internal_error );
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute( String result ) {
            super.onPostExecute( result );
            updateAvatar();
            AppUtils.transitionAnimation( getView().findViewById( R.id.pleaseWaitContainerId ),
                        getView().findViewById( R.id.editProfileScrollId ) );
            ModalMessage.show( getActivity(), getString( R.string.title_notifications ),
                    new String[]{ result }, 2000 );

        }
    }

    public interface OnEditFragmentActionListener {
        void onSignOutFromEditAction();
        void onGetAvatarClick();
        void onRemoveClientFullyAction();
    }

}
