package com.edanyma.owncomponent;

import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.edanyma.AppConstants;
import com.edanyma.EdaNymaApp;
import com.edanyma.R;
import com.edanyma.model.MenuEntityModel;
import com.edanyma.utils.AppUtils;
import com.edanyma.utils.ConvertUtils;
import com.edanyma.utils.PicassoClient;

public class DishEntityCard extends FrameLayout implements View.OnClickListener {

    private static final String TAG = "DishEntityCard";

    private static final int DISH_IMAGE_WIDTH = ( int ) ConvertUtils.convertDpToPixel( 148 );
    private static final int DISH_IMAGE_HEIGHT = ( int ) ConvertUtils.convertDpToPixel( 142 );
    public static final int COLLAPSED_HEIGHT = ( int ) ConvertUtils.convertDpToPixel( 190 );
    private static final int EXPANDED_HEIGHT = ( int ) ConvertUtils.convertDpToPixel( 372 );


    private ImageView mEntityImage;
    private ImageView mClickImage;
    private TextView mEntityTitle;
    private TextView mEntityDesc;
    private TextView mEntityPrice;
    private TextView mEntitySize;
    private FrameLayout mDishCardCollapsed;
    private FrameLayout mDishCardExpanded;
    private FrameLayout mDishContainer;

    private MenuEntityModel mDishEntity;

    private int mWspBtnMargin;
    private boolean mAllWspNull;


    public DishEntityCard( Context context ) {
        super( context );
        inflate( context, R.layout.dish_entity_card, this );
        initialize();
    }

    public DishEntityCard( Context context, @Nullable AttributeSet attrs ) {
        super( context, attrs );
        inflate( context, R.layout.dish_entity_card, this );
        initialize();
    }

    private void initialize() {
        mDishContainer = findViewById( R.id.dishCardMainContainerId );
        mEntityImage = findViewById( R.id.entityImgId );
        mClickImage = findViewById( R.id.expandedImgId );
        mEntityTitle = findViewById( R.id.entityTitleTextId );
        mEntityDesc = findViewById( R.id.entityDescTextId );
        mEntityPrice = findViewById( R.id.entityPriceTextId );
        mDishCardCollapsed = findViewById( R.id.dishCardCollapsedId );
        mDishCardExpanded = findViewById( R.id.dishCardExpandedId );
        mEntitySize = findViewById( R.id.entitySizeId );

        mEntityTitle.setTypeface( AppConstants.B52 );
        mEntityDesc.setTypeface( AppConstants.ROBOTO_CONDENCED );
        mEntitySize.setTypeface( AppConstants.ROBOTO_CONDENCED );
        mEntityPrice.setTypeface( AppConstants.OFFICE, Typeface.BOLD );
    }

    private void animateDishCard( boolean expand ) {
        final View collapsedView = expand ? mDishCardCollapsed : mDishCardExpanded;
        final View expandedVew = expand ? mDishCardExpanded : mDishCardCollapsed;
        collapsedView.setElevation( 8 );
        expandedVew.setElevation( 10 );
        final int start = expand ? COLLAPSED_HEIGHT : EXPANDED_HEIGHT;
        final int end = expand ? EXPANDED_HEIGHT : COLLAPSED_HEIGHT;

        Animation fadeOut = AnimationUtils.loadAnimation( EdaNymaApp.getAppContext(), R.anim.fade_out );
        final Animation fadeIn = AnimationUtils.loadAnimation( EdaNymaApp.getAppContext(), R.anim.fade_in );

        fadeOut.setAnimationListener( new Animation.AnimationListener() {
            @Override
            public void onAnimationStart( Animation animation ) {
            }

            @Override
            public void onAnimationEnd( Animation animation ) {
                collapsedView.setVisibility( View.GONE );
            }

            @Override
            public void onAnimationRepeat( Animation animation ) {
            }
        } );
        collapsedView.startAnimation( fadeOut );
        expandedVew.setVisibility( View.VISIBLE );
        expandedVew.startAnimation( fadeIn );
        animateContainer( mDishContainer, start, end );

    }

    public ImageView getEntityImage() {
        return mEntityImage;
    }

    public void setEntityImage( String uri ) {
        PicassoClient.downloadImage( EdaNymaApp.getAppContext(), uri, this.mEntityImage,
                DISH_IMAGE_WIDTH, DISH_IMAGE_HEIGHT );
        PicassoClient.downloadImage( EdaNymaApp.getAppContext(), uri, this.mClickImage,
                DISH_IMAGE_WIDTH, DISH_IMAGE_HEIGHT );
    }


