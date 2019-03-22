package com.edanyma.owncomponent;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.SearchView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.edanyma.AppConstants;
import com.edanyma.EdaNymaApp;
import com.edanyma.R;
import com.edanyma.utils.AppUtils;

public class OwnSearchView extends LinearLayout {

    private OnFilterClickListener mListener;

    private LinearLayout mThis;

    private SearchView mSearchInput;

    public OwnSearchView( Context context, @Nullable AttributeSet attrs ) {
        super( context, attrs );
        inflate( context, R.layout.own_search_view, this );
        mThis = this;
        initialize();
    }

    public void setOnFilterClickListener( OnFilterClickListener listener ) {
        mListener = listener;
    }

    private void initialize() {
        mSearchInput = this.findViewById( R.id.searchInputId );
        EditText searchEditText = mSearchInput.findViewById( android.support.v7.appcompat.R.id.search_src_text );
        searchEditText.setTypeface( AppConstants.ROBOTO_CONDENCED );
        searchEditText.setTextSize( TypedValue.COMPLEX_UNIT_DIP, 14 );
        searchEditText.setTextColor( EdaNymaApp.getAppContext().getResources().getColor( R.color.splashTextColor ) );
        findViewById( R.id.ownFilterId ).setOnClickListener( new OnClickListener() {
            @Override
            public void onClick( View view ) {
                AppUtils.clickAnimation( view );
                if ( mListener != null ) {
                    mListener.onFilterButtonClick();
                }
            }
        } );
    }

    public interface OnFilterClickListener {
        void onFilterButtonClick();
    }

}
