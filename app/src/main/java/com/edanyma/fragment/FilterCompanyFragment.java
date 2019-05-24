package com.edanyma.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.edanyma.AppConstants;
import com.edanyma.R;
import com.edanyma.manager.GlobalManager;
import com.edanyma.model.DictionaryModel;
import com.edanyma.model.FilterModel;
import com.edanyma.model.MenuCategoryModel;
import com.edanyma.model.MenuTypeModel;
import com.edanyma.owncomponent.FilterSelect;
import com.edanyma.utils.AppUtils;

import java.util.LinkedList;
import java.util.List;

import static com.edanyma.manager.GlobalManager.*;


public class FilterCompanyFragment extends Fragment {


    private OnApplyFilterListener mListener;

    private FilterSelect mDishFilter;
    private FilterSelect mKitchenFilter;
    private FilterSelect mPayFilter;
    private FilterSelect mExtraFilter;

    public FilterCompanyFragment() {
    }

    public static FilterCompanyFragment newInstance() {
        FilterCompanyFragment fragment = new FilterCompanyFragment();
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
        mDishFilter = getView().findViewById( R.id.filterDishId );
        mKitchenFilter = getView().findViewById( R.id.filterKitchenId );
        mPayFilter = getView().findViewById( R.id.filterPayId );
        mExtraFilter = getView().findViewById( R.id.filterExtraId );
        if ( getCompanyFilter() != null ){
            mDishFilter.setSelectedIds( getCompanyFilter().getDishesId() );
            mKitchenFilter.setSelectedIds( getCompanyFilter().getKitchenId() );
            mPayFilter.setSelectedIds( getCompanyFilter().getPayTypes() );
            mExtraFilter.setSelectedIds( getCompanyFilter().getExtraFilters() );
        }

        final TextView clearFilter = getView().findViewById( R.id.clearFilterButtonId );
        clearFilter.setTypeface( AppConstants.ROBOTO_CONDENCED );
        clearFilter.setOnClickListener( (View view) -> {
                clearFilters( view );
        } );

        Button applyButton =  getView().findViewById( R.id.applyFilterButtonId );
        applyButton.setTypeface( AppConstants.ROBOTO_CONDENCED );
        applyButton.setOnClickListener( (View view) -> {
                applyFilters();
        } );

        addCategoriesFilter( false );
        addTypesFilter( false );
        addPayTypesFilter( true );
        addExtraFilter( true );
    }

    private void applyFilters(){
        FilterModel filterModel = new FilterModel();
        filterModel.setDishesId( mDishFilter.getSelectedIds() );
        filterModel.setKitchenId( mKitchenFilter.getSelectedIds() );
        filterModel.setPayTypes( mPayFilter.getSelectedIds() );
        filterModel.setExtraFilters( mExtraFilter.getSelectedIds() );
        if( filterModel.getDishesId().size() == 0
              && filterModel.getKitchenId().size() == 0
                && filterModel.getPayTypes().size() == 0
                    && filterModel.getExtraFilters().size() == 0 ){
            filterModel = null;
        }
        onApplyFilter( filterModel );
    }

    private void clearFilters(View view){
        AppUtils.clickAnimation( view );
        mDishFilter.clearFilter();
        mKitchenFilter.clearFilter();
        mPayFilter.clearFilter();
        mExtraFilter.clearFilter();
    }

    private void addCategoriesFilter( boolean hideShowAll ) {
        List< DictionaryModel > categoriesFilter = new LinkedList<>();
        for ( MenuCategoryModel menuCategory : getBootstrapModel()
                .getDeliveryMenu().getMenuCategories() ) {
            DictionaryModel model = new DictionaryModel();
            model.setId( menuCategory.getId() );
            model.setDisplayName( menuCategory.getDisplayName() );
            categoriesFilter.add( model );
        }
        mDishFilter.addFilters( categoriesFilter, AppConstants.CLOSE_DISH_FILTER_BUTTON, hideShowAll );
    }

    private void addTypesFilter( boolean hideShowAll ) {
        List< DictionaryModel > typesFilter = new LinkedList<>();
        for ( MenuTypeModel menuType : getBootstrapModel()
                .getDeliveryMenu().getMenuTypes() ) {
            DictionaryModel model = new DictionaryModel();
            model.setId( menuType.getId() );
            model.setDisplayName( menuType.getDisplayName() );
            typesFilter.add( model );
        }

        mKitchenFilter.addFilters( typesFilter, AppConstants.CLOSE_KITCHEN_FILTER_BUTTON, hideShowAll );
    }

    private void addPayTypesFilter( boolean hideShowAll ) {
        List< DictionaryModel > payFilter = new LinkedList<>();
        for ( String key : AppConstants.PAY_TYPES.keySet() ) {
            DictionaryModel model = new DictionaryModel();
            model.setId( key );
            model.setDisplayName( AppConstants.PAY_TYPES.get( key ) );
            payFilter.add( model );
        }
        mPayFilter.addFilters( payFilter, 0, hideShowAll );
    }

    private void addExtraFilter( boolean hideShowAll ) {
        List< DictionaryModel > extraFilter = new LinkedList<>();
        for ( String key : AppConstants.EXTRA_FILTER.keySet() ) {
            DictionaryModel model = new DictionaryModel();
            model.setId( key );
            model.setDisplayName( AppConstants.EXTRA_FILTER.get( key ) );
            extraFilter.add( model );
        }
        mExtraFilter.addFilters( extraFilter, 0, hideShowAll );
    }

    public void onApplyFilter( FilterModel filterModel ) {
        if ( mListener != null ) {
            mListener.onApplyFilter( filterModel );
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
        void onApplyFilter( FilterModel filterModel );
    }
}
