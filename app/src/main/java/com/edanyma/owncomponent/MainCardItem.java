package com.edanyma.owncomponent;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edanyma.AppConstants;
import com.edanyma.EdaNymaApp;
import com.edanyma.R;

public class MainCardItem extends LinearLayout {

    private OnMainCardItemClickListener mListener;
    private LinearLayout mThis;
    private int mId;

    public MainCardItem( Context context, @Nullable AttributeSet attrs ) {
        super( context, attrs );
        inflate(context, R.layout.main_card_item, this);
        mThis = this;
        mId = this.getId();
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.MainCardAttrs );
        final int attrCount = attributes.getIndexCount();
        for (int idx = 0; idx < attrCount; idx++) {
            int attr = attributes.getIndex(idx);
            switch (attr) {
                case R.styleable.MainCardAttrs_titleText:
                    String titleText = attributes.getString(attr);
                    setTitleText( titleText );
                    break;
                default:
                    break;
            }
        }
//        LinearLayout linearLayout= (LinearLayout) this.findViewById( R.id.mainCardId );
//        linearLayout.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick( View view) {
//                mThis.startAnimation( AnimationUtils.loadAnimation( EdaNymaApp.getAppContext(),R.anim.rotate) );
//            }
//        });
        attributes.recycle();
    }

    private void setTitleText( String titleText ){
        TextView titleTextView = (TextView) this.findViewById( R.id.dishTitleTextId );
        titleTextView.setText( titleText );
        titleTextView.setTypeface( AppConstants.TAURUS );
//        LinearLayout singleRowContainer = ( LinearLayout )findViewById( R.id.single_text_container );
//        singleRowContainer.setVisibility(View.VISIBLE);
    }

    public void setOnOnMainCardItemClickListener( OnMainCardItemClickListener listener){
        mListener = listener;
    }

    public interface OnMainCardItemClickListener{
        void onMainCardClick( int cardId );
    }
}
