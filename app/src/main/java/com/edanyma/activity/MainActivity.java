package com.edanyma.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.edanyma.AppConstants;
import com.edanyma.EdaNymaApp;
import com.edanyma.R;
import com.edanyma.manager.GlobalManager;
import com.edanyma.model.ActivityState;
import com.edanyma.model.CompanyActionModel;
import com.edanyma.model.HomeMenuModel;
import com.edanyma.recycleview.CompanyActionAdapter;
import com.edanyma.recycleview.HomeMenuAdapter;
import com.edanyma.utils.PicassoClient;
import com.stone.vega.library.VegaLayoutManager;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private final String TAG = "MainActivity";

    private final int[] actionSlideIconIds = new int[]{ R.id.actionSlide1, R.id.actionSlide2,
            R.id.actionSlide3, R.id.actionSlide4, R.id.actionSlide5 };

    private RecyclerView mActionRecView;
    private CompanyActionAdapter mActionAdapter;
    private LinearLayoutManager mHorizontalLayoutManager;

    private RecyclerView mHomeMenuRecView;
    private RecyclerView.Adapter mHomeMenuAdapter;
    private int mPrevScrollState;
    private int mCurrentSlide;
    private Handler mTimer;
    private SliderJob mSliderJob;
    private Context mContext;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        initMainLayout();
    }


    private void initMainLayout() {
        initBaseActivity( new ActivityState( AppConstants.HOME_BOTTOM_INDEX ) );
//        List<CompanyActionModel> companyActionModels = GlobalManager.getInstance().getBootstrapModel()
//                                                            .getCompanyActions();
//        fillActionAdapter( companyActionModels );
        mContext = this;
        fillActionAdapter( fillCompanyAction() );
        fillHomeMenuAdapter( fillHomeMenuModel() );
        initRecyclerView();
    }


    private void initRecyclerView() {

        if ( mActionRecView == null ) {
            mActionRecView = findViewById( R.id.companyActionRVId );
            mHorizontalLayoutManager = new LinearLayoutManager( this, LinearLayoutManager.HORIZONTAL, false );
            mActionRecView.addOnScrollListener( new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged( RecyclerView recyclerView, int newState ) {
                    super.onScrollStateChanged( recyclerView, newState );
                    if ( ( ( RecyclerView.SCROLL_STATE_SETTLING == mPrevScrollState ||
                            RecyclerView.SCROLL_STATE_DRAGGING == mPrevScrollState ) &&
                            RecyclerView.SCROLL_STATE_IDLE == newState ) ||
                            ( RecyclerView.SCROLL_STATE_IDLE == mPrevScrollState &&
                                    RecyclerView.SCROLL_STATE_DRAGGING == newState ) ) {
                        changeCardAccordingToPosition();

                    }
                    mPrevScrollState = newState;
                }


                @Override
                public void onScrolled( RecyclerView recyclerView, int dx, int dy ) {
                    super.onScrolled( recyclerView, dx, dy );
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
            mActionAdapter.addItem( actions.get( i ), i );
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
            currentView[ 1 ] = currentView[ 0 ];
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
                        if ( view.getLeft() == 0 ){
                            mTimer.removeCallbacks( mSliderJob );
                            mTimer.postDelayed( mSliderJob, 4000 );
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

    @Override
    protected void onPause() {
        mTimer.removeCallbacks( mSliderJob );
        mTimer = null;
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPrevScrollState = AppConstants.FAKE_ID;
        mCurrentSlide = 0;
        changeSlideIcon( mCurrentSlide );
        PicassoClient.downloadImage( mContext,
                mActionAdapter.getItem( mCurrentSlide ).getActionImgUrl(),
                ( ImageView ) findViewById( R.id.actionBgImageId ) );
        mTimer = new Handler();
        mSliderJob = new SliderJob();
        mTimer.postDelayed( mSliderJob, 1000 );
        if( GlobalManager.getInstance().getClient() != null
                                && GlobalManager.getInstance().getClient().getUuid() != null ){
            findViewById( R.id.navigation_login ).setBackground( getResources().getDrawable( R.drawable.person_navigation ) );
            (( TextView ) mNavigationView.getHeaderView(0).findViewById( R.id.drawerLoginId ))
                                    .setText( getResources().getString( R.string.personal_area ) );
        } else {
            findViewById( R.id.navigation_login ).setBackground( getResources().getDrawable( R.drawable.login_navigation ) );
            (( TextView ) mNavigationView.getHeaderView(0).findViewById( R.id.drawerLoginId ))
                                    .setText( getResources().getString( R.string.action_login ) );
        }
    }

    private void changeSlideIcon( final int prevSlide ) {
        if ( AppConstants.FAKE_ID != prevSlide ) {
            Animation fadeOut = AnimationUtils.loadAnimation( this, R.anim.fade_out );
            fadeOut.setAnimationListener( new Animation.AnimationListener() {
                @Override
                public void onAnimationStart( Animation animation ) {
                }

                @Override
                public void onAnimationEnd( Animation animation ) {
                    findViewById( actionSlideIconIds[ prevSlide ] ).setBackground( getDrawable( R.drawable.sel_slider_white ) );
                    findViewById( actionSlideIconIds[ prevSlide ] ).setVisibility( View.VISIBLE );
                    findViewById( actionSlideIconIds[ prevSlide ] ).startAnimation(
                            AnimationUtils.loadAnimation( mContext, R.anim.fade_in ) );
                    findViewById( actionSlideIconIds[ mCurrentSlide ] ).setBackground( getDrawable( R.drawable.sel_slider_red ) );
                    findViewById( actionSlideIconIds[ mCurrentSlide ] ).startAnimation(
                            AnimationUtils.loadAnimation( EdaNymaApp.getAppContext(), R.anim.fade_in ) );
                }

                @Override
                public void onAnimationRepeat( Animation animation ) {
                }
            } );
            PicassoClient.downloadImage( mContext,
                    mActionAdapter.getItem( prevSlide ).getActionImgUrl(),
                    ( ImageView ) findViewById( R.id.actionBgImageId ) );
            findViewById( actionSlideIconIds[ prevSlide ] ).
                    startAnimation( fadeOut );
        }

    }

    private void changeSlideByTimer() {
        Animation animation = AnimationUtils.loadAnimation( mContext, R.anim.slide_r );
        animation.setAnimationListener( new Animation.AnimationListener() {
            @Override
            public void onAnimationStart( Animation animation ) {
                mActionRecView.scrollToPosition( mCurrentSlide );
            }

            @Override
            public void onAnimationEnd( Animation animation ) {
                PicassoClient.downloadImage( mContext,
                        mActionAdapter.getItem( mCurrentSlide ).getActionImgUrl(),
                        ( ImageView ) findViewById( R.id.actionBgImageId ) );

            }

            @Override
            public void onAnimationRepeat( Animation animation ) {
            }
        } );
        mActionRecView.startAnimation( animation );
        findViewById( R.id.actionBgImageId )
                .startAnimation( AnimationUtils.loadAnimation( mContext, R.anim.slide_l ) );
    }


   private class SliderJob implements  Runnable {
             @Override
        public void run() {
            int prevSlide = mCurrentSlide;
            mCurrentSlide++;
            mCurrentSlide = mCurrentSlide == 5 ? 0 : mCurrentSlide;
            changeSlideIcon( prevSlide );
            changeSlideByTimer();
            if( mTimer != null ){
                mTimer.postDelayed( mSliderJob, 4000 );
            }
        }
    }

}
