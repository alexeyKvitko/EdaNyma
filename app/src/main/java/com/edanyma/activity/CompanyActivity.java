package com.edanyma.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageButton;

import com.edanyma.AppConstants;
import com.edanyma.R;
import com.edanyma.fragment.ChooseCompanyFragment;
import com.edanyma.fragment.FilterFragment;
import com.edanyma.model.ActivityState;
import com.edanyma.model.CompanyModel;
import com.edanyma.utils.AppUtils;

public class CompanyActivity extends BaseActivity implements ChooseCompanyFragment.OnCompanyChosenListener,
                                                     FilterFragment.OnApplyFilterListener{

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_company );
        initBaseActivity( new ActivityState( AppConstants.COMPANY_BOTTOM_INDEX ) );
        ( ( ImageButton ) findViewById( R.id.navButtonId ) ).setImageDrawable( getResources().getDrawable( R.drawable.ic_chevron_left_black_24dp ) );
        findViewById( R.id.navButtonId ).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                AppUtils.clickAnimation( view );
                onBackPressed();
            }
        } );
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
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition( R.anim.fade_in, R.anim.fade_out );
    }


    @Override
    public void onCompanyChose( CompanyModel company ) {

    }

    @Override
    public void onFilterClick() {
        addReplaceFragment( FilterFragment.newInstance( ) );
    }

    @Override
    public void onApplyFilter( Uri uri ) {

    }
}