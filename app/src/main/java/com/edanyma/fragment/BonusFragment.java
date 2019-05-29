package com.edanyma.fragment;


import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import com.edanyma.AppConstants;
import com.edanyma.R;
import com.edanyma.model.BonusModel;
import com.edanyma.owncomponent.ModalMessage;
import com.edanyma.recyclerview.BonusAdapter;
import com.edanyma.utils.AppUtils;
import com.edanyma.utils.ConvertUtils;

import java.util.ArrayList;
import java.util.List;

import static com.edanyma.manager.GlobalManager.getClient;


public class BonusFragment extends BaseFragment {

    private static final int BONUS_HEADER_HEIGHT = ( int ) ConvertUtils.convertDpToPixel( 56 );
    private static final int BONUS_ROW_HEIGHT = ( int ) ConvertUtils.convertDpToPixel( 104 );
    private static final int ANIMATION_DUARATION = 300;

    private RecyclerView mBonusRecView;
    private BonusAdapter mBonusAdapter;

    private List< BonusModel > mBonuses;

    private int mBonusBodyHeight;

    public BonusFragment() {
    }

    public static BonusFragment newInstance() {
        BonusFragment fragment = new BonusFragment();
        return fragment;
    }

    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState ) {
        return inflater.inflate( R.layout.fragment_bonus, container, false );
    }

    @Override
    public void onActivityCreated( @Nullable Bundle savedInstanceState ) {
        super.onActivityCreated( savedInstanceState );
        mBonuses = getClient().getBonusModels();
        if ( AppUtils.nullOrEmpty( mBonuses ) ) {
            ModalMessage.show( getActivity(), "Сообщение", new String[]{ getString( R.string.no_bonus )
                    , getString( R.string.splash_desc_two ) } ).setOnMessageHideListener( () -> {
                getActivity().onBackPressed();
            } );
            return;
        }
        initTextView( R.id.bonusTitleId, AppConstants.B52 );
        int bonusHeight =  mBonuses.size() > 3 ? 3 : mBonuses.size();
        mBonusBodyHeight = BONUS_HEADER_HEIGHT + BONUS_ROW_HEIGHT* bonusHeight;
        animateBonusBody( 0, mBonusBodyHeight );
        getView().findViewById( R.id.shadowBonusLayoutId ).setOnClickListener( (View view) -> {
            animateBonusBody( mBonusBodyHeight,0 );
        } );
    }

    private void animateBonusBody( int start, int end){
        Animation fade = AnimationUtils.loadAnimation( getActivity(), R.anim.fade_in );
        if ( mBonusBodyHeight == start ){
            fade = AnimationUtils.loadAnimation( getActivity(), R.anim.fade_out );
        }
        getView().findViewById( R.id.shadowBonusLayoutId ).startAnimation( fade );
        final View bonusBody = getView().findViewById( R.id.bonusBodyId );
        final FrameLayout.LayoutParams layoutParams = ( FrameLayout.LayoutParams ) bonusBody.getLayoutParams();
        ValueAnimator valAnimator = ValueAnimator.ofObject( new IntEvaluator(), start, end );
        valAnimator.addUpdateListener( ( ValueAnimator animator ) -> {
                int val = ( ( Integer ) animator.getAnimatedValue() );
                layoutParams.height = val;
                bonusBody.setLayoutParams( layoutParams );
                if ( end == 0 && val == 0 ){
                    getActivity().onBackPressed();
                }
        } );
        valAnimator.setDuration( ANIMATION_DUARATION );
        valAnimator.start();
    }

    private void initRecView() {
        if ( mBonusRecView == null ) {
            mBonusRecView = getView().findViewById( R.id.bonusRVId );
            mBonusRecView.setLayoutManager( new LinearLayoutManager( getContext(), RecyclerView.VERTICAL, false ) );
            mBonusRecView.setAdapter( mBonusAdapter );
            mBonusRecView.setHasFixedSize( false );
        }
        mBonusRecView.getAdapter().notifyDataSetChanged();
    }

    private void initAdapter() {
        if ( mBonusAdapter == null ) {
            fillBonusAdapter( mBonuses );
        }
        mBonusAdapter.notifyDataSetChanged();
    }

    private void fillBonusAdapter( List< BonusModel > bonuses ) {
        if ( mBonusAdapter == null ) {
            mBonusAdapter = new BonusAdapter( new ArrayList< BonusModel>() );
        } else {
            mBonusAdapter.deleteAllItem();
        }
        int idx = 0;
        for ( BonusModel bonus : bonuses ) {
            mBonusAdapter.addItem( bonus, idx );
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
        if ( mBonusRecView != null ) {
            mBonusRecView.setAdapter( null );
            mBonusRecView = null;
        }
        mBonusAdapter = null;
    }
    
}
