package com.edanyma.owncomponent;

import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.edanyma.AppConstants;
import com.edanyma.EdaNymaApp;
import com.edanyma.R;
import com.edanyma.model.MenuEntityModel;
import com.edanyma.utils.AppUtils;
import com.edanyma.utils.ConvertUtils;
import com.edanyma.utils.PicassoClient;

public class CheckOutEntity extends FrameLayout implements View.OnClickListener {

    private static final String TAG = "CheckOutEntity";

    private OnRemoveFromBasketListener mListener;

    private static final int CHECKOUT_IMAGE_WIDTH = ( int ) ConvertUtils.convertDpToPixel( 64 );
    private static final int CHECKOUT_IMAGE_HEIGHT = ( int ) ConvertUtils.convertDpToPixel( 62 );

    private static final int TRASH_MAX_MARGIN = ( int ) ConvertUtils.convertDpToPixel( 48 );

    private ImageView mEntityImage;
    private TextView mEntityTitle;
    private TextView mEntityWSP;
    private TextView mEntityPrice;
    private TextView mEntityCount;
    private TextView mEntitySum;
    private CompanyTotalView mCompanyTotal;

    private MenuEntityModel mDishEntity;
    private boolean mTrashOpen;

    private Integer mPrevSum;
    private Integer mCurrentSum;


    public CheckOutEntity( Context context ) {
        super( context );
        inflate( context, R.layout.checkout_entity, this );
        initialize();
    }

    public CheckOutEntity( Context context, @Nullable AttributeSet attrs ) {
        super( context, attrs );
        inflate( context, R.layout.checkout_entity, this );
        initialize();
    }

    private void initialize() {
        mEntityImage = findViewById( R.id.checkOutEntityImgId );
        mEntityTitle = findViewById( R.id.checkOutTitleTextId );
        mEntityTitle.setTypeface( AppConstants.B52 );
        mEntityWSP = findViewById( R.id.checkOutWspId );
        mEntityWSP.setTypeface( AppConstants.ROBOTO_CONDENCED );

        mEntityCount = findViewById( R.id.dishCountTextId );
        mEntityCount.setTypeface( AppConstants.ROBOTO_CONDENCED );

        mEntityPrice = findViewById( R.id.checkOutPriceTextId );
        mEntityPrice.setTypeface( AppConstants.OFFICE );

        mEntitySum = findViewById( R.id.checkOutSumId );
        mEntitySum.setTypeface( AppConstants.OFFICE, Typeface.BOLD );

        findViewById( R.id.removeDishCountId ).setOnClickListener( this );
        findViewById( R.id.addDishCountId ).setOnClickListener( this );
        findViewById( R.id.checkOutCardId ).setOnClickListener( this );

        findViewById( R.id.checkOutRemoveDishId ).setOnClickListener(
                ( View view ) -> {
                    if ( mListener != null ) {
                        AppUtils.clickAnimation( view );
                        mListener.onRemoveFromBasket( mDishEntity );
                    }
                }
        );
        mTrashOpen = false;
    }

    public void setOnRemoveFromBasketListener( OnRemoveFromBasketListener listener ) {
        mListener = listener;
    }

    public void setDishEntity( MenuEntityModel dishEntity, CompanyTotalView companyTotal ) {
        this.mDishEntity = dishEntity;
        this.mCompanyTotal = companyTotal;
        this.setEntityImage( dishEntity.getImageUrl() );
        this.setEntityTitle( dishEntity.getDisplayName() );
        this.setEntityWSP();
        this.setEntityPrice( dishEntity.getActualPrice() );
        this.setEntityCount( dishEntity.getCount() );
        this.mCurrentSum = mDishEntity.getActualPrice() * mDishEntity.getCount();
        this.setEntitySum();
    }

    public void setEntityPrice( Integer price ) {
        this.mEntityPrice.setText( price.toString() );
    }

    public void setEntitySum() {
        mPrevSum = mCurrentSum;
        mCurrentSum = mDishEntity.getActualPrice() * mDishEntity.getCount();
        this.mEntitySum.setText( mCurrentSum.toString() );
        int delta = mCurrentSum - mPrevSum;
        this.mCompanyTotal.changeCompanyTotal( delta );
        if ( mListener != null ){
            mListener.onChangeEntityCount( delta );
        }
    }

    public void setEntityCount( Integer count ) {
        this.mEntityCount.setText( count.toString() );
    }

    public void setEntityImage( String uri ) {
        PicassoClient.downloadImage( EdaNymaApp.getAppContext(), uri, this.mEntityImage,
                CHECKOUT_IMAGE_WIDTH, CHECKOUT_IMAGE_HEIGHT );
    }

    public void setEntityTitle( String title ) {
        this.mEntityTitle.setText( title );
    }

    public void setEntityWSP() {
        String size = null;
        String weight = null;
        switch ( mDishEntity.getWspType() ) {
            case AppConstants.SEL_TYPE_ONE:
                size = mDishEntity.getSizeOne();
                weight = mDishEntity.getWeightOne();
                break;
            case AppConstants.SEL_TYPE_TWO:
                size = mDishEntity.getSizeTwo();
                weight = mDishEntity.getWeightTwo();
                break;
            case AppConstants.SEL_TYPE_THREE:
                size = mDishEntity.getSizeThree();
                weight = mDishEntity.getWeightThree();
                break;
            case AppConstants.SEL_TYPE_FOUR:
                size = mDishEntity.getSizeFour();
                weight = mDishEntity.getWeightFour();
                break;
        }
        String wsp = size != null ? size : null;
        if ( weight != null ) {
            wsp = wsp != null ? ( wsp + "/" + weight ) : weight;
        }
        this.mEntityWSP.setText( wsp );
    }

    private void showTrashIcon( int start, int end ) {
        final View mainContainer = findViewById( R.id.checkOutCollapsedId );
        final FrameLayout.LayoutParams layoutParams = ( LayoutParams ) mainContainer.getLayoutParams();

        ValueAnimator valAnimator = ValueAnimator.ofObject( new IntEvaluator(), start, end );
        valAnimator.addUpdateListener( new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate( ValueAnimator animator ) {
                int val = ( Integer ) animator.getAnimatedValue();
                layoutParams.leftMargin = -val;
                layoutParams.rightMargin = val;
                mainContainer.setLayoutParams( layoutParams );
            }
        } );
        valAnimator.setDuration( 300 );
        valAnimator.start();

    }

    @Override
    public void onClick( View view ) {
        Integer count = mDishEntity.getCount();
        switch ( view.getId() ) {
            case R.id.removeDishCountId:
                AppUtils.clickAnimation( view );
                count--;
                if ( count == 0 && !mTrashOpen ) {
                    showTrashIcon( 0, TRASH_MAX_MARGIN );
                    mTrashOpen = true;
                }
                count = count < 1 ? 1 : count;
                break;
            case R.id.addDishCountId:
                AppUtils.clickAnimation( view );
                count++;
                if ( count == 2 && mTrashOpen ) {
                    showTrashIcon( TRASH_MAX_MARGIN, 0 );
                    mTrashOpen = false;
                }
                break;
            case R.id.checkOutCardId :
                if( mTrashOpen ){
                    showTrashIcon( TRASH_MAX_MARGIN, 0 );
                    mTrashOpen = false;
                }
                break;
        }
        if ( mDishEntity.getCount() != count ) {
            mDishEntity.setCount( count );
            setEntityCount( count );
            this.setEntitySum();
        }
    }


    public interface OnRemoveFromBasketListener {
        void onRemoveFromBasket( MenuEntityModel dishEntity );
        void onChangeEntityCount( Integer delta );
    }
}
