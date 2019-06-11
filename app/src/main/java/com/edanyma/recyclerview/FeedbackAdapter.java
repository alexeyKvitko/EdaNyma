package com.edanyma.recyclerview;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edanyma.AppConstants;
import com.edanyma.EdaNymaApp;
import com.edanyma.R;
import com.edanyma.model.FeedbackModel;
import com.edanyma.utils.AppUtils;
import com.edanyma.utils.ConvertUtils;
import com.edanyma.utils.GlideClient;

import java.util.Date;
import java.util.LinkedList;

public class FeedbackAdapter extends CommonBaseAdapter< FeedbackModel > {

    public static final int IMAGE_SIZE = ( int ) ConvertUtils.convertDpToPixel( 64 );

    private static final String CLASS_TAG = "FeedbackAdapter";

    public FeedbackAdapter( LinkedList< FeedbackModel > mItemList ) {
        super( mItemList );
    }


    @Override
    public FeedbackAdapter.FeedbackDataObjectHolder onCreateViewHolder( ViewGroup parent, int viewType ) {
        View view = LayoutInflater.from( parent.getContext() )
                .inflate( R.layout.rv_item_feedback, parent, false );
        FeedbackAdapter.FeedbackDataObjectHolder dataObjectHolder = new FeedbackAdapter.FeedbackDataObjectHolder( view );
        mAssetManager = parent.getContext().getAssets();
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder( final BaseDataObjectHolder h, final int position ) {
        FeedbackAdapter.FeedbackDataObjectHolder holder = ( FeedbackAdapter.FeedbackDataObjectHolder ) h;
        FeedbackModel feedback = mItemList.get( position );
        int avatarLayoutVisibility = View.GONE;
        if ( AppUtils.nullOrEmpty( feedback.getPersonAvatar() ) ) {
            avatarLayoutVisibility = View.VISIBLE;
            int blueIndex = feedback.getPerson().hashCode() % 256;
            int color = Color.argb( 180, 83, 91, blueIndex );
            holder.feedbackAvatarLayout.setBackgroundColor( color );
            holder.feedbackFirstLetter.setText( feedback.getPerson().substring( 0, 1 ).toUpperCase() );
        } else {
            String url = feedback.getPersonAvatar() + "?time=" + ( new Date() ).getTime();
            GlideClient.downloadImage( EdaNymaApp.getAppContext(), url, holder.feedbackAvatar );
        }
        holder.feedbackStars.setImageDrawable( EdaNymaApp.getAppContext().getResources().getDrawable(
                AppConstants.STAR_ARRAY[ feedback.getRate() ]) );
        holder.feedbackAvatarLayout.setVisibility( avatarLayoutVisibility );
        holder.feedbackAuth.setText( feedback.getPerson() );
        holder.feedbackDateTime.setText( feedback.getFeedbackDate()+" в "+feedback.getFeedbackTime() );
        holder.feedbackBody.setText( "  "+feedback.getComment() );
    }


    public static class FeedbackDataObjectHolder extends BaseDataObjectHolder {

        public ImageView feedbackAvatar;
        public ImageView feedbackStars;
        public LinearLayout feedbackAvatarLayout;
        public TextView feedbackFirstLetter;
        public TextView feedbackAuth;
        public TextView feedbackDateTime;
        public TextView feedbackBody;

        public FeedbackDataObjectHolder( final View itemView ) {
            super( itemView );
            feedbackAvatar = itemView.findViewById( R.id.feedbackImageAvatarId );
            feedbackStars = itemView.findViewById( R.id.rvFeedbackCompanyRateId );
            feedbackAvatarLayout = itemView.findViewById( R.id.feedbackLayoutAvatarId );
            feedbackFirstLetter = itemView.findViewById( R.id.feedbackFirstLetterId );
            feedbackFirstLetter.setTypeface( AppConstants.ROBOTO_BLACK );

            feedbackAuth = itemView.findViewById( R.id.feedbackAuthId );
            feedbackAuth.setTypeface( AppConstants.OFFICE, Typeface.BOLD );

            feedbackDateTime = itemView.findViewById( R.id.feedbackDateTimeId );
            feedbackDateTime.setTypeface( AppConstants.ROBOTO_CONDENCED, Typeface.BOLD );

            feedbackBody = itemView.findViewById( R.id.feedbackBodyId );
            feedbackBody.setTypeface( AppConstants.ROBOTO_CONDENCED );
        }

        @Override
        public void onClick( View view ) {
            return;
        }
    }
}
