package com.edanyma.fragment;


import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.edanyma.AppConstants;
import com.edanyma.R;
import com.edanyma.activity.PersonActivity;
import com.edanyma.model.BasketModel;
import com.edanyma.model.ClientOrderModel;
import com.edanyma.model.MenuEntityModel;
import com.edanyma.model.OrderStatus;
import com.edanyma.recyclerview.BasketAdapter;
import com.edanyma.utils.AppUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.edanyma.AppConstants.ANIMATION_DUARATION;
import static com.edanyma.AppConstants.BASKET_HEADER_HEIGHT;
import static com.edanyma.AppConstants.BASKET_ROW_HEIGHT;


public class OrderDetailsFragment extends BaseFragment implements View.OnClickListener {

    private static final String CLIENT_ORDER = "client_order_no";

    private OnLeaveFeedbackListener mListener;

    private RecyclerView mOrderDetailsRecView;
    private BasketAdapter mOrderDetailsAdapter;

    private int mOrderDetailsHeight;

    private List< MenuEntityModel > mEntities;
    private ClientOrderModel mOrder;

    public OrderDetailsFragment() {}

    public static OrderDetailsFragment newInstance( ClientOrderModel orderModel ) {
        OrderDetailsFragment fragment = new OrderDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable( CLIENT_ORDER, orderModel );
        fragment.setArguments( args );
        return fragment;
    }

    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        if ( getArguments() != null ) {
            mOrder = ( ClientOrderModel ) getArguments().getSerializable( CLIENT_ORDER );
            mEntities = new ArrayList<>();
            for( BasketModel basketModel : mOrder.getOrders() ){
                for( MenuEntityModel entity: basketModel.getBasket() ){
                    entity.setCompanyName( basketModel.getCompany().getDisplayName() );
                    mEntities.add( entity );
                }
            }
        }
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState ) {
        return inflater.inflate( R.layout.fragment_order_details, container, false );
    }

    @Override
    public void onActivityCreated( @Nullable Bundle savedInstanceState ) {
        super.onActivityCreated( savedInstanceState );
        getView().findViewById( R.id.orderDetailsFragmentId ).setBackground( Drawable.createFromPath( AppUtils.getSnapshotPath() ) );
        int detailsHeight = mEntities.size() > 3 ? 3 : mEntities.size();
        mOrderDetailsHeight = BASKET_HEADER_HEIGHT + BASKET_ROW_HEIGHT * detailsHeight;
        animateOrderDetailsBody( 0, mOrderDetailsHeight );
        getView().findViewById( R.id.orderDetailsFragmentId ).setOnClickListener( this );
        TextView title = initTextView( R.id.orderDetailsTitleId, AppConstants.B52 );
        title.setText( getActivity().getResources().getString( R.string.order_number )+" "+ mOrder.getId().toString() );
        TextView feedBackBtn = initTextView( R.id.feedbackButtonId, AppConstants.ROBOTO_CONDENCED );
        feedBackBtn.setOnClickListener( this );
        feedBackBtn.setVisibility( OrderStatus.COMPLETED.name().equals( mOrder.getOrderStatus() ) ?
                View.VISIBLE : View.GONE );
    }

    private void animateOrderDetailsBody( int start, int end ) {
        Animation fade = AnimationUtils.loadAnimation( getActivity(), R.anim.fade_in );
        if ( mOrderDetailsHeight == start ) {
            fade = AnimationUtils.loadAnimation( getActivity(), R.anim.fade_out );
        }
        getView().findViewById( R.id.shadowOrderDetailsLayoutId ).startAnimation( fade );
        final View detailsBody = getView().findViewById( R.id.orderDetailsBodyId );
        final FrameLayout.LayoutParams layoutParams = ( FrameLayout.LayoutParams ) detailsBody.getLayoutParams();
        ValueAnimator valAnimator = ValueAnimator.ofObject( new IntEvaluator(), start, end );
        valAnimator.addUpdateListener( ( ValueAnimator animator ) -> {
            int val = ( ( Integer ) animator.getAnimatedValue() );
            layoutParams.height = val;
            detailsBody.setLayoutParams( layoutParams );
        } );
        valAnimator.setDuration( ANIMATION_DUARATION );
        valAnimator.start();
    }

    private void initRecView() {
        if ( mOrderDetailsRecView == null ) {
            mOrderDetailsRecView = getView().findViewById( R.id.orderDetailsRVId );
            mOrderDetailsRecView.setLayoutManager( new LinearLayoutManager( getContext(), RecyclerView.VERTICAL, false ) );
            mOrderDetailsRecView.setAdapter( mOrderDetailsAdapter );
            mOrderDetailsRecView.setHasFixedSize( false );
        }
        mOrderDetailsRecView.getAdapter().notifyDataSetChanged();
    }

    private void initAdapter() {
        if ( mOrderDetailsAdapter == null ) {
            fillBasketAdapter( mEntities );
        }
        mOrderDetailsAdapter.notifyDataSetChanged();
    }

    private void fillBasketAdapter( List< MenuEntityModel > entities ) {
        if ( mOrderDetailsAdapter == null ) {
            mOrderDetailsAdapter = new BasketAdapter( new ArrayList< MenuEntityModel >() );
        } else {
            mOrderDetailsAdapter.deleteAllItem();
        }
        int idx = 0;
        for ( MenuEntityModel entity : entities ) {
            mOrderDetailsAdapter.addItem( entity, idx );
        }
        mOrderDetailsAdapter.hideTrashButton();
    }

    @Override
    public void onAttach( Context context ) {
        super.onAttach( context );
        if ( context instanceof OnLeaveFeedbackListener ) {
            mListener = ( OnLeaveFeedbackListener ) context;
        } else {
            throw new RuntimeException( context.toString()
                    + " must implement OnLeaveFeedbackListener" );
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
        if ( mOrderDetailsRecView != null ) {
            mOrderDetailsRecView.setAdapter( null );
            mOrderDetailsRecView = null;
        }
        mOrderDetailsAdapter = null;
        (( PersonActivity ) getActivity()).getHeader().setVisibility( View.VISIBLE );
    }

    private void closeBasket( boolean needBack ) {
        animateOrderDetailsBody( mOrderDetailsHeight, 0 );
        if ( needBack ) {
            new Handler().postDelayed( () -> {
                    getActivity().onBackPressed();
            }, ANIMATION_DUARATION );
        }
    }

    @Override
    public void onClick( View view ) {
        switch ( view.getId() ) {
            case R.id.orderDetailsFragmentId:
                closeBasket( true );
                break;
            case R.id.feedbackButtonId: {
                if ( mListener != null ) {
                    AppUtils.clickAnimation( view );
                    mListener.onLeaveFeedbackAction();
                    closeBasket( false );
                }
            }
        }
    }

    public interface OnLeaveFeedbackListener {
        void onLeaveFeedbackAction();
    }

}
