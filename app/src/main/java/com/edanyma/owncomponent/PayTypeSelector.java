package com.edanyma.owncomponent;

import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edanyma.AppConstants;
import com.edanyma.R;
import com.edanyma.model.PayType;
import com.edanyma.utils.AppUtils;
import com.edanyma.utils.ConvertUtils;

public class PayTypeSelector extends FrameLayout implements View.OnClickListener {

    private static int CASH_MARGIN = ( int ) ConvertUtils.convertDpToPixel( 32 );
    private static int WALLET_MARGIN = ( int ) ConvertUtils.convertDpToPixel( 140 );

    private OnPayTypeSelectListener mListener;

    private TextView mPayTypeCash;
    private TextView mPayTypeWallet;
    private LinearLayout mPayTypeBtn;

    private PayType mSelectedPayType;

    private Context mContext;

    public PayTypeSelector( @NonNull Context context, @Nullable AttributeSet attrs ) {
        super( context, attrs );
        inflate( context, R.layout.pay_type_selector, this );
        mContext = context;
        initialize();
    }

    private void initialize() {
        mSelectedPayType = PayType.CASH;

        mPayTypeCash = findViewById( R.id.clientPayTypeCashId );
        mPayTypeCash.setTypeface( AppConstants.ROBOTO_CONDENCED );
        mPayTypeCash.setOnClickListener( this );

        mPayTypeWallet = findViewById( R.id.clientPayTypeWalletId );
        mPayTypeWallet.setTypeface( AppConstants.ROBOTO_CONDENCED );
        mPayTypeWallet.setOnClickListener( this );

        mPayTypeBtn = findViewById( R.id.payTypeBtnId );
    }

    public void setPayTypeSelectListener( OnPayTypeSelectListener listener ) {
        mListener = listener;
    }

    private void animatePayTypeContainer( final int start, final int end ) {
        final FrameLayout.LayoutParams layoutParams = ( FrameLayout.LayoutParams ) mPayTypeBtn.getLayoutParams();
        ValueAnimator valAnimator = ValueAnimator.ofObject( new IntEvaluator(), start, end );
        valAnimator.addUpdateListener( ( ValueAnimator animator ) -> {
            int val = ( Integer ) animator.getAnimatedValue();
            if ( val == end ) {
                mPayTypeCash.setTextColor( mContext.getResources().getColor( R.color.iconGrayColor ) );
                mPayTypeWallet.setTextColor( mContext.getResources().getColor( R.color.iconGrayColor ) );
                TextView selType = PayType.CASH.equals( mSelectedPayType ) ? mPayTypeCash : mPayTypeWallet;
                selType.setTextColor( mContext.getResources().getColor( R.color.blueNeon ) );
            }
            layoutParams.leftMargin = val;
            mPayTypeBtn.setLayoutParams( layoutParams );
        } );
        valAnimator.setDuration( 300 );
        valAnimator.start();
    }


    private void choosePayType( PayType selectedPayType ) {
        mSelectedPayType = selectedPayType;
        if ( mListener != null ) {
            mListener.onPayTypeSelectAction( selectedPayType );
        }
        if ( PayType.QIWI.equals( selectedPayType ) ) {
            animatePayTypeContainer( CASH_MARGIN, WALLET_MARGIN );
        } else if ( PayType.CASH.equals( selectedPayType ) ) {
            animatePayTypeContainer( WALLET_MARGIN, CASH_MARGIN );
        }
    }


    public void setSelectedPayType( PayType selectedPayType ) {
        this.mSelectedPayType = selectedPayType;
        if( PayType.QIWI.equals( mSelectedPayType) ){
            animatePayTypeContainer( CASH_MARGIN, WALLET_MARGIN );
        } else {
            animatePayTypeContainer(  WALLET_MARGIN, CASH_MARGIN);
        }
    }

    public void enabledPayTypeButton( boolean param ) {
        mPayTypeCash.setEnabled( param );
        mPayTypeWallet.setEnabled( param );
    }

    public void hideBottomBorder(){
        this.setBackground( null );
    }

    @Override
    public void onClick( View view ) {
        AppUtils.clickAnimation( view );
        switch ( view.getId() ) {
            case R.id.clientPayTypeCashId:
                choosePayType( PayType.CASH );
                break;
            case R.id.clientPayTypeWalletId:
                choosePayType( PayType.QIWI );
                break;
            default:
                break;
        }
    }

    public interface OnPayTypeSelectListener {
        void onPayTypeSelectAction( PayType payType );
    }
}
