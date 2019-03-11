package com.edanyma.fragment;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
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
import com.edanyma.utils.AppUtils;

import retrofit2.Call;
import retrofit2.Response;


public class SignUpFragment extends Fragment implements View.OnClickListener{

    private OnSignUpListener mListener;

    private AppCompatEditText mSignUpAuth;
    private AppCompatEditText mPassword;
    private AppCompatEditText mConfirmPassword;
    private TextView mPasswordErrorView;
    private TextView mSignInView;

    public SignUpFragment() {}

    public static SignUpFragment newInstance() {
        SignUpFragment fragment = new SignUpFragment();
        return fragment;
    }

    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
    }


    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState ) {

        return inflater.inflate( R.layout.fragment_sign_out, container, false );
    }

    @Override
    public void onActivityCreated( @Nullable Bundle savedInstanceState ) {
        super.onActivityCreated( savedInstanceState );
        ( ( TextView ) getView().findViewById( R.id.signUpTitleId ) ).setTypeface( AppConstants.SANDORA, Typeface.BOLD );
        ( (TextInputLayout)getView().findViewById( R.id.signUpTextLayoutId )).setTypeface( AppConstants.ROBOTO_CONDENCED );
        ( (TextInputLayout)getView().findViewById( R.id.signUpPasswordLayoutId )).setTypeface( AppConstants.ROBOTO_CONDENCED );
        ( (TextInputLayout)getView().findViewById( R.id.signUpConfirmLayoutId )).setTypeface( AppConstants.ROBOTO_CONDENCED );
        ( (TextView)getView().findViewById( R.id.otherSignInId )).setTypeface( AppConstants.ROBOTO_CONDENCED );

        mPasswordErrorView = getView().findViewById( R.id.confirmErrorFieldId );

        Button signButton = getView().findViewById( R.id.signUpButtonId );
        signButton.setTypeface( AppConstants.SANDORA );
        signButton.setOnClickListener( this );

        mSignInView = getView().findViewById( R.id.signInId );
        mSignInView.setTypeface( AppConstants.ROBOTO_CONDENCED );
        mSignInView.setOnClickListener( this );


        mSignUpAuth = getView().findViewById( R.id.signUpAuthId );
        mPassword = getView().findViewById( R.id.signUpPasswordId );
        mConfirmPassword = getView().findViewById( R.id.signUpConfirmId );
        mSignUpAuth.setTypeface( AppConstants.ROBOTO_CONDENCED );
        mPassword.setTypeface( AppConstants.ROBOTO_CONDENCED );
        mConfirmPassword.setTypeface( AppConstants.ROBOTO_CONDENCED );

    }


    @Override
    public void onAttach( Context context ) {
        super.onAttach( context );
        if ( context instanceof OnSignUpListener ) {
            mListener = ( OnSignUpListener ) context;
        } else {
            throw new RuntimeException( context.toString()
                    + " must implement OnSignUpAListener" );
        }
    }

    public void onStartSignIn(){
        if ( mListener != null ) {
            mListener.OnSignInListener();
        }
    }

    private void clickSignIn(){
        final int colorFrom = getResources().getColor(R.color.blueNeon);
        final int colorTo = getResources().getColor(R.color.colorAccent);
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                int currentValue = (Integer)animator.getAnimatedValue();
                mSignInView.setTextColor( currentValue );
                if( colorTo == currentValue){
                    onStartSignIn();
                }
            }
        });
        colorAnimation.setDuration( 300 );
        colorAnimation.start();
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick( View view ) {
        switch( view.getId()){
            case R.id.signUpButtonId:
                signUpViaEmailPhone();
                break;
            case R.id.signInId:
                clickSignIn();
                break;
            default:
                break;

        }
    }

    private void signUpViaEmailPhone() {
        OurClientModel clientModel = new OurClientModel();
        String signUpAuth = mSignUpAuth.getText().toString();
        String password = mPassword.getText().toString();
        String confirm = mConfirmPassword.getText().toString();
        final TextView loginErrorView = getView().findViewById( R.id.signUpErrorFieldId );
        String authErrorMsg = null;
        if ( signUpAuth.trim().length() == 0 ) {
            authErrorMsg = getResources().getString( R.string.error_required_field );
        }
        if ( authErrorMsg == null && signUpAuth.indexOf( "@" ) > 0 && !AppUtils.validateEmail( signUpAuth ) ) {
            authErrorMsg = getResources().getString( R.string.error_wrong_email );
        }
        if ( authErrorMsg == null && signUpAuth.indexOf( "@" ) == -1 && !AppUtils.validatePhone( signUpAuth ) ) {
            authErrorMsg = getResources().getString( R.string.error_wrong_phone );
        }
        if( signUpAuth.indexOf( "@" ) > 0 ){
            clientModel.setEmail( signUpAuth );
        } else {
            clientModel.setPhone( signUpAuth );
        }
        if ( authErrorMsg != null ) {
            loginErrorView.setText( authErrorMsg );
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
        if( !password.equals( confirm ) ){
            mPasswordErrorView.setText( "Пароли не совпадают" );
            showPasswordError();
            return;
        }
        clientModel.setPassword( password );
        new SignUpFragment.ClientSignUp().execute( clientModel );
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

    public void onSignUpSuccess( ) {
        if ( mListener != null ) {
            mListener.OnSignUpListener();
        }
    }

    public interface OnSignUpListener {
        void OnSignUpListener();
        void OnSignInListener();
    }

    private class ClientSignUp extends AsyncTask< OurClientModel, Void, String > {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground( OurClientModel... ourClient ) {
            String result = null;
            try {
                Call< ApiResponse > signUpCall = RestController.getInstance()
                        .getApi().signUp( AppConstants.AUTH_BEARER
                                + GlobalManager.getInstance().getUserToken(), ourClient[ 0 ] );


                Response< ApiResponse > responseSignUp = signUpCall.execute();
                if ( responseSignUp.body() != null ) {
                    if( responseSignUp.body().getStatus() == 200 ){
                        GlobalManager.getInstance().setUserUUID( (String) responseSignUp.body().getResult() );
                    } else {
                        result = responseSignUp.body().getMessage();
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
            if ( result != null ) {
                mPasswordErrorView.setText( result );
                showPasswordError();
            } else {
                onSignUpSuccess();
            }
        }
    }
}
