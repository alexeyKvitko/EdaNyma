package com.edanyma.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.Toast;

import com.edanyma.AppConstants;
import com.edanyma.R;
import com.edanyma.manager.GlobalManager;
import com.edanyma.model.ActivityState;
import com.edanyma.model.CompanyActionModel;
import com.edanyma.recycleview.CompanyActionAdapter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends BaseActivity{

    private final String TAG = "MainActivity";

    static final int UNIQUE_PERMISSION_CODE = 100;

    protected RecyclerView.LayoutManager mActionLayoutManager;
    protected RecyclerView mActionRecView;
    protected RecyclerView.Adapter mActionAdapter;
    protected LinearLayoutManager mHorizonalLayoutManager;

    private boolean mPermissionGranted;


    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        if ( mActionAdapter == null ) {
            mActionAdapter = new CompanyActionAdapter( new ArrayList< CompanyActionModel >() );
        }
        initialize();
    }

    private void initialize() {
        if ( Build.VERSION.SDK_INT < 23 ) {
            initMainLayout();
        } else {
            checkPermissions( UNIQUE_PERMISSION_CODE );
        }
    }

    private void initMainLayout() {
        initBaseActivity( new ActivityState( AppConstants.HOME_BOTTOM_INDEX ) );
        List<CompanyActionModel> companyActionModels = GlobalManager.getInstance().getBootstrapModel()
                                                            .getCompanyActions();
        fillActionAdapter( companyActionModels );
//        fillActionAdapter( fillCompanyAction() );
        initRecyclerView();
        mPermissionGranted = true;
    }

    private void checkPermissions( int code ) {
        mPermissionGranted = false;
        String[] permissions_required = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION };
        List permissions_not_granted_list = new ArrayList<>();
        for ( String permission : permissions_required ) {
            if ( ActivityCompat.checkSelfPermission( getApplicationContext(), permission ) != PackageManager.PERMISSION_GRANTED ) {
                permissions_not_granted_list.add( permission );
            }
        }
        if ( permissions_not_granted_list.size() > 0 ) {
            String[] permissions = new String[ permissions_not_granted_list.size() ];
            permissions_not_granted_list.toArray( permissions );
            ActivityCompat.requestPermissions( this, permissions, code );
        } else {
            initMainLayout();
        }
    }

    @Override
    public void onRequestPermissionsResult( int requestCode, String permissions[], int[] grantResults ) {
        if ( requestCode == UNIQUE_PERMISSION_CODE ) {
            boolean ok = true;
            for ( int i = 0; i < grantResults.length; ++i ) {
                ok = ok && ( grantResults[ i ] == PackageManager.PERMISSION_GRANTED );
            }
            if ( ok ) {
                initMainLayout();
            } else {
                Toast.makeText( this, "Error: required permissions not granted!", Toast.LENGTH_SHORT ).show();
                finish();
            }
        }
    }


    private void initRecyclerView() {
        if ( mActionLayoutManager == null ) {
            mActionLayoutManager = new StaggeredGridLayoutManager( 1, StaggeredGridLayoutManager.HORIZONTAL );
            mHorizonalLayoutManager = new LinearLayoutManager( this, LinearLayoutManager.HORIZONTAL, false );
        }
        if ( mActionRecView == null ) {
            mActionRecView = findViewById( R.id.companyActionRVId );
            mActionRecView.setAdapter( mActionAdapter );
            mActionRecView.setHasFixedSize( false );
            mActionRecView.setLayoutManager( mHorizonalLayoutManager );
        }
        mActionRecView.getAdapter().notifyDataSetChanged();
        mActionAdapter.notifyDataSetChanged();
    }


    private void fillActionAdapter( List< CompanyActionModel > actions ) {
        for ( int i = 0; i < actions.size(); i++ ) {
            ( ( CompanyActionAdapter ) mActionAdapter ).addItem( actions.get( i ), i );
        }
    }

    private LinkedList< CompanyActionModel > fillCompanyAction() {
        LinkedList< CompanyActionModel > actions = new LinkedList<>();
        actions.add( new CompanyActionModel( "FIDELE", "http://194.58.122.145:8080/static/images/fidele_action.png" ) );
        actions.add( new CompanyActionModel( "ДОСТАВКА КУХНЯ", "http://194.58.122.145:8080/static/images/kuhnya_action.png" ) );
        actions.add( new CompanyActionModel( "PIZZA ROLLA", "http://194.58.122.145:8080/static/images/pizrol_action.png" ) );
        actions.add( new CompanyActionModel( "FOODIE", "http://194.58.122.145:8080/static/images/foodie_action.png" ) );
        actions.add( new CompanyActionModel( "ПАВЛИН МАВЛИН", "http://194.58.122.145:8080/static/images/pavlin_action.png" ) );
        return actions;
    }


    @Override
    public void onStart() {
        super.onStart();
        if ( !mPermissionGranted ) {
            Toast.makeText( this, "Error: required permissions not granted!", Toast.LENGTH_SHORT ).show();
            return;
        }
    }


    @Override
    public void onDestroy() {
        if ( mActionRecView != null ) {
            mActionRecView.setAdapter( null );
            mActionRecView = null;
        }
        mActionLayoutManager = null;
        mActionAdapter = null;
        super.onDestroy();
    }


}
