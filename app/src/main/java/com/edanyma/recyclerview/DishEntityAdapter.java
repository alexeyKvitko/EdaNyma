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
        holder.dishEntityCard.setDishEntity( mItemList.get( position ) );
        holder.dishEntityCard.getEntityImage().setColorFilter( null );
    }


    public static class DishEntityObjectHolder extends BaseDataObjectHolder {

        public DishEntityCard dishEntityCard;

        public DishEntityObjectHolder( final View itemView ) {
            super( itemView );
            dishEntityCard = itemView.findViewById( R.id.dishEntityCardId );
            dishEntityCard.setOnClickListener( this );
        }
    }
}
