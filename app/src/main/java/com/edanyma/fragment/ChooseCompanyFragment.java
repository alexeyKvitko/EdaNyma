package com.edanyma.fragment;

import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.edanyma.AppConstants;
import com.edanyma.R;
import com.edanyma.manager.GlobalManager;
import com.edanyma.model.CompanyLight;
import com.edanyma.model.CompanyModel;
import com.edanyma.owncomponent.OwnSearchView;
import com.edanyma.recycleview.CompanyAdapter;
import com.edanyma.recycleview.VegaLayoutManager;
import com.edanyma.utils.AppUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ChooseCompanyFragment extends Fragment implements OwnSearchView.OnFilterClickListener {

    private final String TAG = "ChooseCompanyFragment";

    private static final String COMPANY_FILTER = "company_filter";

    private OnCompanyChosenListener mListener;

    private RecyclerView mCompanyRecView;
    private CompanyAdapter mCompanyAdapter;
    private VegaLayoutManager mLayoutManager;
    private ColorMatrixColorFilter mGrayFilter;
    private String mCompanyFilter;
    private int mCompanyCount;

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
        if ( getArguments() != null ) {
            mCompanyFilter = getArguments().getString( COMPANY_FILTER );
            switch ( mCompanyFilter ) {
                case AppConstants.DISH_PIZZA:
                    mCategoryIds = GlobalManager.getBootstrapModel().getFastMenu().getPizzaIds();
                    break;
                case AppConstants.DISH_SUSHI:
                    mCategoryIds = GlobalManager.getBootstrapModel().getFastMenu().getShushiIds();
                    break;
                case AppConstants.DISH_BURGERS:
                    mCategoryIds = GlobalManager.getBootstrapModel().getFastMenu().getBurgerIds();
                    break;
                case AppConstants.DISH_GRILL:
                    mCategoryIds = GlobalManager.getBootstrapModel().getFastMenu().getGrillIds();
                    break;
                case AppConstants.DISH_WOK:
                    mCategoryIds = GlobalManager.getBootstrapModel().getFastMenu().getWokIds();
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
        initAdapter();
        TextView companyTitle = getView().findViewById( R.id.companyTitleId );
        companyTitle.setTypeface( AppConstants.B52 );
        companyTitle.setText( mCompanyFilter );
        TextView companyCount = getView().findViewById( R.id.companyCountId );
        companyCount.setTypeface( AppConstants.ROBOTO_CONDENCED );
        companyCount.setText( AppUtils.declension( mCompanyCount+"" ) );
//        initRecView();
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation( 0 );
        mGrayFilter = new ColorMatrixColorFilter( matrix );
        OwnSearchView ownSearchView = getView().findViewById( R.id.searchCompanyId );
        ownSearchView.setOnFilterClickListener( this );
    }

    private void initRecView() {
        if ( mCompanyRecView == null ) {
            mCompanyRecView = getView().findViewById( R.id.companyRVId );
            mCompanyRecView.setOnFlingListener( null );
            mLayoutManager = new VegaLayoutManager();
            mCompanyRecView.setLayoutManager( mLayoutManager );
            mCompanyRecView.setAdapter( mCompanyAdapter );
            mCompanyRecView.setHasFixedSize( false );
            mCompanyRecView.addOnScrollListener( new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged( RecyclerView recyclerView, int newState ) {
                }

                @Override
                public void onScrolled( RecyclerView recyclerView, int dx, int dy ) {
                    super.onScrolled( recyclerView, dx, dy );
                    changeSaturation();
                }
            } );
        }
        mCompanyRecView.getAdapter().notifyDataSetChanged();
    }

    private void initAdapter(){
        if ( mCompanyAdapter == null ) {
            fillCompanyAdapter( GlobalManager.getBootstrapModel().getCompanies() );
        }
//        mCompanyAdapter.setOnItemClickListener( this );
        mCompanyAdapter.notifyDataSetChanged();
    }

    private void fillCompanyAdapter( List< CompanyModel > companies ) {
        if ( mCompanyAdapter == null ) {
            mCompanyAdapter = new CompanyAdapter( new ArrayList< CompanyLight >() );
        }
        int idx = 0;
        for ( CompanyModel companyModel : companies ) {
            boolean filtered = true;
            CompanyLight company = new CompanyLight();
            company.setId( companyModel.getId() );
            company.setThumbUrl( companyModel.getThumbUrl() );
            company.setDisplayName( companyModel.getDisplayName() );
            company.setDelivery( "от " + companyModel.getDelivery().toString() + " руб." );
            company.setCommentCount( companyModel.getCommentCount() );
            company.setDeliveryTimeMin( "~" + companyModel.getDeliveryTimeMin() + "мин." );
            company.setDayoffWork( companyModel.getDayoffWork() );
            company.setWeekdayWork( companyModel.getWeekdayWork() );
            if( mCategoryIds != null ){
                String[] companyCategories =  companyModel.getMenuCategoiesIds().split( "," );
                filtered = false;
                for( Integer categoryId: mCategoryIds ){
                    if ( Arrays.asList(companyCategories).contains( categoryId.toString() ) ){
                        filtered = true;
                        break;
                    }
                }
            }
            if ( filtered ){
                mCompanyAdapter.addItem( company, idx );
                idx++;
            }
        }
        mCompanyCount = idx;
    }

    public void changeSaturation() {
        final View currentView[] = new View[ 3 ];
        int notNull = 0;
        for ( int i = 0; i < mLayoutManager.getItemCount(); i++ ) {
            if ( mCompanyRecView.getChildAt( i ) != null ) {
                currentView[ i ] = mCompanyRecView.getChildAt( i );
                notNull++;
            }
        }
        if ( notNull > 0 ) {
            int grayIdx = ( notNull == 2 || notNull == 1 ) ? 0 : 1;
            for ( int i = 0; i < notNull; i++ ) {
                final ImageView imageView = currentView[ i ].findViewById( R.id.companyImgId );
                if ( grayIdx != i ) {
                    imageView.setColorFilter( mGrayFilter );
                } else {
                    imageView.setColorFilter( null );
                }
            }
        }
    }

    public void onCompanyChoose( CompanyModel company ) {
        if ( mListener != null ) {
            mListener.onCompanyChose( company );
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
        if ( mCompanyRecView!= null ) {
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
    public void onFilterButtonClick() {
       if ( mListener != null ){
           mListener.onFilterClick();
       }
    }


    public interface OnCompanyChosenListener {
        void onCompanyChose( CompanyModel company );
        void onFilterClick();
    }
}
