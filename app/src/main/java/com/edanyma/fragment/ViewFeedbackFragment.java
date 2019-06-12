package com.edanyma.fragment;


import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.edanyma.AppConstants;
import com.edanyma.R;
import com.edanyma.activity.BaseActivity;
import com.edanyma.model.ApiResponse;
import com.edanyma.model.ClientOrderModel;
import com.edanyma.model.CompanyModel;
import com.edanyma.model.ExistOrders;
import com.edanyma.model.FeedbackModel;
import com.edanyma.model.OurClientModel;
import com.edanyma.owncomponent.ModalMessage;
import com.edanyma.recyclerview.FeedbackAdapter;
import com.edanyma.recyclerview.OrderAdapter;
import com.edanyma.rest.RestController;
import com.edanyma.utils.AppUtils;
import com.edanyma.utils.GlideClient;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static com.edanyma.manager.GlobalManager.getBootstrapModel;
import static com.edanyma.manager.GlobalManager.getClient;
import static com.edanyma.manager.GlobalManager.getClientOrders;
import static com.edanyma.manager.GlobalManager.getUserToken;
import static com.edanyma.manager.GlobalManager.setClientOrders;


public class ViewFeedbackFragment extends BaseFragment {

    private final String TAG = "ViewFeedbackFragment";

    private static final String COMPANY_FEEDBACK = "company_feedback";

    private RecyclerView mFeedbackRecView;
    private FeedbackAdapter mFeedbackAdapter;

    private List<FeedbackModel> mFeedbackModels;

    private CompanyModel mCompanyModel;

    public ViewFeedbackFragment() {}

    public static ViewFeedbackFragment newInstance( CompanyModel companyModel ) {
        ViewFeedbackFragment fragment = new ViewFeedbackFragment();
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
        return inflater.inflate( R.layout.fragment_view_feedback, container, false );
    }

    @Override
    public void onActivityCreated( @Nullable Bundle savedInstanceState ) {
        super.onActivityCreated( savedInstanceState );
        initTextView( R.id.viewFeedbackTitleId, AppConstants.B52 );
        initTextView( R.id.viewFeedbackCompanyId, AppConstants.B52,
                mCompanyModel.getDisplayName() );
        GlideClient.downloadImage( getActivity(), getBootstrapModel()
                .getStaticUrl() + String.format( AppConstants.STATIC_COMPANY_LOGO,
                mCompanyModel.getThumb() ), getView().findViewById( R.id.viewFeedbackLogoId ) );
        initTextView( R.id.viewFeedbackCountId, AppConstants.ROBOTO_CONDENCED,
                mCompanyModel.getCommentCount() );

        ImageView feedbackStars = getView().findViewById( R.id.viewFeedbackTotalRateId );
        feedbackStars.setImageDrawable( getActivity().getResources()
                .getDrawable( AppConstants.STAR_ARRAY[ mCompanyModel.getFeedbackRate() ] ) );
        getView().findViewById( R.id.viewFeedbackBackBtnId ).setOnClickListener( ( View view ) -> {
            AppUtils.clickAnimation( view );
            ( ( BaseActivity ) getActivity() ).setHeaderFooterVisibilty( View.VISIBLE );
            getActivity().onBackPressed();
        } );
    }
    
    private void startRecView(){
        initAdapter();
        initRecView();
    }

    private void initRecView() {
        if ( mFeedbackRecView == null ) {
            mFeedbackRecView = getView().findViewById( R.id.feedbackRVId );
            mFeedbackRecView.setLayoutManager( new LinearLayoutManager( getContext(), RecyclerView.VERTICAL, false ) );
            mFeedbackRecView.setAdapter( mFeedbackAdapter );
            mFeedbackRecView.setHasFixedSize( false );
        }
        mFeedbackRecView.getAdapter().notifyDataSetChanged();
    }

    private void initAdapter() {
        if ( mFeedbackAdapter == null ) {
            fillFeedbackAdapter( mFeedbackModels );
        }
        mFeedbackAdapter.notifyDataSetChanged();
    }

    private void fillFeedbackAdapter( List< FeedbackModel > feedbackModels ) {
        if ( mFeedbackAdapter == null ) {
            mFeedbackAdapter = new FeedbackAdapter( new LinkedList< FeedbackModel >() );
        } else {
            mFeedbackAdapter.deleteAllItem();
        }
        int idx = 0;
        for ( FeedbackModel feedback : feedbackModels ) {
            mFeedbackAdapter.addItem( feedback, idx );
            idx++;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if(  !AppUtils.nullOrEmpty( mFeedbackModels ) ){
            startRecView();
        } else {
            AppUtils.transitionAnimation( getView().findViewById( R.id.viewFeedbackLayoutId ),
                    getView().findViewById( R.id.pleaseWaitContainerId ) );
            new FeetchFeedbacks().execute( );
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if ( mFeedbackRecView != null ) {
            mFeedbackRecView.setAdapter( null );
            mFeedbackRecView = null;
        }
        mFeedbackAdapter = null;
    }

    private class FeetchFeedbacks extends AsyncTask< Void, Void, String > {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground( Void... arg0 ) {
            String result = null;
            try {
                Call< ApiResponse< List<FeedbackModel> > > feedbackCall = RestController
                        .getApi().getCompanyFeedbacks( AppConstants.AUTH_BEARER
                                + getUserToken(), Integer.valueOf( mCompanyModel.getId() )  );
                Response< ApiResponse< List<FeedbackModel> > > feedbackResponse = feedbackCall.execute();
                if ( feedbackResponse.body() != null ) {
                    if( feedbackResponse.body().getStatus() == 200 ){
                        mFeedbackModels = feedbackResponse.body().getResult();
                    } else {
                        result = feedbackResponse.body().getMessage();
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
            AppUtils.transitionAnimation( getView().findViewById( R.id.pleaseWaitContainerId ),
                    getView().findViewById( R.id.viewFeedbackLayoutId ) );
            if ( result != null ){
                ModalMessage.show( getActivity(), getActivity().getResources().getString( R.string.message_title)
                                                                        , new String[] {result} );
            } else {
                if( mFeedbackModels.size() == 0 ){
                    ModalMessage.show( getActivity(), getActivity().getResources().getString( R.string.message_title),
                            new String[] { getActivity().getResources().getString( R.string.feedback_empty),
                            getActivity().getResources().getString( R.string.first_feedback)} );
                    getActivity().onBackPressed();
                } else {
                    startRecView();
                }
            }
        }
    }
    
    
}
