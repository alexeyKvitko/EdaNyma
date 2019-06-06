package com.edanyma.fragment;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.edanyma.AppConstants;
import com.edanyma.R;
import com.edanyma.model.CompanyModel;
import com.edanyma.utils.GlideClient;
import com.etiennelawlor.discreteslider.library.ui.DiscreteSlider;

import static com.edanyma.manager.GlobalManager.getBootstrapModel;


public class CreateFeedbackFragment extends BaseFragment {

    private final String TAG = "CreateFeedbackFragment";

    private static final String COMPANY_FEEDBACK = "company_feedback";

    private static final int[] STAR_ARRAY = new int[]{ R.drawable.five_star_00, R.drawable.five_star_05,
                                                      R.drawable.five_star_10, R.drawable.five_star_15,
                                                      R.drawable.five_star_20, R.drawable.five_star_25,
                                                      R.drawable.five_star_30, R.drawable.five_star_35,
                                                      R.drawable.five_star_40, R.drawable.five_star_45,
                                                      R.drawable.five_star_50 };

    private CompanyModel mCompanyModel;

    private DiscreteSlider mFeedbackSlider;

    private ImageView mRateStar;


    public CreateFeedbackFragment() {}

    public static CreateFeedbackFragment newInstance( CompanyModel companyModel) {
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
            mCompanyModel = ( CompanyModel ) getArguments().getSerializable(  COMPANY_FEEDBACK );
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
        initTextView( R.id.feedbackTitleId, AppConstants.B52,
                mCompanyModel.getDisplayName() );
        GlideClient.downloadImage( getActivity(), getBootstrapModel()
                .getStaticUrl() + String.format( AppConstants.STATIC_COMPANY_LOGO,
                mCompanyModel.getThumb() ),  getView().findViewById( R.id.feedbackLogoId ) );
        initTextView( R.id.feedbackCountId, AppConstants.ROBOTO_CONDENCED,
                mCompanyModel.getCommentCount() );
        initTextView( R.id.feedbackRaitingTitleId, AppConstants.B52 );

        mRateStar = getView().findViewById( R.id.feedbackStarsId );

        mFeedbackSlider = getView().findViewById( R.id.feedbackSliderId );
        mFeedbackSlider.setOnDiscreteSliderChangeListener( ( int position ) ->{
            Log.i( TAG,"Slider position: "+position );
            mRateStar.setImageDrawable( getActivity().getResources().getDrawable( STAR_ARRAY[ position] ) );
        } );
    }
}
