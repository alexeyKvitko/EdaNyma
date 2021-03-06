package com.edanyma.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.edanyma.AppConstants;
import com.edanyma.EdaNymaApp;
import com.edanyma.R;
import com.edanyma.fragment.BaseFragment;
import com.edanyma.model.ActivityState;
import com.edanyma.model.CompanyActionModel;
import com.edanyma.model.HomeMenuModel;
import com.edanyma.owncomponent.ModalMessage;
import com.edanyma.pixelshot.PixelShot;
import com.edanyma.recyclerview.CompanyActionAdapter;
import com.edanyma.recyclerview.HomeMenuAdapter;
import com.edanyma.recyclerview.manager.VegaLayoutManager;
import com.edanyma.utils.GlideClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.edanyma.manager.GlobalManager.getBootstrapModel;
import static com.edanyma.manager.GlobalManager.getHomeMenus;

public class MainActivity extends BaseActivity implements HomeMenuAdapter.CardClickListener, PixelShot.PixelShotListener {

    private final String TAG = "MainActivity";

    private final int[] actionSlideIconIds = new int[]{ R.id.actionSlide1, R.id.actionSlide2,
            R.id.actionSlide3, R.id.actionSlide4, R.id.actionSlide5 };

    private RecyclerView mActionRecView;
    private CompanyActionAdapter mActionAdapter;
    private LinearLayoutManager mHorizontalLayoutManager;

    private RecyclerView mHomeMenuRecView;
    private HomeMenuAdapter mHomeMenuAdapter;
    private int mPrevScrollState;
    private int mCurrentSlide;
    private Handler mTimer;
    private SliderJob mSliderJob;
    private Context mContext;
    private ImageView mProfileBtn;
    private BaseFragment mProfileFragment;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        initMainLayout();
    }


    private void initMainLayout() {
        initBaseActivity( new ActivityState( AppConstants.HOME_BOTTOM_INDEX ) );
        final MainActivity me = this;
        final ConstraintLayout mainLayout = findViewById( R.id.contentMainLayoutId );
        mProfileBtn = findViewById( R.id.navButtonId );
        mProfileBtn.setOnClickListener( ( View view ) -> {
            Animation rotate = AnimationUtils.loadAnimation( me, R.anim.icon_rotation );
            view.startAnimation( rotate );
            PixelShot.of( mainLayout ).setResultListener( me ).save();
        } );
        mContext = this;
        fillActionAdapter( getBootstrapModel()
                .getCompanyActions() );
        fillHomeMenuAdapter( getHomeMenus() );
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
        int idx = 0;
        for (CompanyActionModel action : actions ) {
            if ( AppConstants.PROMOTION_DESKTOP.equals( action.getFullScreenAction() )){
                mActionAdapter.addItem( action, idx );
                idx++;
            }
        }
    }

    private void fillHomeMenuAdapter( List< HomeMenuModel > menuModels ) {
        initAdapter();
        for ( int i = 0; i < menuModels.size(); i++ ) {
            mHomeMenuAdapter.addItem( menuModels.get( i ), i );
        }
    }


    @Override
    public void onDestroy() {
        if ( mActionRecView != null ) {
            mActionRecView.setAdapter( null );
            mActionRecView.clearOnScrollListeners();
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
                        if ( view.getLeft() == 0 ) {
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
        changeStatusHomeBtn( true );
        mPrevScrollState = AppConstants.FAKE_ID;
        mCurrentSlide = 0;
        changeSlideIcon( mCurrentSlide );
        GlideClient.downloadImage( mContext,
                mActionAdapter.getItem( mCurrentSlide ).getActionImgUrl(),
                ( ImageView ) findViewById( R.id.actionBgImageId ) );
        mTimer = new Handler();
        mSliderJob = new SliderJob();
        mTimer.postDelayed( mSliderJob, 1000 );
        initAdapter();
    }

    private void initAdapter() {
        if ( mHomeMenuAdapter == null ) {
            mHomeMenuAdapter = new HomeMenuAdapter( new ArrayList< HomeMenuModel >() );
        }
        mHomeMenuAdapter.setOnItemClickListener( this );
        mHomeMenuAdapter.notifyDataSetChanged();
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
            GlideClient.downloadImage( mContext,
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
                GlideClient.downloadImage( mContext,
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

    @Override
    public void onItemClick( int position, View view ) {
        String homeMenuTag = ( ( TextView ) view.findViewById( R.id.dishTitleTextId ) ).getText().toString();
        String homeMenuCount = ( ( TextView ) view.findViewById( R.id.dishCountTextId ) ).getText().toString();
        if ( Integer.valueOf( homeMenuCount.substring( 0, 1 ) ).equals( 0 ) ) {
            ModalMessage.show( this, "Сообщение", new String[]{ "В Вашем городе еще нет выбранных заведений" } );
        } else {
            if ( !AppConstants.ALL_DISHES.equals( homeMenuTag ) ) {
                Map< String, String > params = new HashMap<>();
                params.put( AppConstants.COMPANY_FILTER, homeMenuTag );
                startNewActivity( CompanyActivity.class, params );
            } else {
                startNewActivity( DishActivity.class );
            }

        }
    }

    public void startNavigationActivity() {
        startNewActivity( NavigationActivity.class );
    }


    private class SliderJob implements Runnable {
        @Override
        public void run() {
            int prevSlide = mCurrentSlide;
            mCurrentSlide++;
            mCurrentSlide = mCurrentSlide == 5 ? 0 : mCurrentSlide;
            changeSlideIcon( prevSlide );
            changeSlideByTimer();
            if ( mTimer != null ) {
                mTimer.postDelayed( mSliderJob, 4000 );
            }
        }
    }

}
