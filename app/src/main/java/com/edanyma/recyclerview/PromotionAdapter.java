package com.edanyma.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.edanyma.AppConstants;
import com.edanyma.EdaNymaApp;
import com.edanyma.R;
import com.edanyma.model.CompanyActionModel;
import com.edanyma.utils.ConvertUtils;
import com.edanyma.utils.GlideClient;

import java.util.ArrayList;

public class PromotionAdapter extends CommonBaseAdapter< CompanyActionModel > {

    public static final int IMAGE_WIDTH = ( int ) ConvertUtils.convertDpToPixel( 312 );
    public static final int IMAGE_HEIGH = ( int ) ConvertUtils.convertDpToPixel( 192 );

    private static final String CLASS_TAG = "PromotionAdapter";

    public PromotionAdapter( ArrayList< CompanyActionModel > mItemList ) {
        super( mItemList );
    }


    @Override
    public PromotionAdapter.PromotionDataObjectHolder onCreateViewHolder( ViewGroup parent, int viewType ) {
        View view = LayoutInflater.from( parent.getContext() )
                .inflate( R.layout.rv_item_promotion, parent, false );
        PromotionAdapter.PromotionDataObjectHolder dataObjectHolder = new PromotionAdapter.PromotionDataObjectHolder( view );
        mAssetManager = parent.getContext().getAssets();
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder( final BaseDataObjectHolder h, final int position ) {
        PromotionAdapter.PromotionDataObjectHolder holder = ( PromotionAdapter.PromotionDataObjectHolder ) h;
        CompanyActionModel promotion = mItemList.get( position );
        GlideClient.downloadImage( EdaNymaApp.getAppContext(), promotion.getActionImgUrl(), holder.promotionCard, IMAGE_WIDTH, IMAGE_HEIGH );
        holder.promotionCompnay.setText( promotion.getCompanyName() );
    }


    public static class PromotionDataObjectHolder extends BaseDataObjectHolder {

        public ImageView promotionCard;
        public TextView promotionCompnay;

        public PromotionDataObjectHolder( final View itemView ) {
            super( itemView );
            promotionCard = itemView.findViewById( R.id.promotionCardId );
            promotionCompnay = itemView.findViewById( R.id.promotionNameId );
            promotionCompnay.setTypeface( AppConstants.ROBOTO_BLACK );
        }

        @Override
        public void onClick( View view ) {
                return;
        }
    }
}
