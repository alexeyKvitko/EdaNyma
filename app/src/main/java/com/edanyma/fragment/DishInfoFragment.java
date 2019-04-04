package com.edanyma.fragment;

import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.edanyma.AppConstants;
import com.edanyma.EdaNymaApp;
import com.edanyma.R;
import com.edanyma.activity.BaseActivity;
import com.edanyma.manager.GlobalManager;
import com.edanyma.model.MenuCategoryModel;
import com.edanyma.model.MenuEntityModel;
import com.edanyma.model.MenuTypeModel;
import com.edanyma.utils.AppUtils;
import com.edanyma.utils.ConvertUtils;
import com.edanyma.utils.PicassoClient;

public class DishInfoFragment extends BaseFragment implements View.OnClickListener {

    private static final String COMPANY_NAME_PARAM = "company_name";
    private static final String DISH_ENTITY_PARAM = "dish_param";
    private static final int DISH_IMAGE_WIDTH = ( int ) ConvertUtils.convertDpToPixel( 184 );
    private static final int DISH_IMAGE_HEIGHT = ( int ) ConvertUtils.convertDpToPixel( 176 );

    private String mCompanyName;
    private MenuEntityModel mDishEntity;
    private TextView mDishPrice;

    private int mWspBtnMargin;
    private boolean mAllWspNull;

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
    public void onActivityCreated( @Nullable Bundle savedInstanceState ) {
        super.onActivityCreated( savedInstanceState );
        mAllWspNull = false;
        mWspBtnMargin = 0;
        PicassoClient.downloadImage( EdaNymaApp.getAppContext(), mDishEntity.getImageUrl()
                , ( ImageView )getView().findViewById( R.id.dishInfoImgId )
                                                            , DISH_IMAGE_WIDTH, DISH_IMAGE_HEIGHT );
        initTextView(  R.id.dishInfoCompanyId , AppConstants.OFFICE , mCompanyName );
        String kitchen =  null;
        String category = null;
        for( MenuTypeModel menuType : GlobalManager.getInstance().getBootstrapModel().getDeliveryMenu().getMenuTypes() ){
            if( mDishEntity.getTypeId().equals( menuType.getId() ) ){
                kitchen = menuType.getDisplayName();
                break;
            }
        }
        for( MenuCategoryModel menuCategory : GlobalManager.getInstance().getBootstrapModel().getDeliveryMenu().getMenuCategories() ){
            if( mDishEntity.getCategoryId().equals( menuCategory.getId() ) ){
                category = menuCategory.getDisplayName();
                break;
            }
        }
        if( kitchen != null ){
            initTextView(  R.id.dishInfoKitchenId , AppConstants.OFFICE , kitchen );
        } else {
            getView().findViewById( R.id.dishInfoKitchenId ).setVisibility( View.GONE );
        }
        if( category != null ){
            initTextView(  R.id.dishInfoCategoryId , AppConstants.OFFICE , category );
        } else {
            getView().findViewById( R.id.dishInfoCategoryId ).setVisibility( View.GONE );
        }

        initTextView(  R.id.dishInfoTitleTextId , AppConstants.B52 , mDishEntity.getDisplayName() );
        initTextView(  R.id.dishInfoDescId , AppConstants.ROBOTO_CONDENCED , mDishEntity.getDescription() );
        mDishPrice = initTextView( R.id.dishPriceTextId, AppConstants.ROBOTO_CONDENCED, Typeface.BOLD, mDishEntity.getPriceOne()  );
        checkForNullWsp();
        setWsp( R.id.wspLayoutOneId, R.id.dishSizeOneId, R.id.dishWeightOneId,
                mDishEntity.getSizeOne(), mDishEntity.getWeightOne() );
        setWsp( R.id.wspLayoutTwoId, R.id.dishSizeTwoId, R.id.dishWeightTwoId,
                mDishEntity.getSizeTwo(), mDishEntity.getWeightTwo() );
        setWsp( R.id.wspLayoutThreeId, R.id.dishSizeThreeId, R.id.dishWeightThreeId,
                mDishEntity.getSizeThree(), mDishEntity.getWeightThree() );
        setWsp( R.id.wspLayoutFourId, R.id.dishSizeFourId, R.id.dishWeightFourId,
                mDishEntity.getSizeFour(), mDishEntity.getWeightFour() );
        setWspTextColor( R.id.dishSizeOneId, R.id.dishWeightOneId, R.color.blueNeon );
        initTextView( R.id.dishInfоBackId, AppConstants.ROBOTO_CONDENCED ).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                AppUtils.clickAnimation( view );
                getActivity().onBackPressed();
            }
        } );

    }

    private void setWsp( int layoutId, int sizeId, int weightId, String sizeValue, String weightValue ) {
        if ( AppUtils.nullOrEmpty( sizeValue ) && AppUtils.nullOrEmpty( weightValue ) ) {
            getView().findViewById( layoutId ).setVisibility( View.INVISIBLE );
            return;
        }
        TextView sizeView = initTextView(  sizeId, AppConstants.ROBOTO_CONDENCED, sizeValue );
        if ( !AppUtils.nullOrEmpty( sizeValue ) ) {
            sizeView.setVisibility( View.VISIBLE );
        } else {
            sizeView.setVisibility( View.GONE );
        }

        TextView weightView = initTextView( weightId, AppConstants.ROBOTO_CONDENCED, weightValue );
        if ( !AppUtils.nullOrEmpty( weightValue ) ) {
            weightView.setVisibility( View.VISIBLE );
        } else {
            weightView.setVisibility( View.GONE );
        }
        getView().findViewById( layoutId ).setOnClickListener( this );

    }

    private void setWspTextColor( int sizeId, int weightId, int colorId ) {
        ( ( TextView ) getView().findViewById( sizeId ) ).setTextColor( EdaNymaApp.getAppContext()
                .getResources().getColor( colorId ) );
        ( ( TextView ) getView().findViewById( weightId ) ).setTextColor( EdaNymaApp.getAppContext()
                .getResources().getColor( colorId ) );
    }

    private void animateWspButtonContainer( final int start, final int end, final String price,
                                            final int sizeId, final int weightId ) {
        if ( start == end ) {
            return;
        }
        final View wspButton = getView().findViewById( R.id.wspButtonId );
        final FrameLayout.LayoutParams layoutParams = ( FrameLayout.LayoutParams ) wspButton.getLayoutParams();
        ValueAnimator valAnimator = ValueAnimator.ofObject( new IntEvaluator(), start, end );
        valAnimator.addUpdateListener( new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate( ValueAnimator animator ) {
                int val = ( Integer ) animator.getAnimatedValue();
                layoutParams.leftMargin = val;
                wspButton.setLayoutParams( layoutParams );
                if ( val == end ) {
                    mDishPrice.setText( price );
                    setWspTextColor( R.id.dishSizeOneId, R.id.dishWeightOneId, R.color.tealColor );
                    setWspTextColor( R.id.dishSizeTwoId, R.id.dishWeightTwoId, R.color.tealColor );
                    setWspTextColor( R.id.dishSizeThreeId, R.id.dishWeightThreeId, R.color.tealColor );
                    setWspTextColor( R.id.dishSizeFourId, R.id.dishWeightFourId, R.color.tealColor );

                    setWspTextColor( sizeId, weightId, R.color.blueNeon );

                    mWspBtnMargin = end;
                }
            }
        } );
        valAnimator.setDuration( 300 );
        valAnimator.start();
    }

    private void checkForNullWsp() {
        mAllWspNull = AppUtils.nullOrEmpty( mDishEntity.getSizeOne() )
                && AppUtils.nullOrEmpty( mDishEntity.getWeightOne() )
                && AppUtils.nullOrEmpty( mDishEntity.getSizeTwo() )
                && AppUtils.nullOrEmpty( mDishEntity.getWeightTwo() )
                && AppUtils.nullOrEmpty( mDishEntity.getSizeThree() )
                && AppUtils.nullOrEmpty( mDishEntity.getWeightThree() )
                && AppUtils.nullOrEmpty( mDishEntity.getSizeFour() )
                && AppUtils.nullOrEmpty( mDishEntity.getWeightFour() );
        if ( mAllWspNull ) {
            getView().findViewById( R.id.wspContainerId ).setVisibility( View.GONE );
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
        (( BaseActivity) getActivity()).getHeader().setVisibility( View.VISIBLE );
        (( BaseActivity) getActivity()).getFooter().setVisibility( View.VISIBLE );
        getActivity().findViewById( R.id.dishContainerId )
                            .setBackground( EdaNymaApp.getAppContext().getResources()
                                                .getDrawable( R.drawable.main_background_light ) );
    }

    @Override
    public void onClick( View view ) {
        int newWspBtnMargin = 0;
        switch ( view.getId() ) {
            case R.id.wspLayoutOneId:
                newWspBtnMargin = 0;
                animateWspButtonContainer( mWspBtnMargin, newWspBtnMargin, mDishEntity.getPriceOne()
                        , R.id.dishSizeOneId, R.id.dishWeightOneId );
                break;
            case R.id.wspLayoutTwoId:
                newWspBtnMargin = ( int ) ConvertUtils.convertDpToPixel( 81 );
                animateWspButtonContainer( mWspBtnMargin, newWspBtnMargin, mDishEntity.getPriceTwo()
                        , R.id.dishSizeTwoId, R.id.dishWeightTwoId );
                break;
            case R.id.wspLayoutThreeId:
                newWspBtnMargin = ( int ) ConvertUtils.convertDpToPixel( 162 );
                animateWspButtonContainer( mWspBtnMargin, newWspBtnMargin, mDishEntity.getPriceThree()
                        , R.id.dishSizeThreeId, R.id.dishWeightThreeId );
                break;
            case R.id.wspLayoutFourId:
                newWspBtnMargin = ( int ) ConvertUtils.convertDpToPixel( 243 );
                animateWspButtonContainer( mWspBtnMargin, newWspBtnMargin, mDishEntity.getPriceFour()
                        , R.id.dishSizeFourId, R.id.dishWeightFourId );
                break;
        }
    }


    public interface OnAddToBasketListener {
        void onAddToBasket( MenuEntityModel dishEntity );
    }
}