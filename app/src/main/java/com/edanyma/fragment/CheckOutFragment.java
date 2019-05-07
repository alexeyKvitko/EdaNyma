package com.edanyma.fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edanyma.AppConstants;
import com.edanyma.R;
import com.edanyma.manager.BasketOrderManager;
import com.edanyma.manager.GlobalManager;
import com.edanyma.model.ApiResponse;
import com.edanyma.model.BasketModel;
import com.edanyma.model.ClientLocation;
import com.edanyma.model.MenuEntityModel;
import com.edanyma.owncomponent.CheckOutEntity;
import com.edanyma.owncomponent.CompanyTotalView;
import com.edanyma.rest.RestController;
import com.edanyma.utils.AppUtils;
import com.edanyma.utils.ConvertUtils;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;


public class CheckOutFragment extends ConfirmFragment implements View.OnClickListener,
        CheckOutEntity.OnRemoveFromBasketListener, View.OnFocusChangeListener, View.OnKeyListener  {

    private OnCheckOutFragmentListener mListener;

    private LinearLayout mDishContainer;

    private AppCompatEditText mCheckOutPerson;
    private AppCompatEditText mCheckOutPhone;
    private AppCompatEditText mCheckOutСomment;


    private TextView mCheckOutCommonTotal;
    private TextView mCheckOutCity;
    private TextView mCheckOutAddress;
    private TextView mCheckOutAdditionalAddress;

    private String mConfirmationCode;


    private Map< String, BasketModel > mFilteredBasket;
    private Integer mTotalAmount;

    public CheckOutFragment() {
    }

    public static CheckOutFragment newInstance() {
        CheckOutFragment fragment = new CheckOutFragment();
        return fragment;
    }

    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
    }

    @Override
    public void onActivityCreated( @Nullable Bundle savedInstanceState ) {
        super.onActivityCreated( savedInstanceState );
        initConfirmFragment();
        initTextView( R.id.checkOutLabelId, AppConstants.B52 );
        initTextView( R.id.contactInfoTitleId, AppConstants.B52 );
        initTextView( R.id.deliveryAddressTitleId, AppConstants.B52 );
        initTextInputLayout( R.id.checkOutPersonTextLayoutId, AppConstants.ROBOTO_CONDENCED );
        initTextInputLayout( R.id.checkOutPhoneLayoutId, AppConstants.ROBOTO_CONDENCED );
        initTextInputLayout( R.id.checkOutCommentLayoutId, AppConstants.ROBOTO_CONDENCED );

        mCheckOutCommonTotal = initTextView( R.id.checkOutCommonSumId,
                AppConstants.OFFICE, Typeface.BOLD, null );

        mCheckOutPerson = initEditText( R.id.checkOutPersonId, AppConstants.ROBOTO_CONDENCED );
        mCheckOutСomment = initEditText( R.id.checkOutCommentId, AppConstants.ROBOTO_CONDENCED );
        mCheckOutPhone = initEditText( R.id.checkOutPhoneId, AppConstants.ROBOTO_CONDENCED );
        mCheckOutPhone.addTextChangedListener( new PhoneNumberFormattingTextWatcher() );

        mCheckOutCity = initTextView( R.id.checkOutCityId, AppConstants.ROBOTO_CONDENCED,
                GlobalManager.getInstance().getBootstrapModel().getDeliveryCity() );
        mCheckOutAddress = initTextView( R.id.checkOutStreetHouseId, AppConstants.ROBOTO_CONDENCED,
                getActivity().getResources().getString( R.string.not_available_yet ) );
        mCheckOutAdditionalAddress = initTextView( R.id.checkOutAdditionalAddressId, AppConstants.ROBOTO_CONDENCED,
                getActivity().getResources().getString( R.string.not_available_yet ) );


        getView().findViewById( R.id.checkOutBackBtnId ).setOnClickListener( this );
        getView().findViewById( R.id.checkOutMapId ).setOnClickListener( this );
        getView().findViewById( R.id.checkOutSuccessBtnId ).setOnClickListener( this );
        mDishContainer = getView().findViewById( R.id.checkOutDishContainerId );
        setClientInfo();
        setDeliveryAddress();
        fillDishContainer();
    }

    private void setClientInfo(){
        if ( GlobalManager.getInstance().isSignedIn() ){
            mCheckOutPerson.setText( GlobalManager.getInstance().getClient().getUuid() );
            mCheckOutPhone.setText( GlobalManager.getInstance().getClient().getPhone() );
        }
    }

    private void setDeliveryAddress() {
        ClientLocation location = GlobalManager.getInstance().getClientLocation();
        if ( !AppUtils.nullOrEmpty( location.getStreet() ) ){
            String address = location.getStreet();
            if ( !AppUtils.nullOrEmpty( location.getHouse() ) ){
                address = address+", " + location.getHouse();
            }
            mCheckOutAddress.setText( address );
        }
        StringBuilder additionalAddress = new StringBuilder();
        if ( !AppUtils.nullOrEmpty( location.getEntrance() ) ) {
            additionalAddress.append( getActivity().getResources().getString( R.string.entrace_label )+": " )
                    .append( location.getEntrance() ).append( ", " );
        }
        if ( !AppUtils.nullOrEmpty( location.getIntercom() ) ) {
            additionalAddress.append( getActivity().getResources().getString( R.string.intercom_label )+": " )
                    .append( location.getIntercom() ).append( ", " );
        }
        if ( !AppUtils.nullOrEmpty( location.getFloor() ) ) {
            additionalAddress.append( getActivity().getResources().getString( R.string.floor_label )+": " )
                    .append( location.getFloor() ).append( ", " );
        }
        if( additionalAddress.length() > 0 ){
            additionalAddress.delete( additionalAddress.length()-2, additionalAddress.length() );
            mCheckOutAdditionalAddress.setText( additionalAddress.toString() );
            mCheckOutAdditionalAddress.setVisibility( View.VISIBLE );
        } else {
            mCheckOutAdditionalAddress.setVisibility( View.GONE );
        }
    }

    private void filterBasket() {
        mFilteredBasket = new HashMap<>();
        mTotalAmount = 0;
        for ( MenuEntityModel dish : BasketOrderManager.getInstance().getBasket() ) {
            BasketModel basketModel = mFilteredBasket.get( dish.getCompanyId() );
            if ( basketModel == null ) {
                basketModel = new BasketModel();
                basketModel.setCompany( GlobalManager.getInstance().getCompanyById( dish.getCompanyId() ) );
            }
            basketModel.getBasket().add( dish );
            mFilteredBasket.put( dish.getCompanyId(), basketModel );
        }
        if ( mFilteredBasket.size() == 0 ) {
            getActivity().onBackPressed();
        }
        for ( BasketModel basketModel : mFilteredBasket.values() ) {
            mTotalAmount += basketModel.getPrice();
        }
        mCheckOutCommonTotal.setText( mTotalAmount.toString() );
    }

    private void fillDishContainer() {
        filterBasket();
        mDishContainer.removeAllViews();
        for ( BasketModel basketModel : mFilteredBasket.values() ) {
            mDishContainer.addView( createCompanyTitle( basketModel.getCompany().getDisplayName() ) );
            CompanyTotalView companyTotal = createCompanyTotal( basketModel.getCompany().getId(), basketModel.getPrice() );
            for ( MenuEntityModel dish : basketModel.getBasket() ) {
                mDishContainer.addView( createCheckOutDishEntity( dish, companyTotal ) );
            }
            mDishContainer.addView( companyTotal );
        }
    }

    private CheckOutEntity createCheckOutDishEntity( MenuEntityModel menuEntityModel, CompanyTotalView companyTotal ) {
        CheckOutEntity checkOutEntity = new CheckOutEntity( getContext() );
        checkOutEntity.setDishEntity( menuEntityModel, companyTotal );
        checkOutEntity.setOnRemoveFromBasketListener( this );
        return checkOutEntity;
    }

    private TextView createCompanyTitle( String companyName ) {
        TextView companyTitle = new TextView( getContext() );
        companyTitle.setTypeface( AppConstants.B52 );
        companyTitle.setTextColor( getActivity().getResources().getColor( R.color.iconGrayColor ) );
        companyTitle.setTextSize( TypedValue.COMPLEX_UNIT_DIP, 18 );
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT );
        layoutParams.leftMargin = ( int ) ConvertUtils.convertDpToPixel( 42 );
        layoutParams.bottomMargin = ( int ) ConvertUtils.convertDpToPixel( 8 );

        companyTitle.setLayoutParams( layoutParams );
        companyTitle.setText( companyName );
        return companyTitle;
    }

    private CompanyTotalView createCompanyTotal( String companyId, Integer companyTotal ) {
        CompanyTotalView companyTotalView = new CompanyTotalView( getContext(), companyId, companyTotal );
        return companyTotalView;
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState ) {
        return inflater.inflate( R.layout.fragment_check_out, container, false );
    }

    public void onCheckOut() {
        if ( mListener != null ) {
            mListener.onCheckOut();
        }
    }

    @Override
    public void onAttach( Context context ) {
        super.onAttach( context );
        if ( context instanceof OnCheckOutFragmentListener ) {
            mListener = ( OnCheckOutFragmentListener ) context;
        } else {
            throw new RuntimeException( context.toString()
                    + " must implement OnCheckOutFragmentListener" );
        }
    }

    private void finishCheckOut(){
        String person = mCheckOutPerson.getText().toString();
        String phone = mCheckOutPhone.getText().toString();
        String comment = mCheckOutСomment.getText().toString();
        String errorMsg = null;
        TextView personError = initTextView( R.id.checkOutPersonErrorId, AppConstants.ROBOTO_CONDENCED  );
        TextView phoneError = initTextView( R.id.checkOutPhoneErrorId, AppConstants.ROBOTO_CONDENCED  );
        if ( person.length() == 0 ){
            errorMsg = getResources().getString( R.string.error_required_field );
            personError.setText( errorMsg );
            personError.setVisibility( View.VISIBLE );
        }
        if ( errorMsg == null && phone.length() == 0 ){
            errorMsg = getResources().getString( R.string.error_required_field );
        }
        if ( errorMsg == null && !AppUtils.validatePhone( phone ) ) {
            errorMsg = getResources().getString( R.string.error_wrong_phone );
        }
        if ( errorMsg != null  ){
            if ( personError.getVisibility() == View.GONE ){
                phoneError.setText( errorMsg );
                phoneError.setVisibility( View.VISIBLE );
            }
            ( new Handler() ).postDelayed( new Runnable() {
                @Override
                public void run() {
                    personError.setVisibility( View.GONE );
                    phoneError.setVisibility( View.GONE );
                }
            }, 3000 );
        }
        if( errorMsg == null ){
            new ValidateCheckoutCode().execute( phone );
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick( View view ) {
        switch ( view.getId() ) {
            case R.id.checkOutBackBtnId:
                getActivity().onBackPressed();
                break;
            case R.id.checkOutSuccessBtnId:
                finishCheckOut();
                break;
            case R.id.checkOutMapId:
                if( mListener !=null ){
                    AppUtils.clickAnimation( view );
                    mListener.onShowMapClick();
                }
                break;
        }
    }



    @Override
    public void onRemoveFromBasket( MenuEntityModel dishEntity ) {
        BasketOrderManager.removeEntityFromBasket( dishEntity );
        fillDishContainer();
    }

    @Override
    public void onChangeEntityCount( Integer delta ) {
        mTotalAmount += delta;
        mCheckOutCommonTotal.setText( mTotalAmount.toString() );
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
        if ( i != 67 && KeyEvent.ACTION_DOWN == keyEvent.getAction()) {
            switch ( view.getId()  ) {
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



    public interface OnCheckOutFragmentListener {
        // TODO: Update argument type and name
        void onCheckOut();
        void onShowMapClick();
    }


    private class ValidateCheckoutCode extends AsyncTask< String, Void, String > {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground( String... phone ) {
            String result = null;
            try {
                Call< ApiResponse > validateCodeCall = RestController.getInstance()
                        .getApi().sendSmsCode( AppConstants.AUTH_BEARER
                                + GlobalManager.getInstance().getUserToken(), phone[0] );
                Response< ApiResponse > responseCodeValidate = validateCodeCall.execute();
                if ( responseCodeValidate.body() != null ) {
                    if ( responseCodeValidate.body().getStatus() == 200 ) {
                        mConfirmationCode = ( String ) responseCodeValidate.body().getResult();
//                        if ( AppConstants.SEND_PHONE_CODE.equals( mConfirmationCode ) ) {
//                            mConfirmationCode = AppUtils.getRandomBetweenRange( 4000, 9999 ) + "";
//                            SmsManager.getDefault().sendTextMessage( "+7" + mClientModel.getPhone(),
//                                    "ЕдаНяма", "Код регистрации: " + mConfirmationCode, null, null );
//                        }
                    } else {
                        result = responseCodeValidate.body().getMessage();
                    }
                } else {
                    result = getResources().getString( R.string.internal_error );
                }
            } catch ( Exception e ) {
                result = getResources().getString( R.string.internal_error );
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute( String result ) {
            super.onPostExecute( result );
            if ( result != null ) {
//                AppUtils.transitionAnimation( getView().findViewById( R.id.pleaseWaitContainerId ),
//                        getView().findViewById( R.id.signUpContailnerId ) );
                final TextView errorView = initTextView( R.id.checkOutCommentErrorId, AppConstants.ROBOTO_CONDENCED );
                errorView.setText( result );
                errorView.setVisibility( View.VISIBLE );
                ( new Handler() ).postDelayed( new Runnable() {
                    @Override
                    public void run() {
                        errorView.setVisibility( View.GONE );
                    }
                }, 2000 );
            } else {
                AppUtils.transitionAnimation( getView().findViewById( R.id.checkOutScrollId ),
                        getView().findViewById( R.id.confirmCodeContainerId ) );
                startCountdown();
            }
        }
    }
}
