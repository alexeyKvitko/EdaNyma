package com.edanyma.fragment;

import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.edanyma.AppConstants;
import com.edanyma.R;
import com.edanyma.model.ApiResponse;
import com.edanyma.model.ClientOrderModel;
import com.edanyma.model.ExistOrders;
import com.edanyma.model.OrderStatus;
import com.edanyma.model.OurClientModel;
import com.edanyma.owncomponent.ModalMessage;
import com.edanyma.recyclerview.OrderAdapter;
import com.edanyma.rest.RestController;
import com.edanyma.utils.AppUtils;
import com.edanyma.utils.ConvertUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static com.edanyma.manager.GlobalManager.getClient;
import static com.edanyma.manager.GlobalManager.getClientOrders;
import static com.edanyma.manager.GlobalManager.getUserToken;
import static com.edanyma.manager.GlobalManager.setClientOrders;

public class OrderFragment extends BaseFragment implements View.OnClickListener, OrderAdapter.OnOrderDetailsListener {

    private final String TAG = "OrderFragment";

    private static int IN_PROGRESS_MARGIN = 0;
    private static int COMPLETED_MARGIN = ( int ) ConvertUtils.convertDpToPixel( 116 );
    private static int DECLINE_MARGIN = ( int ) ConvertUtils.convertDpToPixel( 232 );

    private TextView mOrderStatusProgress;
    private TextView mOrderStatusSuccess;
    private TextView mOrderStatusDecline;
    private LinearLayout mOrderStatusBtn;

    private TextView mSelectedOrderStatus;

    private int mCurrentPosition;

    private String mOrderStatus;

    private RecyclerView mOrderRecView;
    private OrderAdapter mOrderAdapter;

    private OnShowOrderDetailsListener mListener;


    public OrderFragment() {
    }


