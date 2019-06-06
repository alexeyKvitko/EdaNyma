package com.edanyma.recyclerview;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.edanyma.AppConstants;
import com.edanyma.EdaNymaApp;
import com.edanyma.R;
import com.edanyma.model.ClientOrderModel;
import com.edanyma.rest.RestController;
import com.edanyma.utils.AppUtils;
import com.edanyma.utils.ConvertUtils;
import com.edanyma.utils.GlideClient;

import java.util.ArrayList;

public class OrderAdapter extends CommonBaseAdapter< ClientOrderModel > {

    public static final int IMAGE_SIZE = ( int ) ConvertUtils.convertDpToPixel( 64 );

    private static final String CLASS_TAG = "OrderAdapter";

    private OnOrderDetailsListener mListener;

    private int colorId;

    public OrderAdapter( ArrayList< ClientOrderModel > mItemList ) {
        super( mItemList );
        this.colorId = R.color.grayTextColor;
    }


    @Override
    public OrderAdapter.OrderDataObjectHolder onCreateViewHolder( ViewGroup parent, int viewType ) {
        View view = LayoutInflater.from( parent.getContext() )
                .inflate( R.layout.rv_item_order, parent, false );
        OrderAdapter.OrderDataObjectHolder dataObjectHolder = new OrderAdapter.OrderDataObjectHolder( view );
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder( final BaseDataObjectHolder h, final int position ) {
        OrderAdapter.OrderDataObjectHolder holder = ( OrderAdapter.OrderDataObjectHolder ) h;
        final ClientOrderModel order = mItemList.get( position );
        GlideClient.downloadImage( EdaNymaApp.getAppContext(), RestController.COMPANIES_LOGO_URL + order.getCompanyLogo(),
                holder.orderCompanyImg, IMAGE_SIZE, IMAGE_SIZE );
        String companyName = AppUtils.nullOrEmpty( order.getCompanyTwoName() ) ? order.getCompanyOneName() :
                                        order.getCompanyOneName()+"/"+order.getCompanyTwoName();
        holder.orderCompanyName.setText( companyName );
        holder.orderPrice.setText( order.getOrderPrice().toString() );
        holder.orderDate.setText( order.getOrderDate()+", "+order.getOrderTime() );
        holder.orderNo.setText( "№ "+order.getId().toString() );
        holder.orderDate.setTextColor( EdaNymaApp.getAppContext().getResources().getColor( colorId ) );
        holder.orderDetails.setOnClickListener( (View view ) ->{
            AppUtils.clickAnimation(  view  );
            mListener.onOrderDetailsClick( order );
        } );
    }

    public void setColorId( int colorId ) {
        this.colorId = colorId;
    }

    public void setOnOrderDetailsListener( OnOrderDetailsListener listener ){
        this.mListener = listener;
    }

    public static class OrderDataObjectHolder extends BaseDataObjectHolder {

        public ImageView orderCompanyImg;
        public TextView orderCompanyName;
        public TextView orderPrice;
        public TextView orderDate;
        public TextView orderNo;
        public ImageView orderDetails;

        public OrderDataObjectHolder( final View itemView ) {
            super( itemView );
            orderCompanyImg = itemView.findViewById( R.id.orderEntityImgId );
            orderCompanyName = itemView.findViewById( R.id.orderCompanyNameId );
            orderCompanyName.setTypeface( AppConstants.B52 );
            orderPrice = itemView.findViewById( R.id.orderPriceId );
            orderPrice.setTypeface( AppConstants.OFFICE, Typeface.BOLD );
            orderDate = itemView.findViewById( R.id.orderDateId );
            orderDate.setTypeface( AppConstants.ROBOTO_CONDENCED, Typeface.BOLD );
            orderNo = itemView.findViewById( R.id.orderNoId );
            orderNo.setTypeface( AppConstants.ROBOTO_CONDENCED, Typeface.BOLD );
            orderDetails = itemView.findViewById( R.id.orderDetailsId );
        }

        @Override
        public void onClick( View view ) {
            return;
        }
    }

    public interface OnOrderDetailsListener {
        void onOrderDetailsClick( ClientOrderModel order );
    }
}