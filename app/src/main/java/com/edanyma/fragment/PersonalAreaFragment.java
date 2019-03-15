package com.edanyma.fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.edanyma.AppConstants;
import com.edanyma.EdaNymaApp;
import com.edanyma.R;
import com.edanyma.manager.GlobalManager;

public class PersonalAreaFragment extends Fragment {


    private OnPersonalAreaActionListener mListener;

    public PersonalAreaFragment() {
    }

    public static PersonalAreaFragment newInstance() {
        PersonalAreaFragment fragment = new PersonalAreaFragment();
        return fragment;
    }

    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState ) {
        return inflater.inflate( R.layout.fragment_personal_area, container, false );
    }

    @Override
    public void onActivityCreated( @Nullable Bundle savedInstanceState ) {
        super.onActivityCreated( savedInstanceState );
        ( ( TextView ) getView().findViewById( R.id.personalTitleId ) ).setTypeface( AppConstants.SANDORA, Typeface.BOLD );
        ( ( TextView ) getView().findViewById( R.id.avatarNameId ) ).setTypeface( AppConstants.B52 );
        ( ( TextView ) getView().findViewById( R.id.accountId ) ).setTypeface( AppConstants.SANDORA, Typeface.BOLD );
        ( ( TextView ) getView().findViewById( R.id.personalMenuEditId ) ).setTypeface( AppConstants.ROBOTO_CONDENCED );
        ( ( TextView ) getView().findViewById( R.id.personalMenuPasswordId ) ).setTypeface( AppConstants.ROBOTO_CONDENCED );
        ( ( TextView ) getView().findViewById( R.id.personalMenuAddressId ) ).setTypeface( AppConstants.ROBOTO_CONDENCED );
        ( ( TextView ) getView().findViewById( R.id.personalMenuPayTypeId ) ).setTypeface( AppConstants.ROBOTO_CONDENCED );
        ( ( TextView ) getView().findViewById( R.id.personalMenuBonusId ) ).setTypeface( AppConstants.ROBOTO_CONDENCED );
        final TextView signOut = getView().findViewById( R.id.signOutId );
        signOut.setTypeface( AppConstants.ROBOTO_CONDENCED );
        signOut.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                signOutClick( view );
            }
        } );

        TextView avatarAuth = getView().findViewById( R.id.avatarAuthId );
        avatarAuth.setTypeface( AppConstants.ROBOTO_CONDENCED );
        avatarAuth.setText( GlobalManager.getClient().getEmail() != null
                ? GlobalManager.getClient().getEmail()
                : GlobalManager.getClient().getPhone() );
    }


    private void signOutClick( final View view ) {
        Animation bounce = AnimationUtils.loadAnimation( EdaNymaApp.getAppContext(), R.anim.short_bounce );
        bounce.setAnimationListener( new Animation.AnimationListener() {
            @Override
            public void onAnimationStart( Animation animation ) {}

            @Override
            public void onAnimationEnd( Animation animation ) {
                onSignOutAction();
            }

            @Override
            public void onAnimationRepeat( Animation animation ) {}
        } );
        view.startAnimation( bounce );
    }

    public void onSignOutAction(){
        if( mListener != null ){
            mListener.onSignOutAction();
        }
    }


    @Override
    public void onAttach( Context context ) {
        super.onAttach( context );
        if ( context instanceof OnPersonalAreaActionListener ) {
            mListener = ( OnPersonalAreaActionListener ) context;
        } else {
            throw new RuntimeException( context.toString()
                    + " must implement OnPersonalAreaActionListener" );
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnPersonalAreaActionListener {
        void onSignOutAction();
    }
}
