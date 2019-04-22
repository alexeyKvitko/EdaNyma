package com.edanyma.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edanyma.AppConstants;
import com.edanyma.R;
import com.edanyma.manager.BasketOrderManager;
import com.edanyma.model.MenuEntityModel;
import com.edanyma.owncomponent.CheckOutEntity;


public class CheckOutFragment extends BaseFragment implements View.OnClickListener,
    CheckOutEntity.OnRemoveFromBasketListener{

    private OnCheckOutFragmentListener mListener;

    public CheckOutFragment() {}


    public static CheckOutFragment newInstance() {
        CheckOutFragment fragment = new CheckOutFragment();
        return fragment;
    }

    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
    }

    @Override
    public void onActivityCreated( @Nullable Bundle savedInstanceState ) {
        super.onActivityCreated( savedInstanceState );
        initTextView( R.id.checkOutLabelId, AppConstants.B52 );
        getView().findViewById( R.id.checkOutBackBtnId ).setOnClickListener( this );
        CheckOutEntity checkOutEntity = getView().findViewById( R.id.checkOutEntityId );
        checkOutEntity.setDishEntity( BasketOrderManager.getInstance().getBasket().get(0) );
        checkOutEntity.setOnRemoveFromBasketListener( this );
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState ) {
        return inflater.inflate( R.layout.fragment_check_out, container, false );
    }

    public void onCheckOut() {
        if ( mListener != null ) {
            mListener.onCheckOut();
        }
    }

    @Override
    public void onAttach( Context context ) {
        super.onAttach( context );
        if ( context instanceof OnCheckOutFragmentListener ) {
            mListener = ( OnCheckOutFragmentListener ) context;
        } else {
            throw new RuntimeException( context.toString()
                    + " must implement OnCheckOutFragmentListener" );
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick( View view ) {
        switch ( view.getId() ){
            case  R.id.checkOutBackBtnId :
                getActivity().onBackPressed();
                break;
        }
    }

    @Override
    public void onRemoveFromBasket( MenuEntityModel dishEntity ) {
        BasketOrderManager.removeEntityFromBasket( dishEntity );
    }


    public interface OnCheckOutFragmentListener {
        // TODO: Update argument type and name
        void onCheckOut();
    }
}
