package com.edanyma.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.edanyma.AppConstants;
import com.edanyma.EdaNymaApp;
import com.edanyma.R;
import com.edanyma.activity.BaseActivity;
import com.edanyma.manager.GlobalManager;
import com.edanyma.model.CompanyModel;
import com.edanyma.utils.PicassoClient;


public class CompanyInfoFragment extends BaseFragment {

    private static final String COMPANY_MODEL = "company_model";

    private CompanyModel mCompanyModel;

    public CompanyInfoFragment() {}


    public static CompanyInfoFragment newInstance( CompanyModel companyModel ) {
        CompanyInfoFragment fragment = new CompanyInfoFragment();
        Bundle args = new Bundle();
        args.putSerializable( COMPANY_MODEL, companyModel );
        fragment.setArguments( args );
        return fragment;
    }

    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        if ( getArguments() != null ) {
            mCompanyModel = ( CompanyModel ) getArguments().getSerializable( COMPANY_MODEL );
        }
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState ) {
        return inflater.inflate( R.layout.fragment_company_info, container, false );
    }


    @Override
    public void onActivityCreated( @Nullable Bundle savedInstanceState ) {
        super.onActivityCreated( savedInstanceState );
        initTextView( R.id.companyInfoTitleId, AppConstants.B52,
                mCompanyModel.getDisplayName() );
        PicassoClient.downloadImage( getActivity(), GlobalManager.getInstance().getBootstrapModel()
                .getStaticUrl() + String.format( AppConstants.STATIC_COMPANY_LOGO,
                mCompanyModel.getThumb() ), ( ImageView ) getView().findViewById( R.id.companyInfoLogoId ) );

        initTextView( R.id.companyInfoReviewCountId , AppConstants.ROBOTO_CONDENCED ,
                mCompanyModel.getCommentCount() );
        initTextView( R.id.companyInfoCityId , AppConstants.ROBOTO_CONDENCED ,
                GlobalManager.getInstance().getBootstrapModel().getDeliveryCity() );
        initTextView( R.id.companyInfoDeliPriceId, AppConstants.ROBOTO_CONDENCED ,
                "от " + mCompanyModel.getDelivery().toString() + " р." );

        initTextView( R.id.companyInfoDeliTimeId , AppConstants.ROBOTO_CONDENCED ,
                "от " + mCompanyModel.getDeliveryTimeMin().toString() + " мин." );

        initTextView( R.id.companyInfoWeekTimeId , AppConstants.ROBOTO_CONDENCED ,
                mCompanyModel.getWeekdayWork() );

        initTextView( R.id.companyInfoHolyTimeId , AppConstants.ROBOTO_CONDENCED ,
                mCompanyModel.getDayoffWork() );

        initTextView( R.id.companyInfoAllReviewId , AppConstants.ROBOTO_CONDENCED );
        initTextView( R.id.addToFavErrorTextId , AppConstants.ROBOTO_CONDENCED );
        initTextView( R.id.companyInfoDeliHouseId , AppConstants.B52 );
        initTextView( R.id.companyInfoPayTypeId , AppConstants.B52 );

        if( mCompanyModel.getPayTypeCash().equals( 0 ) ){
            getView().findViewById( R.id.companyInfoMoneyId ).setVisibility( View.GONE );
        }
        if( mCompanyModel.getPayTypeCard().equals( 0 ) ){
            getView().findViewById( R.id.companyInfoCardId ).setVisibility( View.GONE );
        }
        if( mCompanyModel.getPayTypeWallet().equals( 0 ) ){
            getView().findViewById( R.id.companyInfoWalletId ).setVisibility( View.GONE );
        }
        getView().findViewById( R.id.dishInfoContainerId ).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                getActivity().onBackPressed();
            }
        } );
    }

    @Override
    public void onAttach( Context context ) {
        super.onAttach( context );
    }

    @Override
    public void onDetach() {
        super.onDetach();
        (( BaseActivity ) getActivity()).getHeader().setVisibility( View.VISIBLE );
        (( BaseActivity) getActivity()).getFooter().setVisibility( View.VISIBLE );
        getActivity().findViewById( R.id.dishContainerId )
                .setBackground( EdaNymaApp.getAppContext().getResources()
                        .getDrawable( R.drawable.main_background_light ) );
    }

}
