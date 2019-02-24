package com.edanyma.owncomponent;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
                    setTitleText( titleText,R.id.dishTitleTextId  );
                    break;
                case R.styleable.MainCardAttrs_countText:
                    String countText = attributes.getString(attr);
                    setCountText( countText );
                    break;
                case R.styleable.MainCardAttrs_dishImg:
                    Drawable imgSrc = attributes.getDrawable(attr );
                    setCardImage( imgSrc );
                    break;
                default:
                    break;
            }
        }
        final ImageView imageView = (ImageView ) this.findViewById( R.id.dishImgId ) ;
        ((FrameLayout) this.findViewById( R.id.mainCardId )).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick( View view) {
                imageView.startAnimation( AnimationUtils.loadAnimation( EdaNymaApp.getAppContext(),R.anim.bounce) );
            }
        });
        attributes.recycle();
    }

    private void setTitleText( String titleText, Integer id ){
        TextView titleTextView = (TextView) this.findViewById( id );
        titleTextView.setText( titleText );
        titleTextView.setTypeface( AppConstants.B52 );
    }


    private void setCountText( String countText ){
        int companyCount = Integer.valueOf( countText );
        String countSuffix = " заведени";
        String ending="й";
        int mod = companyCount % 10;
        if ( companyCount < 10 || companyCount > 15 ){
            if( mod == 1 ){
                ending = "е";
            }
            if( mod == 2 || mod == 3 || mod == 4){
                ending = "я";
            }
        }
        TextView countTextView = (TextView) this.findViewById( R.id.dishCountTextId );
        countTextView.setText( countText+countSuffix+ending );
        countTextView.setTypeface( AppConstants.ROBOTO_CONDENCED );
    }

    private void setCardImage( Drawable imgSrc ){
        ImageView btnIcon = ( ImageView ) this.findViewById( R.id.dishImgId );
        btnIcon.setImageDrawable( imgSrc );
    }


    public void setOnOnMainCardItemClickListener( OnMainCardItemClickListener listener){
        mListener = listener;
    }

    public interface OnMainCardItemClickListener{
        void onMainCardClick( int cardId );
    }
}