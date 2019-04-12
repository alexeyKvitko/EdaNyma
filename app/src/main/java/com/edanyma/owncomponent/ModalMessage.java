package com.edanyma.owncomponent;

import android.app.Activity;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.edanyma.AppConstants;
import com.edanyma.EdaNymaApp;
import com.edanyma.R;
import com.edanyma.activity.BaseActivity;


public class ModalMessage extends RelativeLayout {

    private RelativeLayout mModalMessage;
    private FrameLayout mContainer;
    private TextView mMessageTitle;
    private TextView mMessageText;

    public static void show( Activity activity, String title, String body){
        new ModalMessage( activity, title, body );
    }


    public ModalMessage( Activity activity, String title, String text ) {
        super( activity, null );
        inflate( activity, R.layout.modal_message, this );
        mContainer = (( BaseActivity ) activity).getDrawer();
        mContainer.addView( this );
        mContainer.setBackground( activity.getResources().getDrawable( R.drawable.border_debug_blue ) );
        initialize( title, text );

    }

    private void initialize(String title, String text){
        mModalMessage = this;
        mMessageTitle = findViewById( R.id.toastTitleId );
        mMessageText = findViewById( R.id.toastTextId );
        mMessageTitle.setTypeface( AppConstants.B52 );
        mMessageText.setTypeface( AppConstants.ROBOTO_CONDENCED );
        mMessageTitle.setText( title );
        mMessageText.setText( text );
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
        }, 2000 );
    }

    public RelativeLayout getModalMessage() {
        return mModalMessage;
    }

    public TextView getMessageHeader() {
        return mMessageTitle;
    }

    public void setMessageHeader( TextView mMessageHeader ) {
        this.mMessageTitle = mMessageHeader;
    }

    public TextView getMessageText() {
        return mMessageText;
    }

    public void setMessageText( TextView mMessageText ) {
        this.mMessageText = mMessageText;
    }
}
