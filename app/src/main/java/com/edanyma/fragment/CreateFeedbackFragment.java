package com.edanyma.fragment;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

import com.edanyma.AppConstants;
import com.edanyma.R;
import com.edanyma.activity.BaseActivity;
import com.edanyma.model.ApiResponse;
import com.edanyma.model.CompanyModel;
import com.edanyma.model.FeedbackModel;
import com.edanyma.owncomponent.ModalDialog;
import com.edanyma.owncomponent.ModalMessage;
import com.edanyma.rest.RestController;
import com.edanyma.utils.AppUtils;
import com.edanyma.utils.GlideClient;
import com.etiennelawlor.discreteslider.library.ui.DiscreteSlider;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Response;

import static com.edanyma.AppConstants.STAR_ARRAY;
import static com.edanyma.manager.GlobalManager.getBootstrapModel;
import static com.edanyma.manager.GlobalManager.getClient;
import static com.edanyma.manager.GlobalManager.getUserToken;


public class CreateFeedbackFragment extends BaseFragment implements View.OnClickListener {

    private final String TAG = "CreateFeedbackFragment";

    private static final String COMPANY_FEEDBACK = "company_feedback";

    public OnShowFeedbacksListener mListener;

    private CompanyModel mCompanyModel;

    private ImageView mRateStar;

    private AppCompatEditText mFeedbackComment;

    private TextView mFeedbackError;

    private int mSliderPosition;


    public CreateFeedbackFragment() {
    }

