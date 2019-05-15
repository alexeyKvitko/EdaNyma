package com.edanyma.activity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.view.View;
import android.widget.ImageButton;

import com.edanyma.AppConstants;
import com.edanyma.R;
import com.edanyma.fragment.EditProfileFragment;
import com.edanyma.fragment.PersonalAreaFragment;
import com.edanyma.fragment.SignInFragment;
import com.edanyma.fragment.SignUpFragment;
import com.edanyma.manager.BasketOrderManager;
import com.edanyma.manager.GlobalManager;
import com.edanyma.model.ActivityState;
import com.edanyma.utils.AppUtils;

public class PersonActivity extends BaseActivity implements SignInFragment.OnSignInListener,
        SignUpFragment.OnSignUpListener, PersonalAreaFragment.OnPersonalAreaActionListener,
        EditProfileFragment.OnEditFragmentActionListener{

    private String mSign;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_person );
        initBaseActivity( new ActivityState( AppConstants.LOGIN_BOTTOM_INDEX ) );
        if(  !GlobalManager.getInstance().isSignedIn() ){
            findViewById( R.id.navigation_login ).setBackground( getResources().getDrawable( R.drawable.login_navigation ) );
            mSign = this.getIntent().getStringExtra( AppConstants.SIGN_TYPE );
            if( mSign == null || AppConstants.SIGN_IN.equals( mSign ) ){
                addReplaceFragment( SignInFragment.newInstance() );
            } else {
                addReplaceFragment( SignUpFragment.newInstance( AppConstants.NEW_CLIENT ) );
            }
        } else {
            findViewById( R.id.navigation_login ).setBackground( getResources().getDrawable( R.drawable.person_navigation ) );
            addReplaceFragment( PersonalAreaFragment.newInstance() );
        }
        (( ImageButton) findViewById( R.id.navButtonId ) ).setImageDrawable( getResources().getDrawable( R.drawable.ic_chevron_left_black_24dp ) );
    }

    protected void addReplaceFragment( Fragment newFragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations( R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out );
        if( getSupportFragmentManager().getFragments().size() == 0 ){
            fragmentTransaction.add( R.id.personFragmentContainerId , newFragment );
        } else {
            fragmentTransaction.replace( R.id.personFragmentContainerId , newFragment );
            fragmentTransaction.addToBackStack( null );
        }
        fragmentTransaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        changeStatusProfileBtn( true );
    }

    @Override
    public void onSignInAction() {
        if ( mSign != null ){
            changeClientStatus();
            GlobalManager.getInstance().setActionConfirmed( true );
            onBackPressed();
        } else {
            NavUtils.navigateUpFromSameTask( this );
        }
    }

    @Override
    public void onStartSignUpAction() {
        addReplaceFragment( SignUpFragment.newInstance( AppConstants.NEW_CLIENT ) );
    }

    private void signOut(){
        GlobalManager.getInstance().setClient( null );
        BasketOrderManager.getInstance().clearBasket();
        NavUtils.navigateUpFromSameTask( this );
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
        signOut();
    }

    @Override
    public void onEditProfileAction() {
        addReplaceFragment( EditProfileFragment.newInstance() );
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

    @Override
    public void onSignOutFromEditAction() {
        signOut();
    }
}
