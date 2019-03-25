package com.edanyma.owncomponent;

import android.content.Context;
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

    public FilterButton( Context context ) {
        super( context );
        inflate(context, R.layout.filter_button, this);
    }

    public void setOnFilterButtonClickListener(OnFilterButtonClickListener listener ){
        mListener = listener;
    }

    public void setFilterButton( final DictionaryModel filterButton, boolean selected ){
        final TextView buttonTextView =  this.findViewById( R.id.filterButtonId );
        mFilterButton = filterButton;
        mButtonSelect = false;
        buttonTextView.setText( mFilterButton.getDisplayName() );
        buttonTextView.setTypeface( AppConstants.ROBOTO_CONDENCED );
        buttonTextView.setOnClickListener( new OnClickListener() {
            @Override
            public void onClick( View view ) {
                AppUtils.clickAnimation( view );
                mButtonSelect = !mButtonSelect;
                int backgroundId =  mButtonSelect ? R.drawable.border_neon_radius
                                                                : R.drawable.border_disable_radius;
                buttonTextView.setBackground( EdaNymaApp.getAppContext()
                                                    .getResources().getDrawable( backgroundId ) );
                mListener.onFilterButtonClick( filterButton.getId() );
            }
        } );
        if( selected ){
            buttonTextView.setBackground( EdaNymaApp.getAppContext()
                    .getResources().getDrawable( R.drawable.border_neon_radius ) );
        }
        if ( AppConstants.STR_FAKE_ID.equals( filterButton.getId() ) ){
            buttonTextView.setBackground( EdaNymaApp.getAppContext().getResources().getDrawable( R.drawable.background_transparent ) );
            buttonTextView.setOnClickListener( null );
        }
    }


    public interface OnFilterButtonClickListener {
        void onFilterButtonClick( String filterId );
    }

}
