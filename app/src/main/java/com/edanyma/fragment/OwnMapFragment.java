package com.edanyma.fragment;

import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edanyma.AppConstants;
import com.edanyma.R;
import com.edanyma.activity.PersonActivity;
import com.edanyma.model.ApiResponse;
import com.edanyma.model.ClientLocationModel;
import com.edanyma.owncomponent.ModalDialog;
import com.edanyma.owncomponent.ModalMessage;
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

import static com.edanyma.manager.GlobalManager.*;


public class OwnMapFragment extends BaseFragment implements OnMapReadyCallback {

    private static final String USE_PRIMARY_ADDRESS = "primary_address";

    private static final int COLLAPSE_HEIGHT = ( int ) ConvertUtils.convertDpToPixel( 114 );
    private static final int EXPAND_HEIGHT = ( int ) ConvertUtils.convertDpToPixel( 164 );


    private static final String TAG = "OwnMapFragment";

    private GoogleMap mGoogleMap;
    private MapView mMapView;
    private ClientLocationModel mLocation;
    private Marker mMarker;

    private AppCompatEditText mCityEdit;
    private AppCompatEditText mStreetEdit;
    private AppCompatEditText mHouseEdit;
    private AppCompatEditText mEntranceEdit;
    private AppCompatEditText mIntercomEdit;
    private AppCompatEditText mFloorEdit;

    private TextView mStreetError;
    private TextView mHouseError;

    private TextView mAdditionalAddress;
    private LinearLayout mAddressContainer;

    private boolean mExpanded;

    private boolean mUseGlobalAddress;

    public OwnMapFragment() {
    }


