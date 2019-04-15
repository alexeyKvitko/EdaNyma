package com.edanyma.activity;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;

import com.edanyma.AppConstants;
import com.edanyma.R;
import com.edanyma.fragment.DishFragment;
import com.edanyma.fragment.DishInfoFragment;
import com.edanyma.fragment.FilterDishFragment;
import com.edanyma.manager.GlobalManager;
import com.edanyma.model.ActivityState;
import com.edanyma.model.ApiResponse;
import com.edanyma.model.CompanyModel;
import com.edanyma.model.Dishes;
import com.edanyma.model.MenuCategoryModel;
import com.edanyma.model.MenuEntityModel;
import com.edanyma.rest.RestController;
import com.edanyma.utils.AppUtils;

import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class DishActivity extends BaseActivity implements DishFragment.OnDishActionListener,
        DishInfoFragment.OnAddToBasketListener {

    private final String TAG = "DishActivity";

    private List< MenuEntityModel > mDishes;
    private String mDishTitle;
    private Integer mSelectedDishId;
    private Integer mSelectedCompanyId;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_dish );
        initialize();
    }

    private void initialize() {
//        findViewById( R.id.pleaseWaitContainerId ).setVisibility( View.VISIBLE );
        initBaseActivity( new ActivityState( AppConstants.DISH_BOTTOM_INDEX ) );
        GlobalManager.getInstance().setDishFilter( null );
        mSelectedCompanyId = null;
        mSelectedDishId = AppConstants.SUSHI_SET_ID;
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
            fragmentTransaction.addToBackStack( null );
        }
        fragmentTransaction.commit();
    }


    public List< MenuEntityModel > getDishes() {
        return mDishes;
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
        for ( MenuCategoryModel menuCategory : GlobalManager.getBootstrapModel().getDeliveryMenu().getMenuCategories() ) {
            if ( mSelectedDishId == Integer.valueOf( menuCategory.getId() ).intValue() ) {
                mDishTitle = menuCategory.getDisplayName();
                break;
            }
        }
        new FetchDishes().execute();
        AppUtils.transitionAnimation( findViewById( R.id.eatMenuContainerId )
                , findViewById( R.id.pleaseWaitContainerId ) );
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
    }


    public void setSelectedCompanyId( Integer mSelectedCompanyId ) {
        this.mSelectedCompanyId = mSelectedCompanyId;
    }

    @Override
    public void onBackPressed() {
        if ( getSupportFragmentManager().getFragments().size() > 0 &&
                getSupportFragmentManager().getFragments().get( 0 ) instanceof FilterDishFragment ) {
            applyAllDishesFiler();
        }
        super.onBackPressed();
    }

    private class FetchDishes extends AsyncTask< Void, Void, Void > {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground( Void... arg0 ) {
            try {
                Call< ApiResponse< Dishes > > dishesCall = RestController.getInstance()
                        .getApi().getDishes( AppConstants.AUTH_BEARER
                                        + GlobalManager.getInstance().getUserToken(), GlobalManager.getInstance().getBootstrapModel().getDeliveryCity(),
                                mSelectedDishId );
                Response< ApiResponse< Dishes > > responseDishes = dishesCall.execute();
                if ( responseDishes.body() != null ) {
                    ApiResponse< Dishes > apiResponse = responseDishes.body();
                    mDishes = new LinkedList<>();
                    for ( MenuEntityModel menuEntity : apiResponse.getResult().getDishes() ) {
                        if ( mSelectedCompanyId == null ||
                                ( mSelectedCompanyId != null &&
                                        mSelectedCompanyId.equals( Integer.valueOf( menuEntity.getCompanyId() ) ) ) ) {
                            for ( CompanyModel company : GlobalManager.getBootstrapModel().getCompanies() ) {
                                if ( menuEntity.getCompanyId().equals( company.getId() ) ) {
                                    menuEntity.setCompanyName( company.getDisplayName() );
                                    break;
                                }
                            }
                            mDishes.add( menuEntity );
                        }
                    }
                }
            } catch ( Exception e ) {
                Log.e( TAG, e.getMessage() );
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute( Void result ) {
            super.onPostExecute( result );
            GlobalManager.getInstance().setDishEntityPosition( AppConstants.FAKE_ID );
            new Handler().postDelayed( new Runnable() {
                @Override
                public void run() {
                    addReplaceFragment( DishFragment.newInstance(), true );
                }
            }, 400 );

        }
    }
}
