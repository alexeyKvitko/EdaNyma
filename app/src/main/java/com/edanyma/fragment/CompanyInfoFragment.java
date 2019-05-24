package com.edanyma.fragment;

import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.edanyma.AppConstants;
import com.edanyma.EdaNymaApp;
import com.edanyma.R;
import com.edanyma.activity.BaseActivity;
import com.edanyma.activity.CompanyDishActivity;
import com.edanyma.manager.GlobalManager;
import com.edanyma.model.ApiResponse;
import com.edanyma.model.CompanyModel;
import com.edanyma.model.FavoriteCompanyModel;
import com.edanyma.owncomponent.ModalMessage;
import com.edanyma.rest.RestController;
import com.edanyma.utils.AppUtils;
import com.edanyma.utils.ConvertUtils;
import com.edanyma.utils.GlideClient;

import java.util.LinkedList;

import retrofit2.Call;
import retrofit2.Response;

import static com.edanyma.manager.GlobalManager.*;


public class CompanyInfoFragment extends BaseFragment implements View.OnClickListener {

    private final String TAG = "CompanyInfoFragment";

    private static final String COMPANY_MODEL = "company_model";

    private static final String ADD_TO_FAV = "ADD_TO_FAV";
    private static final String REMOVE_FROM_FAV = "REMOVE_FROM_FAV";

    private CompanyModel mCompanyModel;
    private Integer mCompanyId;
    private Integer mClientId;

    private OnSignActionListener mListener;

    private Handler mShowFavoriteHandler;
    private Runnable mShowFavoriteJob;
    private Handler mShowReviewJob;
    private ImageView mAddToFavorite;
    private FavoriteCompanyModel mFavoriteCompany;

    public CompanyInfoFragment() {
    }


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
        GlideClient.downloadImage( getActivity(), getBootstrapModel()
                .getStaticUrl() + String.format( AppConstants.STATIC_COMPANY_LOGO,
                mCompanyModel.getThumb() ),  getView().findViewById( R.id.companyInfoLogoId ) );

        initTextView( R.id.companyInfoReviewCountId, AppConstants.ROBOTO_CONDENCED,
                mCompanyModel.getCommentCount() );
        initTextView( R.id.companyInfoCityId, AppConstants.ROBOTO_CONDENCED,
                getBootstrapModel().getDeliveryCity() );
        initTextView( R.id.companyInfoDeliPriceId, AppConstants.ROBOTO_CONDENCED,
                "от " + mCompanyModel.getDelivery().toString() + " р." );

        initTextView( R.id.companyInfoDeliTimeId, AppConstants.ROBOTO_CONDENCED,
                "от " + mCompanyModel.getDeliveryTimeMin().toString() + " мин." );

        initTextView( R.id.companyInfoWeekTimeId, AppConstants.ROBOTO_CONDENCED,
                mCompanyModel.getWeekdayWork() );

        initTextView( R.id.companyInfoHolyTimeId, AppConstants.ROBOTO_CONDENCED,
                mCompanyModel.getDayoffWork() );

        initTextView( R.id.companyInfoAllReviewId, AppConstants.ROBOTO_CONDENCED );
        initTextView( R.id.addToFavErrorTextId, AppConstants.ROBOTO_CONDENCED );
        initTextView( R.id.companyInfoDeliHouseId, AppConstants.B52 );
        initTextView( R.id.companyInfoPayTypeId, AppConstants.B52 );

        if ( mCompanyModel.getPayTypeCash().equals( 0 ) ) {
            getView().findViewById( R.id.companyInfoMoneyId ).setVisibility( View.GONE );
        }
        if ( mCompanyModel.getPayTypeCard().equals( 0 ) ) {
            getView().findViewById( R.id.companyInfoCardId ).setVisibility( View.GONE );
        }
        if ( mCompanyModel.getPayTypeWallet().equals( 0 ) ) {
            getView().findViewById( R.id.companyInfoWalletId ).setVisibility( View.GONE );
        }

