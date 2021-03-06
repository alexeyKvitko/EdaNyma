package com.edanyma.fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edanyma.AppConstants;
import com.edanyma.R;
import com.edanyma.activity.BasketActivity;
import com.edanyma.manager.BasketOrderManager;
import com.edanyma.model.ApiResponse;
import com.edanyma.model.BasketModel;
import com.edanyma.model.ClientLocationModel;
import com.edanyma.model.ClientOrderModel;
import com.edanyma.model.ClientOrderResponse;
import com.edanyma.model.MenuEntityModel;
import com.edanyma.model.OrderStatus;
import com.edanyma.model.OurClientModel;
import com.edanyma.model.PayType;
import com.edanyma.owncomponent.CheckOutEntity;
import com.edanyma.owncomponent.CompanyTotalView;
import com.edanyma.owncomponent.ModalMessage;
import com.edanyma.owncomponent.OwnRadioGroup;
import com.edanyma.owncomponent.PayTypeSelector;
import com.edanyma.rest.RestController;
import com.edanyma.utils.AppUtils;
import com.edanyma.utils.ConvertUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;

import static com.edanyma.manager.GlobalManager.*;


public class CheckOutFragment extends ConfirmFragment implements View.OnClickListener,
        CheckOutEntity.OnRemoveFromBasketListener {

    private OnCheckOutFragmentListener mListener;

    private LinearLayout mDishContainer;

    private AppCompatEditText mCheckOutPerson;
    private AppCompatEditText mCheckOutPhone;
    private AppCompatEditText mCheckOutСomment;

    private CheckOutFragment mThis;


    private TextView mCheckOutCommonTotal;
    private TextView mCheckOutCity;
    private TextView mCheckOutAddress;
    private TextView mCheckOutAdditionalAddress;

    private TextView mFinishOrderNumber;
    private TextView mFinishOrderAddress;
    private TextView mFinishOrderDay;
    private TextView mFinishOrderTime;
    private TextView mContactInfo;

    private OwnRadioGroup mPayTypeSelector;

    private TextView mFinishOrderStatus;
    private Button mFinishOrderButton;

    private Button mCheckOutBtn;

    private ClientOrderModel mClientOrderModel;

    private PayType mSelectedPayType;

    private boolean mIsClientInfoSet;

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
        mThis = this;
        mSelectedPayType = PayType.CASH;
        mResendLabel.setOnClickListener( this );
        mConfirmCodeBtn.setOnClickListener( this );
        initTextView( R.id.checkOutLabelId, AppConstants.B52 );
        initTextView( R.id.contactInfoTitleId, AppConstants.B52 );
        initTextView( R.id.deliveryAddressTitleId, AppConstants.B52 );
        initTextView( R.id.checkOutPayTypeId, AppConstants.B52 );
        initTextView( R.id.finishTitleRowTwoId, AppConstants.B52 );
        initTextView( R.id.finishTitleRowOneId, AppConstants.B52 );
        initTextView( R.id.finishTitleRowThreeId, AppConstants.OFFICE );

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
                getBootstrapModel().getDeliveryCity() );
        mCheckOutAddress = initTextView( R.id.checkOutStreetHouseId, AppConstants.ROBOTO_CONDENCED,
                getActivity().getResources().getString( R.string.not_available_yet ) );
        mCheckOutAdditionalAddress = initTextView( R.id.checkOutAdditionalAddressId, AppConstants.ROBOTO_CONDENCED,
                getActivity().getResources().getString( R.string.not_available_yet ) );

        mContactInfo = initTextView( R.id.contactInfoBtnId );
        mIsClientInfoSet = false;
        if ( !isSignedIn() ) {
            mContactInfo.setVisibility( View.GONE );
        }

        mPayTypeSelector = getView().findViewById( R.id.checkOutPayTypeContainerId );
        mPayTypeSelector.setOnPayTypeSelectListener( ( PayType payType ) -> {
            mSelectedPayType = payType;
        } );

        mCheckOutBtn = initButton( R.id.checkOutSuccessBtnId, AppConstants.ROBOTO_CONDENCED );
        mCheckOutBtn.setOnClickListener( this );
        getView().findViewById( R.id.confirmCodeContainerId ).setOnClickListener( null );
        mDishContainer = getView().findViewById( R.id.checkOutDishContainerId );
        setThisOnClickListener( R.id.checkOutBackBtnId, R.id.checkOutMapId, R.id.contactInfoBtnId );
        initFinishOrderContainer();
        setDeliveryAddress( getClientLocation() );
        fillDishContainer();
    }


    private void initFinishOrderContainer() {
        initTextView( R.id.finishOrderNuberDescId, AppConstants.ROBOTO_CONDENCED );
        initTextView( R.id.foDeliveryAddressLabelId, AppConstants.ROBOTO_CONDENCED );
        initTextView( R.id.foDeliveryDateLabelId, AppConstants.ROBOTO_CONDENCED );
        initTextView( R.id.foDeliveryTimeLabelId, AppConstants.ROBOTO_CONDENCED );
        initTextView( R.id.foDeliveryStatusLabelId, AppConstants.ROBOTO_CONDENCED );
        mFinishOrderStatus = initTextView( R.id.foDeliveryStatusValueId, AppConstants.B52 );
        mFinishOrderButton = initButton( R.id.finishOrderContinueBtnId, AppConstants.ROBOTO_CONDENCED );
        mFinishOrderButton.setOnClickListener( this );

        mFinishOrderNumber = initTextView( R.id.finishOrderNumberId, AppConstants.OFFICE );
        mFinishOrderAddress = initTextView( R.id.foDeliveryAddressValueId, AppConstants.B52 );
        mFinishOrderDay = initTextView( R.id.foDeliveryDayValueId, AppConstants.B52 );
        mFinishOrderTime = initTextView( R.id.foDeliveryTimeValueId, AppConstants.B52 );
    }

    private void setClientInfo() {
        OurClientModel client = mIsClientInfoSet ? new OurClientModel() : getClient();
        ClientLocationModel location = mIsClientInfoSet ? getClientLocation() : getClient().getClientLocationModel();
        int iconId = mIsClientInfoSet ? R.drawable.ic_account_off_outline_grey600_24dp
                                        : R.drawable.ic_account_outline_blue_24dp;
        mContactInfo.setCompoundDrawablesWithIntrinsicBounds( 0, 0, iconId, 0 );
        mCheckOutPerson.setText( client.getNickName() );
        mCheckOutPhone.setText( client.getPhone() );
        mPayTypeSelector.setPayType( client.getPayType() );
        mSelectedPayType = ConvertUtils.convertStringToPayType( client.getPayType() );
        setDeliveryAddress( location );
        mIsClientInfoSet = !mIsClientInfoSet;
    }

    private void setDeliveryAddress( ClientLocationModel location ) {
        if ( !AppUtils.nullOrEmpty( location.getStreet() ) ) {
            String address = location.getStreet();
            if ( !AppUtils.nullOrEmpty( location.getHouse() ) ) {
                address = address + ", " + location.getHouse();
            }
            mCheckOutAddress.setText( address );
        }
        StringBuilder additionalAddress = new StringBuilder();
        if ( !AppUtils.nullOrEmpty( location.getEntrance() ) ) {
            additionalAddress.append( getActivity().getResources().getString( R.string.entrace_label ) + ", " )
                    .append( location.getEntrance() ).append( ", " );
        }
        if ( !AppUtils.nullOrEmpty( location.getIntercom() ) ) {
            additionalAddress.append( getActivity().getResources().getString( R.string.intercom_label ) + ", " )
                    .append( location.getIntercom() ).append( ", " );
        }
        if ( !AppUtils.nullOrEmpty( location.getFloor() ) ) {
            additionalAddress.append( getActivity().getResources().getString( R.string.floor_label ) + ", " )
                    .append( location.getFloor() ).append( ", " );
        }
        if ( additionalAddress.length() > 0 ) {
            additionalAddress.delete( additionalAddress.length() - 2, additionalAddress.length() );
            mCheckOutAdditionalAddress.setText( additionalAddress.toString() );
            mCheckOutAdditionalAddress.setVisibility( View.VISIBLE );
        } else {
            mCheckOutAdditionalAddress.setVisibility( View.GONE );
        }
    }

    private void filterBasket() {
        mFilteredBasket = new HashMap<>();
        mTotalAmount = 0;
        for ( MenuEntityModel dish : BasketOrderManager.getBasket() ) {
            BasketModel basketModel = mFilteredBasket.get( dish.getCompanyId() );
            if ( basketModel == null ) {
                basketModel = new BasketModel();
                basketModel.setCompany( getCompanyById( dish.getCompanyId() ) );
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

    private void clickResendCode( View view ) {
        AppUtils.clickAnimation( view );
        new Handler().postDelayed( new Runnable() {
            @Override
            public void run() {
                new ValidateCheckoutCode().execute( mCheckOutPhone.getText().toString() );
            }
        }, 300 );
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

    private void finishCheckOut() {
        String person = mCheckOutPerson.getText().toString();
        String phone = mCheckOutPhone.getText().toString();
        String comment = mCheckOutСomment.getText().toString();
        String errorMsg = null;
        TextView personError = initTextView( R.id.checkOutPersonErrorId, AppConstants.ROBOTO_CONDENCED );
        TextView phoneError = initTextView( R.id.checkOutPhoneErrorId, AppConstants.ROBOTO_CONDENCED );
        if ( person.length() == 0 ) {
            errorMsg = getResources().getString( R.string.error_required_field );
            personError.setText( errorMsg );
            personError.setVisibility( View.VISIBLE );
        }
        if ( errorMsg == null && phone.length() == 0 ) {
            errorMsg = getResources().getString( R.string.error_required_field );
        }
        if ( errorMsg == null && !AppUtils.validatePhone( phone ) ) {
            errorMsg = getResources().getString( R.string.error_wrong_phone );
        }
        if ( errorMsg != null ) {
            if ( personError.getVisibility() == View.GONE ) {
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
            mCheckOutBtn.setOnClickListener( this );
        }
        if ( errorMsg == null ) {
            mClientOrderModel = new ClientOrderModel();
            mClientOrderModel.setNickName( person );
            if ( isSignedIn() ) {
                mClientOrderModel.setEmail( getClient().getEmail() );
                mClientOrderModel.setClientUuid( getClient().getUuid() );
            }
            ClientLocationModel clientLocationModel = getClientLocation();
            mClientOrderModel.setPhone( phone );
            mClientOrderModel.setOrderDate( AppUtils.formatDate( AppConstants.ORDER_DATE_FORMAT, new Date() ) );
            mClientOrderModel.setOrderTime( AppUtils.formatDate( AppConstants.ORDER_TIME_FORMAT, new Date() ) );
//            mClientOrderModel.setOrderPrice( mTotalAmount );
            mClientOrderModel.setOrderPrice( 27 );
            mClientOrderModel.setOrderStatus( OrderStatus.IN_PROGRESS.name() );
            mClientOrderModel.setCity( clientLocationModel.getCity() );
            mClientOrderModel.setStreet( clientLocationModel.getStreet() );
            mClientOrderModel.setBuilding( clientLocationModel.getHouse() );
            //TODO if need
            mClientOrderModel.setFlat( null );
            mClientOrderModel.setFloor( clientLocationModel.getFloor() );
            mClientOrderModel.setEntry( clientLocationModel.getEntrance() );
            mClientOrderModel.setIntercom( clientLocationModel.getIntercom() );
            //TODO if need
            mClientOrderModel.setNeedChange( null );
            mClientOrderModel.setComment( comment );

            mClientOrderModel.setPayType( mSelectedPayType.name() );
            for ( BasketModel basketModel : mFilteredBasket.values() ) {
                mClientOrderModel.getOrders().add( basketModel );
            }
            new ValidateCheckoutCode().execute( phone );
        }
    }

    private void confirmCheckOutSend() {
        if ( checkEnteredCode() ) {
            new CreateClientOrder().execute( mClientOrderModel );
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
                AppUtils.clickAnimation( view );
                mCheckOutBtn.setOnClickListener( null );
                finishCheckOut();
                break;
            case R.id.checkOutMapId:
                if ( mListener != null ) {
                    AppUtils.clickAnimation( view );
                    mListener.onShowMapClick();
                }
                break;
            case R.id.resendCodeLabelId:
                clickResendCode( view );
                break;
            case R.id.contactInfoBtnId:
                setClientInfo();
                break;
            case R.id.confirmCodeButtonId:
                AppUtils.hideKeyboardFrom( getActivity(), view );
                confirmCheckOutSend();
                break;
            case R.id.finishOrderContinueBtnId:
                String payOnlineUrl = null;
                if( !PayType.CASH.name().equals( mClientOrderModel.getPayType() ) ){
                    payOnlineUrl = mClientOrderModel.getPayUrl();
                }
                (( BasketActivity ) getActivity() ).setPayOnlineUrl( payOnlineUrl );
                getActivity().onBackPressed();
                break;
        }
    }


    @Override
    public void onRemoveFromBasket( MenuEntityModel dishEntity ) {
        BasketOrderManager.removeEntityFromBasket( dishEntity.getId() );
        fillDishContainer();
    }

    @Override
    public void onChangeEntityCount( Integer delta ) {
        mTotalAmount += delta;
        mCheckOutCommonTotal.setText( mTotalAmount.toString() );
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
                Call< ApiResponse > validateCodeCall = RestController
                        .getApi().sendSmsCode( AppConstants.AUTH_BEARER
                                + getUserToken(), phone[ 0 ] );
                Response< ApiResponse > responseCodeValidate = validateCodeCall.execute();
                if ( responseCodeValidate.body() != null ) {
                    if ( responseCodeValidate.body().getStatus() == 200 ) {
                        mConfirmationCode = responseCodeValidate.body().getMessage();
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
            mCheckOutBtn.setOnClickListener( mThis );
            if ( result != null ) {

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
                startCountdown( R.id.checkOutScrollId );
            }
        }
    }

    private class CreateClientOrder extends AsyncTask< ClientOrderModel, Void, ClientOrderResponse > {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ClientOrderResponse doInBackground( ClientOrderModel... clientOrder ) {
            ClientOrderResponse orderResponse = null;
            try {
                Call< ApiResponse< ClientOrderResponse > > createOrderCall = RestController
                        .getApi().createClientOrder( AppConstants.AUTH_BEARER
                                + getUserToken(), clientOrder[ 0 ] );
                Response< ApiResponse< ClientOrderResponse > > responseCreateOrder = createOrderCall.execute();
                if ( responseCreateOrder.body() != null ) {
                    if ( responseCreateOrder.body().getStatus() == 200 ) {
                        orderResponse =  responseCreateOrder.body().getResult();
                    }
                }
            } catch ( Exception e ) {
                e.printStackTrace();
            }
            return orderResponse;
        }

        @Override
        protected void onPostExecute( ClientOrderResponse orderResponse ) {
            super.onPostExecute( orderResponse );
            String result = orderResponse == null ? getActivity().getResources().getString( R.string.internal_error ) : null ;
            if ( result != null ) {
                AppUtils.transitionAnimation( getView().findViewById( R.id.confirmCodeContainerId ),
                        getView().findViewById( R.id.checkOutScrollId ) );
                ModalMessage.show( getActivity(), "Сообщение", new String[]{ result } );
            } else {
                StringBuilder sb = new StringBuilder();
                if ( mClientOrderModel.getCity() != null ) {
                    sb.append( mClientOrderModel.getCity() );
                }
                if ( mClientOrderModel.getStreet() != null ) {
                    sb.append( ", " ).append( mClientOrderModel.getStreet() );
                }
                if ( mClientOrderModel.getBuilding() != null ) {
                    sb.append( ", " ).append( mClientOrderModel.getBuilding() );
                }
                mClientOrderModel.setId( orderResponse.getOrderId() );
                mClientOrderModel.setPayUrl( orderResponse.getPayUrl() );
                mFinishOrderNumber.setText( orderResponse.getOrderId().toString() );
                mFinishOrderAddress.setText( sb.toString() );
                mFinishOrderDay.setText( mClientOrderModel.getOrderDate() );
                mFinishOrderTime.setText( mClientOrderModel.getOrderTime() );
                if ( !PayType.CASH.name().equals( mClientOrderModel.getPayType() ) ){
                    mFinishOrderButton.setText( getActivity().getResources().getString( R.string.pay_button ) );
                    mFinishOrderStatus.setText( getActivity().getResources().getString( R.string.status_wait_payment ) );
                    getView().findViewById( R.id.expectedPayId ).setVisibility( View.VISIBLE );
                }
                BasketOrderManager.clearBasket();
                AppUtils.transitionAnimation( getView().findViewById( R.id.confirmCodeContainerId ),
                        getView().findViewById( R.id.finishOrderContainerId ) );
            }

        }
    }
}
