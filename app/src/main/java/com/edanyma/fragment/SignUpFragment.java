package com.edanyma.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.telephony.SmsManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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


public class SignUpFragment extends Fragment implements View.OnClickListener, View.OnFocusChangeListener, View.OnKeyListener {

    private final String TAG = "SignUpFragment";

    private static final String IS_NEW_OR_FORGOT_PARAM = "isNewOrForgot";

    private OnSignUpListener mListener;

    private String mIsNewOrForgot;

    private AppCompatEditText mSignUpAuth;
    private AppCompatEditText mPassword;
    private AppCompatEditText mConfirmPassword;


    private AppCompatEditText mDigitOne;
    private AppCompatEditText mDigitTwo;
    private AppCompatEditText mDigitThree;
    private AppCompatEditText mDigitFour;

    private TextView mPasswordErrorView;
    private TextView mSignInView;

    private TextView mResendCodeValue;

    private String mConfirmationCode;

    private OurClientModel mClientModel;

    private int mSecondLeft;

    private Handler mCountdown;

    private Runnable mCountdownJob;

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

        return inflater.inflate( R.layout.fragment_sign_out, container, false );
    }

    @Override
    public void onActivityCreated( @Nullable Bundle savedInstanceState ) {
        super.onActivityCreated( savedInstanceState );
        ( ( TextView ) getView().findViewById( R.id.signUpTitleId ) ).setTypeface( AppConstants.B52 );
        ( ( TextView ) getView().findViewById( R.id.confirmCodeTitleId ) ).setTypeface( AppConstants.B52 );
        ( ( TextInputLayout ) getView().findViewById( R.id.signUpTextLayoutId ) ).setTypeface( AppConstants.ROBOTO_CONDENCED );
        ( ( TextInputLayout ) getView().findViewById( R.id.signUpPasswordLayoutId ) ).setTypeface( AppConstants.ROBOTO_CONDENCED );
        ( ( TextInputLayout ) getView().findViewById( R.id.signUpConfirmLayoutId ) ).setTypeface( AppConstants.ROBOTO_CONDENCED );
        ( ( TextView ) getView().findViewById( R.id.otherSignInId ) ).setTypeface( AppConstants.ROBOTO_CONDENCED );
        ( ( TextView ) getView().findViewById( R.id.sendConfirmCode ) ).setTypeface( AppConstants.ROBOTO_CONDENCED );
        ( ( TextView ) getView().findViewById( R.id.resendCodeLabelId ) ).setTypeface( AppConstants.ROBOTO_CONDENCED );
        ( ( TextView ) getView().findViewById( R.id.successTopId ) ).setTypeface( AppConstants.B52 );
        ( ( TextView ) getView().findViewById( R.id.successBottomId ) ).setTypeface( AppConstants.B52 );
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


        mSignUpAuth = getView().findViewById( R.id.signUpAuthId );
        mPassword = getView().findViewById( R.id.signUpPasswordId );
        mConfirmPassword = getView().findViewById( R.id.signUpConfirmId );
        mSignUpAuth.setTypeface( AppConstants.ROBOTO_CONDENCED );
        mPassword.setTypeface( AppConstants.ROBOTO_CONDENCED );
        mConfirmPassword.setTypeface( AppConstants.ROBOTO_CONDENCED );

        mDigitOne = getView().findViewById( R.id.confirmDigitOneId );
        mDigitTwo = getView().findViewById( R.id.confirmDigitTwoId );
        mDigitThree = getView().findViewById( R.id.confirmDigitThreeId );
        mDigitFour = getView().findViewById( R.id.confirmDigitFourId );

        mDigitOne.setOnFocusChangeListener( this );
        mDigitOne.setOnKeyListener( this );

        mDigitTwo.setOnFocusChangeListener( this );
        mDigitTwo.setOnKeyListener( this );

        mDigitThree.setOnFocusChangeListener( this );
        mDigitThree.setOnKeyListener( this );

        mDigitFour.setOnFocusChangeListener( this );
        mDigitFour.setOnKeyListener( this );

        mResendCodeValue = getView().findViewById( R.id.resendCodeValueId );
        mResendCodeValue.setTypeface( AppConstants.ROBOTO_CONDENCED );
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

    private void startCountdown() {
        mSecondLeft = 90;
        mCountdown = new Handler();
        mCountdownJob = new Runnable() {
            @Override
            public void run() {
                mResendCodeValue.setText( mSecondLeft + "" );
                if ( mSecondLeft == 70 ) {
                    getView().findViewById( R.id.resendCodeLabelId ).setVisibility( View.VISIBLE );
                }
                if ( mSecondLeft == 0 ) {
                    mCountdown.removeCallbacks( this );
                    AppUtils.transitionAnimation( getView().findViewById( R.id.confirmCodeContainerId ),
                            getView().findViewById( R.id.signUpContailnerId ) );
                    return;
                }
                mSecondLeft--;
                mCountdown.postDelayed( this, 1000 );
            }
        };
        mCountdown.postDelayed( mCountdownJob, 1000 );
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
        if ( mCountdown != null && mCountdownJob != null ){
            mCountdown.removeCallbacks( mCountdownJob );
        }
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
        String confirmCode = mDigitOne.getText().toString()
                + mDigitTwo.getText().toString()
                + mDigitThree.getText().toString()
                + mDigitFour.getText().toString();
        if ( !mConfirmationCode.equals( confirmCode ) ) {
            getView().findViewById( R.id.confirmCodeErrorId ).setVisibility( View.VISIBLE );
            ( new Handler() ).postDelayed( new Runnable() {
                @Override
                public void run() {
                    getView().findViewById( R.id.confirmCodeErrorId ).setVisibility( View.GONE );
                }
            }, 10000 );
            return;
        }
        new SignUpFragment.ClientSignUp().execute( mClientModel );
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

    @Override
    public void onFocusChange( View view, boolean isFocused ) {
        if ( isFocused
                && AppConstants.ASTERISKS.equals( ( ( AppCompatEditText ) view ).getText().toString() ) ) {
            ( ( AppCompatEditText ) view ).setText( "" );
        }
    }

    @Override
    public boolean onKey( View view, int i, KeyEvent keyEvent ) {
        if ( i != 67 && KeyEvent.ACTION_DOWN == keyEvent.getAction()) {
            switch ( view.getId()  ) {
                case R.id.confirmDigitOneId:
                    mDigitTwo.requestFocus();
                    break;
                case R.id.confirmDigitTwoId:
                    mDigitThree.requestFocus();
                    break;
                case R.id.confirmDigitThreeId:
                    mDigitFour.requestFocus();
                    break;
                case R.id.confirmDigitFourId:
                    mDigitOne.requestFocus();
                    break;
                default:
                    break;
            }
        }
        return false;
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
                        mConfirmationCode = ( String ) responseValidate.body().getResult();
                        if ( AppConstants.SEND_PHONE_CODE.equals( mConfirmationCode ) ) {
                            mConfirmationCode = AppUtils.getRandomBetweenRange( 4000, 9999 ) + "";
                            SmsManager.getDefault().sendTextMessage( "+7" + mClientModel.getPhone(),
                                    "ЕдаНяма", "Код регистрации: " + mConfirmationCode, null, null );
                        }
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
