package com.edanyma.activity;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;

import com.edanyma.AppConstants;
import com.edanyma.R;
import com.edanyma.fragment.CompanyDishFragment;
import com.edanyma.fragment.DishInfoFragment;
import com.edanyma.fragment.FilterDishFragment;
import com.edanyma.manager.GlobalManager;
import com.edanyma.model.ActivityState;
import com.edanyma.model.CompanyInfoModel;
import com.edanyma.model.MenuEntityModel;
import com.edanyma.rest.RestController;
import com.edanyma.utils.AppUtils;

import java.io.File;

import retrofit2.Call;
import retrofit2.Response;

public class CompanyDishActivity extends BaseActivity implements CompanyDishFragment.OnDishActionListener,
        DishInfoFragment.OnAddToBasketListener, FilterDishFragment.OnApplyDishFilterListener {

    private final String TAG = "CompanyDishActivity";

    private String mCompanyId;
    private CompanyInfoModel mCompanyDish;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_dish );
        initialize();
    }

    private void initialize() {
        mCompanyId = this.getIntent().getStringExtra( AppConstants.COMPANY_ID );
        initBaseActivity( new ActivityState( AppConstants.COMPANY_BOTTOM_INDEX ) );
        findViewById( R.id.pleaseWaitContainerId ).setVisibility( View.VISIBLE );
        new FetchCompanyDishes().execute();
    }


    protected void addReplaceFragment( Fragment newFragment ) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations( R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out );
        if ( getSupportFragmentManager().getFragments().size() == 0 ) {
            fragmentTransaction.add( R.id.dishFragmentContainerId, newFragment );
        } else {
            fragmentTransaction.replace( R.id.dishFragmentContainerId, newFragment );
            fragmentTransaction.addToBackStack( null );
        }
        fragmentTransaction.commit();
    }


    public CompanyInfoModel getCompanyDish() {
        return mCompanyDish;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition( R.anim.fade_in, R.anim.fade_out );
    }

    private String getSnapshotPath(){
        File directory = new File( Environment.getExternalStorageDirectory(), AppConstants.PICTURE_DIR );
        if ( !directory.exists() ) {
            return null;
        }
        File file = new File( directory, AppConstants.FILENAME_DISH + AppConstants.EXTENSION_JPG );
     return file.getAbsolutePath();
    }

    @Override
    public void onMoreDishInfo( String companyName, MenuEntityModel dishEntity ) {
        getHeader().setVisibility( View.GONE );
        getFooter().setVisibility( View.GONE );
        findViewById( R.id.dishContainerId ).setBackground( Drawable.createFromPath( getSnapshotPath() ) );
        addReplaceFragment( DishInfoFragment.newInstance( companyName, dishEntity ) );
    }

    @Override
    public void onFilterDishSelect() {
        getHeader().setVisibility( View.GONE );
        getFooter().setVisibility( View.GONE );
        findViewById( R.id.dishContainerId ).setBackground( Drawable.createFromPath( getSnapshotPath() ) );
        addReplaceFragment(  FilterDishFragment.newInstance() );
    }

    @Override
    public void onAddToBasket( MenuEntityModel dishEntity ) {

    }

    @Override
    public void onApplyDishFiler() {

    }


    private class FetchCompanyDishes extends AsyncTask< Void, Void, Void > {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground( Void... arg0 ) {
            try {
                Call< CompanyInfoModel > companyDishCall = RestController.getInstance()
                        .getApi().getCompanyDishes( AppConstants.AUTH_BEARER
                                + GlobalManager.getInstance().getUserToken(), mCompanyId );
                Response< CompanyInfoModel > responseCompanyDish = companyDishCall.execute();
                if ( responseCompanyDish.body() != null ) {
                    mCompanyDish = responseCompanyDish.body();
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
            AppUtils.transitionAnimation( findViewById( R.id.pleaseWaitContainerId ),
                    findViewById( R.id.dishContainerId ) );
            GlobalManager.getInstance().setDishEntityPosition( AppConstants.FAKE_ID );
            addReplaceFragment( CompanyDishFragment.newInstance() );
        }
    }


}