        mAddToFavorite = getView().findViewById( R.id.addToFavButtonId );
        mAddToFavorite.setOnClickListener( this );
        getView().findViewById( R.id.dishInfoContainerId ).setOnClickListener( this );
        getView().findViewById( R.id.favoriteSignInId ).setOnClickListener( this );
        getView().findViewById( R.id.favoriteSignUpId ).setOnClickListener( this );
    }

    private void addToFavorite( final View view ) {
        AppUtils.clickAnimation( view );
        if ( getClient() == null ) {
            mShowFavoriteHandler = new Handler( );
            mShowFavoriteJob = new Runnable(){
                @Override
                public void run() {
                    animateFavoriteContainer( ( int ) ConvertUtils.convertDpToPixel( 104 ), 0, null );
                    mShowFavoriteHandler.removeCallbacks( mShowFavoriteJob );
                    mShowFavoriteHandler = null;
                }
            };
            mShowFavoriteHandler.postDelayed( mShowFavoriteJob, 5000 );
            animateFavoriteContainer( 0, ( int ) ConvertUtils.convertDpToPixel( 104 ) , null );
        } else {
            postFavoriteAction();
        }
    }

    private void animateFavoriteContainer( int startPos, final int endPos , final String sign ){
        final View signLayout = getView().findViewById( R.id.addToFavErrorContainerId );
        final LinearLayout.LayoutParams layoutParams = ( LinearLayout.LayoutParams ) signLayout.getLayoutParams();
        ValueAnimator valAnimator = ValueAnimator.ofObject( new IntEvaluator(), startPos, endPos );
        valAnimator.addUpdateListener( new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate( ValueAnimator animator ) {
                int val = ( Integer ) animator.getAnimatedValue();
                layoutParams.height = val;
                signLayout.setLayoutParams( layoutParams );
                if( sign != null && val == endPos ){
                    mListener.onSignAction( sign );
                }
            }
        } );
        signLayout.setVisibility( View.VISIBLE );
        valAnimator.setDuration( 300 );
        valAnimator.start();
    }

    private void signAction(View view, String sign ){
        AppUtils.clickAnimation( view );
        animateFavoriteContainer( ( int ) ConvertUtils.convertDpToPixel( 104 ), 0, sign );
    }

    private void postFavoriteAction(){
       new AddRemoveFavorites().execute( );
    }

    private void changeFavoriteIcon(){
        if ( mFavoriteCompany !=  null ){
            mAddToFavorite.setImageDrawable( getActivity().getResources().getDrawable( R.drawable.ic_favorite_enable_24dp ) );
        } else {
            mAddToFavorite.setImageDrawable( getActivity().getResources().getDrawable( R.drawable.ic_favorite_disable_24dp ) );
        }
    }

    @Override
    public void onAttach( Context context ) {
        super.onAttach( context );
        if ( context instanceof OnSignActionListener ) {
            mListener = ( OnSignActionListener ) context;
        } else {
            throw new RuntimeException( context.toString()
                    + " must implement OnSignActionListener" );
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ( ( CompanyDishActivity ) getActivity() ).setHeaderFooterVisibilty( View.VISIBLE );
        getActivity().findViewById( R.id.dishContainerId )
                .setBackground( EdaNymaApp.getAppContext().getResources()
                        .getDrawable( R.drawable.main_background_light ) );

        mListener = null;
    }

    @Override
    public void onPause() {
        if( mShowFavoriteHandler != null ){
            mShowFavoriteHandler.removeCallbacks( mShowFavoriteJob );
            mShowFavoriteHandler = null;
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mFavoriteCompany = null;
        mCompanyId = Integer.valueOf( mCompanyModel.getId() );
        mClientId = getClient() != null ? getClient().getId() : null;
        if( isSignedIn() ){
            for ( FavoriteCompanyModel favoriteCompanyModel :
                    getClient().getFavoriteCompanies() ){
                if(  favoriteCompanyModel.getCompanyId().equals( mCompanyId ) ){
                    mFavoriteCompany = favoriteCompanyModel;
                    break;
                }
            }
        }
        changeFavoriteIcon();
        if ( isActionConfirmed() && mFavoriteCompany == null ){
            postFavoriteAction();
        }
        setActionConfirmed( false );
    }



    @Override
    public void onClick( View view ) {
        switch ( view.getId() ) {
            case R.id.dishInfoContainerId:
                getActivity().onBackPressed();
                break;
            case R.id.addToFavButtonId:
                addToFavorite( view );
                break;
            case R.id.favoriteSignInId:
                signAction( view, AppConstants.SIGN_IN );
                break;
            case R.id.favoriteSignUpId:
                signAction( view, AppConstants.SIGN_UP );
                break;
        }
    }

    public interface OnSignActionListener {
        void onSignAction( String sign );
    }

    private class AddRemoveFavorites extends AsyncTask< Void, Void, String > {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground( Void... arg0 ) {
            FavoriteCompanyModel favoriteCompanyModel = new FavoriteCompanyModel();
            String favAction = getActivity().getResources().getString( R.string.added_to_favorite );
            try {
                Call< ApiResponse > favoriteCall = null;
                ApiResponse response = null;
                if ( mFavoriteCompany == null ){
                    favoriteCompanyModel = new FavoriteCompanyModel();
                    favoriteCompanyModel.setCompanyId( mCompanyId  );
                    favoriteCompanyModel.setClientId( mClientId );
                    favoriteCall = RestController
                            .getApi().addToFavorite( AppConstants.AUTH_BEARER
                            + getUserToken(), favoriteCompanyModel );
                }  else {
                    favoriteCall = RestController
                            .getApi().removeFromFavorite( AppConstants.AUTH_BEARER
                                    + getUserToken(), mFavoriteCompany );
                    favAction = getActivity().getResources().getString( R.string.removed_from_favorite );
                }

                Response< ApiResponse > responseFavorite = favoriteCall.execute();
                if ( responseFavorite.body() != null ) {
                    response = responseFavorite.body();
                    if ( AppConstants.HTTP_OK == response.getStatus() ){
                        if ( getClient().getFavoriteCompanies() == null ){
                            getClient()
                                    .setFavoriteCompanies( new LinkedList< FavoriteCompanyModel >() );
                        }
                        if ( getActivity().getResources().getString( R.string.added_to_favorite ).equals( favAction ) ){
                            mFavoriteCompany = favoriteCompanyModel;
                            mFavoriteCompany.setId( Integer.valueOf( response.getMessage() ) );
                            addToFavorites( mFavoriteCompany );
                        } else {
                            removeFromFavorites( mFavoriteCompany.getCompanyId() );
                            mFavoriteCompany = null;
                        }
                    } else {
                        favAction = null;
                    }
                } else {
                    favAction = null;
                }
            } catch ( Exception e ) {
                Log.e( TAG, e.getMessage() );
                favAction = null;
                e.printStackTrace();
            }
            return favAction;
        }

        @Override
        protected void onPostExecute( String result ) {
            super.onPostExecute( result );
            ModalMessage.show( getActivity(), "Сообщение", new String[] {result} );
            changeFavoriteIcon();
        }
    }
}
