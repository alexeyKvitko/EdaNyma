package com.edanyma.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edanyma.AppConstants;
import com.edanyma.EdaNymaApp;
import com.edanyma.R;

public class MainActivity extends AppCompatActivity {

    private static final int[] MAIN_CARDS = { R.id.topLayoutId, R.id.middleLayoutId, R.id.bottomLayoutId };
    private TextView mDeliveryTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_main);
        initialize();
    }


    private void initialize(){
        mDeliveryTV = this.findViewById( R.id.deliveryCityId );
        mDeliveryTV.setTypeface( AppConstants.ROBOTO_CONDENCED );
        mDeliveryTV.setText( "Симферополь" );
    }

    @Override
    protected void onResume() {
        super.onResume();
        LinearLayout mainCardItem = null;
        Animation[] cardAnimation = new Animation[3];
        for( int i = 0; i < 3; i++ ){
            cardAnimation[i] = AnimationUtils.loadAnimation( EdaNymaApp.getAppContext(),R.anim.bounce);
            cardAnimation[i].setStartOffset( i*100 );
        }
        int idx = 0;
        for( int cardId : MAIN_CARDS ){
            mainCardItem = ( LinearLayout ) this.findViewById( cardId );
            mainCardItem.startAnimation( cardAnimation[idx] );
            idx++;
        }
    }



}
