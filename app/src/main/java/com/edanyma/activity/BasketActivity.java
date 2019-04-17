package com.edanyma.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.edanyma.AppConstants;
import com.edanyma.R;
import com.edanyma.fragment.BasketFragment;
import com.edanyma.manager.GlobalManager;
import com.edanyma.model.ActivityState;
import com.edanyma.utils.AppUtils;

public class BasketActivity extends BaseActivity {

    private final String TAG = "BasketActivity";

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_basket );
        initialize();
    }

    private void initialize() {
        initBaseActivity( new ActivityState( AppConstants.BASKET_BOTTOM_INDEX ) );
        GlobalManager.getInstance().setDishFilter( null );
        getHeader().setVisibility( View.GONE );
        findViewById( R.id.basketContainerId ).setBackground( Drawable.createFromPath( AppUtils.getSnapshotPath() ) );
        addReplaceFragment( BasketFragment.newInstance(), false );
    }

    protected void addReplaceFragment( Fragment newFragment, boolean withAnimation ) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if ( withAnimation ) {
            fragmentTransaction.setCustomAnimations( R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out );
        }
        if ( getSupportFragmentManager().getFragments().size() == 0 ) {
            fragmentTransaction.add( R.id.basketFragmentContainerId, newFragment );
        } else {
            fragmentTransaction.replace( R.id.basketFragmentContainerId, newFragment );
            fragmentTransaction.addToBackStack( null );
        }
        fragmentTransaction.commit();
    }
}
