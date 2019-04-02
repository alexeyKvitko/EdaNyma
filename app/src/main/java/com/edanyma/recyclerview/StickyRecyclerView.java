package com.edanyma.recyclerview;

import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.edanyma.AppConstants;
import com.edanyma.owncomponent.DishEntityCard;
import com.edanyma.recyclerview.manager.StickyLayoutManager;
import com.edanyma.utils.ConvertUtils;

import java.util.LinkedList;
import java.util.List;

public class StickyRecyclerView extends RecyclerView {

    private final String TAG = "SaturationRecyclerView";

    private StickyLayoutManager mLayoutManager;
    private ColorMatrixColorFilter mGrayFilter;
    private int mImgViewId;
    private int mHeaderAction;
    private float mOpenMarginTop;
    private float mCloseMarginTop;
    private boolean mAnimateHeader;

    public StickyRecyclerView( Context context ) {
        super( context );
    }

    private OnActionHeaderListener mListener;

    public StickyRecyclerView( Context context, @Nullable AttributeSet attrs ) {
        super( context, attrs );
    }

    public StickyRecyclerView( Context context, @Nullable AttributeSet attrs, int defStyle ) {
        super( context, attrs, defStyle );
    }

    public void initialize(CommonBaseAdapter adapter, int viewId, int cardHeight, int open, int close){
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation( 0 );
        mImgViewId = viewId;
        mGrayFilter = new ColorMatrixColorFilter( matrix );
        this.setOnFlingListener( null );
        mLayoutManager = new StickyLayoutManager( this.getContext(), this, cardHeight );
        mOpenMarginTop = ConvertUtils.convertDpToPixel( open );
        mCloseMarginTop = ConvertUtils.convertDpToPixel( close );
        this.setLayoutManager( mLayoutManager );
        this.setAdapter( adapter );
        this.setHasFixedSize( false );
        mHeaderAction = AppConstants.HEADER_ACTION_RESTORE;

    }

    public void removeHeaderAction(){
        mLayoutManager.setScroll( -1 );
        if( AppConstants.HEADER_ACTION_REMOVE == mHeaderAction ){
            return;
        }
        animateLayot( mOpenMarginTop, mCloseMarginTop);
        mHeaderAction = AppConstants.HEADER_ACTION_REMOVE;
        mListener.onRemoveHeaderAction();
    }

    private void animateLayot(Float start, Float end ){
        final RelativeLayout.LayoutParams layoutParams = ( RelativeLayout.LayoutParams ) this.getLayoutParams();
        final StickyRecyclerView mThis = this;
        ValueAnimator valAnimator = ValueAnimator.ofObject( new IntEvaluator(), start.intValue(), end.intValue() );
        valAnimator.addUpdateListener( new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate( ValueAnimator animator ) {
                int val = ( Integer ) animator.getAnimatedValue();
                layoutParams.topMargin = val;
                mThis.setLayoutParams( layoutParams );
            }
        } );
        valAnimator.setDuration( 600 );
        valAnimator.start();
    }

    public void restoreHeaderAction(){
        if( AppConstants.HEADER_ACTION_RESTORE == mHeaderAction ){
            return;
        }
        animateLayot( mCloseMarginTop, mOpenMarginTop );
        mListener.onRestoreHeaderAction();
        mHeaderAction = AppConstants.HEADER_ACTION_RESTORE;
        mLayoutManager.setScroll( 0 );
    }

    public void changeSaturation( int dishId, boolean toGray ) {

        final List<View> displayViews = new LinkedList<>( );
        final StickyRecyclerView mThis = this;
        int notNull = 0;
        for ( int i = 0; i < mLayoutManager.getItemCount(); i++ ) {
            if ( mThis.getChildAt( i ) != null ) {
                displayViews.add( mThis.getChildAt( i ) );
                notNull++;
            }
        }
        if ( notNull > 0 ) {
            for ( int i = 0; i < notNull; i++ ) {
                final ImageView imageView = displayViews.get( i ).findViewById( mImgViewId );
                int currentDishId = (( DishEntityCard ) displayViews.get( i )).getDishEntityId();
                if (  toGray && dishId != currentDishId ) {
                    imageView.setColorFilter( mGrayFilter );
                } else {
                    imageView.setColorFilter( null );
                }
            }
        }
    }

    public void scrollToTop( final int position, int viewTop ){
        int offset =  (int) mCloseMarginTop;
        if ( AppConstants.HEADER_ACTION_RESTORE == mHeaderAction ){
            removeHeaderAction();
        }
        ValueAnimator valAnimator = ValueAnimator.ofObject( new IntEvaluator(), viewTop, offset );
        valAnimator.addUpdateListener( new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate( ValueAnimator animator ) {
                int val = ( Integer ) animator.getAnimatedValue();
                mLayoutManager.scrollToPositionWithOffset( position, val );
            }
        } );
        valAnimator.setDuration( 300 );
        valAnimator.start();


    }

    public void setOnActionHeaderListener(OnActionHeaderListener listener){
        mListener = listener;
    }

    public interface OnActionHeaderListener{
        void onRemoveHeaderAction();
        void onRestoreHeaderAction();
    }


    public boolean isAnimateHeader() {
        return mAnimateHeader;
    }

    public void setAnimateHeader( boolean animateHeader ) {
        this.mAnimateHeader = animateHeader;
    }
}
