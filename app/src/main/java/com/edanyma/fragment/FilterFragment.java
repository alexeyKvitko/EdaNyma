package com.edanyma.fragment;

import android.content.Context;
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
import com.edanyma.manager.GlobalManager;
import com.edanyma.model.DictionaryModel;
import com.edanyma.model.MenuCategoryModel;
import com.edanyma.model.MenuTypeModel;
import com.edanyma.owncomponent.FilterSelect;

import java.util.LinkedList;
import java.util.List;


public class FilterFragment extends Fragment {


    private OnApplyFilterListener mListener;

    public FilterFragment() {
    }

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
        addCategoriesFilter();
        addTypesFilter();
        addPayTypesFilter();
        addExtraFilter();
    }

    private void addCategoriesFilter() {
        List< DictionaryModel > categoriesFilter = new LinkedList<>();
        for ( MenuCategoryModel menuCategory : GlobalManager.getBootstrapModel()
                .getDeliveryMenu().getMenuCategories() ) {
            DictionaryModel model = new DictionaryModel();
            model.setId( menuCategory.getId() );
            model.setDisplayName( menuCategory.getDisplayName() );
            categoriesFilter.add( model );
        }
        ( ( FilterSelect ) getView().findViewById( R.id.filterDishId ) ).addFilters( categoriesFilter
                , AppConstants.CLOSE_DISH_FILTER_BUTTON );
    }

    private void addTypesFilter() {
        List< DictionaryModel > typesFilter = new LinkedList<>();
        for ( MenuTypeModel menuType : GlobalManager.getBootstrapModel()
                .getDeliveryMenu().getMenuTypes() ) {
            DictionaryModel model = new DictionaryModel();
            model.setId( menuType.getId() );
            model.setDisplayName( menuType.getDisplayName() );
            typesFilter.add( model );
        }

        ( ( FilterSelect ) getView().findViewById( R.id.filterKitchenId ) ).addFilters( typesFilter
                , AppConstants.CLOSE_KITCHEN_FILTER_BUTTON );
    }

    private void addPayTypesFilter() {
        List< DictionaryModel > payFilter = new LinkedList<>();
        for ( String key : AppConstants.PAY_TYPES.keySet() ) {
            DictionaryModel model = new DictionaryModel();
            model.setId( key );
            model.setDisplayName( AppConstants.PAY_TYPES.get( key ) );
            payFilter.add( model );
        }
        ( ( FilterSelect ) getView().findViewById( R.id.filterPayId ) ).addFilters( payFilter, 0 );
    }

    private void addExtraFilter() {
        List< DictionaryModel > extraFilter = new LinkedList<>();
        for ( String key : AppConstants.EXTRA_FILTER.keySet() ) {
            DictionaryModel model = new DictionaryModel();
            model.setId( key );
            model.setDisplayName( AppConstants.EXTRA_FILTER.get( key ) );
            extraFilter.add( model );
        }
        ( ( FilterSelect ) getView().findViewById( R.id.filterExtraId ) ).addFilters( extraFilter, 0 );
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
