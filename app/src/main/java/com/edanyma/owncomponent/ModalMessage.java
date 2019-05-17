package com.edanyma.owncomponent;

import android.app.Activity;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.CardView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.edanyma.AppConstants;
import com.edanyma.EdaNymaApp;
import com.edanyma.R;
import com.edanyma.activity.BaseActivity;
import com.edanyma.utils.ConvertUtils;


public class ModalMessage extends RelativeLayout {

    private RelativeLayout mModalMessage;
    private FrameLayout mContainer;
    private TextView mMessageTitle;
    private Activity mActivity;
    private int mTimeOut;

    public static void show( Activity activity, String title, String[] body){
        new ModalMessage( activity, title, body, 2000 );
    }

    public static void show( Activity activity, String title, String[] body, int timeOut){
        new ModalMessage( activity, title, body, timeOut );
    }


    public ModalMessage( Activity activity, String title, String[] text, int timeOut ) {
        super( activity, null );
        inflate( activity, R.layout.modal_message, this );
        mActivity = activity;
        mContainer = (( BaseActivity ) activity).getDrawer();
        mContainer.addView( this );
        mTimeOut = timeOut;
        initialize( title, text );
    }

    private void initialize(String title, String[] texts){
        LinearLayout cardView = findViewById( R.id.messageContainerId );
        mModalMessage = this;
        mMessageTitle = findViewById( R.id.toastTitleId );
        mMessageTitle.setTypeface( AppConstants.B52 );
        mMessageTitle.setText( title );
        for( String text : texts ){
            TextView textView = new TextView( mActivity );
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER_HORIZONTAL;
            params.topMargin = (int) ConvertUtils.convertDpToPixel( 8 );
            params.leftMargin = (int) ConvertUtils.convertDpToPixel( 6 );
            params.rightMargin = (int) ConvertUtils.convertDpToPixel( 6 );
            textView.setLayoutParams(params);
            textView.setGravity( Gravity.CENTER_HORIZONTAL );
            textView.setTypeface( AppConstants.ROBOTO_CONDENCED );
            textView.setTextSize( TypedValue.COMPLEX_UNIT_DIP, 14 );
            textView.setTextColor( mActivity.getResources().getColor( R.color.grayTextColor ) );
            textView.setText( text );
            cardView.addView( textView );
        }
        mModalMessage.setOnClickListener( null );
        mModalMessage.startAnimation( AnimationUtils.loadAnimation( EdaNymaApp.getAppContext(), R.anim.fade_in ) );
        mModalMessage.setVisibility( View.VISIBLE );
        ( new Handler() ).postDelayed( new Runnable() {
            @Override
            public void run() {
                Animation fadeOut = AnimationUtils.loadAnimation( EdaNymaApp.getAppContext(), R.anim.fade_out );
                fadeOut.setAnimationListener( new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart( Animation animation ) {
                    }

                    @Override
                    public void onAnimationEnd( Animation animation ) {
                        mModalMessage.setVisibility( View.GONE );
                        mContainer.removeView( mModalMessage );
                        mModalMessage = null;
                    }

                    @Override
                    public void onAnimationRepeat( Animation animation ) {
                    }
                } );
                mModalMessage.startAnimation( fadeOut );
            }
        }, mTimeOut );
    }


}
