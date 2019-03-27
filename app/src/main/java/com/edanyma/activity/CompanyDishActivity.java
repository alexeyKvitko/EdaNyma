package com.edanyma.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;

import com.edanyma.AppConstants;
import com.edanyma.R;
import com.edanyma.fragment.CompanyDishFragment;
import com.edanyma.manager.GlobalManager;
import com.edanyma.model.ActivityState;
import com.edanyma.model.CompanyInfoModel;
import com.edanyma.model.CompanyModel;
import com.edanyma.rest.RestController;
import com.edanyma.utils.AppUtils;

import retrofit2.Call;
import retrofit2.Response;

public class CompanyDishActivity extends BaseActivity {

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
        if( getSupportFragmentManager().getFragments().size() == 0 ){
            fragmentTransaction.add( R.id.dishFragmentContainerId , newFragment );
        } else {
            fragmentTransaction.replace( R.id.dishFragmentContainerId , newFragment );
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
                                        findViewById( R.id.dishContainerId ));
            addReplaceFragment( CompanyDishFragment.newInstance() );
        }
    }


}
