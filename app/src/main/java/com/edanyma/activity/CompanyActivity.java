package com.edanyma.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.edanyma.AppConstants;
import com.edanyma.R;
import com.edanyma.model.ActivityState;

public class CompanyActivity extends BaseActivity {

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_company );
        initialize();
    }

    private void initialize(){
        initBaseActivity( new ActivityState( AppConstants.COMPANY_BOTTOM_INDEX ) );
    }
}
