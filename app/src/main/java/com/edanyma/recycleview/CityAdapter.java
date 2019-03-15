package com.edanyma.recycleview;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.edanyma.AppConstants;
import com.edanyma.EdaNymaApp;
import com.edanyma.R;
import com.edanyma.model.TripleModel;
import com.edanyma.owncomponent.MainCardItem;
import com.edanyma.utils.PicassoClient;

import java.util.ArrayList;

public class CityAdapter extends CommonBaseAdapter< TripleModel > {

    private static final String CLASS_TAG = "CityAdapter";

    public CityAdapter( ArrayList< TripleModel > mItemList ) {
        super( mItemList );
    }

    @Override
    public TripleModelDataObjectHolder onCreateViewHolder( ViewGroup parent, int viewType ) {
        View view = LayoutInflater.from( parent.getContext() )
                .inflate( R.layout.rv_item_city, parent, false );
        TripleModelDataObjectHolder dataObjectHolder = new TripleModelDataObjectHolder( view );
        mAssetManager = parent.getContext().getAssets();
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder( final BaseDataObjectHolder holder, final int position ) {
        TripleModelDataObjectHolder tripleHolder = ( TripleModelDataObjectHolder ) holder;

        tripleHolder.leftTitle.setText( mItemList.get( position ).getLeftItem().getDisplayName() );
        if( mItemList.get( position ).getLeftItem().getDisplayName().length() > 12  ){
            tripleHolder.leftTitle.setTextSize( TypedValue.COMPLEX_UNIT_DIP, 10 );
        }
        tripleHolder.centerTitle.setText( mItemList.get( position ).getCenterItem().getDisplayName() );
        if( mItemList.get( position ).getCenterItem().getDisplayName().length() > 12  ){
            tripleHolder.centerTitle.setTextSize( TypedValue.COMPLEX_UNIT_DIP, 10 );
        }
        tripleHolder.rightTitle.setText( mItemList.get( position ).getRightItem().getDisplayName() );
        if( mItemList.get( position ).getRightItem().getDisplayName().length() > 12  ){
            tripleHolder.rightTitle.setTextSize( TypedValue.COMPLEX_UNIT_DIP, 10 );
        }

        PicassoClient.downloadImage( EdaNymaApp.getAppContext(),
                mItemList.get( position ).getLeftItem().getUrl(), tripleHolder.leftImage);
        PicassoClient.downloadImage( EdaNymaApp.getAppContext(),
                mItemList.get( position ).getCenterItem().getUrl(), tripleHolder.centerImage);
        PicassoClient.downloadImage( EdaNymaApp.getAppContext(),
                mItemList.get( position ).getRightItem().getUrl(), tripleHolder.rightImage);

    }


    public static class TripleModelDataObjectHolder extends BaseDataObjectHolder {

        public ImageView leftImage;
        public TextView leftTitle;

        public ImageView centerImage;
        public TextView centerTitle;

        public ImageView rightImage;
        public TextView rightTitle;


        public TripleModelDataObjectHolder( View itemView ) {
            super( itemView );

            leftImage = itemView.findViewById( R.id.leftCityImgId );
            leftTitle = itemView.findViewById( R.id.leftCityTextId );

            centerImage = itemView.findViewById( R.id.centerCityImgId );
            centerTitle = itemView.findViewById( R.id.centerCityTextId );

            rightImage = itemView.findViewById( R.id.rightCityImgId );
            rightTitle = itemView.findViewById( R.id.rightCityTextId );

            leftTitle.setTypeface( AppConstants.ROBOTO_CONDENCED );
            centerTitle.setTypeface( AppConstants.ROBOTO_CONDENCED );
            rightTitle.setTypeface( AppConstants.ROBOTO_CONDENCED );
        }
    }
}
