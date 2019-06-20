package com.edanyma.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edanyma.AppConstants;
import com.edanyma.EdaNymaApp;
import com.edanyma.R;
import com.edanyma.model.CompanyActionModel;
import com.edanyma.utils.ConvertUtils;
import com.edanyma.utils.GlideClient;

import java.util.ArrayList;

import static com.edanyma.manager.GlobalManager.getDisplayRatio;

public class PromotionAdapter extends CommonBaseAdapter< CompanyActionModel > {

    public static final int IMAGE_WIDTH = ( int ) ConvertUtils.convertDpToPixel( 312 );
    public static final int IMAGE_HEIGH = ( int ) ConvertUtils.convertDpToPixel( 192 );

    public OnPromotionClickListener mListener;

    private static final String CLASS_TAG = "PromotionAdapter";

    public PromotionAdapter( ArrayList< CompanyActionModel > mItemList ) {
        super( mItemList );
    }

    public void setOnPromotionClickListener( OnPromotionClickListener listener ) {
        this.mListener = listener;
    }

    @Override
    public PromotionAdapter.PromotionDataObjectHolder onCreateViewHolder( ViewGroup parent, int viewType ) {
        View view = LayoutInflater.from( parent.getContext() )
                .inflate( R.layout.rv_item_promotion, parent, false );
        PromotionAdapter.PromotionDataObjectHolder dataObjectHolder = new PromotionAdapter.PromotionDataObjectHolder( view );
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder( final BaseDataObjectHolder h, final int position ) {
        PromotionAdapter.PromotionDataObjectHolder holder = ( PromotionAdapter.PromotionDataObjectHolder ) h;
        CompanyActionModel promotion = mItemList.get( position );
        int imageWidth = ( int ) ( IMAGE_WIDTH * getDisplayRatio() );
        GlideClient.downloadImage( EdaNymaApp.getAppContext(), promotion.getActionImgUrl(), holder.promotionCard , imageWidth, IMAGE_HEIGH );
        holder.promotionCard.measure( 0,0 );
        holder.promotionCompany.setText( promotion.getCompanyName() );
        holder.promotionContainer.setOnClickListener( ( View view ) -> {
            if ( mListener != null ) {
                mListener.onPromotionClick( promotion.getCompanyId() );
            }
        } );
    }


    public static class PromotionDataObjectHolder extends BaseDataObjectHolder {

        public LinearLayout promotionContainer;
        public ImageView promotionCard;
        public TextView promotionCompany;

        public PromotionDataObjectHolder( final View itemView ) {
            super( itemView );
            promotionContainer = itemView.findViewById( R.id.promotionContainerId );
            promotionCard = itemView.findViewById( R.id.promotionCardId );
            promotionCompany = itemView.findViewById( R.id.promotionNameId );
            promotionCompany.setTypeface( AppConstants.ROBOTO_BLACK );
        }

        @Override
        public void onClick( View view ) {
            return;
        }
    }

    public interface OnPromotionClickListener {
        void onPromotionClick( Integer companyId );
    }
}
