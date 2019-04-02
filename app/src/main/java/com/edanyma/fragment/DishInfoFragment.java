package com.edanyma.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edanyma.R;
import com.edanyma.model.MenuEntityModel;

public class DishInfoFragment extends Fragment {

    private static final String COMPANY_NAME_PARAM = "company_name";
    private static final String DISH_ENTITY_PARAM = "dish_param";

    private String mCompanyName;
    private MenuEntityModel mDishEntity;

    private OnAddToBasketListener mListener;

    public DishInfoFragment() {}

    public static DishInfoFragment newInstance( String companyName, MenuEntityModel dishEntity ) {
        DishInfoFragment fragment = new DishInfoFragment();
        Bundle args = new Bundle();
        args.putString( COMPANY_NAME_PARAM, companyName );
        args.putSerializable( DISH_ENTITY_PARAM, dishEntity );
        fragment.setArguments( args );
        return fragment;
    }

    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        if ( getArguments() != null ) {
            mCompanyName = getArguments().getString( COMPANY_NAME_PARAM );
            mDishEntity = ( MenuEntityModel ) getArguments().getSerializable( DISH_ENTITY_PARAM );
        }
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState ) {
            return inflater.inflate( R.layout.fragment_dish_info, container, false );
    }


    public void onAddToBasketPressed( ) {
        if ( mListener != null ) {
            mListener.onAddToBasket( mDishEntity );
        }
    }

    @Override
    public void onAttach( Context context ) {
        super.onAttach( context );
        if ( context instanceof OnAddToBasketListener ) {
            mListener = ( OnAddToBasketListener ) context;
        } else {
            throw new RuntimeException( context.toString()
                    + " must implement OnAddToBasketListener" );
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnAddToBasketListener {
        void onAddToBasket( MenuEntityModel dishEntity );
    }
}
