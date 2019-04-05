package com.edanyma.fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.edanyma.AppConstants;
import com.edanyma.R;
import com.edanyma.activity.BaseActivity;
import com.edanyma.activity.CompanyDishActivity;
import com.edanyma.manager.GlobalManager;
import com.edanyma.model.CompanyInfoModel;
import com.edanyma.model.MenuEntityModel;
import com.edanyma.owncomponent.DishEntityCard;
import com.edanyma.owncomponent.OwnSearchView;
import com.edanyma.pixelshot.PixelShot;
import com.edanyma.recyclerview.DishEntityAdapter;
import com.edanyma.recyclerview.StickyRecyclerView;
import com.edanyma.utils.AppUtils;
import com.edanyma.utils.ConvertUtils;
import com.edanyma.utils.PicassoClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class CompanyDishFragment extends BaseFragment implements OwnSearchView.OwnSearchViewListener,
        DishEntityAdapter.CardClickListener, StickyRecyclerView.OnActionHeaderListener,
        PixelShot.PixelShotListener, View.OnClickListener {

    private static final String TAG = "CompanyDishFragment";

    private OnDishActionListener mListener;

    private static final int REC_VIEW_CARD_HEIGHT = 150;
    private static final int HEADER_EXPAND_MARGIN_TOP = 212;
    private static final int HEADER_COLLAPSE_MARGIN_TOP = 28;
    private static final int HEADER_COLLAPSE_ANIMATION_MARGIN = 176;
    private static final int HEADER_ANIMATION_DURATION = 600;


    private TextView mSelectedDish;
    private TextView mSelectedDishTop;
    private StickyRecyclerView mDishRecView;
    private DishEntityAdapter mDishEntityAdapter;
    private RelativeLayout mHeaderContainer;
    private LinearLayout mExpandLine;

    private boolean mSearchMade;
    private CompanyInfoModel mCompanyDish;

    private MenuEntityModel mDishEntity;


    public CompanyDishFragment() {
    }

    public static CompanyDishFragment newInstance() {
        CompanyDishFragment fragment = new CompanyDishFragment();
        return fragment;
    }

    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState ) {
        return inflater.inflate( R.layout.fragment_company_dish, container, false );
    }

    @Override
    public void onActivityCreated( @Nullable Bundle savedInstanceState ) {
        super.onActivityCreated( savedInstanceState );
        mCompanyDish = ( ( CompanyDishActivity ) getActivity() ).getCompanyInfo();
        mSearchMade = false;
        TextView companyTitle = initTextView( R.id.companyDishTitleId, AppConstants.B52,
                mCompanyDish.getCompanyModel().getDisplayName() );
        companyTitle.setOnClickListener( this );
        getView().findViewById( R.id.companyInfoIconId ).setOnClickListener( this );
        PicassoClient.downloadImage( getActivity(), GlobalManager.getInstance().getBootstrapModel()
                .getStaticUrl() + String.format( AppConstants.STATIC_COMPANY_LOGO,
                mCompanyDish.getCompanyModel().getThumb() ), ( ImageView ) getView().findViewById( R.id.companyDishLogoId ) );

        initTextView( R.id.reviewCountId, AppConstants.ROBOTO_CONDENCED,
                mCompanyDish.getCompanyModel().getCommentCount() );
        initTextView( R.id.minDeliPriceId, AppConstants.ROBOTO_CONDENCED,
                "от " + mCompanyDish.getCompanyModel().getDelivery().toString() + " р." );

        initTextView( R.id.deliTimeId, AppConstants.ROBOTO_CONDENCED,
                "от " + mCompanyDish.getCompanyModel().getDeliveryTimeMin().toString() + " мин." );

        ( ( OwnSearchView ) getView().findViewById( R.id.searchDishId ) )
                .setOnApplySearchListener( this );
        String dishTitle = getActivity().getResources().getString( R.string.all_dishes_label );
        if ( GlobalManager.getInstance().getDishFilter() != null ) {
            dishTitle = GlobalManager.getInstance().getDishFilter().getDishName() + " от";
        }

        mSelectedDish = initTextView( R.id.selectedDishTitleId, AppConstants.ROBOTO_CONDENCED, Typeface.BOLD,
                dishTitle + " " + mCompanyDish.getCompanyModel().getDisplayName() );
        mSelectedDish.setOnClickListener( this );

        mSelectedDishTop = initTextView( R.id.selectedDishTopId, AppConstants.ROBOTO_CONDENCED, Typeface.BOLD,
                dishTitle + " " + mCompanyDish.getCompanyModel().getDisplayName() );

        mHeaderContainer = getView().findViewById( R.id.companyDishHeaderId );

        mExpandLine = getView().findViewById( R.id.expandLineId );
        mExpandLine.setOnClickListener( this );
    }

    private void initRecView() {
        if ( mDishRecView == null ) {
            mDishRecView = getView().findViewById( R.id.dishEntityRVId );
            mDishRecView.setOnActionHeaderListener( this );
            mDishRecView.initialize( mDishEntityAdapter, R.id.entityImgId, REC_VIEW_CARD_HEIGHT
                    , HEADER_EXPAND_MARGIN_TOP, HEADER_COLLAPSE_MARGIN_TOP );
            mDishEntity = null;
        }
        mDishRecView.getAdapter().notifyDataSetChanged();
        if ( AppConstants.FAKE_ID != GlobalManager.getInstance().getDishEntityPosition() ) {
            mDishRecView.scrollToTop( GlobalManager.getInstance().getDishEntityPosition() );
            GlobalManager.getInstance().setDishEntityPosition( AppConstants.FAKE_ID );

        }
    }

    private void initAdapter() {
        if ( mDishEntityAdapter == null ) {
            fillDishAdapter( mCompanyDish.getMenuEntities() );
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

    private void moreCompanyInfo() {
        mListener.onMoreCompanyInfo();
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
            fillDishAdapter( mCompanyDish.getMenuEntities() );
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
        for ( MenuEntityModel entity : mCompanyDish.getMenuEntities() ) {
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
            PixelShot.of( getActivity().findViewById( R.id.dishContainerId ) ).setResultListener( this ).save();
        }
    }

    @Override
    public void onRemoveHeaderAction() {
        TranslateAnimation slideUp = new TranslateAnimation( 0, 0, 0,
                -ConvertUtils.convertDpToPixel( HEADER_COLLAPSE_ANIMATION_MARGIN ) );
        slideUp.setDuration( HEADER_ANIMATION_DURATION );
        slideUp.setAnimationListener( new Animation.AnimationListener() {
            @Override
            public void onAnimationStart( Animation animation ) {
                mDishRecView.setAnimateHeader( true );
            }

            @Override
            public void onAnimationEnd( Animation animation ) {
                mHeaderContainer.setVisibility( View.GONE );
                mExpandLine.setVisibility( View.VISIBLE );
                mDishRecView.setAnimateHeader( false );
            }

            @Override
            public void onAnimationRepeat( Animation animation ) {
            }
        } );
        mHeaderContainer.startAnimation( slideUp );
    }

    @Override
    public void onRestoreHeaderAction() {
        TranslateAnimation slideDown = new TranslateAnimation( 0, 0,
                -ConvertUtils.convertDpToPixel( HEADER_COLLAPSE_ANIMATION_MARGIN ), 0 );
        slideDown.setDuration( HEADER_ANIMATION_DURATION );
        slideDown.setAnimationListener( new Animation.AnimationListener() {
            @Override
            public void onAnimationStart( Animation animation ) {
                mHeaderContainer.setVisibility( View.VISIBLE );
                mExpandLine.setVisibility( View.GONE );
                mDishRecView.setAnimateHeader( true );
            }

            @Override
            public void onAnimationEnd( Animation animation ) {
                mDishRecView.setAnimateHeader( false );
            }

            @Override
            public void onAnimationRepeat( Animation animation ) {
            }
        } );
        mHeaderContainer.startAnimation( slideDown );
    }

    @Override
    public void onPixelShotSuccess( String path ) {
        if ( AppConstants.FAKE_ID != GlobalManager.getInstance().getDishEntityPosition() ) {
            mListener.onMoreDishInfo( mCompanyDish.getCompanyModel().getDisplayName(), mDishEntity );
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
            case R.id.expandLineId:
            case R.id.selectedDishTitleId:
                AppUtils.clickAnimation( view );
                onFilterButtonClick();
                break;
            case R.id.companyDishTitleId:
            case R.id.companyInfoIconId:
                AppUtils.bounceAnimation( view );
                moreCompanyInfo();
                break;
            default:
                break;
        }

    }

    public interface OnDishActionListener {
        void onMoreDishInfo( String companyName, MenuEntityModel dishEntity );

        void onMoreCompanyInfo();

        void onFilterDishSelect();
    }

}
