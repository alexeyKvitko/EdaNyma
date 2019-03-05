package com.edanyma.fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.edanyma.AppConstants;
import com.edanyma.R;

import java.util.regex.Pattern;


public class LoginFragment extends Fragment {

    private OnLoginListener mListener;

    private TextInputEditText mLogin;
    private TextInputEditText mPassword;

    public LoginFragment() {
    }


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
        ( ( TextInputLayout ) getView().findViewById( R.id.loginTextLayoutId ) ).setTypeface( AppConstants.ROBOTO_CONDENCED );
        ( ( TextInputLayout ) getView().findViewById( R.id.passwordTextLayoutId ) ).setTypeface( AppConstants.ROBOTO_CONDENCED );
        ( ( Button ) getView().findViewById( R.id.signInButtonId ) ).setTypeface( AppConstants.SANDORA );

        mLogin = getView().findViewById( R.id.emailPhoneId );
        mPassword = getView().findViewById( R.id.passwordId );
        mLogin.setTypeface( AppConstants.ROBOTO_CONDENCED );
        mPassword.setTypeface( AppConstants.ROBOTO_CONDENCED );

    }

    public void onButtonPressed( Uri uri ) {
        if ( mListener != null ) {
            mListener.onLoginAction( uri );
        }
    }

    @Override
    public void onAttach( Context context ) {
        super.onAttach( context );
        if ( context instanceof OnLoginListener ) {
            mListener = ( OnLoginListener ) context;
        } else {
            throw new RuntimeException( context.toString()
                    + " must implement OnLoginListener" );
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnLoginListener {
        // TODO: Update argument type and name
        void onLoginAction( Uri uri );
    }


    public boolean validateEmail( String email ) {
        Pattern pattern = Pattern.compile( AppConstants.EMAIL_PATTERN );
        return pattern.matcher( email ).matches();
    }
}
