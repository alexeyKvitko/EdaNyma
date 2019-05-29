package com.edanyma.recyclerview.manager;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.edanyma.recyclerview.StickyRecyclerView;

public class StickyLayoutManager extends LinearLayoutManager {

    private final String TAG = "StickyLayoutManager";

    private RecyclerView mParent;
    private int mMinHeight;
    private int mScroll;


    public StickyLayoutManager( Context context, RecyclerView parent, int mMinHeight ) {
        super( context, VERTICAL, false );
        this.mParent = parent;
        this.mMinHeight = mMinHeight;
    }


    @Override
    public int scrollVerticallyBy( int dy, RecyclerView.Recycler recycler, RecyclerView.State state ) {
        if ( getItemCount() == 0 || dy == 0 || ( ( StickyRecyclerView ) mParent ).isAnimateHeader() ) {
            return 0;
        }
        if ( dy > 0 && mScroll == 0 ) {
            ( ( StickyRecyclerView ) mParent ).removeHeaderAction();
            return 0;
        }
        if ( dy < 0 && mScroll < mMinHeight ) {
            ( ( StickyRecyclerView ) mParent ).restoreHeaderAction();
            mScroll = 0;
            return super.scrollVerticallyBy( dy, recycler, state );
        }
        mScroll += dy;
        return super.scrollVerticallyBy( dy, recycler, state );
    }


    public int getScroll() {
        return mScroll;
    }

    public void setScroll( int mScroll ) {
        this.mScroll = mScroll;
    }

}
