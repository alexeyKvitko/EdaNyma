package com.edanyma.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edanyma.AppConstants;
import com.edanyma.EdaNymaApp;
import com.edanyma.R;
import com.edanyma.manager.GlobalManager;
import com.edanyma.model.ActivityState;

import java.util.ArrayList;

public abstract class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener,
        OnNavigationItemSelectedListener{

    protected TextView mDeliveryTV;
    protected LinearLayout mBottomNavigation;
    protected DrawerLayout mDrawer;
    protected ImageButton mNavigationButton;
    protected ArrayList<View> mMenuItems = new ArrayList<>();
    protected NavigationView mNavigationView;

    private ActivityState mCurrentState;

    public void initBaseActivity( ActivityState activityState ){
        mCurrentState = activityState;
        mDeliveryTV = this.findViewById( R.id.deliveryCityId );
        mDeliveryTV.setTypeface( AppConstants.ROBOTO_CONDENCED );
        mDeliveryTV.setText( GlobalManager.getInstance().getBootstrapModel() != null ?
                GlobalManager.getInstance().getBootstrapModel().getDeliveryCity():
                "Not Avilable");
        mDrawer = findViewById( R.id.drawer_layout );
        mNavigationButton = findViewById( R.id.navButtonId );
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close );
        mDrawer.addDrawerListener( toggle );
        toggle.syncState();
        mNavigationView = findViewById( R.id.nav_view );
        mNavigationView.setNavigationItemSelectedListener( this );
        mNavigationView.getHeaderView(0).findViewById( R.id.drawerLoginId ).setOnClickListener( this );
        final Animation rotate = AnimationUtils.loadAnimation( this, R.anim.icon_rotation );
        mNavigationButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                mNavigationButton.startAnimation( rotate );
                new Handler().postDelayed( new Runnable() {
                    @Override
                    public void run() {
                        mDrawer.openDrawer( GravityCompat.START );
                    }
                }, 100);

            }
        } );
        mBottomNavigation = findViewById( R.id.bottomNavigationId );
        for( int i=0; i<5; i++){
            mBottomNavigation.getChildAt(i).setOnClickListener( this );
        }

        mNavigationView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Remember to remove the installed OnGlobalLayoutListener
                mNavigationView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                // Loop through and find each MenuItem View
                for (int i = 0; i < mNavigationView.getMenu().size(); i++) {
                    final MenuItem item = mNavigationView.getMenu().getItem( i );
                    mNavigationView.findViewsWithText(mMenuItems, item.getTitle(), View.FIND_VIEWS_WITH_TEXT);
                }
                // Loop through each MenuItem View and apply your custom Typeface
                for (final View menuItem : mMenuItems) {
                    TextView tv = (TextView) menuItem;
                    tv.setTypeface( AppConstants.ROBOTO_CONDENCED);
                    tv.setTextColor( getResources().getColor(R.color.tealColor) );
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if ( mDrawer.isDrawerOpen( GravityCompat.START ) ) {
            mDrawer.closeDrawer( GravityCompat.START );
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected( MenuItem item ) {
        int selId = item.getItemId();
        if( mCurrentState.getSelectedBottomId() == selId ||
                mCurrentState.getSelectedDrawerId() == selId ){
            return false;
        }
        closeDrawer();
        switch ( selId ) {
            case R.id.navigation_login:
                startPersonActivity();
                break;
            case R.id.navigation_home:
                startMainActivity();
                break;
            case R.id.nav_home:
                startMainActivity();
                break;

        }
        return true;
    }

    @Override
    public void onClick( View view ) {
        if( mCurrentState.getSelectedDrawerId() == view.getId() ){
            return;
        }
        closeDrawer();
        unselectBottomNavigation();
        if( view instanceof ImageButton ){
            ((ImageButton) view).setSelected( true );
        }
        switch ( view.getId() ){
            case R.id.drawerLoginId:
                startPersonActivity();
                break;
            case R.id.navigation_home:
                startMainActivity();
                break;
            case R.id.navigation_login:
                startPersonActivity();
                break;
            default:
                break;
        }
    }

    private void closeDrawer(){
        if ( mDrawer.isDrawerOpen( GravityCompat.START  ) ){
            new Handler().postDelayed( new Runnable() {
                @Override
                public void run() {
                    mDrawer.closeDrawer( GravityCompat.START );
                }
            }, 600);
        }
    }

    private void startPersonActivity(){
        Intent intent = new Intent( EdaNymaApp.getAppContext(), PersonActivity.class );
        intent.addFlags( Intent.FLAG_ACTIVITY_NO_ANIMATION );
        intent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK );
        startActivity( intent );
        overridePendingTransition( R.anim.fade_in, R.anim.fade_out );
    }

    private void startMainActivity(){
        Intent intent = new Intent( EdaNymaApp.getAppContext(), MainActivity.class );
        intent.addFlags( Intent.FLAG_ACTIVITY_NO_ANIMATION );
        intent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK );
        startActivity( intent );
        overridePendingTransition( R.anim.fade_in, R.anim.fade_out );
    }

    private void unselectBottomNavigation(){
        for(int i=0; i < mBottomNavigation.getChildCount(); i++){
            mBottomNavigation.getChildAt( i ).setSelected( false );
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        unselectBottomNavigation();
        findViewById( mCurrentState.getSelectedBottomId() ).setSelected( true );
        if( AppConstants.FAKE_ID != mCurrentState.getDrawerMenuIndex()){
            mNavigationView.getMenu().getItem( mCurrentState.getDrawerMenuIndex() ).setChecked( true );
        } else {
             mNavigationView.getHeaderView(0)
                     .findViewById( R.id.drawerLoginId )
                            .setBackgroundColor( getResources().getColor(R.color.transpGrayTextColor) );
        }
    }


}
