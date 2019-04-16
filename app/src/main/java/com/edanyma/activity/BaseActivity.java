package com.edanyma.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.edanyma.AppConstants;
import com.edanyma.EdaNymaApp;
import com.edanyma.R;
import com.edanyma.manager.GlobalManager;
import com.edanyma.model.ActivityState;
import com.edanyma.utils.AppUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseActivity extends AppCompatActivity
        implements View.OnClickListener {

    protected TextView mDeliveryTV;
    protected LinearLayout mFooter;
    protected RelativeLayout mHeader;
    protected FrameLayout mDrawer;

    private static boolean FINISH_ACTIVITY = false;

    private ActivityState mCurrentState;

    public void initBaseActivity( ActivityState activityState ) {
        mCurrentState = activityState;
        mHeader = findViewById( R.id.mainHeaderId );
        mFooter = findViewById( R.id.bottomNavigationId );
        mDeliveryTV = this.findViewById( R.id.deliveryCityId );
        mDeliveryTV.setTypeface( AppConstants.ROBOTO_CONDENCED );
        mDrawer = findViewById( R.id.drawer_layout );

        for ( int i = 0; i < 5; i++ ) {
            mFooter.getChildAt( i ).setOnClickListener( this );
        }
        findViewById( R.id.navButtonCityId ).setOnClickListener( this );
    }

    @Override
    public void onBackPressed() {
        if ( FINISH_ACTIVITY ) {
            mDrawer.setVisibility( View.GONE );
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            for( Fragment removedFragment : getSupportFragmentManager().getFragments() ){
                    fragmentTransaction.remove( removedFragment );
            }
            getSupportFragmentManager().popBackStackImmediate();
            fragmentTransaction.commit();
            this.finish();
            System.exit( 0 );
        }
        FINISH_ACTIVITY = true;
        new Handler().postDelayed( new Runnable() {
            @Override
            public void run() {
                FINISH_ACTIVITY = false;
            }
        }, 1000 );
        super.onBackPressed();
    }


    @Override
    public void onClick( View view ) {
        if ( mCurrentState.getSelectedDrawerId() == view.getId() ) {
            return;
        }
        unselectBottomNavigation();
        if ( view instanceof ImageButton ) {
            view.setSelected( true );
        }
        switch ( view.getId() ) {
            case R.id.navigation_company:
                Map< String, String > params = new HashMap<>();
                params.put( AppConstants.COMPANY_FILTER, AppConstants.ALL_COMPANIES );
                startNewActivity( CompanyActivity.class, params );
                break;
            case R.id.navigation_dish:
                startNewActivity( DishActivity.class );
                break;
            case R.id.navigation_home:
                startNewActivity( MainActivity.class );
                break;
            case R.id.navigation_login:
                startNewActivity( PersonActivity.class );
                break;
            case R.id.navButtonCityId:
                startCityActivity( view );
                break;
            default:
                break;
        }
    }


    protected void startNewActivity( Class< ? > newClass ) {
        startNewActivity( newClass, null );
    }

    protected void startNewActivity( Class< ? > newClass, Map< String, String > params ) {
        Intent intent = new Intent( EdaNymaApp.getAppContext(), newClass );
        intent.addFlags( Intent.FLAG_ACTIVITY_NO_ANIMATION );
        intent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK );
        intent.putExtra( AppConstants.PREV_NAV_STATE, mCurrentState.getBottomMenuIndex() );
        if ( params != null ) {
            for ( String key : params.keySet() ) {
                intent.putExtra( key, params.get( key ) );
            }
        }
        startActivity( intent );
        overridePendingTransition( R.anim.fade_in, R.anim.fade_out );
    }

    private void startCityActivity( View view ) {
        AppUtils.clickAnimation( view );
        startNewActivity( CityActivity.class );
    }

    private void unselectBottomNavigation() {
        for ( int i = 0; i < mFooter.getChildCount(); i++ ) {
            mFooter.getChildAt( i ).setSelected( false );
        }
    }

    protected void changeClientStatus() {
        if ( GlobalManager.getInstance().getClient() != null
                && GlobalManager.getInstance().getClient().getUuid() != null ) {
            findViewById( R.id.navigation_login ).setBackground( getResources().getDrawable( R.drawable.person_navigation ) );
        } else {
            findViewById( R.id.navigation_login ).setBackground( getResources().getDrawable( R.drawable.login_navigation ) );
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        changeClientStatus();
        mDeliveryTV.setText( GlobalManager.getInstance().getBootstrapModel() != null ?
                GlobalManager.getInstance().getBootstrapModel().getDeliveryCity() :
                getResources().getString( R.string.not_available ) );
        unselectBottomNavigation();
        findViewById( mCurrentState.getSelectedBottomId() ).setSelected( true );
    }

    public ActivityState getCurrentState() {
        return mCurrentState;
    }

    public LinearLayout getFooter() {
        return mFooter;
    }

    public RelativeLayout getHeader() {
        return mHeader;
    }

    public FrameLayout getDrawer() {
        return mDrawer;
    }
}
