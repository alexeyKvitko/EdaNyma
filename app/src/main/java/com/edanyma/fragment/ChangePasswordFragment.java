package com.edanyma.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.edanyma.AppConstants;
import com.edanyma.R;
import com.edanyma.model.ApiResponse;
import com.edanyma.model.OurClientModel;
import com.edanyma.owncomponent.ModalMessage;
import com.edanyma.rest.RestController;
import com.edanyma.utils.AppUtils;

import org.mindrot.jbcrypt.BCrypt;

import retrofit2.Call;
import retrofit2.Response;

import static com.edanyma.manager.GlobalManager.*;

public class ChangePasswordFragment extends BaseFragment {

    private AppCompatEditText mOldPassword;
    private AppCompatEditText mNewPassword;
    private AppCompatEditText mConfirmPassword;

    private TextView mOldPasswordError;
    private TextView mNewPasswordError;
    private TextView mConfirmPasswordError;

    public ChangePasswordFragment() {}

    public static ChangePasswordFragment newInstance( ) {
        ChangePasswordFragment fragment = new ChangePasswordFragment();
        return fragment;
    }

    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );

    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState ) {
            return inflater.inflate( R.layout.fragment_change_password, container, false );
    }

    @Override
    public void onActivityCreated( @Nullable Bundle savedInstanceState ) {
        super.onActivityCreated( savedInstanceState );

        initTextView( R.id.changePasswordTitleId, AppConstants.B52 );
        initTextInputLayout( R.id.changePasswordOldId, AppConstants.ROBOTO_CONDENCED );
        initTextInputLayout( R.id.changePasswordNewId, AppConstants.ROBOTO_CONDENCED );
        initTextInputLayout( R.id.changePasswordConfirmId, AppConstants.ROBOTO_CONDENCED );

        mOldPassword = initEditText( R.id.changePasswordOldValueId, AppConstants.ROBOTO_CONDENCED );
        mNewPassword = initEditText( R.id.changePasswordNewValueId, AppConstants.ROBOTO_CONDENCED );
        mConfirmPassword = initEditText( R.id.changePasswordConfirmValueId, AppConstants.ROBOTO_CONDENCED );

        mOldPasswordError = initTextView( R.id.changePasswordOldErrorId, AppConstants.ROBOTO_CONDENCED );
        mNewPasswordError = initTextView( R.id.changePasswordNewErrorId, AppConstants.ROBOTO_CONDENCED );
        mConfirmPasswordError = initTextView( R.id.changePasswordConfirmErrorId, AppConstants.ROBOTO_CONDENCED );

        initButton( R.id.updatePasswordBtnId, AppConstants.ROBOTO_CONDENCED )
                .setOnClickListener( (View view ) -> {
                    AppUtils.clickAnimation( view );
                    updatePassword();
        } );
        initButton( R.id.changePasswordCancelBtnId, AppConstants.ROBOTO_CONDENCED ).setOnClickListener( (View view ) -> {
            AppUtils.clickAnimation( view );
            getActivity().onBackPressed();
        } );
    }

    private void updatePassword(){
        OurClientModel client = getClient();
        String oldPassword = mOldPassword.getText().toString().trim();
        String newPassword = mNewPassword.getText().toString().trim();
        String confirmPassword = mConfirmPassword.getText().toString().trim();
        String errorMessage = null;
        if( oldPassword.length() == 0 ){
            errorMessage = getActivity().getResources().getString( R.string.error_required_field );
        }
        if ( errorMessage == null && !BCrypt.checkpw( oldPassword, client.getPassword() ) ){
            errorMessage = getActivity().getResources().getString( R.string.error_wrong_password );
        }
        if ( errorMessage != null ){
            AppUtils.showError( mOldPasswordError, errorMessage, mOldPasswordError );
            return;
        }
        if( newPassword.length() == 0 ){
            errorMessage = getActivity().getResources().getString( R.string.error_required_field );
            AppUtils.showError( mNewPasswordError, errorMessage, mNewPasswordError );
            return;
        }
        if( confirmPassword.length() == 0 ){
            errorMessage = getActivity().getResources().getString( R.string.error_required_field );
            AppUtils.showError( mConfirmPasswordError, errorMessage, mConfirmPasswordError );
            return;
        }
        if( !newPassword.equals( confirmPassword ) ){
            errorMessage = getActivity().getResources().getString( R.string.password_not_match );
            AppUtils.showError( mConfirmPasswordError, errorMessage, mConfirmPasswordError );
            return;
        }
        client.setPassword( newPassword );
        AppUtils.transitionAnimation( getView().findViewById( R.id.changePasswordContainerId ),
                getView().findViewById( R.id.pleaseWaitContainerId ) );
        new UpdateOurClientPassword().execute( client );
    }

    class UpdateOurClientPassword extends AsyncTask< OurClientModel, Void, String > {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground( OurClientModel... ourClient ) {
            String result = null;
            try {
                Call< ApiResponse > updateCall = RestController.getInstance()
                        .getApi().updateClientPassword( AppConstants.AUTH_BEARER
                                + getInstance().getUserToken(), ourClient[ 0 ] );


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
            AppUtils.transitionAnimation( getView().findViewById( R.id.pleaseWaitContainerId ),
                    getView().findViewById( R.id.changePasswordContainerId ) );
            ModalMessage.show( getActivity(), getString( R.string.title_notifications ),
                    new String[]{ result }, 2000 );
            new Handler( ).postDelayed( () ->{
                getActivity().onBackPressed();
            },2100 );
        }
    }


}
