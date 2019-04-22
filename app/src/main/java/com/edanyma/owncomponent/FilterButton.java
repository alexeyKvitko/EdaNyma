package com.edanyma.owncomponent;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edanyma.AppConstants;
import com.edanyma.EdaNymaApp;
import com.edanyma.R;
import com.edanyma.model.DictionaryModel;
import com.edanyma.utils.AppUtils;

public class FilterButton extends LinearLayout {

    private OnFilterButtonClickListener mListener;

    private DictionaryModel mFilterButton;
    private boolean mButtonSelect;
    private Drawable mDefBackground;

    public FilterButton( Context context ) {
        super( context );
        inflate( context, R.layout.filter_button, this );
    }

    public void setOnFilterButtonClickListener( OnFilterButtonClickListener listener ) {
        mListener = listener;
    }

    public void setFilterButton( final DictionaryModel filterButton, boolean selected ) {
        final TextView buttonTextView = this.findViewById( R.id.filterButtonId );
        mDefBackground = buttonTextView.getBackground();
        mFilterButton = filterButton;
        mButtonSelect = false;
        buttonTextView.setText( mFilterButton.getDisplayName() );
        buttonTextView.setTypeface( AppConstants.ROBOTO_CONDENCED );
        buttonTextView.setOnClickListener( ( View view ) -> {
            AppUtils.clickAnimation( view );
            mButtonSelect = !mButtonSelect;
            int backgroundId = mButtonSelect ? R.drawable.border_neon_radius
                    : R.drawable.border_disable_radius;
            buttonTextView.setBackground( EdaNymaApp.getAppContext()
                    .getResources().getDrawable( backgroundId ) );
            mListener.onFilterButtonClick( filterButton.getId() );
        } );
        if ( selected ) {
            mButtonSelect = true;
            buttonTextView.setBackground( EdaNymaApp.getAppContext()
                    .getResources().getDrawable( R.drawable.border_neon_radius ) );
        } else {
            buttonTextView.setBackground( EdaNymaApp.getAppContext()
                    .getResources().getDrawable( R.drawable.border_disable_radius ) );
        }

    }


    public interface OnFilterButtonClickListener {
        void onFilterButtonClick( String filterId );
    }

}
