package com.edanyma.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.edanyma.AppConstants;
import com.edanyma.R;
import com.edanyma.manager.GlobalManager;
import com.edanyma.model.ApiResponse;
import com.edanyma.model.OurClientModel;
import com.edanyma.rest.RestController;
import com.edanyma.utils.AppUtils;

import retrofit2.Call;
import retrofit2.Response;


public class SignInFragment extends BaseFragment implements View.OnClickListener {

    private final String TAG = "SignInFragment";

    private OnSignInListener mListener;

    private AppCompatEditText mLogin;
    private AppCompatEditText mPassword;

    private TextView mPasswordErrorView;
    private TextView mSignUpView;

    public SignInFragment() {}


    public static SignInFragment newInstance() {
        SignInFragment fragment = new SignInFragment();
        return fragment;
    }

    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState ) {
        return inflater.inflate( R.layout.fragment_sign_in, container, false );
    }

    @Override
    public void onActivityCreated( @Nullable Bundle savedInstanceState ) {
        super.onActivityCreated( savedInstanceState );
        initTextView( R.id.loginTitleId, AppConstants.B52  );
        initTextView( R.id.otherLoginId, AppConstants.ROBOTO_CONDENCED  );
        initTextView( R.id.notSignUpId, AppConstants.ROBOTO_CONDENCED  );
        ( (TextInputLayout)getView().findViewById( R.id.loginTextLayoutId )).setTypeface( AppConstants.ROBOTO_CONDENCED );
        ( (TextInputLayout)getView().findViewById( R.id.passwordTextLayoutId ) ).setTypeface( AppConstants.ROBOTO_CONDENCED );

        Button signButton = getView().findViewById( R.id.signInButtonId );
        signButton.setTypeface( AppConstants.SANDORA );
        signButton.setOnClickListener( this );

        initTextView( R.id.forgotPasswordId, AppConstants.ROBOTO_CONDENCED ).setOnClickListener( this );
        mSignUpView = initTextView(  R.id.signUpId , AppConstants.ROBOTO_CONDENCED );
        mSignUpView.setOnClickListener( this );


        mLogin = getView().findViewById( R.id.emailPhoneId );
        mPassword = getView().findViewById( R.id.passwordId );
        mLogin.setTypeface( AppConstants.ROBOTO_CONDENCED );
        mPassword.setTypeface( AppConstants.ROBOTO_CONDENCED );

        ImageButton btn = getActivity().findViewById( R.id.navButtonId );
        btn.setOnClickListener( this );

    }

    public void onSignInSuccess( ) {
        if ( mListener != null ) {
            mListener.onSignInAction( );
        }
    }

    public void onStartSignUp(View view){
        if ( mListener != null ) {
            AppUtils.clickAnimation( view );
            new Handler().postDelayed( new Runnable() {
                @Override
                public void run() {
                    mListener.onStartSignUpAction();
                }
            }, 300 );
        }
    }

    public void onForgotPassword( View view ){
        if ( mListener != null ) {
            AppUtils.clickAnimation( view );
            new Handler().postDelayed( new Runnable() {
                @Override
                public void run() {
                    mListener.onForgotPasswordAction();
                }
            },300 );
        }
    }

    @Override
    public void onAttach( Context context ) {
        super.onAttach( context );
        if ( context instanceof OnSignInListener ) {
            mListener = ( OnSignInListener ) context;
        } else {
            throw new RuntimeException( context.toString()
                    + " must implement onSignInListener" );
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnSignInListener {
        void onSignInAction( );
        void onStartSignUpAction();
        void onForgotPasswordAction();
    }

    @Override
    public void onClick( View view ) {
        switch( view.getId()){
            case R.id.forgotPasswordId:
                onForgotPassword( view );
                break;
            case R.id.signInButtonId:
                loginViaEmailPhone();
                break;
            case R.id.navButtonId:
                getActivity().onBackPressed();
                break;
            case R.id.signUpId:
                onStartSignUp( view );
                break;
                default:
                    break;

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
        if ( loginErrorMsg == null && login.indexOf( "@" ) > 0 && !AppUtils.validateEmail( login ) ) {
            loginErrorMsg = getResources().getString( R.string.error_wrong_email );
        }
        if ( loginErrorMsg == null && login.indexOf( "@" ) == -1 && !AppUtils.validatePhone( login ) ) {
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
                Call< ApiResponse<OurClientModel> > signInCall = RestController.getInstance()
                        .getApi().signIn( AppConstants.AUTH_BEARER
                                + GlobalManager.getInstance().getUserToken(), ourClient[ 0 ] );


                Response< ApiResponse<OurClientModel> > responseSignIn = signInCall.execute();
                if ( responseSignIn.body() != null ) {
                    if( responseSignIn.body().getStatus() == 200 ){
                      GlobalManager.getInstance().setClient( responseSignIn.body().getResult() );
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
