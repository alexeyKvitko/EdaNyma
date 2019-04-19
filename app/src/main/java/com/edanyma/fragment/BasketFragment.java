package com.edanyma.fragment;


import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import com.edanyma.AppConstants;
import com.edanyma.R;
import com.edanyma.manager.BasketOrderManager;
import com.edanyma.model.MenuEntityModel;
import com.edanyma.recyclerview.BasketAdapter;
import com.edanyma.utils.AppUtils;
import com.edanyma.utils.ConvertUtils;

import java.util.ArrayList;
import java.util.List;


public class BasketFragment extends BaseFragment implements View.OnClickListener,
                    BasketAdapter.CardClickListener{

    private OnBasketCheckOutListener mListener;

    public static final int BASKET_IMAGE_WIDTH = ( int ) ConvertUtils.convertDpToPixel( 64 );
    public static final int BASKET_IMAGE_HEIGHT = ( int ) ConvertUtils.convertDpToPixel( 64 );

    private static final int BASKET_HEADER_HEIGHT = (int) ConvertUtils.convertDpToPixel( 56 );
    private static final int BASKET_ROW_HEIGHT = (int) ConvertUtils.convertDpToPixel( 104 );
    private static final int ANIMATION_DUARATION = 300;

    private RecyclerView mBasketRecView;
    private BasketAdapter mBasketAdapter;

    private int mBasketHeight;
    private List<MenuEntityModel> mBasket;

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
        mBasket = BasketOrderManager.getInstance().getBasket();
        int basketHeight =  mBasket.size() > 3 ? 3 : mBasket.size();
        mBasketHeight = BASKET_HEADER_HEIGHT + BASKET_ROW_HEIGHT* basketHeight;
        animateBasketBody( 0, mBasketHeight );
        getView().findViewById( R.id.basketFragmentId ).setOnClickListener( this );
        initTextView( R.id.basketTitleId, AppConstants.B52 );
        initTextView( R.id.checkOutTitleId, AppConstants.ROBOTO_CONDENCED ).setOnClickListener( this );

    }

    private void animateBasketBody( int start, int end){
        Animation fade = AnimationUtils.loadAnimation( getActivity(), R.anim.fade_in );
        if ( mBasketHeight == start ){
            fade = AnimationUtils.loadAnimation( getActivity(), R.anim.fade_out );
        }
        getView().findViewById( R.id.shadowBasketLayoutId ).startAnimation( fade );
        final View basketBody = getView().findViewById( R.id.basketBodyId );
        final FrameLayout.LayoutParams layoutParams = ( FrameLayout.LayoutParams ) basketBody.getLayoutParams();
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

    private void initRecView() {
        if ( mBasketRecView == null ) {
            mBasketRecView = getView().findViewById( R.id.basketRVId );
            mBasketRecView.setLayoutManager( new LinearLayoutManager( getContext(), RecyclerView.VERTICAL, false ) );
            mBasketRecView.setAdapter( mBasketAdapter );
            mBasketRecView.setHasFixedSize( false );
        }
        mBasketRecView.getAdapter().notifyDataSetChanged();
    }

    private void initAdapter() {
        if ( mBasketAdapter == null ) {
            fillBasketAdapter( mBasket );
        }
        mBasketAdapter.setOnItemClickListener( this );
        mBasketAdapter.notifyDataSetChanged();
    }

    private void fillBasketAdapter( List< MenuEntityModel > entities ) {
        if ( mBasketAdapter == null ) {
            mBasketAdapter = new BasketAdapter( new ArrayList< MenuEntityModel >() );
        } else {
            mBasketAdapter.deleteAllItem();
        }
        int idx = 0;
        for ( MenuEntityModel entity : entities ) {
            mBasketAdapter.addItem( entity, idx );
        }
    }

    @Override
    public void onAttach( Context context ) {
        super.onAttach( context );
        if ( context instanceof OnBasketCheckOutListener ) {
            mListener = ( OnBasketCheckOutListener ) context;
        } else {
            throw new RuntimeException( context.toString()
                    + " must implement OnBasketCheckOutListener" );
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
        if ( mBasketRecView != null ) {
            mBasketRecView.setAdapter( null );
            mBasketRecView = null;
        }
        mBasketAdapter = null;
    }

    private void closeBasket( boolean needBack ){
        animateBasketBody( mBasketHeight, 0 );
        if ( needBack ){
            new Handler( ).postDelayed( new Runnable() {
                @Override
                public void run() {
                    getActivity().onBackPressed();
                }
            }, ANIMATION_DUARATION );
        }
    }

    @Override
    public void onClick( View view ) {
        switch ( view.getId() ){
            case R.id.basketFragmentId:
                closeBasket( true );
                break;
            case R.id.checkOutTitleId:{
                if( mListener != null ){
                    AppUtils.clickAnimation( view );
                    mListener.onBasketCheckOut();
                    closeBasket( false );
                }
            }
        }
    }

    @Override
    public void onItemClick( int position, View v ) {
        BasketOrderManager.getInstance().removeEntityFromBasket( mBasket.get( position ) );
        mBasketAdapter.deleteItem( position );
        mBasketAdapter.notifyDataSetChanged();
        mBasket = BasketOrderManager.getInstance().getBasket();
        if ( mBasket.size() < 3 ){
            int oldHeight = mBasketHeight;
            mBasketHeight = BASKET_HEADER_HEIGHT + BASKET_ROW_HEIGHT* mBasket.size();
            animateBasketBody( oldHeight, mBasketHeight );
        }
        if ( mBasket.size() == 0 ){
            closeBasket( true );
        }
    }

    public interface OnBasketCheckOutListener {
        void onBasketCheckOut();
    }
}
