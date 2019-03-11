package com.edanyma.fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
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

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick( View view ) {

    }


    public interface OnSignUpListener {

        void OnSignUpListener();
    }
}
