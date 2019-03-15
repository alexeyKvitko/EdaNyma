package com.edanyma.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.edanyma.AppConstants;
import com.edanyma.R;
import com.edanyma.manager.GlobalManager;
import com.edanyma.model.CompanyActionModel;
import com.edanyma.model.DictionaryModel;
import com.edanyma.model.TripleModel;
import com.edanyma.recycleview.CityAdapter;
import com.stone.vega.library.VegaLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class CityActivity extends AppCompatActivity {

    private TextView mDeliveryCity;

    private RecyclerView mCityRecView;
    private CityAdapter mCityAdapter;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_city );
        fillCityAdapter( GlobalManager.getInstance().getBootstrapModel().getCities() );
        initialize();
    }
    
    private void initialize(){
        initViews();
        initRecView();
    }
    
    private void initViews(){
        mDeliveryCity = this.findViewById( R.id.deliveryCityId );
        mDeliveryCity.setTypeface( AppConstants.ROBOTO_CONDENCED );
        mDeliveryCity.setText( GlobalManager.getInstance().getBootstrapModel() != null ?
                GlobalManager.getInstance().getBootstrapModel().getDeliveryCity():
                getResources().getString( R.string.not_available) );
        ((TextView) findViewById( R.id.cityTitleId )).setTypeface( AppConstants.B52 );
    }
    
    private void initRecView(){
        if ( mCityRecView == null ) {
            mCityRecView = findViewById( R.id.citySelectRVId );
            mCityRecView.setLayoutManager( new VegaLayoutManager() );
            mCityRecView.setAdapter( mCityAdapter );
            mCityRecView.setHasFixedSize( false );
        }
        mCityRecView.getAdapter().notifyDataSetChanged();
        mCityAdapter.notifyDataSetChanged();
    }

    private void fillCityAdapter( List< DictionaryModel > cities ) {
        if ( mCityAdapter == null ) {
            mCityAdapter = new CityAdapter( new ArrayList< TripleModel >() );
        }
        int idx = 0;
        for ( int i = 0; i < cities.size(); i += 3 ) {
            TripleModel tripleModel = new TripleModel();
            tripleModel.setLeftItem( cities.get(i) );
            tripleModel.setCenterItem( cities.get(i+1) );
            tripleModel.setRightItem( cities.get(i+2) );
            mCityAdapter.addItem( tripleModel, idx );
            idx++;
        }
    }

    @Override
    public void onDestroy() {
        if ( mCityRecView != null ) {
            mCityRecView.setAdapter( null );
            mCityRecView = null;
        }
        mCityAdapter = null;
        super.onDestroy();
    }


}
