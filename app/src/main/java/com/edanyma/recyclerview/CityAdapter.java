package com.edanyma.recyclerview;

import android.os.Handler;
import android.support.v7.widget.CardView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edanyma.AppConstants;
import com.edanyma.EdaNymaApp;
import com.edanyma.R;
import com.edanyma.manager.GlobalManager;
import com.edanyma.model.TripleModel;
import com.edanyma.utils.AppUtils;
import com.edanyma.utils.ConvertUtils;
import com.edanyma.utils.GlideClient;

import java.util.ArrayList;
import java.util.Date;

import static com.edanyma.manager.GlobalManager.*;

public class CityAdapter extends CommonBaseAdapter< TripleModel > {

    private static final String CLASS_TAG = "CityAdapter";

    private static int GERB_SIZE = ( int ) ConvertUtils.convertDpToPixel( 76 );

    public CommonBaseAdapter mAdapter;

    public CityAdapter( ArrayList< TripleModel > mItemList ) {
        super( mItemList );
        mAdapter = this;
    }

    @Override
    public TripleModelDataObjectHolder onCreateViewHolder( ViewGroup parent, int viewType ) {
        View view = LayoutInflater.from( parent.getContext() )
                .inflate( R.layout.rv_item_city, parent, false );
        TripleModelDataObjectHolder dataObjectHolder = new TripleModelDataObjectHolder( view );
        mAssetManager = parent.getContext().getAssets();
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder( final BaseDataObjectHolder holder, final int position ) {
        TripleModelDataObjectHolder tripleHolder = ( TripleModelDataObjectHolder ) holder;

        String addUrl = "?time="+(new Date()).getTime();
        if ( mItemList.get( position ).getLeftItem() != null ) {
            setTextAndSelectedCard( tripleHolder.leftTitle, mItemList.get( position ).getLeftItem().getDisplayName(),
                    tripleHolder.leftCheckImage );
            GlideClient.downloadImage( EdaNymaApp.getAppContext(),
                    mItemList.get( position ).getLeftItem().getUrl()+addUrl, tripleHolder.leftImage, GERB_SIZE, GERB_SIZE );
        }

        if ( mItemList.get( position ).getCenterItem() != null ) {
            setTextAndSelectedCard( tripleHolder.centerTitle, mItemList.get( position ).getCenterItem().getDisplayName(),
                    tripleHolder.centerCheckImage );
            GlideClient.downloadImage( EdaNymaApp.getAppContext(),
                    mItemList.get( position ).getCenterItem().getUrl()+addUrl, tripleHolder.centerImage, GERB_SIZE, GERB_SIZE );
        }

        if ( mItemList.get( position ).getRightItem() != null ) {
            setTextAndSelectedCard( tripleHolder.rightTitle, mItemList.get( position ).getRightItem().getDisplayName(),
                    tripleHolder.rightCheckImage );
            GlideClient.downloadImage( EdaNymaApp.getAppContext(),
                    mItemList.get( position ).getRightItem().getUrl()+addUrl, tripleHolder.rightImage, GERB_SIZE, GERB_SIZE );
        }
        tripleHolder.leftContainer.setVisibility( mItemList.get( position ).getLeftItem() != null ? View.VISIBLE : View.GONE );
        tripleHolder.centerContainer.setVisibility( mItemList.get( position ).getCenterItem() != null ? View.VISIBLE : View.GONE );
        tripleHolder.rightContainer.setVisibility( mItemList.get( position ).getRightItem() != null ? View.VISIBLE : View.GONE );

    }

    private void setTextAndSelectedCard( TextView textView, String title, ImageView checkImage ) {
        textView.setText( title );
        if ( title.length() > 12 ) {
            textView.setTextSize( TypedValue.COMPLEX_UNIT_DIP, 10 );
        }
        if ( getBootstrapModel()
                .getDeliveryCity().toUpperCase().equals( title.toUpperCase() ) ) {
            checkImage.setVisibility( View.VISIBLE );
            textView.setTextColor( EdaNymaApp.getAppContext().getResources().getColor( R.color.blueNeon ) );
        } else {
            checkImage.setVisibility( View.GONE );
            textView.setTextColor( EdaNymaApp.getAppContext().getResources().getColor( R.color.primaryTextColor ) );
        }
    }

    public static class TripleModelDataObjectHolder extends BaseDataObjectHolder {

        public ImageView leftImage;
        public ImageView leftCheckImage;
        public TextView leftTitle;

        public ImageView centerImage;
        public ImageView centerCheckImage;
        public TextView centerTitle;

        public ImageView rightImage;
        public ImageView rightCheckImage;
        public TextView rightTitle;

        public FrameLayout leftContainer;
        public FrameLayout centerContainer;
        public FrameLayout rightContainer;

        public TripleModelDataObjectHolder( View itemView ) {
            super( itemView );

            leftContainer = itemView.findViewById( R.id.leftCityCardId );
            centerContainer = itemView.findViewById( R.id.centerCityCardId );
            rightContainer = itemView.findViewById( R.id.rightCityCardId );

            leftImage = itemView.findViewById( R.id.leftCityImgId );
            leftCheckImage = itemView.findViewById( R.id.leftCheckImgId );
            leftTitle = itemView.findViewById( R.id.leftCityTextId );
            setCardClickListener( ( FrameLayout ) itemView.findViewById( R.id.leftCityCardId ) );

            centerImage = itemView.findViewById( R.id.centerCityImgId );
            centerCheckImage = itemView.findViewById( R.id.centerCheckImgId );
            centerTitle = itemView.findViewById( R.id.centerCityTextId );
            setCardClickListener( ( FrameLayout ) itemView.findViewById( R.id.centerCityCardId ) );

            rightImage = itemView.findViewById( R.id.rightCityImgId );
            rightCheckImage = itemView.findViewById( R.id.rightCheckImgId );
            rightTitle = itemView.findViewById( R.id.rightCityTextId );
            setCardClickListener( ( FrameLayout ) itemView.findViewById( R.id.rightCityCardId ) );

            leftTitle.setTypeface( AppConstants.ROBOTO_CONDENCED );
            centerTitle.setTypeface( AppConstants.ROBOTO_CONDENCED );
            rightTitle.setTypeface( AppConstants.ROBOTO_CONDENCED );

        }

        private void setCardClickListener( FrameLayout layout ) {
            layout.setOnClickListener( this );
        }

        @Override
        public void onClick( final View view ) {
            if ( !( view instanceof FrameLayout ) ){
                return;
            }
            FrameLayout layout = ( FrameLayout ) view;
            for ( int i = 0; i < layout.getChildCount(); i++ ) {
                if ( layout.getChildAt( i ) instanceof ImageView &&
                        layout.getChildAt( i ).getElevation() == 21 ) {
                    AppUtils.bounceAnimation( layout.getChildAt( i ) );
                }
                if ( layout.getChildAt( i ) instanceof CardView ) {
                    TextView textView = ( ( TextView ) ( ( CardView ) layout.getChildAt( i ) ).getChildAt( 0 ) );
                    getBootstrapModel()
                            .setDeliveryCity( textView.getText().toString() );
                }
            }
            new Handler().postDelayed( new Runnable() {
                @Override
                public void run() {
                    TripleModelDataObjectHolder.super.onClick( view );
                }
            }, 200 );
        }
    }
}
