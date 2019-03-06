package com.edanyma.fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.edanyma.AppConstants;
import com.edanyma.R;
import com.edanyma.manager.GlobalManager;
import com.edanyma.model.ApiResponse;
import com.edanyma.model.OurClientModel;
import com.edanyma.rest.RestController;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Response;


public class LoginFragment extends Fragment implements View.OnClickListener {

    private final String TAG = "LoginFragment";

    private OnSignInListener mListener;

    private AppCompatEditText mLogin;
    private AppCompatEditText mPassword;
    private TextInputLayout mLoginLayout;
    private TextInputLayout mPasswordLayout;
    private TextView mPasswordErrorView;

    public LoginFragment() {}


    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState ) {
        return inflater.inflate( R.layout.fragment_login, container, false );
    }

    @Override
    public void onActivityCreated( @Nullable Bundle savedInstanceState ) {
        super.onActivityCreated( savedInstanceState );
        ( ( TextView ) getView().findViewById( R.id.loginTitleId ) ).setTypeface( AppConstants.SANDORA, Typeface.BOLD );
        ( ( TextView ) getView().findViewById( R.id.forgotPassword ) ).setTypeface( AppConstants.ROBOTO_CONDENCED );
        ( ( TextView ) getView().findViewById( R.id.otherLognId ) ).setTypeface( AppConstants.ROBOTO_CONDENCED );
        mLoginLayout = getView().findViewById( R.id.loginTextLayoutId );
        mLoginLayout.setTypeface( AppConstants.ROBOTO_CONDENCED );
        mPasswordLayout = getView().findViewById( R.id.passwordTextLayoutId );
        mPasswordLayout.setTypeface( AppConstants.ROBOTO_CONDENCED );
        Button signButton = getView().findViewById( R.id.signInButtonId );
        signButton.setTypeface( AppConstants.SANDORA );
        signButton.setOnClickListener( this );

        mLogin = getView().findViewById( R.id.emailPhoneId );
        mPassword = getView().findViewById( R.id.passwordId );
        mLogin.setTypeface( AppConstants.ROBOTO_CONDENCED );
        mPassword.setTypeface( AppConstants.ROBOTO_CONDENCED );

    }

    public void onSignInSuccess( ) {
        if ( mListener != null ) {
            mListener.onLoginAction( );
        }
    }

    @Override
    public void onAttach( Context context ) {
        super.onAttach( context );
        if ( context instanceof OnSignInListener ) {
            mListener = ( OnSignInListener ) context;
        } else {
            throw new RuntimeException( context.toString()
                    + " must implement OnSignInListener" );
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnSignInListener {
        void onLoginAction( );
    }


    public boolean validateEmail( String email ) {
        Pattern pattern = Pattern.compile( AppConstants.EMAIL_PATTERN );
        return pattern.matcher( email ).matches();
    }

    public boolean validatePhone( String source ) {
        String phone = source.replaceAll(" ","").replace("(","").replace( ")","" )
                .replace( "+","" ).replaceAll( "-","" );
        Pattern pattern = Pattern.compile( AppConstants.PHONE_PATTERN );
        return pattern.matcher( phone ).matches();
    }

    @Override
    public void onClick( View view ) {
        if ( view.getId() == R.id.signInButtonId ) {
            loginViaEmailPhone();
        }
    }

    private void loginViaEmailPhone() {
        OurClientModel clientModel = new OurClientModel();
        String login = mLogin.getText().toString();
        String password = mPassword.getText().toString();
        final TextView loginErrorView = getView().findViewById( R.id.loginErrorFieldId );
        mPasswordErrorView = getView().findViewById( R.id.passwordErrorFieldId );
        String loginErrorMsg = null;
        if ( login.trim().length() == 0 ) {
            loginErrorMsg = getResources().getString( R.string.error_required_field );
        }
        if ( loginErrorMsg == null && login.indexOf( "@" ) > 0 && !validateEmail( login ) ) {
            loginErrorMsg = getResources().getString( R.string.error_wrong_email );
        }
        if ( loginErrorMsg == null && login.indexOf( "@" ) == -1 && !validatePhone( login ) ) {
            loginErrorMsg = getResources().getString( R.string.error_wrong_phone );
        }
        if( login.indexOf( "@" ) > 0 ){
           clientModel.setEmail( login );
        } else {
            clientModel.setPhone( login );
        }
        if ( loginErrorMsg != null ) {
            loginErrorView.setText( loginErrorMsg );
            loginErrorView.setVisibility( View.VISIBLE );
            ( new Handler() ).postDelayed( new Runnable() {
                @Override
                public void run() {
                    loginErrorView.setVisibility( View.GONE );
                }
            }, 3000 );
            return;
        }
        if ( password.trim().length() == 0 ) {
            showPasswordError();
            return;
        }
        clientModel.setPassword( password );
        new ClientSignIn().execute( clientModel );
    }

    private void showPasswordError(){
        mPasswordErrorView.setVisibility( View.VISIBLE );
        ( new Handler() ).postDelayed( new Runnable() {
            @Override
            public void run() {
                mPasswordErrorView.setVisibility( View.GONE );
            }
        }, 3000 );
    }

    private class ClientSignIn extends AsyncTask< OurClientModel, Void, String > {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground( OurClientModel... ourClient ) {
            String result = null;
            try {
                Call< ApiResponse > signInCall = RestController.getInstance()
                        .getApi().signIn( AppConstants.AUTH_BEARER
                                + GlobalManager.getInstance().getUserToken(), ourClient[ 0 ] );
                Response< ApiResponse > responseSignIn = signInCall.execute();
                if ( responseSignIn.body() != null ) {
                    if( responseSignIn.body().getStatus() == 200 ){
                      GlobalManager.getInstance().setUserUUID( (String) responseSignIn.body().getResult() );
                    } else {
                        result = responseSignIn.body().getMessage();
                    }
                } else {
                    result = getResources().getString( R.string.internal_error );
                }
            } catch ( Exception e ) {
                result = getResources().getString( R.string.internal_error );
                Log.i( TAG, e.getMessage() );
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute( String result ) {
            super.onPostExecute( result );
            if ( result != null ) {
                mPasswordErrorView.setText( result );
                showPasswordError();
            } else {
                onSignInSuccess();
            }
        }
    }
}
