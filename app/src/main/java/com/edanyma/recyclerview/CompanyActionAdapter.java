package com.edanyma.recyclerview;

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
import com.edanyma.model.CompanyActionModel;
import com.edanyma.utils.PicassoClient;

import java.util.ArrayList;

public class CompanyActionAdapter extends CommonBaseAdapter< CompanyActionModel > {

    private static final String CLASS_TAG = "CompanyActionAdapter";

    public CompanyActionAdapter( ArrayList< CompanyActionModel > mItemList ) {
        super( mItemList );
    }

    @Override
    public CompanyActionDataObjectHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate( R.layout.rv_item_action, parent, false);
        CompanyActionDataObjectHolder dataObjectHolder = new CompanyActionDataObjectHolder( view );
        mAssetManager =  parent.getContext().getAssets();
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final BaseDataObjectHolder h, final int position) {
        CompanyActionDataObjectHolder holder = ( CompanyActionDataObjectHolder ) h;
        holder.actionCompany.setText( mItemList.get( position ).getCompanyName() );
        PicassoClient.downloadImage( EdaNymaApp.getAppContext(),
                mItemList.get( position ).getActionImgUrl(),holder.actionImage);
    }


    public static class CompanyActionDataObjectHolder extends BaseDataObjectHolder {

        private FrameLayout actionCard;
        public ImageView actionImage;
        public TextView actionCompany;

        public CompanyActionDataObjectHolder( View itemView) {
            super(itemView);
            actionCard = itemView.findViewById( R.id.rvItemActionId );
            actionImage = itemView.findViewById( R.id.actionImageId );
            actionCompany = itemView.findViewById( R.id.actionCompanyId );
            actionCompany.setTypeface( AppConstants.B52 );
            actionCard.setOnClickListener( (View view) -> {
                    actionCard.startAnimation( AnimationUtils.loadAnimation( EdaNymaApp.getAppContext(),R.anim.bounce) );
            } );
        }
    }
}
