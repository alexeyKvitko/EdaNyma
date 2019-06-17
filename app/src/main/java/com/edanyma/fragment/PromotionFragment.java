package com.edanyma.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.edanyma.AppConstants;
import com.edanyma.R;
import com.edanyma.model.CompanyActionModel;
import com.edanyma.recyclerview.PromotionAdapter;
import com.edanyma.recyclerview.manager.VegaLayoutManager;
import com.edanyma.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

import static com.edanyma.manager.GlobalManager.getBootstrapModel;


public class PromotionFragment extends BaseFragment {

    private RecyclerView mPromotionRecView;
    private PromotionAdapter mPromotionAdapter;

    public PromotionFragment() {}


    public static PromotionFragment newInstance() {
        PromotionFragment fragment = new PromotionFragment();
        return fragment;
    }

    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState ) {
        return inflater.inflate( R.layout.fragment_promotion, container, false );
    }

    @Override
    public void onActivityCreated( @Nullable Bundle savedInstanceState ) {
        super.onActivityCreated( savedInstanceState );
        initTextView( R.id.promotionTitleId, AppConstants.B52 );
        getView().findViewById( R.id.orderBackBtnId ).setOnClickListener( ( View view ) -> {
            getActivity().onBackPressed();
        } );
    }

    private void initRecView() {
        if ( mPromotionRecView == null ) {
            mPromotionRecView = getView().findViewById( R.id.promotionRVId );
            mPromotionRecView.setLayoutManager( new VegaLayoutManager() );
            mPromotionRecView.setAdapter( mPromotionAdapter );
//            mPromotionRecView.setLayoutManager( new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL )  );
        }
        mPromotionRecView.getAdapter().notifyDataSetChanged();
    }



    private void initAdapter() {
        if ( mPromotionAdapter == null ) {
            fillPromotionAdapter( getBootstrapModel().getCompanyActions() );
        }
        mPromotionAdapter.notifyDataSetChanged();
    }

    private void fillPromotionAdapter( List< CompanyActionModel > promotions ) {
        if ( mPromotionAdapter == null ) {
            mPromotionAdapter = new PromotionAdapter( new ArrayList< CompanyActionModel>() );
        } else {
            mPromotionAdapter.deleteAllItem();
        }
        int idx = 0;
        for ( CompanyActionModel promotion : promotions ) {
            if( AppConstants.PROMOTION_FRAGMENT.equals( promotion.getFullScreenAction() ) ){
                mPromotionAdapter.addItem( promotion, idx );
                idx++;
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        initAdapter();
        initRecView();
    }

    @Override
    public void onPause() {
        super.onPause();
        if ( mPromotionRecView != null ) {
            mPromotionRecView.setAdapter( null );
            mPromotionRecView = null;
        }
        mPromotionAdapter = null;
    }

}
