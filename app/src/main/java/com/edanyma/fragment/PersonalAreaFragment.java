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
import com.edanyma.utils.AppUtils;

public class PersonalAreaFragment extends BaseFragment implements View.OnClickListener {


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
        initTextView( R.id.personalTitleId, AppConstants.SANDORA, Typeface.BOLD, null );
        initTextView( R.id.avatarNameId, AppConstants.B52 );
        initTextView( R.id.accountId, AppConstants.SANDORA, Typeface.BOLD, null );
        initTextView( R.id.personalMenuPasswordId, AppConstants.ROBOTO_CONDENCED );
        initTextView( R.id.personalMenuAddressId, AppConstants.ROBOTO_CONDENCED );
        initTextView( R.id.personalMenuPayTypeId, AppConstants.ROBOTO_CONDENCED );
        initTextView( R.id.personalMenuBonusId, AppConstants.ROBOTO_CONDENCED );
        initTextView( R.id.personalMenuEditId, AppConstants.ROBOTO_CONDENCED ).setOnClickListener( this );
        initTextView( R.id.signOutId, AppConstants.ROBOTO_CONDENCED ).setOnClickListener( this );
        initTextView( R.id.avatarAuthId , AppConstants.ROBOTO_CONDENCED,
                                                GlobalManager.getClient().getEmail() != null
                                                    ? GlobalManager.getClient().getEmail()
                                                            : GlobalManager.getClient().getPhone() );
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

    @Override
    public void onClick( View view ) {
        if ( mListener == null ){
            return;
        }
        AppUtils.clickAnimation( view );
        switch ( view.getId() ){
            case R.id.signOutId :
                mListener.onSignOutAction();
                break;
            case R.id.personalMenuEditId:
                mListener.onEditProfileAction();
                break;
        }
    }

    public interface OnPersonalAreaActionListener {
        void onSignOutAction();
        void onEditProfileAction();
    }
}
