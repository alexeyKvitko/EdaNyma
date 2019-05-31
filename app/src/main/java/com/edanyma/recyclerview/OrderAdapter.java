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

    public OrderAdapter( ArrayList< ClientOrderModel > mItemList ) {
        super( mItemList );
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
        ClientOrderModel order = mItemList.get( position );
        GlideClient.downloadImage( EdaNymaApp.getAppContext(), RestController.COMPANIES_LOGO_URL + order.getCompanyLogo(),
                holder.orderCompanyImg, IMAGE_SIZE, IMAGE_SIZE );
        holder.orderCompanyName.setText( order.getCompanyOneName() );
        holder.orderPrice.setText( order.getOrderPrice().toString() );
        holder.orderDate.setText( order.getOrderDate()+", "+order.getOrderTime() );
    }


    public static class OrderDataObjectHolder extends BaseDataObjectHolder {

        public ImageView orderCompanyImg;
        public TextView orderCompanyName;
        public TextView orderPrice;
        public TextView orderDate;

        public OrderDataObjectHolder( final View itemView ) {
            super( itemView );
            orderCompanyImg = itemView.findViewById( R.id.orderEntityImgId );
            orderCompanyName = itemView.findViewById( R.id.orderCompanyNameId );
            orderCompanyName.setTypeface( AppConstants.B52 );
            orderPrice = itemView.findViewById( R.id.orderPriceId );
            orderPrice.setTypeface( AppConstants.OFFICE, Typeface.BOLD );
            orderDate = itemView.findViewById( R.id.orderDateId );
            orderDate.setTypeface( AppConstants.ROBOTO_CONDENCED, Typeface.BOLD );
            ((TextView )itemView.findViewById( R.id.orderDateId ))
                                                .setTypeface( AppConstants.ROBOTO_CONDENCED );
        }

        @Override
        public void onClick( View view ) {
            AppUtils.clickAnimation( view );
            super.onClick( view );
        }
    }
}