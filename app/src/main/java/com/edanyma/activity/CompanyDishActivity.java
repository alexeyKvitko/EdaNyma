package com.edanyma.activity;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;

import com.edanyma.AppConstants;
import com.edanyma.R;
import com.edanyma.fragment.CompanyDishFragment;
import com.edanyma.fragment.CompanyInfoFragment;
import com.edanyma.fragment.DishInfoFragment;
import com.edanyma.fragment.FilterCompanyDishFragment;
import com.edanyma.manager.GlobalManager;
import com.edanyma.model.ActivityState;
import com.edanyma.model.CompanyInfoModel;
import com.edanyma.model.CompanyMenu;
import com.edanyma.model.MenuEntityModel;
import com.edanyma.rest.RestController;
import com.edanyma.utils.AppUtils;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;

import static com.edanyma.manager.GlobalManager.*;

public class CompanyDishActivity extends BaseActivity implements CompanyDishFragment.OnDishActionListener,
        DishInfoFragment.OnAddToBasketListener, FilterCompanyDishFragment.OnApplyDishFilterListener,
        CompanyInfoFragment.OnSignActionListener {

    private final String TAG = "CompanyDishActivity";

    private String mCompanyId;
    private CompanyInfoModel mCompanyInfo;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_company_dish );
        initialize();
    }

    private void initialize() {
        mCompanyId = this.getIntent().getStringExtra( AppConstants.COMPANY_ID );
        initBaseActivity( new ActivityState( AppConstants.COMPANY_BOTTOM_INDEX ) );
        findViewById( R.id.pleaseWaitContainerId ).setVisibility( View.VISIBLE );
        new FetchCompanyDishes().execute();
    }


    protected void addReplaceFragment( Fragment newFragment, boolean withAnimation ) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if ( withAnimation ) {
            fragmentTransaction.setCustomAnimations( R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out );
        }
        if ( getSupportFragmentManager().getFragments().size() == 0 ) {
            fragmentTransaction.add( R.id.dishFragmentContainerId, newFragment );
        } else {
            fragmentTransaction.replace( R.id.dishFragmentContainerId, newFragment );
            fragmentTransaction.addToBackStack( null );
        }
        fragmentTransaction.commit();
    }


    public CompanyInfoModel getCompanyInfo() {
        return mCompanyInfo;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition( R.anim.fade_in, R.anim.fade_out );
    }


    @Override
    public void onMoreDishInfo( String companyName, MenuEntityModel dishEntity ) {
        getHeader().setVisibility( View.GONE );
        getFooter().setVisibility( View.GONE );
        findViewById( R.id.dishContainerId ).setBackground( Drawable.createFromPath( AppUtils.getSnapshotPath() ) );
        addReplaceFragment( DishInfoFragment.newInstance( companyName, dishEntity ), true );
    }

    @Override
    public void onMoreCompanyInfo() {
        getHeader().setVisibility( View.GONE );
        getFooter().setVisibility( View.GONE );
        findViewById( R.id.dishContainerId ).setBackground( Drawable.createFromPath( AppUtils.getSnapshotPath() ) );
        addReplaceFragment( CompanyInfoFragment.newInstance( mCompanyInfo.getCompanyModel() ), false );
    }

    @Override
    public void onFilterDishSelect() {
        getHeader().setVisibility( View.GONE );
        getFooter().setVisibility( View.GONE );
        addReplaceFragment( FilterCompanyDishFragment.newInstance( new CompanyMenu( mCompanyInfo.getMenuTypes() ) ), false );
    }

    @Override
    public void onAddToBasket( MenuEntityModel dishEntity ) {

    }

    @Override
    public void onApplyDishFiler() {
        addReplaceFragment( CompanyDishFragment.newInstance(), true );
    }

    @Override
    public void onSignAction( String sign ) {
        Map< String, String > params = new HashMap<>();
        params.put( AppConstants.SIGN_TYPE, sign );
        startNewActivity( PersonActivity.class, params );
    }


    private class FetchCompanyDishes extends AsyncTask< Void, Void, Void > {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground( Void... arg0 ) {
            try {
                Call< CompanyInfoModel > companyDishCall = RestController
                        .getApi().getCompanyDishes( AppConstants.AUTH_BEARER
                                + getUserToken(), mCompanyId );
                Response< CompanyInfoModel > responseCompanyDish = companyDishCall.execute();
                if ( responseCompanyDish.body() != null ) {
                    mCompanyInfo = responseCompanyDish.body();
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
            findViewById( R.id.pleaseWaitContainerId ).setVisibility( View.GONE );
            findViewById( R.id.dishContainerId ).setVisibility( View.VISIBLE );
            setDishEntityPosition( AppConstants.FAKE_ID );
            addReplaceFragment( CompanyDishFragment.newInstance(), true );
        }
    }

}
