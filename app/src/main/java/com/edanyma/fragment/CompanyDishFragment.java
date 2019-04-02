package com.edanyma.fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.edanyma.AppConstants;
import com.edanyma.EdaNymaApp;
import com.edanyma.R;
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
import java.util.List;


public class CompanyDishFragment extends Fragment implements OwnSearchView.OwnSearchViewListener,
        DishEntityAdapter.CardClickListener, StickyRecyclerView.OnActionHeaderListener,
        PixelShot.PixelShotListener {

    private static final String TAG = "CompanyDishFragment";

    private OnDishInfoListener mListener;

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
    private ImageView mSmallFilterBtn;

    private boolean mSearchMade;
    private CompanyInfoModel mCompanyDish;

    private int mSelectedDishId;
    private View mSelectedView;
    private boolean mCardClick;

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
        mCompanyDish = ( ( CompanyDishActivity ) getActivity() ).getCompanyDish();
        mSearchMade = false;
        mCardClick = false;
        TextView companyTitle = getView().findViewById( R.id.companyDishTitleId );
        ImageView companyLogo = getView().findViewById( R.id.companyDishLogoId );
        PicassoClient.downloadImage( getActivity(), GlobalManager.getInstance().getBootstrapModel()
                .getStaticUrl() + String.format( AppConstants.STATIC_COMPANY_LOGO,
                mCompanyDish.getCompanyModel().getThumb() ), companyLogo );
        companyTitle.setTypeface( AppConstants.B52 );
        companyTitle.setText( mCompanyDish.getCompanyModel().getDisplayName() );

        TextView companyReview = getView().findViewById( R.id.reviewCountId );
        companyReview.setTypeface( AppConstants.ROBOTO_CONDENCED );
        companyReview.setText( mCompanyDish.getCompanyModel().getCommentCount() );

        TextView companyDeliPrice = getView().findViewById( R.id.minDeliPriceId );
        companyDeliPrice.setTypeface( AppConstants.ROBOTO_CONDENCED );
        companyDeliPrice.setText( "от " + mCompanyDish.getCompanyModel().getDelivery().toString() + " р." );

        TextView companyDeliTime = getView().findViewById( R.id.deliTimeId );
        companyDeliTime.setTypeface( AppConstants.ROBOTO_CONDENCED );
        companyDeliTime.setText( "от " + mCompanyDish.getCompanyModel().getDeliveryTimeMin().toString() + " мин." );

        ((OwnSearchView) getView().findViewById( R.id.searchDishId ))
                                                            .setOnApplySearchListener( this );

        mSelectedDish = getView().findViewById( R.id.selectedDishTitleId );
        mSelectedDish.setTypeface( AppConstants.ROBOTO_CONDENCED, Typeface.BOLD );
        mSelectedDish.setText( "Все Блюда от " + mCompanyDish.getCompanyModel().getDisplayName() );

        mSelectedDishTop = getView().findViewById( R.id.selectedDishTopId );
        mSelectedDishTop.setTypeface( AppConstants.ROBOTO_CONDENCED, Typeface.BOLD );
        mSelectedDishTop.setText( "Все Блюда от " + mCompanyDish.getCompanyModel().getDisplayName() );

        mHeaderContainer = getView().findViewById( R.id.companyDishHeaderId );
        mExpandLine = getView().findViewById( R.id.expandLineId );
        mSmallFilterBtn = getView().findViewById( R.id.filterSmallBtnId );

    }

    private void initRecView() {
        if ( mDishRecView == null ) {
            mDishRecView = getView().findViewById( R.id.dishEntityRVId );
            mDishRecView.setOnActionHeaderListener( this );
            mDishRecView.initialize( mDishEntityAdapter, R.id.entityImgId, REC_VIEW_CARD_HEIGHT
                                        , HEADER_EXPAND_MARGIN_TOP, HEADER_COLLAPSE_MARGIN_TOP );
            mDishRecView.addOnScrollListener( new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged( RecyclerView recyclerView, int newState ) {}

                @Override
                public void onScrolled( RecyclerView recyclerView, int dx, int dy ) {
                    if( AppConstants.FAKE_ID != mSelectedDishId ){
                        onItemClick( mSelectedDishId, mSelectedView );
                    }
                    super.onScrolled( recyclerView, dx, dy );

                }
            } );
            mSelectedDishId = AppConstants.FAKE_ID;
        }
        mDishRecView.getAdapter().notifyDataSetChanged();
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
            boolean filtered = true;
            if ( filtered ) {
                mDishEntityAdapter.addItem( entity, idx );
                idx++;
            }
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
        if ( context instanceof OnDishInfoListener ) {
            mListener = ( OnDishInfoListener ) context;
        } else {
            throw new RuntimeException( context.toString()
                    + " must implement OnDishInfoListener" );
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
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

    }

    @Override
    public void onItemClick( int position, View view ) {
//        if( mCardClick ){
//            return;
//        }
        if ( view instanceof DishEntityCard ){
            DishEntityCard entityCard = ( DishEntityCard ) view;
            PixelShot.of( getActivity().findViewById( R.id.dishContainerId ) ).setResultListener(this).save();
            mListener.onMoreDishInfo( mCompanyDish.getCompanyModel().getDisplayName(), entityCard.getDishEntity()  );
//            mCardClick = true;
//            if ( AppConstants.FAKE_ID == mSelectedDishId ){
//                mDishRecView.scrollToTop( position, entityCard.getTop() );
//                mSelectedDishId = entityCard.getDishEntityId();
//                mDishRecView.changeSaturation( mSelectedDishId, true );
//                mSelectedView = entityCard;
//                entityCard.dishCardClick( mSelectedDishId );
//            } else if( mSelectedDishId == entityCard.getDishEntityId() ){
//                mDishRecView.changeSaturation( mSelectedDishId, false );
//                ((DishEntityCard) view).dishCardClick( mSelectedDishId );
//                mSelectedDishId = AppConstants.FAKE_ID;
//                mSelectedView = null;
//            }

        }
//        new Handler(  ).postDelayed( new Runnable() {
//            @Override
//            public void run() {
//                mCardClick = false;
//            }
//        }, 350 );
    }

    @Override
    public void onRemoveHeaderAction() {
        TranslateAnimation slideUp = new TranslateAnimation( 0,0,0,
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
                mSmallFilterBtn.startAnimation( AnimationUtils.loadAnimation( EdaNymaApp.getAppContext(), R.anim.fade_in ) );
                mExpandLine.setVisibility( View.VISIBLE );
                mDishRecView.setAnimateHeader( false );
            }

            @Override
            public void onAnimationRepeat( Animation animation ) {}
        } );
        mHeaderContainer.startAnimation( slideUp );
    }

    @Override
    public void onRestoreHeaderAction() {
        TranslateAnimation slideDown = new TranslateAnimation( 0,0,
                            -ConvertUtils.convertDpToPixel( HEADER_COLLAPSE_ANIMATION_MARGIN ), 0 );
        slideDown.setDuration( HEADER_ANIMATION_DURATION );
        slideDown.setAnimationListener( new Animation.AnimationListener() {
            @Override
            public void onAnimationStart( Animation animation ) {
                mHeaderContainer.setVisibility( View.VISIBLE );
                mSmallFilterBtn.startAnimation( AnimationUtils.loadAnimation( EdaNymaApp.getAppContext(), R.anim.fade_out ) );
                mExpandLine.setVisibility( View.GONE );
                mDishRecView.setAnimateHeader( true );
            }

            @Override
            public void onAnimationEnd( Animation animation ) {
                mDishRecView.setAnimateHeader( false );
            }

            @Override
            public void onAnimationRepeat( Animation animation ) {}
        } );
        mHeaderContainer.startAnimation( slideDown );
    }

    @Override
    public void onPixelShotSuccess( String path ) {
        Log.i(TAG, "SnapShot SUCCESS");
    }

    @Override
    public void onPixelShotFailed() {
        Log.e(TAG, "SnapShot FAILED");
    }

    public interface OnDishInfoListener {
        void onMoreDishInfo(String companyName, MenuEntityModel dishEntity );
    }

}
