package com.edanyma.recyclerview;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import androidx.collection.LruCache;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.edanyma.EdaNymaApp;

import java.util.ArrayList;
import java.util.List;

public class CommonBaseAdapter<T> extends  RecyclerView.Adapter< BaseDataObjectHolder >{

    private T t;
    protected List<T> mItemList;
    protected static CardClickListener mCardClickListener;

    public CommonBaseAdapter( List<T> mItemList) {
        this.mItemList = mItemList;
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

    @Override
    public BaseDataObjectHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(BaseDataObjectHolder holder, int position) {
    }

    public void addItem(T t, int index) {
        mItemList.add(index, t);
        notifyItemInserted(index);
    }

    public T getItem( int position ){
        return mItemList.get( position );
    }

    public void deleteItem(int index) {
        mItemList.remove(index);
        notifyItemRemoved(index);
    }

    public void deleteAllItem(){
        int size = getItemCount()-1;
        mItemList.clear();
        notifyItemRangeRemoved( 0,size );
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    public void setOnItemClickListener( CardClickListener clickListener) {
        mCardClickListener = clickListener;
        BaseDataObjectHolder.setCardClickListener( mCardClickListener );
    }

    public interface CardClickListener {
        void onItemClick(int position, View v);
//        void onAdditionalItemClick(int position, View v);
    }


}
