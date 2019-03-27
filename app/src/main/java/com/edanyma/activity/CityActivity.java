package com.edanyma.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.edanyma.AppConstants;
import com.edanyma.R;
import com.edanyma.manager.GlobalManager;
import com.edanyma.model.ActivityState;
import com.edanyma.model.BootstrapModel;
import com.edanyma.model.DictionaryModel;
import com.edanyma.model.TripleModel;
import com.edanyma.recyclerview.CityAdapter;
import com.edanyma.recyclerview.VegaLayoutManager;
import com.edanyma.rest.RestController;
import com.edanyma.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class CityActivity extends BaseActivity implements CityAdapter.CardClickListener {

    private static final String CLASS_TAG = "CityActivity";


    private RecyclerView mCityRecView;
    private CityAdapter mCityAdapter;

    private Activity instance;

    private boolean mDoOnes;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        instance = this;
        setContentView( R.layout.activity_city );
        fillCityAdapter( GlobalManager.getInstance().getBootstrapModel().getCities() );
        initialize();
    }

    private void initialize() {
        initViews();
        initRecView();
    }

    private void initViews() {
        initBaseActivity( new ActivityState( this.getIntent().getIntExtra( AppConstants.PREV_NAV_STATE, AppConstants.FAKE_ID ) ) );
        ( ( TextView ) findViewById( R.id.cityTitleId ) ).setTypeface( AppConstants.B52 );
        ( ( ImageButton ) findViewById( R.id.navButtonId ) ).setImageDrawable( getResources().getDrawable( R.drawable.ic_chevron_left_black_24dp ) );
        findViewById( R.id.navButtonId ).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                AppUtils.clickAnimation( view );
                onBackPressed();
            }
        } );
    }

    private void initRecView() {
        if ( mCityRecView == null ) {
            mCityRecView = findViewById( R.id.citySelectRVId );
            mCityRecView.setLayoutManager( new VegaLayoutManager() );
            mCityRecView.setAdapter( mCityAdapter );
            mCityRecView.setHasFixedSize( false );
            mCityRecView.addOnScrollListener( new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged( RecyclerView recyclerView, int newState ) {
                    if( mDoOnes ){
                        for( int i = 0; i < recyclerView.getLayoutManager().getChildCount(); i++ ){
                            if ( recyclerView.getLayoutManager().getChildAt( i ).getElevation() > 3 ){
                                mCityAdapter.notifyDataSetChanged();
                                Log.i( CLASS_TAG, "Reload Adapter: "+recyclerView.getLayoutManager().getChildAt( i ).getElevation() );
                            }
                            if( recyclerView.getLayoutManager().getChildAt( i ).getElevation() == 5 ){
                                mDoOnes = false;
                            }

                        }
                    }
                   }
                @Override
                public void onScrolled( RecyclerView recyclerView, int dx, int dy ) {
                    super.onScrolled( recyclerView, dx, dy );
                }
            } );
        }
        mCityRecView.getAdapter().notifyDataSetChanged();
        mCityAdapter.notifyDataSetChanged();
        mCityAdapter.setOnItemClickListener( this );
    }

    private void fillCityAdapter( List< DictionaryModel > cities ) {
        if ( mCityAdapter == null ) {
            mCityAdapter = new CityAdapter( new ArrayList< TripleModel >() );
            mCityAdapter.setOnItemClickListener( this );
        }
        int idx = 0;
        for ( int i = 0; i < cities.size(); i += 3 ) {
            TripleModel tripleModel = new TripleModel();
            tripleModel.setLeftItem( cities.get( i ) );
            tripleModel.setCenterItem( cities.get( i + 1 ) );
            tripleModel.setRightItem( cities.get( i + 2 ) );
            mCityAdapter.addItem( tripleModel, idx );
            idx++;
        }
    }

    @Override
    protected void onResume() {
        mDoOnes = true;
        super.onResume();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition( R.anim.fade_in, R.anim.fade_out );
    }

    @Override
    public void onItemClick( int position, View v ) {
        mDeliveryTV.setText( GlobalManager.getInstance().getBootstrapModel() != null ?
                GlobalManager.getInstance().getBootstrapModel().getDeliveryCity() :
                getResources().getString( R.string.not_available ) );
        mCityRecView.getAdapter().notifyDataSetChanged();
        AppUtils.transitionAnimation( findViewById( R.id.citiesContainerId ),
                findViewById( R.id.pleaseWaitContainerId ) );
        new CityActivity.ReloadBootstrapData().execute();
    }

    private class ReloadBootstrapData extends AsyncTask< Void, Void, Void > {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground( Void... arg0 ) {
            try {
                DictionaryModel currentCity = null;
                for ( DictionaryModel city : GlobalManager.getInstance().getBootstrapModel().getCities() ) {
                    if ( GlobalManager.getInstance().getBootstrapModel()
                            .getDeliveryCity().toUpperCase().equals( city.getDisplayName().toUpperCase() ) ) {
                        currentCity = city;
                        break;
                    }
                }
                if ( currentCity != null ) {
                    Call< BootstrapModel > bootstrapCall = RestController.getInstance()
                            .getApi().fetchBootstrapData( AppConstants.AUTH_BEARER
                                            + GlobalManager.getInstance().getUserToken(),
                                    currentCity.getLatitude(), currentCity.getLongitude() );
                    Response< BootstrapModel > responseBootstrap = bootstrapCall.execute();
                    if ( responseBootstrap.body() != null ) {
                        GlobalManager.getInstance().setBootstrapModel( responseBootstrap.body() );
                    }
                }
            } catch ( Exception e ) {
                Log.i( CLASS_TAG, e.getMessage() );
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute( Void result ) {
            super.onPostExecute( result );
            instance.onBackPressed();
        }
    }


}
