package com.edanyma.owncomponent;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.SearchView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.edanyma.AppConstants;
import com.edanyma.EdaNymaApp;
import com.edanyma.R;

public class OwnSearchView extends LinearLayout {

    private LinearLayout mThis;

    private SearchView mSearchInput;

    public OwnSearchView( Context context, @Nullable AttributeSet attrs ) {
        super( context, attrs );
        inflate(context, R.layout.own_search_view, this);
        mThis = this;
        initialize();
    }

    private void initialize(){
        mSearchInput = this.findViewById( R.id.searchInputId );
        EditText searchEditText = mSearchInput.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTypeface( AppConstants.ROBOTO_CONDENCED );
        searchEditText.setTextSize( TypedValue.COMPLEX_UNIT_DIP, 14 );
        searchEditText.setTextColor( EdaNymaApp.getAppContext().getResources().getColor( R.color.splashTextColor ) );

    }


}
