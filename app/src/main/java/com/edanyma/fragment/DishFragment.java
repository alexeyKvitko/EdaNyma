package com.edanyma.fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.edanyma.AppConstants;
import com.edanyma.R;
import com.edanyma.activity.BaseActivity;
import com.edanyma.activity.DishActivity;
import com.edanyma.manager.GlobalManager;
import com.edanyma.model.MenuCategoryModel;
import com.edanyma.model.MenuEntityModel;
import com.edanyma.owncomponent.DishEntityCard;
import com.edanyma.owncomponent.OwnSearchView;
import com.edanyma.pixelshot.PixelShot;
import com.edanyma.recyclerview.DishEntityAdapter;
import com.edanyma.utils.AppUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DishFragment extends BaseFragment implements OwnSearchView.OwnSearchViewListener,
        DishEntityAdapter.CardClickListener, PixelShot.PixelShotListener, View.OnClickListener {

    private static final String TAG = "DishFragment";

    private OnDishActionListener mListener;

    private TextView mSelectedDish;
    private RecyclerView mDishRecView;
    private DishEntityAdapter mDishEntityAdapter;

    private boolean mSearchMade;
    private List<MenuEntityModel> mDishes;

    private MenuEntityModel mDishEntity;


    public DishFragment() {
    }

    public static DishFragment newInstance() {
        DishFragment fragment = new DishFragment();
        return fragment;
    }

    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState ) {
        return inflater.inflate( R.layout.fragment_dish, container, false );
    }

    @Override
    public void onActivityCreated( @Nullable Bundle savedInstanceState ) {
        super.onActivityCreated( savedInstanceState );
        mDishes = ( ( DishActivity ) getActivity() ).getDishes();
        mSearchMade = false;
       ( ( OwnSearchView ) getView().findViewById( R.id.searchEatMenuId ) ) .setOnApplySearchListener( this );
        String dishTitle = "";
        for( MenuCategoryModel menuCategory : GlobalManager.getBootstrapModel().getDeliveryMenu().getMenuCategories() ){
            if( AppConstants.SUSHI_SET_ID == Integer.valueOf( menuCategory.getId() ).intValue() ){
                dishTitle = menuCategory.getDisplayName();
                break;
            }
        }
//        if ( GlobalManager.getInstance().getDishFilter() != null ) {
//            dishTitle = GlobalManager.getInstance().getDishFilter().getDishName() + " от";
//        }

        mSelectedDish = initTextView( R.id.selectedEatMenuTitleId, AppConstants.ROBOTO_CONDENCED, Typeface.BOLD,
                dishTitle + " " + AppUtils.declensionDish( mDishes.size() ) );
        mSelectedDish.setOnClickListener( this );

    }

    private void initRecView() {
        if ( mDishRecView == null ) {
            mDishRecView = getView().findViewById( R.id.eatMenuEntityRVId );
            mDishRecView.setLayoutManager( new LinearLayoutManager( getContext(), RecyclerView.VERTICAL, false ) );
            mDishRecView.setAdapter( mDishEntityAdapter );
            mDishRecView.setHasFixedSize( false );
            mDishEntity = null;
        }
        mDishRecView.getAdapter().notifyDataSetChanged();
        if ( AppConstants.FAKE_ID != GlobalManager.getInstance().getDishEntityPosition() ) {
            GlobalManager.getInstance().setDishEntityPosition( AppConstants.FAKE_ID );
        }
    }

    private void initAdapter() {
        if ( mDishEntityAdapter == null ) {
            fillDishAdapter( mDishes );
        }
        mDishEntityAdapter.setOnItemClickListener( this );
        mDishEntityAdapter.notifyDataSetChanged();
    }

    private void fillDishAdapter( List< MenuEntityModel > entities ) {
        if ( mDishEntityAdapter == null ) {
            mDishEntityAdapter = new DishEntityAdapter( new ArrayList< MenuEntityModel >() );
        } else {
            mDishEntityAdapter.deleteAllItem();
        }
        int idx = 0;
        for ( MenuEntityModel entity : entities ) {
            boolean filtered = isFilterCondition( entity );
            if ( filtered ) {
                mDishEntityAdapter.addItem( entity, idx );
                idx++;
            }
        }
    }

    private boolean isFilterCondition( MenuEntityModel menuEntityModel ) {
        if ( GlobalManager.getInstance().getDishFilter() == null ) {
            return true;
        }
        boolean result = false;
        if ( AppConstants.FAST_MENU.equals( GlobalManager.getInstance().getDishFilter().getKitchenId() ) ) {
            String[] categoryIds = GlobalManager.getInstance().getDishFilter().getDishId().split( "," );
            if ( Arrays.asList( categoryIds ).contains( menuEntityModel.getCategoryId() ) ) {
                result = true;
            }
        } else {
            if ( GlobalManager.getInstance().getDishFilter()
                    .getKitchenId().equals( menuEntityModel.getTypeId() )
                    && GlobalManager.getInstance().getDishFilter()
                    .getDishId().equals( menuEntityModel.getCategoryId() ) ) {
                result = true;
            }
        }

        return result;
    }



    @Override
    public void onResume() {
        super.onResume();
        initAdapter();
        initRecView();
    }

    @Override
    public void onPause() {
        super.onPause();
        if ( mDishRecView != null ) {
            mDishRecView.setAdapter( null );
            mDishRecView.clearOnScrollListeners();
            mDishRecView = null;
        }
        mDishEntityAdapter = null;
    }


    @Override
    public void onAttach( Context context ) {
        super.onAttach( context );
        ( ( BaseActivity ) getActivity() ).getHeader().findViewById( R.id.navButtonId ).setVisibility( View.GONE );
        View filterNavBtn = ( ( BaseActivity ) getActivity() ).getHeader()
                .findViewById( R.id.dishFilterNavButtonId );
        filterNavBtn.setVisibility( View.VISIBLE );
        filterNavBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                onFilterButtonClick();
            }
        } );
        if ( context instanceof OnDishActionListener ) {
            mListener = ( OnDishActionListener ) context;
        } else {
            throw new RuntimeException( context.toString()
                    + " must implement OnDishActionListener" );
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ( ( BaseActivity ) getActivity() ).getHeader().findViewById( R.id.navButtonId ).setVisibility( View.VISIBLE );
        ( ( BaseActivity ) getActivity() ).getHeader().findViewById( R.id.dishFilterNavButtonId ).setVisibility( View.GONE );
        mListener = null;
    }

    @Override
    public void onApplySearch( String query ) {
        if ( query == null && mSearchMade ) {
            fillDishAdapter( mDishes );
            mDishRecView.getAdapter().notifyDataSetChanged();
            mDishEntityAdapter.notifyDataSetChanged();
            mSearchMade = false;
            AppUtils.hideKeyboardFrom( getActivity(), getView() );
            return;
        } else if ( query == null && !mSearchMade ) {
            return;
        }
        if ( query.length() < 3 ) {
            return;
        }
        mDishEntityAdapter.deleteAllItem();
        int idx = 0;
        for ( MenuEntityModel entity : mDishes ) {
            if ( entity.getDisplayName().toUpperCase().indexOf( query.toUpperCase() ) > -1 ) {
                mDishEntityAdapter.addItem( entity, idx );
                idx++;
            }
        }
        mSearchMade = true;
        mDishRecView.getAdapter().notifyDataSetChanged();
        mDishEntityAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFilterButtonClick() {
        PixelShot.of( getActivity().findViewById( R.id.dishContainerId ) ).setResultListener( this ).save();
    }

    @Override
    public void onItemClick( final int position, View view ) {
        if ( view instanceof DishEntityCard ) {
            mDishEntity = ( ( DishEntityCard ) view ).getDishEntity();
            AppUtils.bounceAnimation( view.findViewById( R.id.entityImgId ) );
            GlobalManager.getInstance().setDishEntityPosition( position );
            PixelShot.of( getActivity().findViewById( R.id.eatMenuContainerId ) ).setResultListener( this ).save();
        }
    }


    @Override
    public void onPixelShotSuccess( String path ) {
        if ( AppConstants.FAKE_ID != GlobalManager.getInstance().getDishEntityPosition() ) {
            mListener.onMoreDishInfo( mDishEntity.getCompanyName(), mDishEntity );
        } else {
            mListener.onFilterDishSelect();
        }
    }

    @Override
    public void onPixelShotFailed() {
        Log.e( TAG, "SnapShot FAILED" );
    }

    @Override
    public void onClick( View view ) {
        switch ( view.getId() ) {
            case R.id.selectedEatMenuTitleId:
                AppUtils.clickAnimation( view );
                onFilterButtonClick();
                break;

            default:
                break;
        }

    }

    public interface OnDishActionListener {
        void onMoreDishInfo( String companyName, MenuEntityModel dishEntity );


        void onFilterDishSelect();
    }

}
