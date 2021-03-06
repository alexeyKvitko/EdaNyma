package com.edanyma.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Handler;

import androidx.core.app.NavUtils;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.edanyma.AppConstants;
import com.edanyma.EdaNymaApp;
import com.edanyma.R;
import com.edanyma.manager.BasketOrderManager;
import com.edanyma.manager.GlobalManager;
import com.edanyma.model.ActivityState;
import com.edanyma.owncomponent.ModalMessage;
import com.edanyma.pixelshot.PixelShot;
import com.edanyma.utils.AppUtils;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseActivity extends AppCompatActivity
        implements View.OnClickListener, PixelShot.PixelShotListener {

    private final String TAG = "BaseActivity";

    private IntentFilter mIntentFilter;
    private BasketMessageReceiver mBasketMessageReceiver;

    protected TextView mDeliveryTV;
    protected LinearLayout mFooter;
    protected RelativeLayout mHeader;
    protected FrameLayout mDrawer;
    protected static TextView mBasketPriceText;
    protected static ImageButton mCompanyBtn;
    protected static ImageButton mDishBtn;
    protected static ImageButton mHomeBtn;
    protected static ImageButton mBasketBtn;
    protected static ImageButton mProfileBtn;

    private static boolean FINISH_ACTIVITY = false;
    private int mFastClickCount;

    private ActivityState mCurrentState;
    private Integer mBasketPrice;
    private boolean mShowBasket;

    public void initBaseActivity( ActivityState activityState ) {
        mIntentFilter = new IntentFilter( AppConstants.BASKET_CONTENT_CHANGE );
        mBasketMessageReceiver = new BasketMessageReceiver();
        mCurrentState = activityState;
        mHeader = findViewById( R.id.mainHeaderId );
        mFooter = findViewById( R.id.bottomNavigationId );
        mDeliveryTV = this.findViewById( R.id.deliveryCityId );
        mDeliveryTV.setTypeface( AppConstants.ROBOTO_CONDENCED );
        mDrawer = findViewById( R.id.drawer_layout );
        findViewById( R.id.navButtonCityId ).setOnClickListener( this );
        mCompanyBtn = findViewById( R.id.navigation_company );
        mDishBtn = findViewById( R.id.navigation_dish );
        mHomeBtn = findViewById( R.id.navigation_home );
        mBasketBtn = findViewById( R.id.navigation_basket );
        mProfileBtn = findViewById( R.id.navigation_login );
        mCompanyBtn.setOnClickListener( this );
        mDishBtn.setOnClickListener( this );
        mHomeBtn.setOnClickListener( this );
        mBasketBtn.setOnClickListener( this );
        mProfileBtn.setOnClickListener( this );
        mBasketPriceText = findViewById( R.id.basketPriceTextId );
        mBasketPriceText.setTypeface( AppConstants.OFFICE, Typeface.BOLD );
    }

    protected void removeAllFragments() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        for ( int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++ ) {
            getSupportFragmentManager().popBackStackImmediate();
        }
        for ( Fragment removedFragment : getSupportFragmentManager().getFragments() ) {
            fragmentTransaction.remove( removedFragment );
        }
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        if ( FINISH_ACTIVITY ) {
            mDrawer.setVisibility( View.GONE );
            removeAllFragments();
            new Handler().postDelayed( () -> {
                System.exit( 0 );
            }, 200 );
            return;
        }
        mFastClickCount++;
        new Handler().postDelayed( new Runnable() {
            @Override
            public void run() {
                FINISH_ACTIVITY = false;
                mFastClickCount = 0;
            }
        }, 500 );
        if ( mFastClickCount == 1 ) {
            Toast.makeText( this, "Нажмите еще раз для выхода", Toast.LENGTH_SHORT );
            FINISH_ACTIVITY = true;
            new Handler().postDelayed( new Runnable() {
                @Override
                public void run() {
                    FINISH_ACTIVITY = false;
                    mFastClickCount = 0;
                }
            }, 500 );
        }
        super.onBackPressed();
    }


    @Override
    public void onClick( View view ) {
        if ( mCurrentState.getSelectedBottomId() == view.getId() ) {
            return;
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
            case R.id.navigation_basket:
                startBasketActivity();
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
//        overridePendingTransition( R.anim.act_fade_in, R.anim.act_fade_out );
    }

    public void setHeaderFooterVisibilty( int visibility ) {
        getHeader().setVisibility( visibility );
        getFooter().setVisibility( visibility );
    }

    public void startBasketActivity() {
        if ( BasketOrderManager.getInstance().getBasketPrice() == 0 ) {
            ModalMessage.show( this, getString( R.string.empty_basket_msg ),
                    new String[]{ getString( R.string.splash_desc_two )
                            , getString( R.string.splash_desc_three ) }, 2000 );
            return;
        }
        View snapView = null;
        if ( this instanceof MainActivity ) {
            snapView = findViewById( R.id.contentMainLayoutId );
        } else {
            snapView = mDrawer.getChildAt( 0 );
        }
        mShowBasket = true;
        PixelShot.of( snapView ).setResultListener( this ).save();
    }

    private void startCityActivity( View view ) {
        AppUtils.clickAnimation( view );
        startNewActivity( CityActivity.class );
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
        FINISH_ACTIVITY = false;
        mFastClickCount = 0;
        showBasketPrice();
        registerReceiver( mBasketMessageReceiver, mIntentFilter );
        changeClientStatus();
        mDeliveryTV.setText( GlobalManager.getInstance().getBootstrapModel() != null ?
                GlobalManager.getInstance().getBootstrapModel().getDeliveryCity() :
                getResources().getString( R.string.not_available ) );
        unSelectAllBtn();
        findViewById( mCurrentState.getSelectedBottomId() ).setSelected( true );
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver( mBasketMessageReceiver );
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

    public void showBasketPrice() {
        mBasketPrice = BasketOrderManager.getInstance().getBasketPrice();
        int visibility = mBasketPrice > 0 ? View.VISIBLE : View.GONE;
        mBasketPriceText.setVisibility( visibility );
        String price = mBasketPrice == 0 ? null : mBasketPrice.toString() + ".00 руб";
        mBasketPriceText.setText( price );
        ( ( TextView ) this.getFooter().findViewById( R.id.basketPriceTextId ) ).setText( price );
    }

    public void changeStatusCompanyBtn( boolean status ) {
        unSelectAllBtn();
        mCompanyBtn.setSelected( status );
    }

    public void changeStatusDishBtn( boolean status ) {
        unSelectAllBtn();
        mDishBtn.setSelected( status );
    }

    public void changeStatusHomeBtn( boolean status ) {
        unSelectAllBtn();
        mHomeBtn.setSelected( status );
    }

    public void changeStatusBasketBtn( boolean status ) {
        unSelectAllBtn();
        mBasketBtn.setSelected( status );
    }

    public void changeStatusProfileBtn( boolean status ) {
        unSelectAllBtn();
        mBasketBtn.setSelected( status );
    }


    private void unSelectAllBtn() {
        mCompanyBtn.setSelected( false );
        mDishBtn.setSelected( false );
        mHomeBtn.setSelected( false );
        mBasketBtn.setSelected( false );
        mProfileBtn.setSelected( false );
    }


    class BasketMessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive( Context context, Intent intent ) {
            boolean showBasketBrice = intent.getBooleanExtra( AppConstants.BASKET_PRICE_SHOW, false );
            if ( showBasketBrice ) {
                showBasketPrice();
            }
        }
    }


    @Override
    public void onPixelShotSuccess( String path ) {
        if ( mShowBasket ) {
            startNewActivity( BasketActivity.class );
            mShowBasket = false;
        } else if ( this instanceof MainActivity ) {
            ( ( MainActivity ) this ).startNavigationActivity();
        } else if ( this instanceof PersonActivity ) {
            ( ( PersonActivity ) this ).showOrderDetails();
        }
    }

    @Override
    public void onPixelShotFailed() {

    }
}
