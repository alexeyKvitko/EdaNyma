package com.edanyma.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.edanyma.AppConstants;
import com.edanyma.R;
import com.edanyma.fragment.ChooseCompanyFragment;
import com.edanyma.model.ActivityState;
import com.edanyma.model.CompanyModel;

public class CompanyActivity extends BaseActivity implements ChooseCompanyFragment.OnCompanyChosenListener{

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_company );
        initBaseActivity( new ActivityState( AppConstants.COMPANY_BOTTOM_INDEX ) );
        addReplaceFragment( ChooseCompanyFragment.newInstance(
                                this.getIntent().getStringExtra( AppConstants.COMPANY_FILTER ) ) );
    }

    protected void addReplaceFragment( Fragment newFragment ) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations( R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out );
        if( getSupportFragmentManager().getFragments().size() == 0 ){
            fragmentTransaction.add( R.id.companyFragmentContainerId , newFragment );
        } else {
            fragmentTransaction.replace( R.id.companyFragmentContainerId , newFragment );
            fragmentTransaction.addToBackStack( null );
        }
        fragmentTransaction.commit();
    }


    @Override
    public void onCompanyChose( CompanyModel company ) {

    }
}
