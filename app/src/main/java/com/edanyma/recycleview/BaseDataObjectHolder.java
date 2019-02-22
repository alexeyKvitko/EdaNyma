package com.edanyma.recycleview;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public class BaseDataObjectHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener  {

    private static CommonBaseAdapter.CardClickListener mCardClickListener;


    public BaseDataObjectHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if( mCardClickListener != null ){
            mCardClickListener.onItemClick(getAdapterPosition(), v );
        }
    }

    public static void setCardClickListener( CommonBaseAdapter.CardClickListener mCardClickListener ) {
        BaseDataObjectHolder.mCardClickListener = mCardClickListener;
    }


}
