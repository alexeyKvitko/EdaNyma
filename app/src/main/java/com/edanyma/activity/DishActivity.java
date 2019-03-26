package com.edanyma.activity;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;

import com.edanyma.AppConstants;
import com.edanyma.R;
import com.edanyma.model.ActivityState;

public class DishActivity extends BaseActivity {

    private String mCompanyId;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_dish );
        initialize();
    }

    private void initialize(){
        mCompanyId = this.getIntent().getStringExtra( AppConstants.COMPANY_ID );
        initBaseActivity( new ActivityState( AppConstants.COMPANY_BOTTOM_INDEX ) );
        findViewById( R.id.pleaseWaitContainerId ).setVisibility( View.VISIBLE );
    }



}
