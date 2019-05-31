package com.edanyma.fragment;

import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.edanyma.AppConstants;
import com.edanyma.EdaNymaApp;
import com.edanyma.R;
import com.edanyma.activity.MainActivity;
import com.edanyma.utils.AppUtils;
import com.edanyma.utils.ConvertUtils;
import com.edanyma.utils.GlideClient;

import java.util.Date;

import static com.edanyma.AppConstants.DECOR_CORNER_RADIUS;
import static com.edanyma.AppConstants.DECOR_HEIGHT;
import static com.edanyma.AppConstants.DECOR_LEFT_MARGIN;
import static com.edanyma.AppConstants.DECOR_WIDTH;
import static com.edanyma.AppConstants.HORIZONTAL_RATIO;
import static com.edanyma.AppConstants.MARGIN_RATIO;
import static com.edanyma.AppConstants.VERTICAL_RATIO;
import static com.edanyma.manager.GlobalManager.*;


public class ProfileFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "ProfileFragment";

    private OnProfileFrafmentListener mListener;

    public ProfileFragment() {
    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState ) {
        return inflater.inflate( R.layout.fragment_profile, container, false );
    }

    @Override
    public void onActivityCreated( @Nullable Bundle savedInstanceState ) {
        super.onActivityCreated( savedInstanceState );
        final RelativeLayout snapshotLayout = getView().findViewById( R.id.snapShotLayoutId );
        snapshotLayout.setBackground( Drawable.createFromPath( AppUtils.getSnapshotPath() ) );
        snapshotLayout.setOnClickListener( ( View view ) -> {
            backAnimateSnapShotLayout( snapshotLayout );
        } );
        if ( isSignedIn() ) {
            int avatarLayoutVisibility = View.GONE;
            LinearLayout avatarLayout = getView().findViewById( R.id.profileLayoutAvatarId );
            TextView firstLatter = initTextView( R.id.profileFirstLetterId, AppConstants.ROBOTO_BLACK );
            if ( AppUtils.nullOrEmpty( getClient().getPhoto() ) ) {
                avatarLayoutVisibility = View.VISIBLE;
                int blueIndex = getClient().getNickName().hashCode() % 256;
                int color = Color.argb( 180, 83, 91, blueIndex );
                avatarLayout.setBackgroundColor( color );
                firstLatter.setText( getClient().getNickName().substring( 0, 1 ).toUpperCase() );
            } else {
                String url = getClient().getPhoto() + "?time=" + ( new Date() ).getTime();
                GlideClient.downloadImage( getActivity(), url,
                        getView().findViewById( R.id.profileImageAvatarId ) );
            }
            avatarLayout.setVisibility( avatarLayoutVisibility );
        }

        initTextView( R.id.profileSignInId, AppConstants.ROBOTO_CONDENCED );
        initTextView( R.id.profileSignUpId, AppConstants.ROBOTO_CONDENCED );
        initTextView( R.id.profilePersonalAreaId, AppConstants.ROBOTO_CONDENCED );
        initTextView( R.id.profileBasketId, AppConstants.ROBOTO_CONDENCED );
        initTextView( R.id.profileGiftId, AppConstants.ROBOTO_CONDENCED );
        initTextView( R.id.profileDiscountId, AppConstants.ROBOTO_CONDENCED );
        initTextView( R.id.profileSupportId, AppConstants.ROBOTO_CONDENCED );

        animateSnapShotLayout( snapshotLayout );
        setMainContainerVisibility( View.GONE );
        getView().findViewById( R.id.profileBackBtnId ).setOnClickListener( ( View view ) -> {
            AppUtils.clickAnimation( view );
            backAnimateSnapShotLayout( snapshotLayout );
        } );
        if ( isSignedIn() ) {
            swithVisibilityOnSignIn( View.GONE, View.VISIBLE );
        } else {
            swithVisibilityOnSignIn( View.VISIBLE, View.GONE );
        }
        setThisOnClickListener( R.id.profileBasketId, R.id.profileSignInId, R.id.profileSignUpId,
                R.id.profilePersonalAreaId );

    }

    private void setMainContainerVisibility( int visibility ) {
        ( ( MainActivity ) getActivity() ).setHeaderFooterVisibilty( visibility );
        getActivity().findViewById( R.id.contentMainLayoutId ).setVisibility( visibility );
    }

    private void swithVisibilityOnSignIn( int signInVisibility, int profileVisibility ) {
        getView().findViewById( R.id.profileSignHeaderId ).setVisibility( signInVisibility );
        getView().findViewById( R.id.profilePersonalHeaderId ).setVisibility( profileVisibility );
        getView().findViewById( R.id.profileMultiplyOrderId ).setVisibility( profileVisibility );
    }

    @Override
    public void onAttach( Context context ) {
        super.onAttach( context );
        if ( context instanceof OnProfileFrafmentListener ) {
            mListener = ( OnProfileFrafmentListener ) context;
        } else {
            throw new RuntimeException( context.toString()
                    + " must implement OnProfileFrafmentListener" );
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        setMainContainerVisibility( View.VISIBLE );
        getActivity().findViewById( R.id.profileFragmentContainerId ).setVisibility( View.GONE );
        getActivity().findViewById( R.id.contentMainLayoutId )
                .setBackground( EdaNymaApp.getAppContext().getResources()
                        .getDrawable( R.drawable.main_background_light ) );
    }

    private void animateSnapShotLayout( final RelativeLayout snapShotLayout ) {
        snapShotLayout.setOutlineProvider( new ViewOutlineProvider() {
            @Override
            public void getOutline( View view, Outline outline ) {
                outline.setRoundRect( 0, 0, view.getWidth(), view.getHeight(), DECOR_CORNER_RADIUS );
            }
        } );
        snapShotLayout.setClipToOutline( true );
        final FrameLayout.LayoutParams layoutParams = ( FrameLayout.LayoutParams ) snapShotLayout.getLayoutParams();
        ValueAnimator valAnimator = ValueAnimator.ofObject( new IntEvaluator(), 0, ( int ) DECOR_LEFT_MARGIN );
        valAnimator.addUpdateListener( new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate( ValueAnimator animator ) {
                float val = ( ( Integer ) animator.getAnimatedValue() ).floatValue();
                int margin = ( int ) ( val * MARGIN_RATIO );
                layoutParams.leftMargin = ( int ) val;
                layoutParams.width = ( int ) ( DECOR_WIDTH - HORIZONTAL_RATIO * val );
                layoutParams.height = ( int ) ( DECOR_HEIGHT - VERTICAL_RATIO * val );
                layoutParams.topMargin = margin;
                layoutParams.bottomMargin = margin;
                snapShotLayout.setLayoutParams( layoutParams );
            }
        } );
        valAnimator.setDuration( 200 );
        valAnimator.start();
    }

    private void backAnimateSnapShotLayout( final RelativeLayout snapShotLayout ) {
        getView().findViewById( R.id.profileSignHeaderId ).setVisibility( View.GONE );
        getView().findViewById( R.id.profilePersonalHeaderId ).setVisibility( View.GONE );
        snapShotLayout.setClipToOutline( false );
        final FrameLayout.LayoutParams layoutParams = ( FrameLayout.LayoutParams ) snapShotLayout.getLayoutParams();
        final int end = -( int ) ConvertUtils.convertDpToPixel( 0 );
        ValueAnimator valAnimator = ValueAnimator.ofObject( new IntEvaluator(), ( int ) DECOR_LEFT_MARGIN, end );
        valAnimator.addUpdateListener( new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate( ValueAnimator animator ) {
                float val = ( ( Integer ) animator.getAnimatedValue() ).floatValue();
                int margin = ( int ) ( val * MARGIN_RATIO );
                layoutParams.leftMargin = ( int ) val;
                int width = ( int ) ( DECOR_WIDTH - HORIZONTAL_RATIO * val );
                int height = ( int ) ( DECOR_HEIGHT - VERTICAL_RATIO * val );
                layoutParams.width = width > DECOR_WIDTH ? ( int ) DECOR_WIDTH : width;
                layoutParams.height = height > DECOR_HEIGHT ? ( int ) DECOR_HEIGHT : height;
                layoutParams.topMargin = margin;
                layoutParams.bottomMargin = margin;
                snapShotLayout.setLayoutParams( layoutParams );
                if ( ( int ) val == end && getActivity() != null ) {
                    getActivity().onBackPressed();
                }
            }
        } );
        valAnimator.setDuration( 200 );
        valAnimator.start();
    }

    @Override
    public void onClick( View view ) {
        if ( mListener == null ) {
            return;
        }
        AppUtils.clickAnimation( view );
        switch ( view.getId() ) {
            case R.id.profileSignInId:
            case R.id.profilePersonalAreaId:
                mListener.onProfileFragmentSignIn();
                break;
            case R.id.profileSignUpId:
                mListener.onProfileFragmentSignUp();
                break;
            case R.id.profileBasketId:
                mListener.onProfileFragmentBasket();
                break;
        }
    }


    public interface OnProfileFrafmentListener {
        void onProfileFragmentSignIn();
        void onProfileFragmentSignUp();
        void onProfileFragmentBasket();
    }
}