    public void setEntityTitle( String title ) {
        this.mEntityTitle.setText( title );
        TextView textView = findViewById( R.id.expandedTitleTextId );
        textView.setTypeface( AppConstants.B52 );
        textView.setText( title );
    }


    public void setEntityDesc( String desc ) {
        this.mEntityDesc.setText( desc );
        TextView textView = findViewById( R.id.expandedTextId );
        textView.setTypeface( AppConstants.ROBOTO_CONDENCED );
        textView.setText( desc );
    }


    public void setEntityPrice( String price ) {
        this.mEntityPrice.setText( price );
    }

    public void setEntitySize( String value ) {
        this.mEntitySize.setText( value );
    }

    private void setWsp( int layoutId, int sizeId, int weightId, String sizeValue, String weightValue ) {
        if ( AppUtils.nullOrEmpty( sizeValue ) && AppUtils.nullOrEmpty( weightValue ) ) {
            return;
        }
        TextView sizeView = findViewById( sizeId );
        if ( !AppUtils.nullOrEmpty( sizeValue ) ) {
            sizeView.setVisibility( View.VISIBLE );
            sizeView.setTypeface( AppConstants.ROBOTO_CONDENCED );
            sizeView.setText( sizeValue );
        } else {
            sizeView.setVisibility( View.GONE );
        }

        TextView weightView = findViewById( weightId );
        if ( !AppUtils.nullOrEmpty( weightValue ) ) {
            weightView.setVisibility( View.VISIBLE );
            weightView.setTypeface( AppConstants.ROBOTO_CONDENCED );
            weightView.setText( weightValue );
        } else {
            weightView.setVisibility( View.GONE );
        }
        findViewById( layoutId ).setOnClickListener( this );

    }

    private void setWspTextColor( int sizeId, int weightId, int colorId ) {
        ( ( TextView ) findViewById( sizeId ) ).setTextColor( EdaNymaApp.getAppContext()
                .getResources().getColor( colorId ) );
        ( ( TextView ) findViewById( weightId ) ).setTextColor( EdaNymaApp.getAppContext()
                .getResources().getColor( colorId ) );

    }


    public void dishCardClick( int dishId ) {
        if ( getDishEntityId() != dishId ) {
            return;
        }
        mAllWspNull = false;
        if ( View.VISIBLE == mDishCardCollapsed.getVisibility() ) {
            checkForNullWsp();
            animateDishCard( true );
            mWspBtnMargin = 0;
            animateWspButtonContainer( -1, mWspBtnMargin, mDishEntity.getPriceOne()
                    , R.id.dishSizeOneId, R.id.dishWeightOneId );
        } else {
            animateDishCard( false );
        }
    }

    private void setDishPrice( String price ) {
        TextView priceView = findViewById( R.id.dishPriceTextId );
        priceView.setTypeface( AppConstants.ROBOTO_CONDENCED, Typeface.BOLD );
        priceView.setText( price );
    }

    public void setDishEntity( MenuEntityModel dishEntity ) {
        this.mDishEntity = dishEntity;
        this.setEntityImage( dishEntity.getImageUrl() );
        this.setEntityTitle( dishEntity.getDisplayName() );
        this.setEntityDesc( dishEntity.getDescription() );
        this.setEntityPrice( dishEntity.getPriceOne() );
        this.setEntitySize( dishEntity.getSizeOne() != null ?
                dishEntity.getSizeOne() : dishEntity.getWeightOne() );
        this.setWsp( R.id.wspLayoutOneId, R.id.dishSizeOneId, R.id.dishWeightOneId,
                dishEntity.getSizeOne(), dishEntity.getWeightOne() );
        this.setWsp( R.id.wspLayoutTwoId, R.id.dishSizeTwoId, R.id.dishWeightTwoId,
                dishEntity.getSizeTwo(), dishEntity.getWeightTwo() );
        this.setWsp( R.id.wspLayoutThreeId, R.id.dishSizeThreeId, R.id.dishWeightThreeId,
                dishEntity.getSizeThree(), dishEntity.getWeightThree() );
        this.setWsp( R.id.wspLayoutFourId, R.id.dishSizeFourId, R.id.dishWeightFourId,
                dishEntity.getSizeFour(), dishEntity.getWeightFour() );
        this.setDishPrice( dishEntity.getPriceOne() );
        this.setWspTextColor( R.id.dishSizeOneId, R.id.dishWeightOneId, R.color.blueNeon );
        animateContainer( mDishContainer, COLLAPSED_HEIGHT, COLLAPSED_HEIGHT );
        mDishCardExpanded.setVisibility( View.GONE );
        mDishCardCollapsed.setVisibility( View.VISIBLE );
        mEntityImage.setColorFilter( null );
    }

