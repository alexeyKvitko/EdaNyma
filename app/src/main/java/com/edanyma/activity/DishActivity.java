package com.edanyma.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.edanyma.AppConstants;
import com.edanyma.R;
import com.edanyma.fragment.DishFragment;
import com.edanyma.fragment.DishInfoFragment;
import com.edanyma.fragment.FilterDishFragment;
import com.edanyma.manager.GlobalManager;
import com.edanyma.model.ActivityState;
import com.edanyma.model.MenuCategoryModel;
import com.edanyma.model.MenuEntityModel;
import com.edanyma.utils.AppUtils;

import java.util.List;

import static com.edanyma.manager.GlobalManager.*;

public class DishActivity extends BaseActivity implements DishFragment.OnDishActionListener,
        DishInfoFragment.OnAddToBasketListener, FilterDishFragment.OnDishFilterActionListener {

    private final String TAG = "DishActivity";

    private String mDishTitle;
    private Integer mSelectedDishId;
    private Integer mSelectedCompanyId;
    private Integer mPrevCompanyId;
    private Integer mPrevDishId;
    private List< MenuEntityModel > mDishes;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_dish );
        initialize();
    }

    private void initialize() {
        initBaseActivity( new ActivityState( AppConstants.DISH_BOTTOM_INDEX ) );
        setDishFilter( null );
        mSelectedCompanyId = null;
        mSelectedDishId = AppConstants.SUSHI_SET_ID;
        mPrevCompanyId = null;
        mPrevDishId = null;
        applyAllDishesFiler();
    }


    protected void addReplaceFragment( Fragment newFragment, boolean withAnimation ) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if ( withAnimation ) {
            fragmentTransaction.setCustomAnimations( R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out );
        }
        if ( getSupportFragmentManager().getFragments().size() == 0 ) {
            fragmentTransaction.add( R.id.eatMenuFragmentContainerId, newFragment );
        } else {
            fragmentTransaction.replace( R.id.eatMenuFragmentContainerId, newFragment );
        }
        if ( newFragment instanceof FilterDishFragment ) {
            System.out.println( "Filter" );
        } else {
            fragmentTransaction.addToBackStack( null );
        }
        fragmentTransaction.commit();
    }


    @Override
    public void onMoreDishInfo( String companyName, MenuEntityModel dishEntity ) {
        getHeader().setVisibility( View.GONE );
        getFooter().setVisibility( View.GONE );
        findViewById( R.id.eatMenuContainerId ).setBackground( Drawable.createFromPath( AppUtils.getSnapshotPath() ) );
        addReplaceFragment( DishInfoFragment.newInstance( companyName, dishEntity ), true );
    }

    @Override
    public void onFilterDishSelect() {
        getHeader().setVisibility( View.GONE );
        getFooter().setVisibility( View.GONE );
        addReplaceFragment( FilterDishFragment.newInstance(), false );
    }

    @Override
    public void onAddToBasket( MenuEntityModel dishEntity ) {
    }


    private void applyAllDishesFiler() {
        for ( MenuCategoryModel menuCategory : getBootstrapModel().getDeliveryMenu().getMenuCategories() ) {
            if ( mSelectedDishId == Integer.valueOf( menuCategory.getId() ).intValue() ) {
                mDishTitle = menuCategory.getDisplayName();
                break;
            }
        }
        addReplaceFragment( DishFragment.newInstance().newInstance(), true );

    }


    public String getDishTitle() {
        return mDishTitle;
    }

    public Integer getSelectedDishId() {
        return mSelectedDishId;
    }

    public Integer getSelectedCompanyId() {
        return mSelectedCompanyId;
    }


    public void setSelectedDishId( Integer mSelectedDishId ) {
        this.mSelectedDishId = mSelectedDishId;
        this.mSelectedCompanyId = null;
    }


    public void setSelectedCompanyId( Integer mSelectedCompanyId ) {
        this.mSelectedCompanyId = mSelectedCompanyId;
    }

    @Override
    public void onBackPressed() {
        if ( getSupportFragmentManager().getFragments() != null && getSupportFragmentManager().getFragments().size() == 1 ) {
            Fragment fragment = getSupportFragmentManager().getFragments().get( 0 );
            if ( fragment instanceof DishFragment ) {
                int backStackEntry = getSupportFragmentManager().getBackStackEntryCount();
                for ( int i = 0; i < backStackEntry; i++ ) {
                    getSupportFragmentManager().popBackStackImmediate();
                }
            } else if ( fragment instanceof FilterDishFragment ) {
                onDishFilterAction();
                return;
            }
        }
        super.onBackPressed();
    }

    @Override
    public void onDishFilterAction() {
        clearStack();
        applyAllDishesFiler();
    }

    protected void clearStack() {
        if ( getSupportFragmentManager().getFragments() != null && getSupportFragmentManager().getFragments().size() > 0 ) {
            for ( int i = 0; i < getSupportFragmentManager().getFragments().size(); i++ ) {
                Fragment mFragment = getSupportFragmentManager().getFragments().get( i );
                if ( mFragment != null && mFragment instanceof FilterDishFragment ) {
                    getSupportFragmentManager().beginTransaction().remove( mFragment ).commit();
                    getSupportFragmentManager().executePendingTransactions();
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        changeStatusDishBtn( true );
    }

    public Integer getPrevCompanyId() {
        return mPrevCompanyId;
    }

    public void setPrevCompanyId( Integer mPrevCompanyId ) {
        this.mPrevCompanyId = mPrevCompanyId;
    }

    public Integer getPrevDishId() {
        return mPrevDishId;
    }

    public void setPrevDishId( Integer mPrevDishId ) {
        this.mPrevDishId = mPrevDishId;
    }

    public List< MenuEntityModel > getDishes() {
        return mDishes;
    }

    public void setDishes( List< MenuEntityModel > mDishes ) {
        this.mDishes = mDishes;
    }
}