    public static OwnMapFragment newInstance( boolean usePrimaryAddress ) {
        OwnMapFragment fragment = new OwnMapFragment();
        Bundle args = new Bundle();
        args.putBoolean( USE_PRIMARY_ADDRESS, usePrimaryAddress );
        fragment.setArguments( args );
        return fragment;
    }

    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        if ( getArguments() != null ) {
            mUseGlobalAddress = getArguments().getBoolean( USE_PRIMARY_ADDRESS );
        }
    }

    @Override
    public void onActivityCreated( @Nullable Bundle savedInstanceState ) {
        super.onActivityCreated( savedInstanceState );
    }

    @Override
    public void onViewCreated( @NonNull View view, Bundle savedInstanceState ) {
        super.onViewCreated( view, savedInstanceState );
        mExpanded = false;
        if ( mUseGlobalAddress ){
            mLocation = getInstance().getClientLocation();
        } else {
            mLocation = new ClientLocationModel();
            mLocation.setLatitude( getInstance().getClientLocation().getLatitude() );
            mLocation.setLongitude( getInstance().getClientLocation().getLongitude() );
        }

        mCityEdit = initEditText( R.id.checkOutCityId, AppConstants.ROBOTO_CONDENCED );
        mCityEdit.setEnabled( false );
        mStreetEdit = initEditText( R.id.checkOutStreetId, AppConstants.ROBOTO_CONDENCED );
        mHouseEdit = initEditText( R.id.checkOutHouseId, AppConstants.ROBOTO_CONDENCED );
        mIntercomEdit = initEditText( R.id.checkOutIntercomId, AppConstants.ROBOTO_CONDENCED );
        mEntranceEdit = initEditText( R.id.checkOutEntranceId, AppConstants.ROBOTO_CONDENCED );
        mFloorEdit = initEditText( R.id.checkOutFloorId, AppConstants.ROBOTO_CONDENCED );

        mStreetError = initTextView( R.id.checkOutStreetErrorId, AppConstants.ROBOTO_CONDENCED );
        mHouseError = initTextView( R.id.checkOutHouseErrorId, AppConstants.ROBOTO_CONDENCED );

        getView().findViewById( R.id.chooseAddressId ).setOnClickListener( ( View v ) -> {
            onChooseAddress();
        } );
        mAdditionalAddress = initTextView( R.id.additionalAddressId, AppConstants.ROBOTO_CONDENCED );
        mAdditionalAddress.setOnClickListener( ( View v ) -> {
            if ( mExpanded ) {
                onAdditionalAddressClick( EXPAND_HEIGHT, COLLAPSE_HEIGHT, View.GONE,
                        getActivity().getResources().getString( R.string.additional_address ) );
            } else {
                onAdditionalAddressClick( COLLAPSE_HEIGHT, EXPAND_HEIGHT, View.VISIBLE,
                        getActivity().getResources().getString( R.string.hide_label ) );
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


    public void onChooseAddress() {
        mLocation.setCity( mCityEdit.getText().toString() );
        mLocation.setStreet( mStreetEdit.getText().toString() );
        mLocation.setHouse( mHouseEdit.getText().toString() );
        mLocation.setEntrance( mEntranceEdit.getText().toString() );
        mLocation.setIntercom( mIntercomEdit.getText().toString() );
        mLocation.setFloor( mFloorEdit.getText().toString() );
        String errorMsg = getActivity().getResources().getString( R.string.error_required_field );;
        if( AppUtils.nullOrEmpty( mLocation.getStreet() ) ){
            AppUtils.showError( mStreetError, errorMsg, mStreetError );
            return;
        }
        if( AppUtils.nullOrEmpty( mLocation.getHouse() ) ){
            AppUtils.showError( mHouseError, errorMsg, mHouseError );
            return;
        }
        getInstance().setClientLocation( mLocation );
        if ( getActivity() instanceof PersonActivity ) {
            mLocation.setUuid( getInstance().getClient().getUuid() );
            ModalDialog.DialogParams params = ModalDialog.getDialogParms();
            params.setTitle( "Основной адрес" )
                    .setMessage( mLocation.getFullAddressAsString() )
                    .setBlueButtonText( getResources().getString( R.string.accept_message ) )
                    .setBlueButtonId( R.drawable.ic_check_outline_white_24dp )
                    .setWhiteButtonText( getResources().getString( R.string.decline_message ) )
                    .setWhiteButtonId( R.drawable.ic_close_outline_grey600_24dp );
            ModalDialog.execute( getActivity(), params ).setOnModalBtnClickListener( new ModalDialog.OnModalBtnClickListener() {
                @Override
                public void onBlueButtonClick() {
                    new UpdateClientAddress().execute( mLocation );
                }
                @Override
                public void onWhiteBtnClick() {}
            } );
            return;
        }
        getActivity().onBackPressed();
    }

    private void onAdditionalAddressClick( int start, int end, final int visibility, final String text ) {
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
    public void onMapReady( GoogleMap googleMap ) {
        if ( mUseGlobalAddress ) {
            setDeliveryAddress();
        } else {
            new FetchCurrentLocation().execute();
        }

        MapsInitializer.initialize( getContext() );
        mGoogleMap = googleMap;
        mGoogleMap.setMapType( GoogleMap.MAP_TYPE_NORMAL );
        LatLng myCurrentPosition = new LatLng( mLocation.getLatitude(), mLocation.getLongitude() );
        mMarker = mGoogleMap.addMarker( new MarkerOptions().position( myCurrentPosition ) );
        moveCameraToPosition( myCurrentPosition );
        mGoogleMap.setOnMapClickListener( ( LatLng latLng ) -> {
            mLocation.setLatitude( latLng.latitude );
            mLocation.setLongitude( latLng.longitude );
            AppUtils.transitionAnimation( getView().findViewById( R.id.addressContainerId ),
                    getView().findViewById( R.id.pleaseWaitContainerId ) );
            new FetchCurrentLocation().execute();
            moveCameraToPosition( latLng );

        } );
    }

    private void moveCameraToPosition( LatLng position ) {
        mMarker.setPosition( position );
        CameraPosition cameraPosition = CameraPosition.builder().target( position ).zoom( 17 ).bearing( 0 ).build();
        mGoogleMap.moveCamera( CameraUpdateFactory.newCameraPosition( cameraPosition ) );
    }

    private void setDeliveryAddress() {
        mCityEdit.setText( mLocation.getCity() );
        mStreetEdit.setText( mLocation.getStreet() );
        mHouseEdit.setText( mLocation.getHouse() );
        mEntranceEdit.setText( mLocation.getEntrance() );
        mFloorEdit.setText( mLocation.getFloor() );
        mIntercomEdit.setText( mLocation.getIntercom() );
        if ( mLocation.isAdditionalInfoExist() && !mExpanded ){
            onAdditionalAddressClick( COLLAPSE_HEIGHT, EXPAND_HEIGHT, View.VISIBLE,
                    getActivity().getResources().getString( R.string.hide_label ) );
        }
        AppUtils.transitionAnimation( getView().findViewById( R.id.pleaseWaitContainerId ),
                getView().findViewById( R.id.addressContainerId ) );
    }

    class UpdateClientAddress extends AsyncTask< ClientLocationModel, Void, String > {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground( ClientLocationModel... clientLocationModels ) {
            String result = null;
            try {
                Call< ApiResponse > updateCall = RestController.getInstance()
                        .getApi().updateClientAddress( AppConstants.AUTH_BEARER
                                + getInstance().getUserToken(), clientLocationModels[ 0 ] );
                Response< ApiResponse > responseUpdate = updateCall.execute();
                if ( responseUpdate.body() != null ) {
                    if ( responseUpdate.body().getStatus() == 200 ) {
                        result = responseUpdate.body().getMessage();
                        getClient().setClientLocationModel( clientLocationModels[0] );
                        setClientLocation( clientLocationModels[0] );
                    } else {
                        result = responseUpdate.body().getMessage();
                    }
                } else {
                    result = getResources().getString( R.string.internal_error );
                }
            } catch ( Exception e ) {
                result = getResources().getString( R.string.internal_error );
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute( String result ) {
            super.onPostExecute( result );
            ModalMessage.show( getActivity(), getString( R.string.title_notifications ),
                    new String[]{ result }, 500 );
            new Handler().postDelayed( () -> {
                getActivity().onBackPressed();
            }, 510 );
        }
    }

    private class FetchCurrentLocation extends AsyncTask< Void, Void, Void > {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground( Void... arg0 ) {
            try {
                String geocode = mLocation.getLongitude() + "," + mLocation.getLatitude();
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
