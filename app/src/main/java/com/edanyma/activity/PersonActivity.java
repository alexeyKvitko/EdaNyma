package com.edanyma.activity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;

import com.edanyma.AppConstants;
import com.edanyma.R;
import com.edanyma.fragment.SignInFragment;
import com.edanyma.fragment.SignUpFragment;
import com.edanyma.model.ActivityState;

public class PersonActivity extends BaseActivity implements SignInFragment.OnSignInListener,
        SignUpFragment.OnSignUpListener{

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_person );
        initBaseActivity( new ActivityState( AppConstants.LOGIN_BOTTOM_INDEX ) );
        addReplaceFragment( SignInFragment.newInstance() );
    }

    private void addReplaceFragment( Fragment newFragment ){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations( R.anim.fade_in, R.anim.fade_out );
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
        addReplaceFragment( SignUpFragment.newInstance() );
    }

    @Override
    public void OnSignUpListener() {

    }

}
