package com.edanyma.fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.edanyma.AppConstants;
import com.edanyma.R;
import com.edanyma.activity.CompanyDishActivity;
import com.edanyma.manager.GlobalManager;
import com.edanyma.model.CompanyInfoModel;
import com.edanyma.model.MenuEntityModel;
import com.edanyma.owncomponent.OwnSearchView;
import com.edanyma.recyclerview.DishEntityAdapter;
import com.edanyma.recyclerview.SaturationRecyclerView;
import com.edanyma.utils.AppUtils;
import com.edanyma.utils.PicassoClient;

import java.util.ArrayList;
import java.util.List;


public class CompanyDishFragment extends Fragment implements OwnSearchView.OwnSearchViewListener,
        DishEntityAdapter.CardClickListener,
        SaturationRecyclerView.OnActionHeaderListener {

    private final String TAG = "CompanyDishFragment";

    private CompanyInfoModel mCompanyDish;
    private TextView mSelectedDish;

    private SaturationRecyclerView mDishRecView;
    private DishEntityAdapter mDishEntityAdapter;
    private RelativeLayout mHeaderContainer;
    private ImageView mExpandLine;

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

        mSelectedDish = getView().findViewById( R.id.selectedDishTitleId );
        mSelectedDish.setTypeface( AppConstants.ROBOTO_CONDENCED, Typeface.BOLD );
        mSelectedDish.setText( "Все Блюда от " + mCompanyDish.getCompanyModel().getDisplayName() );

        mHeaderContainer = getView().findViewById( R.id.companyDishHeaderId );
        mExpandLine = getView().findViewById( R.id.expandLineId );

        mExpandLine.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                AppUtils.clickAnimation( view );
                mDishRecView.restoreHeaderAction();
            }
        } );
    }

    private void initRecView() {
        if ( mDishRecView == null ) {
            mDishRecView = getView().findViewById( R.id.dishEntityRVId );
            mDishRecView.setOnActionHeaderListener( this );
            mDishRecView.initialize( mDishEntityAdapter, R.id.entityImgId, 150, 204, 16 );
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
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onApplySearch( String query ) {

    }

    @Override
    public void onFilterButtonClick() {

    }

    @Override
    public void onItemClick( int position, View v ) {

    }

    @Override
    public void onRemoveHeaderAction() {
        Animation slideUp = AnimationUtils.loadAnimation( getActivity(), R.anim.slide_up );
        Animation fadeIn = AnimationUtils.loadAnimation( getActivity(), R.anim.fade_in );
        slideUp.setAnimationListener( new Animation.AnimationListener() {
            @Override
            public void onAnimationStart( Animation animation ) {
                mExpandLine.setVisibility( View.VISIBLE );
            }

            @Override
            public void onAnimationEnd( Animation animation ) {
                mHeaderContainer.setVisibility( View.GONE );

            }

            @Override
            public void onAnimationRepeat( Animation animation ) {}
        } );
        mHeaderContainer.startAnimation( slideUp );
        mExpandLine.startAnimation( fadeIn );

    }

    @Override
    public void onRestoreHeaderAction() {
        Animation slideDown = AnimationUtils.loadAnimation( getActivity(), R.anim.slide_down );
        Animation fadeOut = AnimationUtils.loadAnimation( getActivity(), R.anim.fade_out );
        slideDown.setAnimationListener( new Animation.AnimationListener() {
            @Override
            public void onAnimationStart( Animation animation ) {
                mHeaderContainer.setVisibility( View.VISIBLE );
            }

            @Override
            public void onAnimationEnd( Animation animation ) {
                mExpandLine.setVisibility( View.GONE );
            }

            @Override
            public void onAnimationRepeat( Animation animation ) {}
        } );
        mHeaderContainer.startAnimation( slideDown );
        mExpandLine.startAnimation( fadeOut );
    }
}
