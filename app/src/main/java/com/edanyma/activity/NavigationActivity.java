package com.edanyma.activity;

import android.os.Bundle;
import android.view.View;

import androidx.core.app.NavUtils;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.edanyma.AppConstants;
import com.edanyma.R;
import com.edanyma.fragment.ProfileFragment;
import com.edanyma.fragment.PromotionFragment;
import com.edanyma.model.ActivityState;

import java.util.HashMap;
import java.util.Map;

public class NavigationActivity extends BaseActivity implements
        ProfileFragment.OnProfileFrafmentListener{

    private final String TAG = "NavigationActivity";

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_navigation );
        initNavigationActivity();
    }

    private void initNavigationActivity(){
        initBaseActivity( new ActivityState( AppConstants.HOME_BOTTOM_INDEX ) );
        getHeader().setVisibility( View.GONE );
        addReplaceFragment( ProfileFragment.newInstance() );
    }

    protected void addReplaceFragment( Fragment newFragment ) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations( R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out );
        if ( getSupportFragmentManager().getFragments().size() == 0 ) {
            fragmentTransaction.add( R.id.navigationContainerId, newFragment );
        } else {
            fragmentTransaction.replace( R.id.navigationContainerId, newFragment );
            fragmentTransaction.addToBackStack( null );
        }
        fragmentTransaction.commit();
    }

    @Override
    public void onProfileFragmentSignIn() {
        startNewActivity( PersonActivity.class );
    }

    @Override
    public void onProfileFragmentSignUp() {
        Map< String, String > params = new HashMap<>();
        params.put( AppConstants.SIGN_TYPE, AppConstants.SIGN_UP );
        startNewActivity( PersonActivity.class, params );
    }

    @Override
    public void onProfileFragmentBasket() {
        startBasketActivity();
    }

    @Override
    public void onProfileFragmentPromoion() {
        addReplaceFragment( PromotionFragment.newInstance() );
    }

    @Override
    public void onBackPressed() {
        setHeaderFooterVisibilty( View.VISIBLE );
        super.onBackPressed();
        overridePendingTransition( R.anim.fade_in, R.anim.fade_out );
    }

    @Override
    protected void onPause() {
        removeAllFragments();
        super.onPause();
    }
}
