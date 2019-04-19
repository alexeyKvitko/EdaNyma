package com.edanyma.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.edanyma.R;
import com.edanyma.model.HomeMenuModel;
import com.edanyma.owncomponent.MainCardItem;
import com.edanyma.utils.AppUtils;

import java.util.ArrayList;

public class HomeMenuAdapter extends CommonBaseAdapter< HomeMenuModel > {

    private static final String CLASS_TAG = "HomeMenuAdapter";


    public HomeMenuAdapter(ArrayList< HomeMenuModel > mItemList ) {
        super( mItemList );
    }

    @Override
    public HomeMenuDataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate( R.layout.rv_item_home_menu, parent, false);
        HomeMenuDataObjectHolder dataObjectHolder = new HomeMenuDataObjectHolder( view );
        mAssetManager =  parent.getContext().getAssets();
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final BaseDataObjectHolder holder, final int position) {
        HomeMenuDataObjectHolder homeMenuCardHolder = (HomeMenuDataObjectHolder) holder;

        homeMenuCardHolder.leftMenuImage.setImageDrawable( mItemList.get( position).getLeftMenuImg());
        homeMenuCardHolder.leftMenuTitle.setText( mItemList.get( position).getLeftMenuName() );
        homeMenuCardHolder.leftMenuCount.setText( AppUtils.declension( mItemList.get( position).getLeftMenuCount() ) );


        homeMenuCardHolder.rightMenuImage.setImageDrawable( mItemList.get( position).getRightMenuImg());
        homeMenuCardHolder.rightMenuTitle.setText( mItemList.get( position).getRightMenuName() );
        homeMenuCardHolder.rightMenuCount.setText( AppUtils.declension( mItemList.get( position).getRightMenuCount() ) );
    }


    public static class HomeMenuDataObjectHolder extends BaseDataObjectHolder {

        public ImageView leftMenuImage;
        public TextView leftMenuTitle;
        public TextView leftMenuCount;

        public ImageView rightMenuImage;
        public TextView rightMenuTitle;
        public TextView rightMenuCount;

        public HomeMenuDataObjectHolder(View itemView) {
            super(itemView);
            MainCardItem leftCardItem = itemView.findViewById( R.id.leftCardId );
            leftMenuImage = leftCardItem.findViewById( R.id.dishImgId );
            leftMenuTitle = leftCardItem.findViewById( R.id.dishTitleTextId );
            leftMenuCount = leftCardItem.findViewById( R.id.dishCountTextId );
            leftCardItem.setOnClickListener( this );
            
            MainCardItem rightCardItem = itemView.findViewById( R.id.rightCardId );
            rightMenuImage = rightCardItem.findViewById( R.id.dishImgId );
            rightMenuTitle = rightCardItem.findViewById( R.id.dishTitleTextId );
            rightMenuCount = rightCardItem.findViewById( R.id.dishCountTextId );
            rightCardItem.setOnClickListener( this );
        }

        @Override
        public void onClick( View view ) {
            AppUtils.bounceAnimation( view.findViewById( R.id.cardDishImgId ) );
            super.onClick( view );
        }
    }
}
