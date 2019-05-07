package com.edanyma.fragment;

import android.os.Handler;
import android.support.v7.widget.AppCompatEditText;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.edanyma.AppConstants;
import com.edanyma.R;
import com.edanyma.utils.AppUtils;

public class ConfirmFragment extends BaseFragment implements View.OnFocusChangeListener, View.OnKeyListener {

    protected AppCompatEditText mDigitOne;
    protected AppCompatEditText mDigitTwo;
    protected AppCompatEditText mDigitThree;
    protected AppCompatEditText mDigitFour;
    protected TextView mResendCodeValue;

    protected String mConfirmationCode;

    protected int mSecondLeft;

    protected Handler mCountdown;

    protected Runnable mCountdownJob;

    protected void initConfirmFragment() {

        mResendCodeValue = initTextView( R.id.resendCodeValueId, AppConstants.ROBOTO_CONDENCED );

        mDigitOne = initEditText( R.id.confirmDigitOneId );
        mDigitTwo = initEditText( R.id.confirmDigitTwoId );
        mDigitThree = initEditText( R.id.confirmDigitThreeId );
        mDigitFour = initEditText( R.id.confirmDigitFourId );

        mDigitOne.setOnFocusChangeListener( this );
        mDigitOne.setOnKeyListener( this );

        mDigitTwo.setOnFocusChangeListener( this );
        mDigitTwo.setOnKeyListener( this );

        mDigitThree.setOnFocusChangeListener( this );
        mDigitThree.setOnKeyListener( this );

        mDigitFour.setOnFocusChangeListener( this );
        mDigitFour.setOnKeyListener( this );
    }

    protected void startCountdown() {
        mSecondLeft = 90;
        mCountdown = new Handler();
        mCountdownJob = new Runnable() {
            @Override
            public void run() {
                mResendCodeValue.setText( mSecondLeft + "" );
                if ( mSecondLeft == 70 ) {
                    getView().findViewById( R.id.resendCodeLabelId ).setVisibility( View.VISIBLE );
                }
                if ( mSecondLeft == 0 ) {
                    mCountdown.removeCallbacks( this );
                    AppUtils.transitionAnimation( getView().findViewById( R.id.confirmCodeContainerId ),
                            getView().findViewById( R.id.signUpContailnerId ) );
                    return;
                }
                mSecondLeft--;
                mCountdown.postDelayed( this, 1000 );
            }
        };
        mCountdown.postDelayed( mCountdownJob, 1000 );
    }

    protected boolean checkEnteredCode() {
        boolean success = true;
        String confirmCode = mDigitOne.getText().toString()
                + mDigitTwo.getText().toString()
                + mDigitThree.getText().toString()
                + mDigitFour.getText().toString();
        if ( !mConfirmationCode.equals( confirmCode ) ) {
            getView().findViewById( R.id.confirmCodeErrorId ).setVisibility( View.VISIBLE );
            ( new Handler() ).postDelayed( new Runnable() {
                @Override
                public void run() {
                    getView().findViewById( R.id.confirmCodeErrorId ).setVisibility( View.GONE );
                }
            }, 10000 );
            success = false;
        }
        return success;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if ( mCountdown != null && mCountdownJob != null ) {
            mCountdown.removeCallbacks( mCountdownJob );
        }
    }

    @Override
    public void onFocusChange( View view, boolean isFocused ) {
        if ( isFocused
                && AppConstants.ASTERISKS.equals( ( ( AppCompatEditText ) view ).getText().toString() ) ) {
            ( ( AppCompatEditText ) view ).setText( "" );
        }
    }

    @Override
    public boolean onKey( View view, int i, KeyEvent keyEvent ) {
        if ( i != 67 && KeyEvent.ACTION_DOWN == keyEvent.getAction() ) {
            switch ( view.getId() ) {
                case R.id.confirmDigitOneId:
                    mDigitTwo.requestFocus();
                    break;
                case R.id.confirmDigitTwoId:
                    mDigitThree.requestFocus();
                    break;
                case R.id.confirmDigitThreeId:
                    mDigitFour.requestFocus();
                    break;
                case R.id.confirmDigitFourId:
                    mDigitOne.requestFocus();
                    break;
                default:
                    break;
            }
        }
        return false;
    }


}
