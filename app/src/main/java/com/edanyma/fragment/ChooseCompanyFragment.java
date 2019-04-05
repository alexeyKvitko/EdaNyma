package com.edanyma.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.edanyma.AppConstants;
import com.edanyma.EdaNymaApp;
import com.edanyma.R;
import com.edanyma.manager.GlobalManager;
import com.edanyma.model.CompanyLight;
import com.edanyma.model.CompanyModel;
import com.edanyma.model.FilterDishModel;
import com.edanyma.model.FilterModel;
import com.edanyma.owncomponent.OwnSearchView;
import com.edanyma.recyclerview.CompanyAdapter;
import com.edanyma.recyclerview.StickyRecyclerView;
import com.edanyma.utils.AppUtils;
import com.edanyma.utils.ConvertUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ChooseCompanyFragment extends Fragment implements OwnSearchView.OwnSearchViewListener,
        CompanyAdapter.CardClickListener,
        StickyRecyclerView.OnActionHeaderListener {

    private final String TAG = "ChooseCompanyFragment";

    private static final String COMPANY_FILTER = "company_filter";

    private OnCompanyChosenListener mListener;

    private StickyRecyclerView mCompanyRecView;
    private CompanyAdapter mCompanyAdapter;


    private String mInitFilterParam;
    private FilterModel mCompanyFilter;
    private int mCompanyCount;
    private boolean mSearchMade;
    private RelativeLayout mHeaderContainer;

    private List< Integer > mCategoryIds;


    public ChooseCompanyFragment() {
    }

    public static ChooseCompanyFragment newInstance( String param ) {
        ChooseCompanyFragment fragment = new ChooseCompanyFragment();
        Bundle args = new Bundle();
        args.putString( COMPANY_FILTER, param );
        fragment.setArguments( args );
        return fragment;
    }

    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        mCompanyFilter = null;
        if ( getArguments() != null ) {
            mInitFilterParam = getArguments().getString( COMPANY_FILTER );
            switch ( mInitFilterParam ) {
                case AppConstants.DISH_PIZZA:
                    mCategoryIds = GlobalManager.getInstance().getBootstrapModel().getFastMenu().getPizzaIds();
                    break;
                case AppConstants.DISH_SUSHI:
                    mCategoryIds = GlobalManager.getInstance().getBootstrapModel().getFastMenu().getShushiIds();
                    break;
                case AppConstants.DISH_BURGERS:
                    mCategoryIds = GlobalManager.getInstance().getBootstrapModel().getFastMenu().getBurgerIds();
                    break;
                case AppConstants.DISH_GRILL:
                    mCategoryIds = GlobalManager.getInstance().getBootstrapModel().getFastMenu().getGrillIds();
                    break;
                case AppConstants.DISH_WOK:
                    mCategoryIds = GlobalManager.getInstance().getBootstrapModel().getFastMenu().getWokIds();
                    break;
                case AppConstants.CUSTOM_FILTER:
                    setCustomFilter();
                    mCategoryIds = null;
                    break;
                default:
                    mCategoryIds = null;
                    break;
            }
        }
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState ) {
        return inflater.inflate( R.layout.fragment_choose_company, container, false );
    }


    @Override
    public void onActivityCreated( @Nullable Bundle savedInstanceState ) {
        super.onActivityCreated( savedInstanceState );
        mSearchMade = false;
        mHeaderContainer = getView().findViewById( R.id.chooseCompanyHeaderId );
        TextView companyTitle = getView().findViewById( R.id.companyTitleId );
        companyTitle.setTypeface( AppConstants.B52 );
        companyTitle.setText( mInitFilterParam );
        TextView companyCount = getView().findViewById( R.id.companyCountId );
        companyCount.setTypeface( AppConstants.ROBOTO_CONDENCED );
        OwnSearchView ownSearchView = getView().findViewById( R.id.searchCompanyId );
        ownSearchView.setOnApplySearchListener( this );
    }

    private void initRecView() {
        if ( mCompanyRecView == null ) {
            mCompanyRecView = getView().findViewById( R.id.companyRVId );
            mCompanyRecView.initialize( mCompanyAdapter, R.id.companyImgId , 200, 116, 16);
            mCompanyRecView.setOnActionHeaderListener( this );
        }
        mCompanyRecView.getAdapter().notifyDataSetChanged();
        mCompanyAdapter.setOnItemClickListener( this );
    }

    private void initAdapter() {
        if ( mCompanyAdapter == null ) {
            fillCompanyAdapter( GlobalManager.getInstance().getBootstrapModel().getCompanies() );
        }
        mCompanyAdapter.notifyDataSetChanged();
    }

    private void fillCompanyAdapter( List< CompanyModel > companies ) {
        if ( mCompanyAdapter == null ) {
            mCompanyAdapter = new CompanyAdapter( new ArrayList< CompanyLight >() );
        } else {
            mCompanyAdapter.deleteAllItem();
        }
        int idx = 0;
        for ( CompanyModel companyModel : companies ) {
            boolean filtered = true;
            CompanyLight company = ConvertUtils.convertToCompanyLight( companyModel );
            if ( mCategoryIds != null ) {
                String[] companyCategories = companyModel.getMenuCategoiesIds().split( "," );
                filtered = false;
                for ( Integer categoryId : mCategoryIds ) {
                    if ( Arrays.asList( companyCategories ).contains( categoryId.toString() ) ) {
                        filtered = true;
                        break;
                    }
                }
            }
            if ( mCompanyFilter != null ) {
                filtered = applyCompanyFilter( companyModel );
            }
            if ( filtered ) {
                mCompanyAdapter.addItem( company, idx );
                idx++;
            }
        }
        mCompanyCount = idx;
        setCompanyCountText();
    }

    private void setCompanyCountText() {
        ( ( TextView ) getView().findViewById( R.id.companyCountId ) )
                .setText( AppUtils.declension( mCompanyCount + "" ) );
    }

    private void setCustomFilter() {
        mCompanyFilter = GlobalManager.getInstance().getCompanyFilter();
        if ( mCompanyFilter != null ) {
            int totalFilter = mCompanyFilter.getDishesId().size()
                    + mCompanyFilter.getKitchenId().size()
                    + mCompanyFilter.getPayTypes().size()
                    + mCompanyFilter.getExtraFilters().size();
            mInitFilterParam = EdaNymaApp.getAppContext().getResources().getString( R.string.filter_by_label ) +
                    AppUtils.declensionFilter( totalFilter );
        } else {
            mInitFilterParam = AppConstants.ALL_COMPANIES;
        }
    }

    public boolean applyCompanyFilter( CompanyModel company ) {
        boolean filtered = true;
        if ( mCompanyFilter.getDishesId().size() > 0 ) {
            String[] companyDishes = company.getMenuCategoiesIds().split( "," );
            filtered = false;
            for ( String dishId : mCompanyFilter.getDishesId() ) {
                if ( Arrays.asList( companyDishes ).contains( dishId ) ) {
                    filtered = true;
                    break;
                }
            }
        }
        if ( filtered && mCompanyFilter.getKitchenId().size() > 0 ) {
            String companyKitchens = company.getMenuTypeIds();
            filtered = false;
            for ( String kitchenId : mCompanyFilter.getKitchenId() ) {
                if ( Arrays.asList( companyKitchens ).contains( kitchenId ) ) {
                    filtered = true;
                    break;
                }
            }
        }
        if ( filtered && mCompanyFilter.getPayTypes().size() > 0 ) {
            filtered = false;
            for ( String payType : mCompanyFilter.getPayTypes() ) {
                switch ( payType ) {
                    case AppConstants.PAY_TYPE_CASH:
                        filtered = company.getPayTypeCash().equals( 1 );
                        break;
                    case AppConstants.PAY_TYPE_CARD:
                        filtered = company.getPayTypeCard().equals( 1 );
                        break;
                    case AppConstants.PAY_TYPE_WALLET:
                        filtered = company.getPayTypeWallet().equals( 1 );
                        break;
                    default:
                        filtered = false;
                        break;
                }
            }
        }
        //TODO APPLY EXTRA FILTERS
        return filtered;
    }


    public void onCompanyChoose( CompanyLight company ) {
        if ( mListener != null ) {
            mListener.onCompanyChose( company.getId() );
        }
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
        if ( mCompanyRecView != null ) {
            mCompanyRecView.setAdapter( null );
            mCompanyRecView.clearOnScrollListeners();
            mCompanyRecView = null;
        }
        mCompanyAdapter = null;
    }

    @Override
    public void onAttach( Context context ) {
        super.onAttach( context );
        if ( context instanceof OnCompanyChosenListener ) {
            mListener = ( OnCompanyChosenListener ) context;
        } else {
            throw new RuntimeException( context.toString()
                    + " must implement OnFragmentInteractionListener" );
        }
    }

    @Override
    public void onDetach() {
        if ( mCompanyRecView != null ) {
            mCompanyRecView.setAdapter( null );
            mCompanyRecView = null;
        }
        mCompanyAdapter = null;
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onApplySearch( String query ) {
        if ( query == null && mSearchMade ) {
            fillCompanyAdapter( GlobalManager.getInstance().getBootstrapModel().getCompanies() );
            mCompanyRecView.getAdapter().notifyDataSetChanged();
            mCompanyAdapter.notifyDataSetChanged();
            mSearchMade = false;
            AppUtils.hideKeyboardFrom( getActivity(), getView() );
            return;
        } else if ( query == null && !mSearchMade ) {
            return;
        }
        if ( query.length() < 3 ) {
            return;
        }
        mCompanyAdapter.deleteAllItem();
        int idx = 0;
        for ( CompanyModel companyModel : GlobalManager.getInstance().getBootstrapModel().getCompanies() ) {
            if ( companyModel.getDisplayName().toUpperCase().indexOf( query.toUpperCase() ) > -1 ) {
                mCompanyAdapter.addItem( ConvertUtils.convertToCompanyLight( companyModel ), idx );
                idx++;
            }
        }
        mSearchMade = true;
        mCompanyCount = mCompanyAdapter.getItemCount();
        setCompanyCountText();
        mCompanyRecView.getAdapter().notifyDataSetChanged();
        mCompanyAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick( final int position, View view ) {
        AppUtils.bounceAnimation( view.findViewById( R.id.companyImgId ) );
        new Handler( ).postDelayed( new Runnable() {
            @Override
            public void run() {
                if ( mCategoryIds != null ){
                    StringBuilder categoryIds = new StringBuilder(  );
                    for ( Integer categoryId : mCategoryIds ){
                        categoryIds.append( categoryId).append(",");
                    }
                    categoryIds.deleteCharAt( categoryIds.length()-1 );
                    GlobalManager.getInstance().setDishFilter( new FilterDishModel( AppConstants.FAST_MENU, categoryIds.toString(), mInitFilterParam ) );
                } else {
                    GlobalManager.getInstance().setDishFilter( null );
                }
                onCompanyChoose( mCompanyAdapter.getItem( position ) );
            }
        }, 300 );
    }

    @Override
    public void onRemoveHeaderAction() {
        Animation slideUp = AnimationUtils.loadAnimation( getActivity(), R.anim.slide_up );
        slideUp.setAnimationListener( new Animation.AnimationListener() {
            @Override
            public void onAnimationStart( Animation animation ) {}

            @Override
            public void onAnimationEnd( Animation animation ) {
                mHeaderContainer.setVisibility( View.GONE );
            }

            @Override
            public void onAnimationRepeat( Animation animation ) {}
        } );
        mHeaderContainer.startAnimation( slideUp );

    }

    @Override
    public void onRestoreHeaderAction() {
        Animation slideDown = AnimationUtils.loadAnimation( getActivity(), R.anim.slide_down );
        slideDown.setAnimationListener( new Animation.AnimationListener() {
            @Override
            public void onAnimationStart( Animation animation ) {
                mHeaderContainer.setVisibility( View.VISIBLE );
            }

            @Override
            public void onAnimationEnd( Animation animation ) {
            }

            @Override
            public void onAnimationRepeat( Animation animation ) {}
        } );
        mHeaderContainer.startAnimation( slideDown );
    }

    @Override
    public void onFilterButtonClick() {
        if ( mListener != null ) {
            mListener.onFilterClick();
        }
    }

    public interface OnCompanyChosenListener {
        void onCompanyChose( String companyId );
        void onFilterClick();
    }
}
