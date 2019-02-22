package com.edanyma.recycleview;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
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
import com.edanyma.model.CompanyAction;

import java.util.ArrayList;

public class CompanyActionAdapter extends CommonBaseAdapter< CompanyAction > {

    private static final String CLASS_TAG = "CompanyActionAdapter";

    public CompanyActionAdapter( ArrayList< CompanyAction > mItemList ) {
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
    public void onBindViewHolder(final BaseDataObjectHolder holder, final int position) {
        CompanyActionDataObjectHolder categoryCardHolder = ( CompanyActionDataObjectHolder ) holder;
        categoryCardHolder.actionCompany.setText( mItemList.get( position ).getCompanyName() );
        categoryCardHolder.actionImage.setImageResource( mItemList.get( position ).getDrawableId() );
    }

    @Override
    public void onViewRecycled( @NonNull final BaseDataObjectHolder holder ) {
        super.onViewRecycled( holder );
        Log.i( CLASS_TAG, "Adapter Position: "+holder.getAdapterPosition() );
    }

    public static class CompanyActionDataObjectHolder extends BaseDataObjectHolder {

        private CardView actionCard;
        public ImageView actionImage;
        public TextView actionCompany;

        public CompanyActionDataObjectHolder( View itemView) {
            super(itemView);
            actionCard = itemView.findViewById( R.id.rvItemActionId );
            actionImage = itemView.findViewById( R.id.actionImageId );
            actionCompany = itemView.findViewById( R.id.actionCompanyId );
            actionCompany.setTypeface( AppConstants.B52 );
            actionCard.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick( View view ) {
                    actionCard.startAnimation( AnimationUtils.loadAnimation( EdaNymaApp.getAppContext(),R.anim.bounce) );
                }
            } );
        }
    }
}
