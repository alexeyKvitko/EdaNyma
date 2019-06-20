package com.edanyma.recyclerview;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edanyma.AppConstants;
import com.edanyma.EdaNymaApp;
import com.edanyma.R;
import com.edanyma.model.ClientOrderModel;
import com.edanyma.model.CompanyLight;
import com.edanyma.utils.AppUtils;
import com.edanyma.utils.GlideClient;

import java.util.ArrayList;

public class CompanyAdapter extends CommonBaseAdapter< CompanyLight > {

    private static final String CLASS_TAG = "CompanyAdapter";

    private OnCompanyFeedbackListener mListener;

    public CompanyAdapter( ArrayList< CompanyLight > mItemList ) {
        super( mItemList );
    }

    @Override
    public CompanyAdapter.CompanyDataObjectHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate( R.layout.rv_item_company, parent, false);
        CompanyAdapter.CompanyDataObjectHolder dataObjectHolder = new CompanyAdapter.CompanyDataObjectHolder( view );

        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final BaseDataObjectHolder h, final int position) {
        CompanyAdapter.CompanyDataObjectHolder holder = ( CompanyAdapter.CompanyDataObjectHolder ) h;
        CompanyLight company = mItemList.get( position );
        GlideClient.downloadImage( EdaNymaApp.getAppContext(),
                company.getThumbUrl(),holder.companyImg );
        int favVisibility = company.isFavorite() ? View.VISIBLE : View.GONE;
        holder.companyFavoriteImg.setVisibility( favVisibility );
        holder.сompanyTitle.setText( company.getDisplayName() );
        holder.сompanyDeliCash.setText( company.getDelivery() );
        holder.сompanyStar.setText( company.getCommentCount() );
        holder.сompanyDeliTime.setText( company.getDeliveryTimeMin() );
        holder.сompanyWork.setText( company.getDayoffWork() );
        holder.сompanyWorkWeekend.setText( company.getWeekdayWork() );
        holder.companyFeedbackImg.setImageDrawable( EdaNymaApp.getAppContext()
                .getResources().getDrawable( AppConstants.STAR_ARRAY[ company.getFeedbackRate() ] ) );
        holder.feedbackLayout.setOnClickListener( ( View view ) -> {
            AppUtils.clickAnimation( view );
            if( mListener != null ){
                mListener.onCompanyFeedbackClick(  company.getId() );
            }
        } );
    }

    public void setFeedbackClickListener( OnCompanyFeedbackListener listener ){
        this.mListener = listener;
    }


    public static class CompanyDataObjectHolder extends BaseDataObjectHolder {

        private FrameLayout companyCard;
        private LinearLayout feedbackLayout;
        public ImageView companyImg;
        public ImageView companyFavoriteImg;
        public ImageView companyFeedbackImg;
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
            companyFavoriteImg = itemView.findViewById( R.id.companyFavoriteId );
            companyFeedbackImg= itemView.findViewById( R.id.cardFeedbackCompanyRateId );

            сompanyTitle = itemView.findViewById( R.id.cardCompanyTitleId );
            сompanyDeliCash = itemView.findViewById( R.id.cardCompanyDeliCashId );
            сompanyStar = itemView.findViewById( R.id.cardCompanyStarId );
            сompanyDeliTime = itemView.findViewById( R.id.cardCompanyDeliTimeId );
            сompanyWork = itemView.findViewById( R.id.cardCompanyWorkId );
            сompanyWorkWeekend = itemView.findViewById( R.id.cardCompanyWorkWeekId );
            feedbackLayout = itemView.findViewById( R.id.cardTotalFeedbackId );

            сompanyTitle.setTypeface( AppConstants.B52 );
            сompanyDeliCash.setTypeface( AppConstants.ROBOTO_CONDENCED );
            сompanyStar.setTypeface( AppConstants.ROBOTO_CONDENCED );
            сompanyDeliTime.setTypeface( AppConstants.ROBOTO_CONDENCED, Typeface.BOLD );
            сompanyWork.setTypeface( AppConstants.ROBOTO_CONDENCED, Typeface.BOLD );
            сompanyWorkWeekend.setTypeface( AppConstants.ROBOTO_CONDENCED, Typeface.BOLD );


            companyCard.setOnClickListener( (View view) -> {
                    itemView.findViewById( R.id.cardCompanyImgId ).startAnimation(
                            AnimationUtils.loadAnimation( EdaNymaApp.getAppContext(),R.anim.bounce) );
                    CompanyDataObjectHolder.super.onClick( view );
            } );
        }
    }

    public interface OnCompanyFeedbackListener {
        void onCompanyFeedbackClick( String companyId );
    }
}
