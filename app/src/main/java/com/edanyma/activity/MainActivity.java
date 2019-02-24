package com.edanyma.activity;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edanyma.AppConstants;
import com.edanyma.EdaNymaApp;
import com.edanyma.R;
import com.edanyma.model.CompanyAction;
import com.edanyma.recycleview.CompanyActionAdapter;

import java.util.ArrayList;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {

    private static final int[] MAIN_CARDS = { R.id.topLayoutId, R.id.middleLayoutId, R.id.bottomLayoutId };
    private TextView mDeliveryTV;
    private BottomNavigationView mBottomNavigation;

    protected RecyclerView.LayoutManager mActionLayoutManager;
    protected RecyclerView mActionRecView;
    protected RecyclerView.Adapter mActionAdapter;
    protected LinearLayoutManager mHorizonalLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_main);
        if ( mActionAdapter == null ) {
            mActionAdapter = new CompanyActionAdapter(  new ArrayList< CompanyAction >());
        }
        initialize();
    }


    private void initialize(){
        mDeliveryTV = this.findViewById( R.id.deliveryCityId );
        mDeliveryTV.setTypeface( AppConstants.ROBOTO_CONDENCED );
        mDeliveryTV.setText( "Симферополь" );
        fillActionAdapter( fillCompanyAction() );
        initRecyclerView();
    }
    
    private void initRecyclerView(){
        if ( mActionLayoutManager == null ) {
            mActionLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
            mHorizonalLayoutManager = new LinearLayoutManager( this,LinearLayoutManager.HORIZONTAL,false);
        }
        if ( mActionRecView == null ) {
            mActionRecView =  findViewById( R.id.companyActionRVId );
            mActionRecView.setAdapter( mActionAdapter );
            mActionRecView.setHasFixedSize(false);
            mActionRecView.setLayoutManager(mHorizonalLayoutManager);
        }
        mActionRecView.getAdapter().notifyDataSetChanged();
        mActionAdapter.notifyDataSetChanged();
    }


    private void fillActionAdapter( LinkedList<CompanyAction> actions ){
        for (int i = 0; i < actions.size(); i++) {
            (( CompanyActionAdapter) mActionAdapter).addItem( actions.get(i), i);
        }
    }

    private LinkedList<CompanyAction> fillCompanyAction(){
        LinkedList<CompanyAction> actions = new LinkedList<>();
        actions.add( new CompanyAction( "FIDELE", "http://194.58.122.145:8080/static/images/fidele_action.png" ) );
        actions.add( new CompanyAction( "ДОСТАВКА КУХНЯ", "http://194.58.122.145:8080/static/images/kuhnya_action.png")  );
        actions.add( new CompanyAction( "PIZZA ROLLA", "http://194.58.122.145:8080/static/images/pizrol_action.png" ) );
        actions.add( new CompanyAction( "FOODIE", "http://194.58.122.145:8080/static/images/foodie_action.png" ) );
        actions.add( new CompanyAction( "ПАВЛИН МАВЛИН", "http://194.58.122.145:8080/static/images/pavlin_action.png" ) );
        return actions;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBottomNavigation = findViewById(R.id.bottomNavigationId);
        mBottomNavigation.findViewById( R.id.navigation_home ).performClick();
        LinearLayout mainCardItem = null;
        Animation[] cardAnimation = new Animation[3];
        for( int i = 0; i < 3; i++ ){
            cardAnimation[i] = AnimationUtils.loadAnimation( EdaNymaApp.getAppContext(),R.anim.bounce);
            cardAnimation[i].setStartOffset( i*100 );
        }
        int idx = 0;
        for( int cardId : MAIN_CARDS ){
            mainCardItem = ( LinearLayout ) this.findViewById( cardId );
            mainCardItem.startAnimation( cardAnimation[idx] );
            idx++;
        }
    }

    @Override
    public void onDestroy() {
        mActionRecView.setAdapter(null);
        mActionRecView = null;
        mActionLayoutManager = null;
        mActionAdapter =null;
        super.onDestroy();
    }

}