    public static CreateFeedbackFragment newInstance( CompanyModel companyModel ) {
        CreateFeedbackFragment fragment = new CreateFeedbackFragment();
        Bundle args = new Bundle();
        args.putSerializable( COMPANY_FEEDBACK, companyModel );
        fragment.setArguments( args );
        return fragment;
    }

    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        if ( getArguments() != null ) {
            mCompanyModel = ( CompanyModel ) getArguments().getSerializable( COMPANY_FEEDBACK );
        }
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState ) {

        return inflater.inflate( R.layout.fragment_create_feedback, container, false );
    }

    @Override
    public void onActivityCreated( @Nullable Bundle savedInstanceState ) {
        super.onActivityCreated( savedInstanceState );
        getView().findViewById( R.id.feedbackContainerId ).setBackground( Drawable.createFromPath( AppUtils.getSnapshotPath() ) );
        initTextView( R.id.feedbackTitleId, AppConstants.B52,
                mCompanyModel.getDisplayName() );
        GlideClient.downloadImage( getActivity(), getBootstrapModel()
                .getStaticUrl() + String.format( AppConstants.STATIC_COMPANY_LOGO,
                mCompanyModel.getThumb() ), getView().findViewById( R.id.feedbackLogoId ) );
        initTextView( R.id.feedbackCountId, AppConstants.ROBOTO_CONDENCED,
                mCompanyModel.getCommentCount() );
        initTextView( R.id.feedbackRaitingTitleId, AppConstants.B52 );
        initTextView( R.id.feedbackCommentTitleId, AppConstants.B52 );

        initTextView( R.id.feedbackAllReviewId , AppConstants.ROBOTO_CONDENCED);

        mRateStar = getView().findViewById( R.id.feedbackStarsId );
        ( ( DiscreteSlider ) getView().findViewById( R.id.feedbackSliderId ) )
                .setOnDiscreteSliderChangeListener( ( int position ) -> {
                    mSliderPosition = position;
                    mRateStar.setImageDrawable( getActivity().getResources().getDrawable( STAR_ARRAY[ position ] ) );
                } );

        mFeedbackComment = initEditText( R.id.feedbackCommentValueId, AppConstants.ROBOTO_CONDENCED );
        mFeedbackError = initTextView( R.id.feedbackErrorFieldId, AppConstants.ROBOTO_CONDENCED );
        initButton( R.id.feedbackButtonId, AppConstants.ROBOTO_CONDENCED );
        setThisOnClickListener(  R.id.feedbackAllReviewId, R.id.feedbackButtonId, R.id.feedbackBackBtnId  );
    }


    private void validateFeedback() {
        final String comment = mFeedbackComment.getText().toString();
        if ( AppUtils.nullOrEmpty( comment ) ) {
            ( new Handler() ).postDelayed( () -> {
                mFeedbackError.setVisibility( View.GONE );
            }, 3000 );
            return;
        }
        if ( mSliderPosition < 3 ) {
            ModalDialog.DialogParams params = ModalDialog.getDialogParms();
            params.setTitle( "Подтверждение" )
                    .setMessage( "Вы действительно хотите поставить такую низкую оченку !" )
                    .setBlueButtonText( getResources().getString( R.string.no_it_joke ) )
                    .setBlueButtonId( R.drawable.ic_emoticon_wink_outline_white_24dp )
                    .setWhiteButtonText( getResources().getString( R.string.yes_sure ) )
                    .setWhiteButtonId( R.drawable.ic_thumb_down_gray600_24dp );
            ModalDialog.execute( getActivity(), params ).setOnModalBtnClickListener( new ModalDialog.OnModalBtnClickListener() {
                @Override
                public void onBlueButtonClick() {
                    return;
                }

                @Override
                public void onWhiteBtnClick() {
                    postFeedback( comment );
                }
            } );
            return;
        }
        postFeedback( comment );
    }

    private void postFeedback( String comment ) {
        FeedbackModel feedbackModel = new FeedbackModel();
        feedbackModel.setCompanyId( Integer.valueOf( mCompanyModel.getId() ) );
        feedbackModel.setPerson( getClient().getNickName() );
        feedbackModel.setPersonAvatar( getClient().getPhoto() );
        feedbackModel.setFeedbackDate( AppUtils.formatDate( AppConstants.ORDER_DATE_FORMAT, new Date() ) );
        feedbackModel.setFeedbackTime( AppUtils.formatDate( AppConstants.ORDER_TIME_FORMAT, new Date() ) );
        feedbackModel.setRate( mSliderPosition );
        feedbackModel.setComment( comment );
        new PostFeedback().execute( feedbackModel );
    }

    private void backPressed() {
        ( ( BaseActivity ) getActivity() ).getHeader().setVisibility( View.VISIBLE );
        ( ( BaseActivity ) getActivity() ).getFooter().setVisibility( View.VISIBLE );
        getActivity().onBackPressed();
    }

    @Override
    public void onClick( View view ) {
        AppUtils.clickAnimation( view );
        switch ( view.getId() ) {
            case R.id.feedbackButtonId:
                validateFeedback();
                break;
            case  R.id.feedbackBackBtnId:
                backPressed();
                break;
            case R.id.feedbackAllReviewId:
                if ( mListener != null ){
                    mListener.onShowFeedbacksAction( mCompanyModel );
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onAttach( Context context ) {
        super.onAttach( context );
        if ( context instanceof OnShowFeedbacksListener ) {
            mListener = ( OnShowFeedbacksListener ) context;
        } else {
            throw new RuntimeException( context.toString()
                    + " must implement OnShowFeedbacksListener" );
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnShowFeedbacksListener {
        void onShowFeedbacksAction( CompanyModel companyModel );
    }

    private class PostFeedback extends AsyncTask< FeedbackModel, Void, String > {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground( FeedbackModel... feedbackModels ) {
            String result = null;
            try {
                Call< ApiResponse > feedbackCall = RestController
                        .getApi().saveFeedback( AppConstants.AUTH_BEARER
                                + getUserToken(), feedbackModels[ 0 ] );
                Response< ApiResponse > responseFeedback = feedbackCall.execute();
                if ( responseFeedback.body() != null ) {
                    if ( responseFeedback.body().getStatus() == 200 ) {
                        result = "Спасибо за Ваш отзыв. Нам очень важно Ваше мнение.";
                    } else {
                        result = responseFeedback.body().getMessage();
                    }
                } else {
                    result = getResources().getString( R.string.internal_error );
                }
            } catch ( Exception e ) {
                result = getResources().getString( R.string.internal_error );
                Log.i( TAG, e.getMessage() );
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute( String result ) {
            super.onPostExecute( result );
            ModalMessage.show( getActivity(), "Сообщение", new String[]{ result } );
            ( new Handler() ).postDelayed( () -> {
                backPressed();
            }, 2000 );

        }
    }
}
