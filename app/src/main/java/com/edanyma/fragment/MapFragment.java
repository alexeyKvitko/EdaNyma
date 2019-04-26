package com.edanyma.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edanyma.EdaNymaApp;
import com.edanyma.R;
import com.edanyma.manager.GlobalManager;
import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.runtime.image.ImageProvider;


public class MapFragment extends Fragment {

    private final String MAPKIT_API_KEY = "828c2db4-d67a-43e7-95fd-0ff1615c58ff";
    private final Point TARGET_LOCATION = new Point( GlobalManager.getLatitude(), GlobalManager.getLongitude() );

    private OnChooseAddressOnMap mListener;

    private MapView mMapView;

    public MapFragment() {
    }


    public static MapFragment newInstance() {
        MapFragment fragment = new MapFragment();

        return fragment;
    }

    @Override
    public void onCreate( Bundle savedInstanceState ) {
        MapKitFactory.setApiKey( MAPKIT_API_KEY );
        MapKitFactory.initialize( EdaNymaApp.getAppContext() );
        super.onCreate( savedInstanceState );
    }

    @Override
    public void onActivityCreated( @Nullable Bundle savedInstanceState ) {
        super.onActivityCreated( savedInstanceState );
        mMapView = ( MapView ) getView().findViewById( R.id.mapview );
        mMapView.getMap().move(
                new CameraPosition( TARGET_LOCATION, 18.0f, 0.0f, 0.0f ),
                new Animation( Animation.Type.LINEAR, 1 ),
                null );
        ImageProvider marker =  ImageProvider.fromResource( getActivity(),  R.drawable.ic_map_color_24dp );
        mMapView.getMap().getMapObjects().addPlacemark( TARGET_LOCATION, marker );
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState ) {
        // Inflate the layout for this fragment
        return inflater.inflate( R.layout.fragment_map, container, false );
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed( Uri uri ) {
        if ( mListener != null ) {
            mListener.onChooseAddress( uri );
        }
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
    public void onStop() {
        mMapView.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }

    @Override
    public void onStart() {
        // Activity onStart call must be passed to both MapView and MapKit instance.
        super.onStart();
        MapKitFactory.getInstance().onStart();
        mMapView.onStart();
    }



    public interface OnChooseAddressOnMap {
        // TODO: Update argument type and name
        void onChooseAddress( Uri uri );
    }
}
