package com.edanyma.fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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
import com.edanyma.manager.BasketOrderManager;
import com.edanyma.manager.GlobalManager;
import com.edanyma.model.ApiResponse;
import com.edanyma.model.CompanyModel;
import com.edanyma.model.Dishes;
import com.edanyma.model.MenuEntityModel;
import com.edanyma.owncomponent.DishEntityCard;
import com.edanyma.owncomponent.OwnSearchView;
import com.edanyma.pixelshot.PixelShot;
import com.edanyma.recyclerview.DishEntityAdapter;
import com.edanyma.rest.RestController;
import com.edanyma.utils.AppUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class DishFragment extends BaseFragment implements OwnSearchView.OwnSearchViewListener,
        DishEntityAdapter.CardClickListener, PixelShot.PixelShotListener, View.OnClickListener {

    private static final String TAG = "DishFragment";

    private OnDishActionListener mListener;

    private TextView mSelectedDish;
    private RecyclerView mDishRecView;
    private DishEntityAdapter mDishEntityAdapter;

    private boolean mSearchMade;
    private List< MenuEntityModel > mDishes;

    private MenuEntityModel mDishEntity;
    private Integer mSelectedDishId;
    private Integer mSelectedCompanyId;
    private String mSelectedCompanyName;


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
        mSearchMade = false;
        ( ( OwnSearchView ) getActivity().findViewById( R.id.searchEatMenuId ) ).setOnApplySearchListener( this );
        mSelectedDish = initTextView( R.id.selectedEatMenuTitleId, AppConstants.ROBOTO_CONDENCED, Typeface.BOLD, null );
        mSelectedDish.setOnClickListener( this );
    }

    private void initRecView() {
        if ( mDishRecView == null ) {
            mDishRecView = getActivity().findViewById( R.id.eatMenuEntityRVId );
            mDishRecView.setLayoutManager( new LinearLayoutManager( getContext(), RecyclerView.VERTICAL, false ) );
            mDishRecView.setAdapter( mDishEntityAdapter );
            mDishRecView.setHasFixedSize( false );
            mDishEntity = null;
        }
        mDishRecView.getAdapter().notifyDataSetChanged();
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
            entity.setCount( BasketOrderManager.getInstance().getEntityCountInBasket( entity.getId() ) );
            mDishEntityAdapter.addItem( entity, idx );
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        boolean needRefresh = false;
        mSelectedCompanyId = ( ( DishActivity ) getActivity() ).getSelectedCompanyId();
        mSelectedDishId = ( ( DishActivity ) getActivity() ).getSelectedDishId();
        Integer prevCompanyId = ( ( DishActivity ) getActivity() ).getPrevCompanyId();
        Integer prevDishId = ( ( DishActivity ) getActivity() ).getPrevDishId();
        if ( mSelectedCompanyId != prevCompanyId ) {
            needRefresh = true;
            ( ( DishActivity ) getActivity() ).setPrevCompanyId( mSelectedCompanyId );

        }
        if ( mSelectedDishId != prevDishId ) {
            needRefresh = true;
            ( ( DishActivity ) getActivity() ).setPrevDishId( mSelectedDishId );
        }
        if ( needRefresh ) {
            mSelectedCompanyName = null;
            new FetchDishes().execute();
        } else {
            afterDishesLoaded();
        }
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
            AppUtils.hideKeyboardFrom( getActivity(), getActivity().findViewById( R.id.eatMenuBodyId ) );
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
        PixelShot.of( getActivity().findViewById( R.id.eatMenuContainerId ) ).setResultListener( this ).save();
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

    private void afterDishesLoaded() {
        if ( mDishes == null ) {
            mDishes = ( ( DishActivity ) getActivity() ).getDishes();
        }
        String dishTitle = ( ( DishActivity ) getActivity() ).getDishTitle() + " " + AppUtils.declensionDish( mDishes.size() );
        if ( mSelectedCompanyName != null ) {
            dishTitle = dishTitle + ", от " + mSelectedCompanyName;
        }
        mSelectedDish.setText( dishTitle );
        initAdapter();
        AppUtils.transitionAnimation( getActivity().findViewById( R.id.dishWaitContainerId ),
                getActivity().findViewById( R.id.eatMenuEntityRVId ) );
        new Handler().postDelayed( new Runnable() {
            @Override
            public void run() {
                initRecView();
                if ( AppConstants.FAKE_ID != GlobalManager.getInstance().getDishEntityPosition() ) {
                    mDishRecView.scrollToPosition( GlobalManager.getInstance().getDishEntityPosition() );
                }
                GlobalManager.getInstance().setDishEntityPosition( AppConstants.FAKE_ID );
            }
        }, 200 );


    }

    public interface OnDishActionListener {
        void onMoreDishInfo( String companyName, MenuEntityModel dishEntity );

        void onFilterDishSelect();
    }


    private class FetchDishes extends AsyncTask< Void, Void, Void > {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground( Void... arg0 ) {
            try {
                Call< ApiResponse< Dishes > > dishesCall = RestController.getInstance()
                        .getApi().getDishes( AppConstants.AUTH_BEARER
                                        + GlobalManager.getInstance().getUserToken(), GlobalManager.getInstance().getBootstrapModel().getDeliveryCity(),
                                mSelectedDishId );
                Response< ApiResponse< Dishes > > responseDishes = dishesCall.execute();
                if ( responseDishes.body() != null ) {
                    ApiResponse< Dishes > apiResponse = responseDishes.body();
                    mDishes = new LinkedList<>();
                    for ( MenuEntityModel menuEntity : apiResponse.getResult().getDishes() ) {
                        if ( mSelectedCompanyId == null ||
                                ( mSelectedCompanyId != null &&
                                        mSelectedCompanyId.equals( Integer.valueOf( menuEntity.getCompanyId() ) ) ) ) {
                            for ( CompanyModel company : GlobalManager.getBootstrapModel().getCompanies() ) {
                                if ( mSelectedCompanyId == null && menuEntity.getCompanyId().equals( company.getId() ) ) {
                                    menuEntity.setCompanyName( company.getDisplayName() );
                                    break;
                                } else if ( mSelectedCompanyId != null && menuEntity.getCompanyId().equals( company.getId() ) ) {
                                    mSelectedCompanyName = company.getDisplayName();
                                }
                            }
                            mDishes.add( menuEntity );
                        }
                    }
                    ( ( DishActivity ) getActivity() ).setDishes( mDishes );
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
            afterDishesLoaded();
        }
    }

}
