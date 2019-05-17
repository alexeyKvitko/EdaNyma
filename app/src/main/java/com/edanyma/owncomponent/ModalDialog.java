package com.edanyma.owncomponent;

import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.edanyma.AppConstants;
import com.edanyma.R;
import com.edanyma.activity.BaseActivity;
import com.edanyma.utils.AppUtils;
import com.edanyma.utils.ConvertUtils;

import java.io.Serializable;

public class ModalDialog extends RelativeLayout {


    private OnModalBtnClickListener mListener;

    private static final int DIALOG_HEIGHT = ( int ) ConvertUtils.convertDpToPixel( 250 );

    private static final String TAG = "ModalDialog";

    private RelativeLayout mModalDialog;
    private Activity mActivity;
    private FrameLayout mContainer;
    private DialogParams mDialogParams;



    public static ModalDialog execute( Activity activity, DialogParams dialogParams ){
        return new ModalDialog( activity, dialogParams );
    }


    public ModalDialog( Activity activity, DialogParams dialogParams ) {
        super( activity, null );
        inflate( activity, R.layout.modal_dialog, this );
        mActivity = activity;
        mContainer = (( BaseActivity ) activity).getDrawer();
        mContainer.addView( this );
        mContainer.setBackground( activity.getResources().getDrawable( R.drawable.border_debug_blue ) );
        mDialogParams = dialogParams;
        initialize();
    }

    private void initialize(){
        mModalDialog = this;
        mModalDialog.setOnClickListener( null );
        TextView modalTitle = findViewById( R.id.modalDialogTitleId );
        modalTitle.setTypeface( AppConstants.ROBOTO_BLACK );
        modalTitle.setText( mDialogParams.getTitle() );
        TextView modalMessage = findViewById( R.id.modalDialogMessageId );
        modalMessage.setTypeface( AppConstants.ROBOTO_CONDENCED );
        modalMessage.setText( mDialogParams.getMessage() );
        findViewById( R.id.modalDialogCardBlueId ).setOnClickListener( (View view ) -> {
            blueBtnClick( view );
        } );
        findViewById( R.id.modalDialogCardWhiteId ).setOnClickListener( (View view ) -> {
            whiteBtnClick(  view );
        } );
        animateDialogContainer( 0, DIALOG_HEIGHT );
        
        TextView blueBtn = findViewById( R.id.modalDialogBlueBtnId );
        blueBtn.setTypeface( AppConstants.B52 );
        blueBtn.setText( mDialogParams.getBlueButtonText() );
        blueBtn.setCompoundDrawablesWithIntrinsicBounds( 0, mDialogParams.getBlueButtonId() , 0, 0 );

        TextView whiteBtn = findViewById( R.id.modalDialogWhiteBtnId );
        whiteBtn.setTypeface( AppConstants.B52 );
        whiteBtn.setText( mDialogParams.getWhiteButtonText() );
        whiteBtn.setCompoundDrawablesWithIntrinsicBounds( 0,mDialogParams.getWhiteButtonId(), 0, 0 );
    }

    public static DialogParams getDialogParms(){
        return new DialogParams();
    }

    private void blueBtnClick( View view){
        AppUtils.btnClickAnimation( view );
        animateDialogContainer( DIALOG_HEIGHT, 0 );
        if( mListener != null ){
            mListener.onBlueButtonClick();
        }
    }

    private void whiteBtnClick( View view){
        AppUtils.btnClickAnimation( view );
        animateDialogContainer( DIALOG_HEIGHT, 0 );
        if( mListener != null ){
            mListener.onWhiteBtnClick();
        }
    }

    private void animateDialogContainer( final int start, final int end ) {
        final CardView dialogContainer = findViewById( R.id.messageDialogContainerId );
        final RelativeLayout.LayoutParams layoutParams =
                                ( RelativeLayout.LayoutParams ) dialogContainer.getLayoutParams();
        ValueAnimator valAnimator = ValueAnimator.ofObject( new IntEvaluator(), start, end );
        valAnimator.addUpdateListener( ( ValueAnimator animator ) -> {
                int val = ( Integer ) animator.getAnimatedValue();
                layoutParams.height = val;
                dialogContainer.setLayoutParams( layoutParams );
                if( end == 0 && val <= 0 ){
                    if ( mModalDialog != null ){
                        mModalDialog.setVisibility( View.GONE );
                        mModalDialog = null;
                        if ( mContainer != null ){
                            mContainer.removeView( mModalDialog );
                        }
                    }
                    mListener = null;
                }
        } );
        valAnimator.setDuration( 300 );
        valAnimator.start();
    }

    public void setOnModalBtnClickListener( OnModalBtnClickListener listener ){
        mListener = listener;
    }

    public static class DialogParams implements Serializable {

        private String title;
        private String message;
        private String blueButtonText;
        private int blueButtonId;
        private String whiteButtonText;
        private int whiteButtonId;

        public String getTitle() {
            return title;
        }

        public DialogParams setTitle( String title ) {
            this.title = title;
            return this;
        }

        public String getMessage() {
            return message;
        }

        public DialogParams setMessage( String message ) {
            this.message = message;
            return this;
        }

        public String getBlueButtonText() {
            return blueButtonText;
        }

        public DialogParams setBlueButtonText( String blueButtonText ) {
            this.blueButtonText = blueButtonText;
            return this;
        }

        public int getBlueButtonId() {
            return blueButtonId;
        }

        public DialogParams setBlueButtonId( int blueButtonId ) {
            this.blueButtonId = blueButtonId;
            return this;
        }

        public String getWhiteButtonText() {
            return whiteButtonText;
        }

        public DialogParams setWhiteButtonText( String whiteButtonText ) {
            this.whiteButtonText = whiteButtonText;
            return this;
        }

        public int getWhiteButtonId() {
            return whiteButtonId;
        }

        public DialogParams setWhiteButtonId( int whiteButtonId ) {
            this.whiteButtonId = whiteButtonId;
            return this;
        }
    }

    public interface OnModalBtnClickListener {
        void onBlueButtonClick();
        void onWhiteBtnClick();
    }
}
