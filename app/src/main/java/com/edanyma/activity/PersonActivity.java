package com.edanyma.activity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.widget.ImageButton;

import com.edanyma.AppConstants;
import com.edanyma.R;
import com.edanyma.fragment.PersonalAreaFragment;
import com.edanyma.fragment.SignInFragment;
import com.edanyma.fragment.SignUpFragment;
import com.edanyma.manager.GlobalManager;
import com.edanyma.model.ActivityState;

public class PersonActivity extends BaseActivity implements SignInFragment.OnSignInListener,
        SignUpFragment.OnSignUpListener, PersonalAreaFragment.OnPersonalAreaActionListener {

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_person );
        initBaseActivity( new ActivityState( AppConstants.LOGIN_BOTTOM_INDEX ) );
        if(  GlobalManager.getClient() == null ){
            findViewById( R.id.navigation_login ).setBackground( getResources().getDrawable( R.drawable.login_navigation ) );
            addReplaceFragment( SignInFragment.newInstance() );
        } else {
            findViewById( R.id.navigation_login ).setBackground( getResources().getDrawable( R.drawable.person_navigation ) );
            addReplaceFragment( PersonalAreaFragment.newInstance() );
        }
        (( ImageButton) findViewById( R.id.navButtonId ) ).setImageDrawable( getResources().getDrawable( R.drawable.ic_chevron_left_black_24dp ) );
    }

    private void addReplaceFragment( Fragment newFragment ){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations( R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out );
        if( getSupportFragmentManager().getFragments().size() == 0 ){
            fragmentTransaction.add( R.id.personFragmentContainerId, newFragment );
        } else {
            fragmentTransaction.replace( R.id.personFragmentContainerId, newFragment );
            fragmentTransaction.addToBackStack( null );
        }
        fragmentTransaction.commit();
    }

    @Override
    public void onSignInAction() {
        NavUtils.navigateUpFromSameTask( this );
    }

    @Override
    public void onStartSignUpAction() {
        addReplaceFragment( SignUpFragment.newInstance( AppConstants.NEW_CLIENT ) );
    }

    @Override
    public void onSignUpAction() {
        NavUtils.navigateUpFromSameTask( this );
        overridePendingTransition( R.anim.fade_in, R.anim.fade_out );
    }

    @Override
    public void onSignInListener() {
        addReplaceFragment( SignInFragment.newInstance() );
    }

    @Override
    public void onSignOutAction() {
        GlobalManager.setClient( null );
        NavUtils.navigateUpFromSameTask( this );
    }

    @Override
    public void onForgotPasswordAction() {
        addReplaceFragment( SignUpFragment.newInstance( AppConstants.FORGOT_PASSWORD ) );
    }

    @Override
    public void onBackPressed() {
        if( getSupportFragmentManager().getFragments().size() == 0 ){
            NavUtils.navigateUpFromSameTask( this );
            overridePendingTransition( R.anim.fade_in, R.anim.fade_out );
        }
        super.onBackPressed();
        overridePendingTransition( R.anim.fade_in, R.anim.fade_out );
    }
}