    public int getDishEntityId() {
        return Integer.valueOf( this.mDishEntity.getId() );
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
                newWspBtnMargin = ( int ) ConvertUtils.convertDpToPixel( 73 );
                animateWspButtonContainer( mWspBtnMargin, newWspBtnMargin, mDishEntity.getPriceTwo()
                        , R.id.dishSizeTwoId, R.id.dishWeightTwoId );
                break;
            case R.id.wspLayoutThreeId:
                newWspBtnMargin = ( int ) ConvertUtils.convertDpToPixel( 145 );
                animateWspButtonContainer( mWspBtnMargin, newWspBtnMargin, mDishEntity.getPriceThree()
                        , R.id.dishSizeThreeId, R.id.dishWeightThreeId );
                break;
            case R.id.wspLayoutFourId:
                newWspBtnMargin = ( int ) ConvertUtils.convertDpToPixel( 218 );
                animateWspButtonContainer( mWspBtnMargin, newWspBtnMargin, mDishEntity.getPriceFour()
                        , R.id.dishSizeFourId, R.id.dishWeightFourId );
                break;
        }
    }

    private void animateContainer( final View view, final int start, final int end ) {
        final FrameLayout.LayoutParams layoutParams = ( FrameLayout.LayoutParams ) view.getLayoutParams();
        ValueAnimator valAnimator = ValueAnimator.ofObject( new IntEvaluator(), start, end );
        valAnimator.addUpdateListener( new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate( ValueAnimator animator ) {
                int val = ( Integer ) animator.getAnimatedValue();
                layoutParams.height = val;
                view.setLayoutParams( layoutParams );
            }
        } );
        valAnimator.setDuration( 300 );
        valAnimator.start();
    }

    private void animateWspButtonContainer( final int start, final int end, final String price,
                                            final int sizeId, final int weightId ) {
        if ( start == end ) {
            return;
        }
        final View wspButton = findViewById( R.id.wspButtonId );
        final LayoutParams layoutParams = ( LayoutParams ) wspButton.getLayoutParams();
        ValueAnimator valAnimator = ValueAnimator.ofObject( new IntEvaluator(), start, end );
        valAnimator.addUpdateListener( new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate( ValueAnimator animator ) {
                int val = ( Integer ) animator.getAnimatedValue();
                layoutParams.leftMargin = val;
                wspButton.setLayoutParams( layoutParams );
                if ( val == end ) {
                    setDishPrice( price );
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
        View basketContainer = findViewById( R.id.dishBasketId );
        RelativeLayout.LayoutParams layoutParams = ( RelativeLayout.LayoutParams ) basketContainer.getLayoutParams();
        findViewById( R.id.wspContainerId ).setVisibility( View.VISIBLE );
        int marginTop = ( int ) ConvertUtils.convertDpToPixel( 60 );
        mAllWspNull = AppUtils.nullOrEmpty( mDishEntity.getSizeOne() )
                && AppUtils.nullOrEmpty( mDishEntity.getWeightOne() )
                && AppUtils.nullOrEmpty( mDishEntity.getSizeTwo() )
                && AppUtils.nullOrEmpty( mDishEntity.getWeightTwo() )
                && AppUtils.nullOrEmpty( mDishEntity.getSizeThree() )
                && AppUtils.nullOrEmpty( mDishEntity.getWeightThree() )
                && AppUtils.nullOrEmpty( mDishEntity.getSizeFour() )
                && AppUtils.nullOrEmpty( mDishEntity.getWeightFour() );
        if ( mAllWspNull ) {
            findViewById( R.id.wspContainerId ).setVisibility( View.GONE );
            marginTop = ( int ) ConvertUtils.convertDpToPixel( 24 );
        }
        layoutParams.topMargin = marginTop;
        basketContainer.setLayoutParams( layoutParams );
    }

    public MenuEntityModel getDishEntity() {
        return mDishEntity;
    }
}
