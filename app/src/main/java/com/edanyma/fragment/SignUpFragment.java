package com.edanyma.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
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


public class SignUpFragment extends ConfirmFragment implements View.OnClickListener {

    private final String TAG = "SignUpFragment";

    private static final String IS_NEW_OR_FORGOT_PARAM = "isNewOrForgot";

    private OnSignUpListener mListener;

    private String mIsNewOrForgot;

    private AppCompatEditText mSignUpAuth;
    private AppCompatEditText mPassword;
    private AppCompatEditText mConfirmPassword;


    private TextView mPasswordErrorView;
    private TextView mSignInView;


    private OurClientModel mClientModel;



    public SignUpFragment() {
    }

    public static SignUpFragment newInstance( String param ) {
        SignUpFragment fragment = new SignUpFragment();
        Bundle args = new Bundle();
        args.putString( IS_NEW_OR_FORGOT_PARAM, param );
        fragment.setArguments( args );
        return fragment;
    }

    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        if ( getArguments() != null ) {
            mIsNewOrForgot = getArguments().getString( IS_NEW_OR_FORGOT_PARAM );
        }
    }


    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState ) {

        return inflater.inflate( R.layout.fragment_sign_up, container, false );
    }

    @Override
    public void onActivityCreated( @Nullable Bundle savedInstanceState ) {
        super.onActivityCreated( savedInstanceState );
        initConfirmFragment();
        initTextView( R.id.signUpTitleId , AppConstants.B52 );
        initTextView( R.id.confirmCodeTitleId , AppConstants.B52 );
        initTextInputLayout( R.id.signUpTextLayoutId , AppConstants.ROBOTO_CONDENCED );
        initTextInputLayout( R.id.signUpPasswordLayoutId, AppConstants.ROBOTO_CONDENCED );
        initTextInputLayout( R.id.signUpConfirmLayoutId, AppConstants.ROBOTO_CONDENCED );
        initTextView( R.id.otherSignInId, AppConstants.ROBOTO_CONDENCED );
        initTextView( R.id.sendConfirmCode, AppConstants.ROBOTO_CONDENCED );
        initTextView( R.id.resendCodeLabelId, AppConstants.ROBOTO_CONDENCED );
        initTextView( R.id.successTopId, AppConstants.B52 );
        initTextView( R.id.successBottomId, AppConstants.B52 );
        getView().findViewById( R.id.resendCodeLabelId ).setOnClickListener( this );

        mPasswordErrorView = getView().findViewById( R.id.confirmErrorFieldId );

        Button signButton = getView().findViewById( R.id.signUpButtonId );
        signButton.setTypeface( AppConstants.SANDORA );
        signButton.setOnClickListener( this );

        Button confirmSignUp = getView().findViewById( R.id.confirmCodeButtonId );
        confirmSignUp.setTypeface( AppConstants.SANDORA );
        confirmSignUp.setOnClickListener( this );

        mSignInView = getView().findViewById( R.id.signInId );
        mSignInView.setTypeface( AppConstants.ROBOTO_CONDENCED );
        mSignInView.setOnClickListener( this );


        mSignUpAuth = initEditText( R.id.signUpAuthId, AppConstants.ROBOTO_CONDENCED );
        mPassword = initEditText( R.id.signUpPasswordId, AppConstants.ROBOTO_CONDENCED );
        mConfirmPassword = initEditText( R.id.signUpConfirmId, AppConstants.ROBOTO_CONDENCED );

        getActivity().findViewById( R.id.navButtonId ).setOnClickListener( this );

        if( AppConstants.FORGOT_PASSWORD.equals( getArguments().getString( IS_NEW_OR_FORGOT_PARAM ) ) ){
            hideUnnecessaryViews();
        }
    }

    private void hideUnnecessaryViews(){
        getView().findViewById( R.id.signInId ).setVisibility( View.GONE );
        getView().findViewById( R.id.signOutLineId ).setVisibility( View.GONE );
        getView().findViewById( R.id.otherSignInId ).setVisibility( View.GONE );
        getView().findViewById( R.id.signWithGoogleId ).setVisibility( View.GONE );
        ( ( TextView ) getView().findViewById( R.id.signUpTitleId ) )
                                .setText( getResources().getString( R.string.fogot_password ) );
        ( ( TextView ) getView().findViewById( R.id.successBottomId ) )
                                .setText( getResources().getString( R.string.success_change_password ) );
        ( ( TextInputLayout ) getView().findViewById( R.id.signUpPasswordLayoutId ) )
                                    .setHint( getResources().getString( R.string.new_password ) );
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

    public void onStartSignIn() {
        if ( mListener != null ) {
            mListener.onSignInListener();
        }
    }

    private void clickSignIn( View view ) {
        AppUtils.clickAnimation( view );
        new Handler().postDelayed( new Runnable() {
            @Override
            public void run() {
                onStartSignIn();
            }
        }, 300 );
    }

    private void clickResendCode( View view ) {
        AppUtils.clickAnimation( view );
        new Handler().postDelayed( new Runnable() {
            @Override
            public void run() {
                new SignUpFragment.ValidateClientSignUp().execute( mClientModel );
            }
        }, 300 );
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onClick( View view ) {
        switch ( view.getId() ) {
            case R.id.signUpButtonId:
                signUpViaEmailPhone();
                break;
            case R.id.confirmCodeButtonId:
                AppUtils.hideKeyboardFrom( getActivity(), view );
                confirmEmailPhone();
                break;
            case R.id.resendCodeLabelId:
                clickResendCode( view );
                break;
            case R.id.signInId:
                clickSignIn( view );
                break;
            case R.id.navButtonId:
                getActivity().onBackPressed();
                break;
            default:
                break;

        }
    }

    private void confirmEmailPhone() {
        if ( checkEnteredCode() ){
            new SignUpFragment.ClientSignUp().execute( mClientModel );
        }
    }

    private void signUpViaEmailPhone() {
        mClientModel = new OurClientModel();
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
        if ( signUpAuth.indexOf( "@" ) > 0 ) {
            mClientModel.setEmail( signUpAuth );
        } else {
            mClientModel.setPhone( signUpAuth );
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
        if ( !password.equals( confirm ) ) {
            mPasswordErrorView.setText( "Пароли не совпадают" );
            showPasswordError();
            return;
        }
        mClientModel.setPassword( password );
        if ( AppConstants.FORGOT_PASSWORD.equals( mIsNewOrForgot ) ){
            mClientModel.setAdditionalMessage( AppConstants.FORGOT_PASSWORD );
        }
        AppUtils.transitionAnimation( getView().findViewById( R.id.signUpContailnerId ),
                getView().findViewById( R.id.pleaseWaitContainerId ) );
        new SignUpFragment.ValidateClientSignUp().execute( mClientModel );

    }

    private void showPasswordError() {
        mPasswordErrorView.setVisibility( View.VISIBLE );
        ( new Handler() ).postDelayed( new Runnable() {
            @Override
            public void run() {
                mPasswordErrorView.setVisibility( View.GONE );
            }
        }, 3000 );
    }

    public void onSignUpSuccess() {
        if ( mListener != null ) {
            AppUtils.transitionAnimation( getView().findViewById( R.id.confirmCodeContainerId ),
                    getView().findViewById( R.id.successContainerId ) );
            new Handler().postDelayed( new Runnable() {
                @Override
                public void run() {
                    mListener.onSignUpAction();
                }
            }, 3000 );

        }
    }


    public interface OnSignUpListener {
        void onSignUpAction();
        void onSignInListener();
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
                Call< ApiResponse<OurClientModel> > signUpCall = RestController.getInstance()
                        .getApi().signUp( AppConstants.AUTH_BEARER
                                + GlobalManager.getInstance().getUserToken(), ourClient[ 0 ] );


                Response< ApiResponse<OurClientModel> > responseSignUp = signUpCall.execute();
                if ( responseSignUp.body() != null ) {
                    if ( responseSignUp.body().getStatus() == 200 ) {
                        GlobalManager.getInstance().setClient( responseSignUp.body().getResult() );
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

    private class ValidateClientSignUp extends AsyncTask< OurClientModel, Void, String > {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground( OurClientModel... ourClient ) {
            String result = null;
            try {
                Call< ApiResponse<String> > validateCall = RestController.getInstance()
                        .getApi().validateAndSendEmail( AppConstants.AUTH_BEARER
                                + GlobalManager.getInstance().getUserToken(), ourClient[ 0 ] );


                Response< ApiResponse<String> > responseValidate = validateCall.execute();
                if ( responseValidate.body() != null ) {
                    if ( responseValidate.body().getStatus() == 200 ) {
//                        mConfirmationCode = ( String ) responseValidate.body().getResult();
//                        if ( AppConstants.SEND_PHONE_CODE.equals( mConfirmationCode ) ) {
//                            mConfirmationCode = AppUtils.getRandomBetweenRange( 4000, 9999 ) + "";
//                            SmsManager.getDefault().sendTextMessage( "+7" + mClientModel.getPhone(),
//                                    "ЕдаНяма", "Код регистрации: " + mConfirmationCode, null, null );
//                        }
                    } else {
                        result = responseValidate.body().getMessage();
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
                AppUtils.transitionAnimation( getView().findViewById( R.id.pleaseWaitContainerId ),
                        getView().findViewById( R.id.signUpContailnerId ) );
                mPasswordErrorView.setText( result );
                showPasswordError();
            } else {
                AppUtils.transitionAnimation( getView().findViewById( R.id.pleaseWaitContainerId ),
                        getView().findViewById( R.id.confirmCodeContainerId ) );
                startCountdown();
            }
        }
    }
}
