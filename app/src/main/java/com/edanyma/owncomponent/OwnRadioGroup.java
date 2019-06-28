package com.edanyma.owncomponent;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.edanyma.EdaNymaApp;
import com.edanyma.R;
import com.edanyma.model.PayType;
import com.edanyma.utils.AppUtils;
import com.edanyma.utils.ConvertUtils;

import java.util.HashMap;
import java.util.Map;

public class OwnRadioGroup extends LinearLayout implements View.OnClickListener {

    private static Map<Integer,Integer> ENABLE_BUTTONS = new HashMap< Integer, Integer >(  ){{
        put( R.id.moneyPaidId, R.drawable.money_paid );
        put( R.id.qiwiPaidId, R.drawable.qiwi_paid );
        put( R.id.payeerPaidId, R.drawable.payeer_paid );
    }};

    private static Map<Integer,Integer> DISABLE_BUTTONS = new HashMap< Integer, Integer >(  ){{
        put( R.id.moneyPaidId, R.drawable.money_paid_disable );
        put( R.id.qiwiPaidId, R.drawable.qiwi_paid_disable );
        put( R.id.payeerPaidId, R.drawable.payeer_paid_disable );
    }};

    private static Map<Integer, PayType > PAYTYPE_MAP = new HashMap< Integer, PayType >(  ){{
        put( R.id.moneyPaidId, PayType.CASH );
        put( R.id.qiwiPaidId, PayType.QIWI );
        put( R.id.payeerPaidId, PayType.PAYEER );
    }};

    public  OnPayTypeSelectListener mListener;

    private ImageView mMoneyPaid;
    private ImageView mQiwiPaid;
    private ImageView mPayeerPaid;

    private boolean mBusy;


    public OwnRadioGroup( Context context ) {
        super( context );
        inflate( context, R.layout.own_radio_group, this );
        init();
    }

    public OwnRadioGroup( Context context, @Nullable AttributeSet attrs ) {
        super( context, attrs );
        inflate( context, R.layout.own_radio_group, this );
        init();
    }

    private void init(){
        mBusy =false;
        mMoneyPaid = findViewById( R.id.moneyPaidId );
        mQiwiPaid = findViewById( R.id.qiwiPaidId );
        mPayeerPaid = findViewById( R.id.payeerPaidId );

        mMoneyPaid.setOnClickListener( this );
        mQiwiPaid.setOnClickListener( this );
        mPayeerPaid.setOnClickListener( this );
    }

    public void setOnPayTypeSelectListener( OnPayTypeSelectListener listener ){
        mListener = listener;
    }

    @Override
    public void onClick( View view ) {
        if ( mBusy ){
            return;
        }
        AppUtils.clickAnimation( view );
        enableButton( view.getId() );
        if( mListener != null ){
            mBusy = true;
            mListener.onPayTypeChange( PAYTYPE_MAP.get( view.getId() ) );
        }
    }

    public void setPayType( String payTypeStr ){
        PayType payType = ConvertUtils.convertStringToPayType( payTypeStr );
        for( Integer imageId : PAYTYPE_MAP.keySet() ){
            if ( payType.equals( PAYTYPE_MAP.get( imageId ) ) ){
                enableButton( imageId );
                break;
            }
        }
    }

    private void enableButton( int imageBtnId ){
        for(Integer imageId : DISABLE_BUTTONS.keySet() ){
            ((ImageView)  findViewById( imageId )).setImageDrawable( EdaNymaApp.getAppContext()
                    .getResources().getDrawable( DISABLE_BUTTONS.get( imageId ) ) );
        }
        ((ImageView)  findViewById( imageBtnId )).setImageDrawable( EdaNymaApp.getAppContext()
                .getResources().getDrawable( ENABLE_BUTTONS.get( imageBtnId ) ) );
    }

    public void enabledPayTypeButton(){
        mBusy = false;
    }

    public interface OnPayTypeSelectListener{
        void onPayTypeChange( PayType payType );
    }
}
