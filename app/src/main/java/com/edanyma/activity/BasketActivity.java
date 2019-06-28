package com.edanyma.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.View;

import com.edanyma.AppConstants;
import com.edanyma.R;
import com.edanyma.fragment.BasketFragment;
import com.edanyma.fragment.CheckOutFragment;
import com.edanyma.fragment.OwnMapFragment;
import com.edanyma.fragment.PayWebViewFragment;
import com.edanyma.manager.BasketOrderManager;
import com.edanyma.model.ActivityState;
import com.edanyma.utils.AppUtils;

public class BasketActivity extends BaseActivity implements BasketFragment.OnBasketCheckOutListener,
                                                CheckOutFragment.OnCheckOutFragmentListener{

    private final String TAG = "BasketActivity";

    private String mPayOnlineUrl;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_basket );
        initialize();
    }

    private void initialize() {
        mPayOnlineUrl = null;
        initBaseActivity( new ActivityState( AppConstants.BASKET_BOTTOM_INDEX ) );
        getHeader().setVisibility( View.GONE );
        findViewById( R.id.basketContainerId ).setBackground( Drawable.createFromPath( AppUtils.getSnapshotPath() ) );
        addReplaceFragment( BasketFragment.newInstance(), false );
    }

    @Override
    protected void onResume() {
        super.onResume();
        changeStatusBasketBtn( true );
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

    @Override
    public void onBackPressed() {
        setHeaderFooterVisibilty( View.VISIBLE );
        if( BasketOrderManager.getInstance().getBasket().size() == 0 ){
            clearBackStack();
        }
        if( mPayOnlineUrl != null  ){
            setHeaderFooterVisibilty( View.GONE );
            addReplaceFragment( PayWebViewFragment.newInstance( mPayOnlineUrl ), true );
            return;
        }
        super.onBackPressed();
    }

    protected void clearBackStack(){
        int backStackEntry = getSupportFragmentManager().getBackStackEntryCount();
        for ( int i = 0; i < backStackEntry; i++ ) {
            getSupportFragmentManager().popBackStackImmediate();
        }
    }

    @Override
    public void onBasketCheckOut() {
        getHeader().setVisibility( View.GONE );
        getFooter().setVisibility( View.GONE );
        addReplaceFragment( CheckOutFragment.newInstance(), true );
    }

    @Override
    public void onCheckOut() {

    }

    @Override
    public void onShowMapClick() {
        addReplaceFragment( OwnMapFragment.newInstance( false ), true );
    }

    public void setPayOnlineUrl( String payOnlineUrl ) {
        this.mPayOnlineUrl = payOnlineUrl;
    }
}
