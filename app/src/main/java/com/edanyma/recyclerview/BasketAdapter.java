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
import com.edanyma.fragment.BasketFragment;
import com.edanyma.model.MenuEntityModel;
import com.edanyma.utils.AppUtils;
import com.edanyma.utils.GlideClient;

import java.util.ArrayList;

public class BasketAdapter extends CommonBaseAdapter< MenuEntityModel > {

    private static final String CLASS_TAG = "BasketAdapter";

    public BasketAdapter( ArrayList< MenuEntityModel > mItemList ) {
        super( mItemList );
    }


    @Override
    public BasketAdapter.BasketDataObjectHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate( R.layout.rv_item_basket, parent, false);
        BasketAdapter.BasketDataObjectHolder dataObjectHolder = new BasketAdapter.BasketDataObjectHolder( view );
        mAssetManager =  parent.getContext().getAssets();
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final BaseDataObjectHolder h, final int position) {
        BasketAdapter.BasketDataObjectHolder holder = ( BasketAdapter.BasketDataObjectHolder ) h;
        MenuEntityModel dish = mItemList.get( position );
        GlideClient.downloadImage( EdaNymaApp.getAppContext(), dish.getImageUrl(),
                holder.basketEntityImg, BasketFragment.BASKET_IMAGE_WIDTH, BasketFragment.BASKET_IMAGE_HEIGHT );
        holder.basketEntityName.setText( dish.getDisplayName() );
        holder.basketEntityPrice.setText( dish.getActualPrice().toString() );
        holder.basketEntityCount.setText( dish.getCount().toString() );
    }


    public static class BasketDataObjectHolder extends BaseDataObjectHolder {

        public ImageView basketEntityImg;
        public TextView basketEntityName;
        public TextView basketEntityPrice;
        public TextView basketEntityCount;

        public BasketDataObjectHolder( final View itemView) {
            super(itemView);

            basketEntityImg = itemView.findViewById( R.id.basketEntityImgId );
            basketEntityName = itemView.findViewById( R.id.basketEntityNameId );
            basketEntityName.setTypeface( AppConstants.B52 );
            basketEntityPrice = itemView.findViewById( R.id.basketEntityPriceId );
            basketEntityPrice.setTypeface( AppConstants.OFFICE, Typeface.BOLD );
            basketEntityCount = itemView.findViewById( R.id.basketEntityCountId );
            basketEntityCount.setTypeface( AppConstants.ROBOTO_CONDENCED,Typeface.BOLD );
            itemView.findViewById( R.id.basketTrashId ).setOnClickListener( this );
        }

        @Override
        public void onClick( View view ) {
            AppUtils.clickAnimation( view );
            super.onClick( view );
        }
    }
}
