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

public class CommonBaseAdapter<T> extends  RecyclerView.Adapter< BaseDataObjectHolder >{

    private T t;
    protected ArrayList<T> mItemList;
    protected AssetManager mAssetManager;
    protected LruCache<Integer,Bitmap> mImages;
    protected LruCache<Integer, Drawable > mIcons;
    protected static CardClickListener mCardClickListener;

    public CommonBaseAdapter(ArrayList<T> mItemList) {
        this.mItemList = mItemList;
        mAssetManager = EdaNymaApp.getAppContext().getAssets();
        mImages = new LruCache<>(10);
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

//    public void clearImages() {
//        mImages = null;
//        mIcons = null;
//    }

    public void deleteItem(int index) {
        mItemList.remove(index);
        notifyItemRemoved(index);
    }

    public void deleteAllItem(){
        int size = getItemCount()-1;
        mItemList.clear();
//        mImages.evictAll();
//        mIcons.evictAll();
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

    public LruCache<Integer, Bitmap> getImages() {
        return mImages;
    }

    public LruCache<Integer, Drawable> getIcons() {
        return mIcons;
    }

}
