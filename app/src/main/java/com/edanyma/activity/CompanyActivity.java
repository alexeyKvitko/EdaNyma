package com.edanyma.activity;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

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
        ( ( TextView ) findViewById( R.id.companyTitleId ) ).setTypeface( AppConstants.B52 );
        ( ( TextView ) findViewById( R.id.cardCompanyTitleId ) ).setTypeface( AppConstants.B52 );
        ( ( TextView ) findViewById( R.id.cardCompanyCountId ) ).setTypeface( AppConstants.ROBOTO_CONDENCED );
        ( ( TextView ) findViewById( R.id.cardCompanyStarId ) ).setTypeface( AppConstants.ROBOTO_CONDENCED );
        ( ( TextView ) findViewById( R.id.cardCompanyWorkId ) ).setTypeface( AppConstants.ROBOTO_CONDENCED, Typeface.BOLD );
        ( ( TextView ) findViewById( R.id.cardCompanyWorkWeekId ) ).setTypeface( AppConstants.ROBOTO_CONDENCED, Typeface.BOLD );
        ( ( TextView ) findViewById( R.id.cardCompanyDeliId ) ).setTypeface( AppConstants.ROBOTO_CONDENCED, Typeface.BOLD );
//         final ImageView imgview =  findViewById( R.id.companyImgId );
//
//
//        (new Handler() ).postDelayed( new Runnable() {
//            @Override
//            public void run() {
//                ColorMatrix matrix = new ColorMatrix();
//                matrix.setSaturation(0);
//                ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
//                imgview.setColorFilter(filter);
//            }
//        },3000 );
//
//        (new Handler() ).postDelayed( new Runnable() {
//            @Override
//            public void run() {
//                imgview.setColorFilter(null);
//            }
//        },8000 );

    }
}
