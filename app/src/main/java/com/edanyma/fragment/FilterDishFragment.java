package com.edanyma.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edanyma.EdaNymaApp;
import com.edanyma.R;
import com.edanyma.activity.BaseActivity;


public class FilterDishFragment extends BaseFragment {

    private OnApplyDishFilterListener mListener;

    public FilterDishFragment() {}

    public static FilterDishFragment newInstance( ) {
        FilterDishFragment fragment = new FilterDishFragment();
        return fragment;
    }

    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState ) {
        return inflater.inflate( R.layout.fragment_filter_dish_entity, container, false );
    }


    public void applyDishFilter(  ) {
        if ( mListener != null ) {
            mListener.onApplyDishFiler();
        }
    }

    @Override
    public void onAttach( Context context ) {
        super.onAttach( context );
        if ( context instanceof OnApplyDishFilterListener ) {
            mListener = ( OnApplyDishFilterListener ) context;
        } else {
            throw new RuntimeException( context.toString()
                    + " must implement OnApplyDishFilterListener" );
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        (( BaseActivity ) getActivity()).getHeader().setVisibility( View.VISIBLE );
        (( BaseActivity) getActivity()).getFooter().setVisibility( View.VISIBLE );
        getActivity().findViewById( R.id.dishContainerId )
                .setBackground( EdaNymaApp.getAppContext().getResources()
                        .getDrawable( R.drawable.main_background_light ) );
    }


    public interface OnApplyDishFilterListener {
        void onApplyDishFiler();
    }
}
