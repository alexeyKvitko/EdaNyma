package com.edanyma.activity;


import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;

import com.edanyma.AppConstants;
import com.edanyma.R;
import com.edanyma.fragment.LoginFragment;
import com.edanyma.model.ActivityState;

public class PersonActivity extends BaseActivity implements LoginFragment.OnSignInListener {

    private LoginFragment mLoginFragment;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_person );
        initBaseActivity( new ActivityState( AppConstants.LOGIN_BOTTOM_INDEX ) );
        showLoginFragment();
    }

    private void showLoginFragment(){
        mLoginFragment = LoginFragment.newInstance();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add( R.id.personFragmentContainerId, mLoginFragment );
        fragmentTransaction.commit();
    }

    @Override
    public void onLoginAction() {
        NavUtils.navigateUpFromSameTask( this );
    }
}
