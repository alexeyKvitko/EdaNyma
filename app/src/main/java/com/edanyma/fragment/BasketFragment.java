package com.edanyma.fragment;


import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.edanyma.AppConstants;
import com.edanyma.R;
import com.edanyma.utils.ConvertUtils;


public class BasketFragment extends BaseFragment implements View.OnClickListener {

    private static final int BASKET_BODY_HEIGHT = (int) ConvertUtils.convertDpToPixel( 340 );
    private static final int ANIMATION_DUARATION = 300;


    public BasketFragment() {}

    public static BasketFragment newInstance( ) {
        BasketFragment fragment = new BasketFragment();
        return fragment;
    }

    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState ) {
        return inflater.inflate( R.layout.fragment_basket, container, false );
    }

    @Override
    public void onActivityCreated( @Nullable Bundle savedInstanceState ) {
        super.onActivityCreated( savedInstanceState );
        animateBasketBody( 0, BASKET_BODY_HEIGHT );
        getView().findViewById( R.id.basketFragmentId ).setOnClickListener( this );
        initTextView( R.id.basketTitleId, AppConstants.B52 );
        initTextView( R.id.checkOutTitleId, AppConstants.ROBOTO_CONDENCED );
    }

    private void animateBasketBody( int start, int end){
        final View basketBody = getView().findViewById( R.id.basketBodyId );
        final RelativeLayout.LayoutParams layoutParams = ( RelativeLayout.LayoutParams ) basketBody.getLayoutParams();
        ValueAnimator valAnimator = ValueAnimator.ofObject( new IntEvaluator(), start, end );
        valAnimator.addUpdateListener( new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate( ValueAnimator animator ) {
                int val = ( ( Integer ) animator.getAnimatedValue() );
                layoutParams.height = val;
                basketBody.setLayoutParams( layoutParams );
            }
        } );
        valAnimator.setDuration( ANIMATION_DUARATION );
        valAnimator.start();
    }

    private void closeBasket(){
        animateBasketBody( BASKET_BODY_HEIGHT, 0 );
        new Handler( ).postDelayed( new Runnable() {
            @Override
            public void run() {
                getActivity().onBackPressed();
            }
        }, ANIMATION_DUARATION );
    }

    @Override
    public void onClick( View view ) {
        switch ( view.getId() ){
            case R.id.basketFragmentId:
                closeBasket();
                break;
        }
    }
}
