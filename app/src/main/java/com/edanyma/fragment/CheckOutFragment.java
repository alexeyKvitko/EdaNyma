package com.edanyma.fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edanyma.AppConstants;
import com.edanyma.R;
import com.edanyma.manager.BasketOrderManager;
import com.edanyma.manager.GlobalManager;
import com.edanyma.model.BasketModel;
import com.edanyma.model.MenuEntityModel;
import com.edanyma.owncomponent.CheckOutEntity;
import com.edanyma.owncomponent.CompanyTotalView;
import com.edanyma.utils.ConvertUtils;

import java.util.HashMap;
import java.util.Map;


public class CheckOutFragment extends BaseFragment implements View.OnClickListener,
    CheckOutEntity.OnRemoveFromBasketListener{

    private OnCheckOutFragmentListener mListener;

    private LinearLayout mDishContainer;

    private AppCompatEditText mCheckOutPerson;
    private AppCompatEditText mCheckOutPhone;

    private AppCompatEditText mCheckOutCity;
    private AppCompatEditText mCheckOutStreet;
    private AppCompatEditText mCheckOutHouse;

    private TextView mCheckOutCommonTotal;

    private Map<String,BasketModel> mFilteredBasket;
    private Integer mTotalAmount;

    public CheckOutFragment() {}

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
        initTextView( R.id.checkOutLabelId, AppConstants.B52 );
        initTextView( R.id.contactInfoTitleId, AppConstants.B52 );
        initTextView( R.id.deliveryAddressTitleId, AppConstants.B52 );
        initTextInputLayout( R.id.checkOutPersonTextLayoutId , AppConstants.ROBOTO_CONDENCED );
        initTextInputLayout( R.id.checkOutPhoneLayoutId , AppConstants.ROBOTO_CONDENCED );
        initTextInputLayout( R.id.checkOutCityTextLayoutId , AppConstants.ROBOTO_CONDENCED );
        initTextInputLayout( R.id.checkOutStreeTextLayoutId , AppConstants.ROBOTO_CONDENCED );
        initTextInputLayout( R.id.checkOutHouseTextLayoutId , AppConstants.ROBOTO_CONDENCED );

        mCheckOutCommonTotal = initTextView( R.id.checkOutCommonSumId,
                                                        AppConstants.OFFICE, Typeface.BOLD, null );

        mCheckOutPerson = initEditText( R.id.checkOutPersonId , AppConstants.ROBOTO_CONDENCED );
        mCheckOutPhone = initEditText( R.id.checkOutPhoneId , AppConstants.ROBOTO_CONDENCED );
        mCheckOutPhone.addTextChangedListener( new PhoneNumberFormattingTextWatcher() );

        mCheckOutCity = initEditText( R.id.checkOutCityId , AppConstants.ROBOTO_CONDENCED );
        mCheckOutStreet = initEditText( R.id.checkOutStreetId , AppConstants.ROBOTO_CONDENCED );
        mCheckOutHouse = initEditText( R.id.checkOutHouseId , AppConstants.ROBOTO_CONDENCED );

        getView().findViewById( R.id.checkOutBackBtnId ).setOnClickListener( this );
        mDishContainer = getView().findViewById( R.id.checkOutDishContainerId );
        fillDishContainer();
    }

    private void filterBasket(){
        mFilteredBasket = new HashMap<>( );
        mTotalAmount = 0;
        for(MenuEntityModel dish: BasketOrderManager.getInstance().getBasket() ){
            BasketModel basketModel = mFilteredBasket.get( dish.getCompanyId() );
            if ( basketModel == null ){
                basketModel = new BasketModel();
                basketModel.setCompany( GlobalManager.getInstance().getCompanyById( dish.getCompanyId() ));
            }
            basketModel.getBasket().add( dish );
            mFilteredBasket.put( dish.getCompanyId(), basketModel );
        }
        if( mFilteredBasket.size() == 0 ){
            getActivity().onBackPressed();
        }
        for(BasketModel basketModel : mFilteredBasket.values() ){
            mTotalAmount += basketModel.getPrice();
        }
        mCheckOutCommonTotal.setText( mTotalAmount.toString() );
    }

    private void fillDishContainer(){
        filterBasket();
        mDishContainer.removeAllViews();
        for(BasketModel basketModel : mFilteredBasket.values() ){
            mDishContainer.addView( createCompanyTitle( basketModel.getCompany().getDisplayName() ) );
            CompanyTotalView companyTotal = createCompanyTotal( basketModel.getCompany().getId(), basketModel.getPrice() );
            for(MenuEntityModel dish : basketModel.getBasket() ){
                mDishContainer.addView( createCheckOutDishEntity( dish, companyTotal ) );
            }
            mDishContainer.addView( companyTotal );
        }
    }

    private CheckOutEntity createCheckOutDishEntity( MenuEntityModel menuEntityModel, CompanyTotalView companyTotal){
        CheckOutEntity checkOutEntity =  new CheckOutEntity( getContext() );
        checkOutEntity.setDishEntity( menuEntityModel, companyTotal );
        checkOutEntity.setOnRemoveFromBasketListener( this );
        return checkOutEntity;
    }

    private TextView createCompanyTitle( String companyName ){
        TextView companyTitle = new TextView( getContext() );
        companyTitle.setTypeface( AppConstants.B52 );
        companyTitle.setTextColor( getActivity().getResources().getColor( R.color.iconGrayColor ) );
        companyTitle.setTextSize( TypedValue.COMPLEX_UNIT_DIP, 18 );
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT );
        layoutParams.leftMargin = (int ) ConvertUtils.convertDpToPixel( 42 );
        layoutParams.bottomMargin = (int ) ConvertUtils.convertDpToPixel( 8 );

        companyTitle.setLayoutParams( layoutParams );
        companyTitle.setText( companyName );
        return companyTitle;
    }

    private CompanyTotalView createCompanyTotal( String companyId, Integer companyTotal ){
        CompanyTotalView companyTotalView =  new CompanyTotalView( getContext(),companyId,companyTotal );
        return  companyTotalView;
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

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick( View view ) {
        switch ( view.getId() ){
            case  R.id.checkOutBackBtnId :
                getActivity().onBackPressed();
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


    public interface OnCheckOutFragmentListener {
        // TODO: Update argument type and name
        void onCheckOut();
    }
}
