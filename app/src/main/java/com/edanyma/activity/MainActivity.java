package com.edanyma.activity;

import android.animation.ObjectAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.edanyma.EdaNymaApp;
import com.edanyma.R;
import com.edanyma.owncomponent.MainCardItem;

public class MainActivity extends AppCompatActivity {

    private static final int[] MAIN_CARDS = { R.id.wokCardId, R.id.burgerCardId, R.id.sushiCardId, R.id.pizzaCardId, R.id.restCardId };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MainCardItem mainCardItem = null;
        Animation[] cardAnimation = new Animation[5];
        for( int i = 0; i < 5; i++ ){
            cardAnimation[i] = AnimationUtils.loadAnimation( EdaNymaApp.getAppContext(),R.anim.bounce);
            cardAnimation[i].setStartOffset( i*100 );
        }
        int idx = 0;
        for( int cardId : MAIN_CARDS ){
            mainCardItem = ( MainCardItem ) this.findViewById( cardId );
            mainCardItem.startAnimation( cardAnimation[idx] );
            idx++;
        }
    }



}
