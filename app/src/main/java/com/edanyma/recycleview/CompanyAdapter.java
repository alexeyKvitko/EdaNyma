package com.edanyma.recycleview;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.edanyma.AppConstants;
import com.edanyma.EdaNymaApp;
import com.edanyma.R;
import com.edanyma.model.CompanyLight;
import com.edanyma.utils.PicassoClient;

import java.util.ArrayList;

public class CompanyAdapter extends CommonBaseAdapter< CompanyLight > {

    private static final String CLASS_TAG = "CompanyAdapter";

    public CompanyAdapter( ArrayList< CompanyLight > mItemList ) {
        super( mItemList );
    }

    @Override
    public CompanyAdapter.CompanyDataObjectHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate( R.layout.rv_item_company, parent, false);
        CompanyAdapter.CompanyDataObjectHolder dataObjectHolder = new CompanyAdapter.CompanyDataObjectHolder( view );
        mAssetManager =  parent.getContext().getAssets();
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final BaseDataObjectHolder h, final int position) {
        CompanyAdapter.CompanyDataObjectHolder holder = ( CompanyAdapter.CompanyDataObjectHolder ) h;
        PicassoClient.downloadImage( EdaNymaApp.getAppContext(),
                mItemList.get( position ).getThumbUrl(),holder.companyImg );

        holder.сompanyTitle.setText( mItemList.get( position ).getDisplayName() );
        holder.сompanyDeliCash.setText( mItemList.get( position ).getDelivery() );
        holder.сompanyStar.setText( mItemList.get( position ).getCommentCount() );
        holder.сompanyDeliTime.setText( mItemList.get( position ).getDeliveryTimeMin() );
        holder.сompanyWork.setText( mItemList.get( position ).getDayoffWork() );
        holder.сompanyWorkWeekend.setText( mItemList.get( position ).getWeekdayWork() );
    }


    public static class CompanyDataObjectHolder extends BaseDataObjectHolder {

        private FrameLayout companyCard;
        public ImageView companyImg;
        public TextView сompanyTitle;
        public TextView сompanyDeliCash;
        public TextView сompanyStar;
        public TextView сompanyDeliTime;
        public TextView сompanyWork;
        public TextView сompanyWorkWeekend;

        public CompanyDataObjectHolder( final View itemView) {
            super(itemView);
            companyCard = itemView.findViewById( R.id.companyCardId );
            companyImg = itemView.findViewById( R.id.companyImgId );

            сompanyTitle = itemView.findViewById( R.id.cardCompanyTitleId );
            сompanyDeliCash = itemView.findViewById( R.id.cardCompanyDeliCashId );
            сompanyStar = itemView.findViewById( R.id.cardCompanyStarId );
            сompanyDeliTime = itemView.findViewById( R.id.cardCompanyDeliTimeId );
            сompanyWork = itemView.findViewById( R.id.cardCompanyWorkId );
            сompanyWorkWeekend = itemView.findViewById( R.id.cardCompanyWorkWeekId );

            сompanyTitle.setTypeface( AppConstants.B52 );
            сompanyDeliCash.setTypeface( AppConstants.ROBOTO_CONDENCED );
            сompanyStar.setTypeface( AppConstants.ROBOTO_CONDENCED );
            сompanyDeliTime.setTypeface( AppConstants.ROBOTO_CONDENCED, Typeface.BOLD );
            сompanyWork.setTypeface( AppConstants.ROBOTO_CONDENCED, Typeface.BOLD );
            сompanyWorkWeekend.setTypeface( AppConstants.ROBOTO_CONDENCED, Typeface.BOLD );

            companyCard.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick( View view ) {
                    itemView.findViewById( R.id.cardCompanyImgId ).startAnimation(
                            AnimationUtils.loadAnimation( EdaNymaApp.getAppContext(),R.anim.bounce) );
                    CompanyDataObjectHolder.super.onClick( view );
                }
            } );
        }
    }
}
