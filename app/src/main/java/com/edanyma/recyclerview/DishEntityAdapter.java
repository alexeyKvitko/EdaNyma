package com.edanyma.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edanyma.R;
import com.edanyma.model.MenuEntityModel;
import com.edanyma.owncomponent.DishEntityCard;

import java.util.ArrayList;

public class DishEntityAdapter extends CommonBaseAdapter< MenuEntityModel > {

    private static final String CLASS_TAG = "DishEntityAdapter";

    public DishEntityAdapter( ArrayList< MenuEntityModel > mItemList ) {
        super( mItemList );
    }

    @Override
    public DishEntityAdapter.DishEntityObjectHolder onCreateViewHolder( ViewGroup parent, int viewType ) {
        View view = LayoutInflater.from( parent.getContext() )
                .inflate( R.layout.rv_item_dish_entity, parent, false );
        DishEntityAdapter.DishEntityObjectHolder dataObjectHolder = new DishEntityAdapter.DishEntityObjectHolder( view );
        mAssetManager = parent.getContext().getAssets();
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder( final BaseDataObjectHolder h, final int position ) {
        DishEntityAdapter.DishEntityObjectHolder holder = ( DishEntityAdapter.DishEntityObjectHolder ) h;
        holder.dishEntityCard.setEntityImage( mItemList.get( position ).getImageUrl() );
        holder.dishEntityCard.setEntityTitle( mItemList.get( position ).getDisplayName() );
        holder.dishEntityCard.setEntityDesc( mItemList.get( position ).getDescription() );
        holder.dishEntityCard.setEntityPrice( mItemList.get( position ).getPriceOne() );
        holder.dishEntityCard.setEntitySize( mItemList.get( position ).getSizeOne() != null ?
                mItemList.get( position ).getSizeOne() : mItemList.get( position ).getWeightOne() );
    }


    public static class DishEntityObjectHolder extends BaseDataObjectHolder {

        public DishEntityCard dishEntityCard;

        public DishEntityObjectHolder( final View itemView ) {
            super( itemView );
            dishEntityCard = itemView.findViewById( R.id.dishEntityCardId );
        }
    }
}
