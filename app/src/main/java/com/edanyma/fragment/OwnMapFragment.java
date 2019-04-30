package com.edanyma.fragment;

import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edanyma.AppConstants;
import com.edanyma.R;
import com.edanyma.manager.GlobalManager;
import com.edanyma.model.ClientLocation;
import com.edanyma.rest.RestApi;
import com.edanyma.rest.RestController;
import com.edanyma.utils.AppUtils;
import com.edanyma.utils.ConvertUtils;
import com.edanyma.utils.GeoUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import retrofit2.Call;
import retrofit2.Response;


public class OwnMapFragment extends BaseFragment implements OnMapReadyCallback {

    private static final int COLLAPSE_HEIGHT = (int) ConvertUtils.convertDpToPixel( 102 );
    private static final int EXPAND_HEIGHT = (int) ConvertUtils.convertDpToPixel( 152 );


    private static final String TAG = "OwnMapFragment";
    private OnChooseAddressOnMap mListener;

    private GoogleMap mGoogleMap;
    private MapView mMapView;
    private ClientLocation mLocation;
    private Marker mMarker;

    private AppCompatEditText mCityEdit;
    private AppCompatEditText mStreetEdit;
    private AppCompatEditText mHouseEdit;
    private AppCompatEditText mEntranceEdit;
    private AppCompatEditText mIntercomEdit;
    private AppCompatEditText mFloorEdit;

    private TextView mAdditionalAddress;
    private LinearLayout mAddressContainer;

    private boolean mExpanded;



    public OwnMapFragment() {
    }


