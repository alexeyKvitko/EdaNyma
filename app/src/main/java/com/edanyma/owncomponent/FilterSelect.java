package com.edanyma.owncomponent;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edanyma.AppConstants;
import com.edanyma.EdaNymaApp;
import com.edanyma.R;
import com.edanyma.model.DictionaryModel;
import com.edanyma.utils.AppUtils;
import com.google.android.flexbox.FlexboxLayout;

import java.util.LinkedList;
import java.util.List;

public class FilterSelect extends LinearLayout implements FilterButton.OnFilterButtonClickListener {

    private String mFilterState;
    private int  mDefButtonCount;
    private List< DictionaryModel > mFilters;
    private List< String > mSelectedIds;

    public FilterSelect( Context context, @Nullable AttributeSet attrs ) {
        super( context, attrs );
        inflate(context, R.layout.filter_select, this);
        mFilterState = AppConstants.CLOSE;
        mFilters = new LinkedList<>();
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.FilterSelectAttrs );
        final int attrCount = attributes.getIndexCount();
        for (int idx = 0; idx < attrCount; idx++) {
            int attr = attributes.getIndex(idx);
            switch (attr) {
                case R.styleable.FilterSelectAttrs_filterTitle:
                    String titleText = attributes.getString(attr);
                    setTitleText( titleText,R.id.filterTitleId  );
                    break;
                default:
                    break;
            }
        }
        attributes.recycle();
    }

    private void setTitleText( String titleText, Integer id ){
        final TextView titleTextView =  this.findViewById( id );
        titleTextView.setText( titleText );
        titleTextView.setTypeface( AppConstants.ROBOTO_BLACK );
        titleTextView.setOnClickListener( new OnClickListener() {
            @Override
            public void onClick( View view ) {
                int drawableId;
                AppUtils.clickAnimation( view );
               if( AppConstants.CLOSE.equals( mFilterState ) ){
                   mFilterState = AppConstants.OPEN;
                   drawableId = R.drawable.ic_menu_up_black_18dp;
                   showFilters( mFilters.size() );
                } else {
                    mFilterState = AppConstants.CLOSE;
                   drawableId = R.drawable.ic_menu_down_black_18dp;
                   showFilters( mDefButtonCount );
                }
                titleTextView.setCompoundDrawablesWithIntrinsicBounds( 0, 0, drawableId, 0 );
            }
        } );
    }

    public void addFilters(List<DictionaryModel> filters, int defButtonCount ){
        mFilters.addAll( filters );
        mDefButtonCount = defButtonCount;
        mSelectedIds = new LinkedList<>(  );
        showFilters( mDefButtonCount );
    }

    public void showFilters( int buttonCount ){
        FlexboxLayout buttonContainer = findViewById( R.id.filterButtonsContainerId );
        buttonContainer.removeAllViewsInLayout();
        for( int i = 0; i < buttonCount; i++ ){
           FilterButton filterButton = new FilterButton( EdaNymaApp.getAppContext() );
           filterButton.setFilterButton(  mFilters.get( i ), mSelectedIds.indexOf( mFilters.get( i ).getId() ) > -1 );
           filterButton.setOnFilterButtonClickListener( this );
           buttonContainer.addView( filterButton );
        }
        if ( buttonCount > 0 && ( buttonCount < mFilters.size() ) ){
            FilterButton filterButton = new FilterButton( EdaNymaApp.getAppContext() );
            DictionaryModel last = new DictionaryModel();
            last.setId( AppConstants.STR_FAKE_ID );
            last.setDisplayName( "..." );
            filterButton.setFilterButton(  last,false );
            buttonContainer.addView( filterButton );
        }
    }

    @Override
    public void onFilterButtonClick( String filterId ) {
        if( mSelectedIds.indexOf( filterId ) > -1 ){
            mSelectedIds.remove( filterId );
        } else {
            mSelectedIds.add( filterId );
        }

    }
}