    public static OrderFragment newInstance() {
        OrderFragment fragment = new OrderFragment();
        return fragment;
    }


    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState ) {
        return inflater.inflate( R.layout.fragment_order, container, false );
    }

    @Override
    public void onActivityCreated( @Nullable Bundle savedInstanceState ) {
        super.onActivityCreated( savedInstanceState );

        mCurrentPosition = IN_PROGRESS_MARGIN;

        mOrderStatus = OrderStatus.IN_PROGRESS.name();

        initTextView( R.id.orderTitleId, AppConstants.B52 );
        mOrderStatusProgress = initTextView( R.id.orderStatusProgressId, AppConstants.ROBOTO_CONDENCED );
        mOrderStatusSuccess = initTextView( R.id.orderStatusSuccesId, AppConstants.ROBOTO_CONDENCED );
        mOrderStatusDecline = initTextView( R.id.orderStatusDeclineId, AppConstants.ROBOTO_CONDENCED );
        mOrderStatusBtn = getView().findViewById( R.id.orderStatusBtnId );
        setThisOnClickListener( R.id.orderStatusProgressId, R.id.orderStatusSuccesId,
                                    R.id.orderStatusDeclineId );
    }

    private void animateOrderStatusContainer( final int start, final int end ) {
        final FrameLayout.LayoutParams layoutParams = ( FrameLayout.LayoutParams ) mOrderStatusBtn.getLayoutParams();
        ValueAnimator valAnimator = ValueAnimator.ofObject( new IntEvaluator(), start, end );
        valAnimator.addUpdateListener( ( ValueAnimator animator ) -> {
            int val = ( Integer ) animator.getAnimatedValue();
            if ( val == end ) {
                setOrderStatusColor();
                fillOrderAdapter( getClientOrders().getExistOrders() );
                mOrderAdapter.notifyDataSetChanged();
            }
            layoutParams.leftMargin = val;
            mOrderStatusBtn.setLayoutParams( layoutParams );
        } );
        valAnimator.setDuration( 300 );
        valAnimator.start();
    }

    private void setOrderStatusColor() {
        mOrderStatusProgress.setTextColor( getActivity().getResources().getColor( R.color.iconGrayColor ) );
        mOrderStatusSuccess.setTextColor( getActivity().getResources().getColor( R.color.iconGrayColor ) );
        mOrderStatusDecline.setTextColor( getActivity().getResources().getColor( R.color.iconGrayColor ) );
        mSelectedOrderStatus.setTextColor( getActivity().getResources().getColor( R.color.blueNeon ) );
    }


    private void chooseOrderStatus( OrderStatus selectedOrderStatus ) {
        mOrderStatus = selectedOrderStatus.name();
        if ( OrderStatus.IN_PROGRESS.equals( selectedOrderStatus ) ) {
            mSelectedOrderStatus = mOrderStatusProgress;
            animateOrderStatusContainer( mCurrentPosition, IN_PROGRESS_MARGIN );
            mCurrentPosition = IN_PROGRESS_MARGIN;
            mOrderAdapter.setColorId( R.color.grayTextColor );
        } else if ( OrderStatus.COMPLETED.equals( selectedOrderStatus ) ) {
            mSelectedOrderStatus = mOrderStatusSuccess;
            animateOrderStatusContainer( mCurrentPosition, COMPLETED_MARGIN );
            mCurrentPosition = COMPLETED_MARGIN;
            mOrderAdapter.setColorId( R.color.colorPrimaryDark );
        } else if ( OrderStatus.DECLINE.equals( selectedOrderStatus ) ) {
            mSelectedOrderStatus = mOrderStatusDecline;
            animateOrderStatusContainer( mCurrentPosition, DECLINE_MARGIN );
            mCurrentPosition = DECLINE_MARGIN;
            mOrderAdapter.setColorId( R.color.error_color );
        }
    }

    private void startRecView(){
        initAdapter();
        initRecView();
    }


    private void initRecView() {
        if ( mOrderRecView == null ) {
            mOrderRecView = getView().findViewById( R.id.orderRVId );
            mOrderRecView.setLayoutManager( new LinearLayoutManager( getContext(), RecyclerView.VERTICAL, false ) );
            mOrderRecView.setAdapter( mOrderAdapter );
            mOrderRecView.setHasFixedSize( false );
        }
        mOrderRecView.getAdapter().notifyDataSetChanged();
    }

    private void initAdapter() {
        if ( mOrderAdapter == null ) {
            fillOrderAdapter( getClientOrders().getExistOrders() );
        }
        mOrderAdapter.setOnOrderDetailsListener( this );
        mOrderAdapter.notifyDataSetChanged();
    }

    private void fillOrderAdapter( List< ClientOrderModel > orders ) {
        if ( mOrderAdapter == null ) {
            mOrderAdapter = new OrderAdapter( new ArrayList< ClientOrderModel >() );
        } else {
            mOrderAdapter.deleteAllItem();
        }
        int idx = 0;
        for ( ClientOrderModel order : orders ) {
            if ( mOrderStatus.equals( order.getOrderStatus() ) ){
                mOrderAdapter.addItem( order, idx );
                idx++;
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if( getClientOrders() != null && !AppUtils.nullOrEmpty( getClientOrders().getExistOrders() ) ){
            startRecView();
        } else {
            AppUtils.transitionAnimation( getView().findViewById( R.id.orderContainerId ),
                    getView().findViewById( R.id.pleaseWaitContainerId ) );
            new FeetchClientOrders().execute( );
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if ( mOrderRecView != null ) {
            mOrderRecView.setAdapter( null );
            mOrderRecView = null;
        }
        mOrderAdapter = null;
    }


    @Override
    public void onAttach( Context context ) {
        super.onAttach( context );
        if ( context instanceof OnShowOrderDetailsListener ) {
            mListener = ( OnShowOrderDetailsListener ) context;
        } else {
            throw new RuntimeException( context.toString()
                    + " must implement OnShowOrderDetailsListener" );
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    

    @Override
    public void onClick( View view ) {
        AppUtils.clickAnimation( view );
        switch ( view.getId() ) {
            case R.id.orderStatusProgressId:
                chooseOrderStatus( OrderStatus.IN_PROGRESS );
                break;
            case R.id.orderStatusSuccesId:
                chooseOrderStatus( OrderStatus.COMPLETED );
                break;
            case R.id.orderStatusDeclineId:
                chooseOrderStatus( OrderStatus.DECLINE );
                break;
            default:
                break;
        }
    }

    @Override
    public void onOrderDetailsClick( ClientOrderModel order ) {
        if( mListener != null ){
            mListener.onShowOrderDetailsAction( order );
        }
    }

    public interface OnShowOrderDetailsListener {
        void onShowOrderDetailsAction( ClientOrderModel order );
    }

    private class FeetchClientOrders extends AsyncTask< Void, Void, String > {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground( Void... arg0 ) {
            String result = null;
            try {
                OurClientModel client = getClient();
                Call< ApiResponse< ExistOrders > > orderCall = RestController
                        .getApi().getClientOrders(AppConstants.AUTH_BEARER
                                + getUserToken(), client.getUuid()  );
                Response< ApiResponse< ExistOrders > > orderResponse = orderCall.execute();
                if ( orderResponse.body() != null ) {
                    if( orderResponse.body().getStatus() == 200 ){
                        ExistOrders orders = orderResponse.body().getResult();
                        setClientOrders( orders );
                    } else {
                        result = orderResponse.body().getMessage();
                    }
                } else {
                    result = getResources().getString( R.string.internal_error );
                }
            } catch ( Exception e ) {
                result = getResources().getString( R.string.internal_error );
                Log.i( TAG, e.getMessage() );
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute( String result ) {
            super.onPostExecute( result );
            AppUtils.transitionAnimation( getView().findViewById( R.id.pleaseWaitContainerId ),
                    getView().findViewById( R.id.orderContainerId ) );
            if ( result != null ){
                ModalMessage.show( getActivity(), "Сообщение", new String[] {result} );
            } else {
                startRecView();
            }
        }
    }


}
