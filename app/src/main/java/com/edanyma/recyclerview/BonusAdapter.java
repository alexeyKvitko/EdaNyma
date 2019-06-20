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
import com.edanyma.model.BonusModel;
import com.edanyma.rest.RestController;
import com.edanyma.utils.AppUtils;
import com.edanyma.utils.ConvertUtils;
import com.edanyma.utils.GlideClient;

import java.util.ArrayList;

public class BonusAdapter extends CommonBaseAdapter< BonusModel > {

    public static final int IMAGE_SIZE = ( int ) ConvertUtils.convertDpToPixel( 64 );

    private static final String CLASS_TAG = "BonusAdapter";

    public BonusAdapter( ArrayList< BonusModel > mItemList ) {
        super( mItemList );
    }


    @Override
    public BonusAdapter.BonusDataObjectHolder onCreateViewHolder( ViewGroup parent, int viewType ) {
        View view = LayoutInflater.from( parent.getContext() )
                .inflate( R.layout.rv_item_bonus, parent, false );
        BonusAdapter.BonusDataObjectHolder dataObjectHolder = new BonusAdapter.BonusDataObjectHolder( view );

        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder( final BaseDataObjectHolder h, final int position ) {
        BonusAdapter.BonusDataObjectHolder holder = ( BonusAdapter.BonusDataObjectHolder ) h;
        BonusModel bonus = mItemList.get( position );
        GlideClient.downloadImage( EdaNymaApp.getAppContext(), RestController.COMPANIES_LOGO_URL+bonus.getCompanyLogo(),
                holder.bonusCompanyImg, IMAGE_SIZE, IMAGE_SIZE );
        holder.bonusCompanyName.setText( bonus.getCompanyName() );
        holder.bonusValue.setText( bonus.getBonusValue() );
        holder.bonusName.setText( bonus.getBonusType() );
    }


    public static class BonusDataObjectHolder extends BaseDataObjectHolder {

        public ImageView bonusCompanyImg;
        public TextView bonusCompanyName;
        public TextView bonusValue;
        public TextView bonusName;

        public BonusDataObjectHolder( final View itemView ) {
            super( itemView );
            bonusCompanyImg = itemView.findViewById( R.id.bonusCompanyImgId );
            bonusCompanyName = itemView.findViewById( R.id.bonusCompanyNameId );
            bonusCompanyName.setTypeface( AppConstants.B52 );
            bonusValue = itemView.findViewById( R.id.bonusValueId );
            bonusValue.setTypeface( AppConstants.OFFICE, Typeface.BOLD );
            bonusName = itemView.findViewById( R.id.bonusNameId );
            bonusName.setTypeface( AppConstants.ROBOTO_CONDENCED, Typeface.BOLD );
        }

        @Override
        public void onClick( View view ) {
            AppUtils.clickAnimation( view );
            super.onClick( view );
        }
    }
}