    public static OwnMapFragment newInstance() {
        OwnMapFragment fragment = new OwnMapFragment();

        return fragment;
    }

    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
    }

    @Override
    public void onActivityCreated( @Nullable Bundle savedInstanceState ) {
        super.onActivityCreated( savedInstanceState );
    }

    @Override
    public void onViewCreated( @NonNull View view, Bundle savedInstanceState ) {
        super.onViewCreated( view, savedInstanceState );
        mExpanded = false;
        mLocation = new ClientLocation();
        mLocation.setLatitude( GlobalManager.getInstance().getClientLocation().getLatitude() );
        mLocation.setLongitude( GlobalManager.getInstance().getClientLocation().getLongitude() );

        mCityEdit = initEditText( R.id.checkOutCityId , AppConstants.ROBOTO_CONDENCED );
        mCityEdit.setEnabled( false );
        mStreetEdit = initEditText( R.id.checkOutStreetId , AppConstants.ROBOTO_CONDENCED );
        mHouseEdit = initEditText( R.id.checkOutHouseId , AppConstants.ROBOTO_CONDENCED );
        mIntercomEdit = initEditText( R.id.checkOutIntercomId , AppConstants.ROBOTO_CONDENCED );
        mEntranceEdit = initEditText( R.id.checkOutEntranceId , AppConstants.ROBOTO_CONDENCED );
        mFloorEdit = initEditText( R.id.checkOutFloorId , AppConstants.ROBOTO_CONDENCED );
        getView().findViewById( R.id.chooseAddressId ).setOnClickListener( (View v ) ->{
            onChooseAddress();
        } );
        mAdditionalAddress = initTextView( R.id.additionalAddressId, AppConstants.ROBOTO_CONDENCED );
        mAdditionalAddress.setOnClickListener( ( View v ) -> {
            if ( mExpanded ){
                onAdditionalAddressClick( EXPAND_HEIGHT, COLLAPSE_HEIGHT, View.GONE,
                            getActivity().getResources().getString( R.string.additional_address ) );
            } else {
                onAdditionalAddressClick( COLLAPSE_HEIGHT, EXPAND_HEIGHT, View.VISIBLE,
                            getActivity().getResources().getString( R.string.hide_label ));
            }

        } );

        mAddressContainer = getView().findViewById( R.id.addressBodyId );
        mAddressContainer.setOnClickListener( null );
        mMapView = getView().findViewById( R.id.map );
        if ( mMapView != null ) {
            mMapView.onCreate( null );
            mMapView.onResume();
            mMapView.getMapAsync( this );
        }
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState ) {
        return inflater.inflate( R.layout.fragment_map, container, false );
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onChooseAddress() {
        if ( mListener != null ) {
            mLocation.setCity( mCityEdit.getText().toString() );
            mLocation.setStreet( mStreetEdit.getText().toString() );
            mLocation.setHouse( mHouseEdit.getText().toString() );
            mLocation.setEntrance( mEntranceEdit.getText().toString() );
            mLocation.setIntercom( mIntercomEdit.getText().toString() );
            mLocation.setFloor( mFloorEdit.getText().toString() );
            GlobalManager.getInstance().setClientLocation( mLocation );
            getActivity().onBackPressed();
        }
    }

    private void onAdditionalAddressClick( int start, int end, final int visibility, final String text ){
        AppUtils.clickAnimation( mAdditionalAddress );
        getView().findViewById( R.id.additionalAddressLayoutId ).setVisibility( visibility );
        mExpanded = !mExpanded;
        final LinearLayout.LayoutParams layoutParams = ( LinearLayout.LayoutParams ) mAddressContainer.getLayoutParams();
        ValueAnimator valAnimator = ValueAnimator.ofObject( new IntEvaluator(), start, end );
        valAnimator.addUpdateListener( ( ValueAnimator animator ) -> {
                int val = ( Integer ) animator.getAnimatedValue();
                layoutParams.height = val;
                if ( val == end ) {
                    mAdditionalAddress.setText( text );
                }
                mAddressContainer.setLayoutParams( layoutParams );
        } );
        valAnimator.setDuration( 200 );
        valAnimator.start();
    }

    @Override
    public void onAttach( Context context ) {
        super.onAttach( context );
        if ( context instanceof OnChooseAddressOnMap ) {
            mListener = ( OnChooseAddressOnMap ) context;
        } else {
            throw new RuntimeException( context.toString()
                    + " must implement OnChooseAddressOnMap" );
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    @Override
    public void onMapReady( GoogleMap googleMap ) {
        if ( GlobalManager.getInstance().getClientLocation().getStreet() != null ){
            mLocation.setCity( GlobalManager.getInstance().getClientLocation().getCity() );
            mLocation.setStreet( GlobalManager.getInstance().getClientLocation().getStreet() );
            mLocation.setHouse( GlobalManager.getInstance().getClientLocation().getHouse() );
            setDeliveryAddress();
        } else {
            new FetchCurrentLocation().execute(  );
        }

        MapsInitializer.initialize( getContext() );
        mGoogleMap = googleMap;
        mGoogleMap.setMapType( GoogleMap.MAP_TYPE_NORMAL );
        LatLng myCurrentPosition = new LatLng( mLocation.getLatitude(), mLocation.getLongitude() );
        mMarker = mGoogleMap.addMarker( new MarkerOptions().position( myCurrentPosition ) );
        moveCameraToPosition( myCurrentPosition );
        mGoogleMap.setOnMapClickListener(  (LatLng latLng ) -> {
                mLocation.setLatitude( latLng.latitude );
                mLocation.setLongitude( latLng.longitude );
                AppUtils.transitionAnimation(getView().findViewById( R.id.addressContainerId ),
                        getView().findViewById( R.id.pleaseWaitContainerId));
                new FetchCurrentLocation().execute(  );
                moveCameraToPosition( latLng );

        } );
    }

    private void moveCameraToPosition(LatLng position){
        mMarker.setPosition( position );
        CameraPosition cameraPosition = CameraPosition.builder().target( position ).zoom( 17 ).bearing( 0 ).build();
        mGoogleMap.moveCamera( CameraUpdateFactory.newCameraPosition( cameraPosition ) );
    }

    private void setDeliveryAddress(){
        mCityEdit.setText( mLocation.getCity() );
        mStreetEdit.setText( mLocation.getStreet() );
        mHouseEdit.setText( mLocation.getHouse() );
        AppUtils.transitionAnimation( getView().findViewById( R.id.pleaseWaitContainerId) ,
                getView().findViewById( R.id.addressContainerId ) );
    }

    public interface OnChooseAddressOnMap {
        // TODO: Update argument type and name
        void onChooseAddress();
    }

    private class FetchCurrentLocation extends AsyncTask< Void, Void, Void > {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground( Void... arg0 ) {
            try {
                String geocode = mLocation.getLongitude()+","+mLocation.getLatitude();
                Call< Object > locationCall = RestController.getInstance()
                        .getApi().getLocationAddress( RestApi.API_KEY, RestApi.FORMAT_JSON, geocode );
                Response< Object > locationResponse = locationCall.execute();
                if ( locationResponse.body() != null ) {
                    String source = locationResponse.body().toString();
                    mLocation.setCity( GeoUtils.getValueFromGeocoder( source, GeoUtils.CITY_KEY ) );
                    mLocation.setStreet( GeoUtils.getValueFromGeocoder( source, GeoUtils.STREET_KEY ) );
                    mLocation.setHouse( GeoUtils.getValueFromGeocoder( source, GeoUtils.HOUSE_KEY ) );
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
            setDeliveryAddress();
        }
    }
}
