package com.edanyma.fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.edanyma.AppConstants;
import com.edanyma.R;


public class FilterFragment extends Fragment {


    private OnApplyFilterListener mListener;

    public FilterFragment() {}

    public static FilterFragment newInstance() {
        FilterFragment fragment = new FilterFragment();
        return fragment;
    }

    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState ) {

        return inflater.inflate( R.layout.fragment_filter, container, false );
    }

    @Override
    public void onActivityCreated( @Nullable Bundle savedInstanceState ) {
        super.onActivityCreated( savedInstanceState );
        ( ( TextView ) getView().findViewById( R.id.filterTitleId ) ).setTypeface( AppConstants.B52 );

    }

    public void onApplyFilter( Uri uri ) {
        if ( mListener != null ) {
            mListener.onApplyFilter( uri );
        }
    }

    @Override
    public void onAttach( Context context ) {
        super.onAttach( context );
        if ( context instanceof OnApplyFilterListener ) {
            mListener = ( OnApplyFilterListener ) context;
        } else {
            throw new RuntimeException( context.toString()
                    + " must implement OnApplyFilterListener" );
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnApplyFilterListener {
        void onApplyFilter( Uri uri );
    }
}
