package com.edanyma.activity;

import android.os.Bundle;
import android.os.Handler;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;

import com.edanyma.AppConstants;
import com.edanyma.EdaNymaApp;
import com.edanyma.R;
import com.edanyma.model.ActivityState;
import com.edanyma.model.CompanyActionModel;
import com.edanyma.model.HomeMenuModel;
import com.edanyma.recycleview.CompanyActionAdapter;
import com.edanyma.recycleview.HomeMenuAdapter;
import com.stone.vega.library.VegaLayoutManager;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private final String TAG = "MainActivity";

    private final int[] actionSlideIconIds = new int[]{ R.id.actionSlide1,R.id.actionSlide2,
            R.id.actionSlide3, R.id.actionSlide4, R.id.actionSlide5 };

    private RecyclerView mActionRecView;
    private RecyclerView.Adapter mActionAdapter;
    private LinearLayoutManager mHorizontalLayoutManager;

    private RecyclerView mHomeMenuRecView;
    private RecyclerView.Adapter mHomeMenuAdapter;
    private int mPrevScrollState;
    private int mCurrentSlide;
    private Handler mTimer;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        fillActionAdapter( fillCompanyAction() );
        fillHomeMenuAdapter( fillHomeMenuModel() );
        initMainLayout();
    }


    private void initMainLayout() {
        initBaseActivity( new ActivityState( AppConstants.HOME_BOTTOM_INDEX ) );
//        List<CompanyActionModel> companyActionModels = GlobalManager.getInstance().getBootstrapModel()
//                                                            .getCompanyActions();
//        fillActionAdapter( companyActionModels );

        mTimer = new Handler();
        mTimer.postDelayed(new Runnable()
        {
            private long time = 0;

            @Override
            public void run()
            {
                int prevSlide = mCurrentSlide;
                mCurrentSlide++;
                mCurrentSlide = mCurrentSlide == 5 ? 0 : mCurrentSlide;
                changeSlideIcon( prevSlide );
                mTimer.postDelayed(this, 3000);
            }
        }, 3000);

        initRecyclerView();
    }


    private void initRecyclerView() {
        mPrevScrollState = AppConstants.FAKE_ID;
        mCurrentSlide = 0;
        changeSlideIcon( mCurrentSlide );
        if ( mActionRecView == null ) {
            mActionRecView = findViewById( R.id.companyActionRVId );
            mHorizontalLayoutManager = new LinearLayoutManager( this, LinearLayoutManager.HORIZONTAL, false );
            mActionRecView.addOnScrollListener( new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged( RecyclerView recyclerView, int newState ) {
                    super.onScrollStateChanged( recyclerView, newState );
                    if( ((RecyclerView.SCROLL_STATE_SETTLING == mPrevScrollState ||
                            RecyclerView.SCROLL_STATE_DRAGGING == mPrevScrollState ) &&
                            RecyclerView.SCROLL_STATE_IDLE == newState) ||
                            (RecyclerView.SCROLL_STATE_IDLE == mPrevScrollState &&
                                    RecyclerView.SCROLL_STATE_DRAGGING == newState )){
                        changeCardAccordingToPosition();

                    }
                    Log.i(TAG, "Prev State "+mPrevScrollState+", NEW STATE: "+ newState  );
                    mPrevScrollState =  newState;
                }


                @Override
                public void onScrolled( RecyclerView recyclerView, int dx, int dy ) {
                    super.onScrolled( recyclerView, dx, dy );
                    Log.i(TAG, "Scrolled" );

                }
            } );
            mActionRecView.setLayoutManager( mHorizontalLayoutManager );
            mActionRecView.setAdapter( mActionAdapter );
            mActionRecView.setHasFixedSize( false );
        }
        mActionRecView.getAdapter().notifyDataSetChanged();
        mActionAdapter.notifyDataSetChanged();

        if ( mHomeMenuRecView == null ) {
            mHomeMenuRecView = findViewById( R.id.homeMenuRVId );
            mHomeMenuRecView.setLayoutManager( new VegaLayoutManager() );
            mHomeMenuRecView.setAdapter( mHomeMenuAdapter );
        }
        mHomeMenuRecView.getAdapter().notifyDataSetChanged();
        mHomeMenuAdapter.notifyDataSetChanged();
    }


    private void fillActionAdapter( List< CompanyActionModel > actions ) {
        if ( mActionAdapter == null ) {
            mActionAdapter = new CompanyActionAdapter( new ArrayList< CompanyActionModel >() );
        }
        for ( int i = 0; i < actions.size(); i++ ) {
            ( ( CompanyActionAdapter ) mActionAdapter ).addItem( actions.get( i ), i );
        }
    }

    private void fillHomeMenuAdapter( List< HomeMenuModel > menuModels ) {
        if ( mHomeMenuAdapter == null ) {
            mHomeMenuAdapter = new HomeMenuAdapter( new ArrayList< HomeMenuModel >() );
        }
        for ( int i = 0; i < menuModels.size(); i++ ) {
            ( ( HomeMenuAdapter ) mHomeMenuAdapter ).addItem( menuModels.get( i ), i );
        }
    }

    private LinkedList< CompanyActionModel > fillCompanyAction() {
        LinkedList< CompanyActionModel > actions = new LinkedList<>();
        actions.add( new CompanyActionModel( "FIDELE", "http://194.58.122.145:8080/static/images/fidele_action.jpg" ) );
        actions.add( new CompanyActionModel( "ДОСТАВКА КУХНЯ", "http://194.58.122.145:8080/static/images/kuhnya_action.jpg" ) );
        actions.add( new CompanyActionModel( "PIZZA ROLLA", "http://194.58.122.145:8080/static/images/pizrol_action.jpg" ) );
        actions.add( new CompanyActionModel( "FOODIE", "http://194.58.122.145:8080/static/images/foodie_action.jpg" ) );
        actions.add( new CompanyActionModel( "ПАВЛИН МАВЛИН", "http://194.58.122.145:8080/static/images/pavlin_action.jpg" ) );
        return actions;
    }

    private LinkedList< HomeMenuModel > fillHomeMenuModel() {

        LinkedList< HomeMenuModel > homeMenuModels = new LinkedList<>();
        homeMenuModels.add( new HomeMenuModel( "ВСЕ ЗАВЕДЕНИЯ", getResources().getDrawable( R.drawable.restaurant ), "102",
                "ВСЕ БЛЮДА", getResources().getDrawable( R.drawable.all_dishes ), "24" ) );
        homeMenuModels.add( new HomeMenuModel( "ПИЦЦА", getResources().getDrawable( R.drawable.pizza ), "31",
                "СУШИ/СЭТЫ", getResources().getDrawable( R.drawable.sushi ), "12" ) );
        homeMenuModels.add( new HomeMenuModel( "БУРГЕРЫ", getResources().getDrawable( R.drawable.burger ), "12",
                "МАНГАЛ МЕНЮ", getResources().getDrawable( R.drawable.shashlik ), "18" ) );
        homeMenuModels.add( new HomeMenuModel( "WOK МЕНЮ", getResources().getDrawable( R.drawable.wok ), "11",
                "ИЗБРАННОЕ", getResources().getDrawable( R.drawable.favorite ), "02" ) );

        return homeMenuModels;
    }


    @Override
    public void onDestroy() {
        if ( mActionRecView != null ) {
            mActionRecView.setAdapter( null );
            mActionRecView = null;
        }
        if ( mHomeMenuRecView != null ) {
            mHomeMenuRecView.setAdapter( null );
            mHomeMenuRecView = null;
        }
        mActionAdapter = null;
        mHomeMenuAdapter = null;
        mTimer = null;
        super.onDestroy();
    }

    private void changeCardAccordingToPosition() {
        final View currentView[] = new View[ 2 ];
        for ( int i = 0; i < mHorizontalLayoutManager.getItemCount(); i++ ) {
            if ( mActionRecView.getChildAt( i ) != null ) {
                currentView[ i ] = mActionRecView.getChildAt( i );
            }
        }
        final int firstSlidePos;
        final int secondSlidePos;
        int frameCount;
        if ( currentView[ 1 ] != null ) {
            if ( currentView[ 1 ].getLeft() > AppConstants.BANNER_CENTER ) {
                frameCount = AppConstants.BANNER_END - currentView[ 1 ].getLeft();
                firstSlidePos = AppConstants.BANNER_LEFT;
                secondSlidePos = AppConstants.BANNER_END;
            } else {
                frameCount = -currentView[ 1 ].getLeft();
                firstSlidePos = -AppConstants.BANNER_END;
                secondSlidePos = AppConstants.BANNER_LEFT;
            }
        } else {
            frameCount = 0;
            currentView[1] = currentView[0];
            firstSlidePos = AppConstants.BANNER_LEFT;
            secondSlidePos = AppConstants.BANNER_LEFT;
        }
            Animation anim = new TranslateAnimation( 0, frameCount, 0, 0 );
            anim.setDuration( Math.abs( frameCount * 300 / AppConstants.BANNER_CENTER ) );
            anim.setAnimationListener( new Animation.AnimationListener() {
                @Override
                public void onAnimationStart( Animation animation ) {
                }

                @Override
                public void onAnimationEnd( Animation animation ) {
                    currentView[ 0 ].setLeft( firstSlidePos );
                    currentView[ 1 ].setLeft( secondSlidePos );
                    for ( int i = 0; i < mHorizontalLayoutManager.getItemCount(); i++ ) {
                        if ( mActionRecView.getChildAt( i ) != null ) {
                            View view = mActionRecView.getChildAt( i );
                            if ( view.getLeft() == 0 ) {
                                int prevSlide = mCurrentSlide;
                                mCurrentSlide = mActionRecView.getChildAdapterPosition( view );

                                changeSlideIcon( prevSlide );
                            }
                        }
                    }
                    mActionRecView.getAdapter().notifyDataSetChanged();
                }

                @Override
                public void onAnimationRepeat( Animation animation ) {
                }
            } );
            currentView[ 0 ].startAnimation( anim );
            currentView[ 1 ].startAnimation( anim );


    }

    private void changeSlideIcon( final int prevSlide ){
        if( AppConstants.FAKE_ID != prevSlide ){
            Animation fadeOut = AnimationUtils.loadAnimation( this,R.anim.fade_out);
            fadeOut.setAnimationListener( new Animation.AnimationListener() {
                @Override
                public void onAnimationStart( Animation animation ) {
                }

                @Override
                public void onAnimationEnd( Animation animation ) {
                    Transition fade = new Fade( );
                    fade.setDuration( 600 );
                    TransitionManager.beginDelayedTransition( mActionRecView,fade );
                    mActionRecView.scrollToPosition( mCurrentSlide );
                    findViewById( actionSlideIconIds[prevSlide] ).setBackground( getDrawable( R.drawable.sel_slider_white ) );
                    findViewById( actionSlideIconIds[prevSlide] ).setVisibility( View.VISIBLE );
                    findViewById( actionSlideIconIds[prevSlide] ).startAnimation(
                            AnimationUtils.loadAnimation( EdaNymaApp.getAppContext(),R.anim.fade_in) );
                    findViewById( actionSlideIconIds[mCurrentSlide] ).setBackground( getDrawable( R.drawable.sel_slider_red ) );
                    findViewById( actionSlideIconIds[mCurrentSlide] ).startAnimation(
                                AnimationUtils.loadAnimation( EdaNymaApp.getAppContext(),R.anim.fade_in) );
                }

                @Override
                public void onAnimationRepeat( Animation animation ) {
                }
            } );
            findViewById( actionSlideIconIds[prevSlide] ).
            startAnimation( fadeOut );
        }

    }


}
