package com.edanyma.recyclerview;

import android.animation.ArgbEvaluator;
import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.edanyma.AppConstants;
import com.edanyma.EdaNymaApp;
import com.edanyma.R;
import com.edanyma.utils.ConvertUtils;

import java.util.LinkedList;
import java.util.List;

public class SaturationRecyclerView extends RecyclerView {

    private final String TAG = "SaturationRecyclerView";

    private VegaLayoutManager mLayoutManager;
    private ColorMatrixColorFilter mGrayFilter;
    private int mImgViewId;
    private int mHeaderAction;
    private boolean mFirstScrool;
    private int mScrollValue;
    private float mOpenMarginTop;
    private float mCloseMarginTop;

    public SaturationRecyclerView( Context context ) {
        super( context );
    }

    private OnActionHeaderListener mListener;

    public SaturationRecyclerView( Context context, @Nullable AttributeSet attrs ) {
        super( context, attrs );
    }

    public SaturationRecyclerView( Context context, @Nullable AttributeSet attrs, int defStyle ) {
        super( context, attrs, defStyle );
    }

    public void initialize(CommonBaseAdapter adapter, int viewId, int cardHeight, int open, int close){
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation( 0 );
        mImgViewId = viewId;
        mGrayFilter = new ColorMatrixColorFilter( matrix );
        this.setOnFlingListener( null );
        mLayoutManager = new VegaLayoutManager( this, cardHeight );
        mOpenMarginTop = ConvertUtils.convertDpToPixel( open );
        mCloseMarginTop = ConvertUtils.convertDpToPixel( close );
        this.setLayoutManager( mLayoutManager );
        this.setAdapter( adapter );
        this.setHasFixedSize( false );
        mFirstScrool = true;
        mHeaderAction = AppConstants.HEADER_ACTION_RESTORE;
        mScrollValue = 0;
        this.addOnScrollListener( new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged( RecyclerView recyclerView, int newState ) {}

            @Override
            public void onScrolled( RecyclerView recyclerView, int dx, int dy ) {
               super.onScrolled( recyclerView, dx, dy );
//               changeSaturation();
            }
        } );
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
        final SaturationRecyclerView mThis = this;
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

    private void changeSaturation() {
        final List<View> displayViews = new LinkedList<>( );
        final SaturationRecyclerView mThis = this;
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
                if (  i > notNull-2 ) {
                    imageView.setColorFilter( mGrayFilter );
                } else {
                    imageView.setColorFilter( null );
                }
            }
        }
    }

    public void setOnActionHeaderListener(OnActionHeaderListener listener){
        mListener = listener;
    }

    public interface OnActionHeaderListener{
        void onRemoveHeaderAction();
        void onRestoreHeaderAction();
    }


}
