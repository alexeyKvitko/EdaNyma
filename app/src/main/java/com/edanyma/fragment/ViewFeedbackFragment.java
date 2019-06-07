package com.edanyma.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edanyma.R;
import com.edanyma.model.CompanyModel;


public class ViewFeedbackFragment extends BaseFragment {

    private final String TAG = "ViewFeedbackFragment";

    private static final String COMPANY_FEEDBACK = "company_feedback";


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

}
