package com.edanyma.fragment;

import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Outline;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.edanyma.AppConstants;
import com.edanyma.EdaNymaApp;
import com.edanyma.R;
import com.edanyma.activity.BaseActivity;
import com.edanyma.activity.CompanyDishActivity;
import com.edanyma.manager.GlobalManager;
import com.edanyma.model.CompanyMenu;
import com.edanyma.model.FilterDishModel;
import com.edanyma.model.MenuCategoryModel;
import com.edanyma.model.MenuTypeModel;
import com.edanyma.owncomponent.MenuTextView;
import com.edanyma.utils.AppUtils;
import com.edanyma.utils.ConvertUtils;

import java.util.List;

import static com.edanyma.AppConstants.DECOR_CORNER_RADIUS;
import static com.edanyma.AppConstants.DECOR_HEIGHT;
import static com.edanyma.AppConstants.DECOR_LEFT_MARGIN;
import static com.edanyma.AppConstants.DECOR_WIDTH;
import static com.edanyma.AppConstants.HORIZONTAL_RATIO;
import static com.edanyma.AppConstants.MARGIN_RATIO;
import static com.edanyma.AppConstants.VERTICAL_RATIO;
import static com.edanyma.manager.GlobalManager.*;


public class FilterCompanyDishFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "FilterCompanyDishFragment";

    private static final String COMPANY_MENU_PARAM = "company_menu";

    private static final int MENU_TYPE_TOP_MARGIN = ( int ) ConvertUtils.convertDpToPixel( 16 );
    private static final int MENU_TYPE_BOTTOM_MARGIN = ( int ) ConvertUtils.convertDpToPixel( 8 );

    private static final int MENU_CATEGORY_PADDING_LEFT = ( int ) ConvertUtils.convertDpToPixel( 12 );
    private static final int MENU_CATEGORY_PADDING_RIGHT = ( int ) ConvertUtils.convertDpToPixel( 8 );
    private static final int MENU_CATEGORY_PADDING_TOP = ( int ) ConvertUtils.convertDpToPixel( 4 );
    private static final int MENU_CATEGORY_PADDING_BOTTOM = ( int ) ConvertUtils.convertDpToPixel( 6 );

    private ScrollView mScrollView;


    private OnApplyDishFilterListener mListener;

    private List< MenuTypeModel > mCompanyMenus;

    public FilterCompanyDishFragment() {
    }

    public static FilterCompanyDishFragment newInstance( CompanyMenu companyMenu ) {
        FilterCompanyDishFragment fragment = new FilterCompanyDishFragment();
        Bundle args = new Bundle();
        args.putSerializable( COMPANY_MENU_PARAM, companyMenu );
        fragment.setArguments( args );
        return fragment;
    }

    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        if ( getArguments() != null ) {
            CompanyMenu menu = ( CompanyMenu ) getArguments().getSerializable( COMPANY_MENU_PARAM );
            mCompanyMenus = menu.getCompanyMenus();
        }
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState ) {
        return inflater.inflate( R.layout.fragment_filter_company_dish, container, false );
    }

    @Override
    public void onActivityCreated( @Nullable Bundle savedInstanceState ) {
        super.onActivityCreated( savedInstanceState );
        final RelativeLayout decorLayout = getView().findViewById( R.id.decorLayoutId );
        decorLayout.setOnClickListener( ( View view ) -> {
            backAnimateSnapShotLayout( decorLayout );
        } );
        decorLayout.setBackground( Drawable.createFromPath( AppUtils.getSnapshotPath() ) );
        initTextView( R.id.companyMenuTitleId, AppConstants.B52 );
        mScrollView = getView().findViewById( R.id.filterDishScrollId );
        mScrollView.setVisibility( View.VISIBLE );
        fillCompanyMenu();
        animateDecorLayout( decorLayout );
        getView().findViewById( R.id.filterBackBtnId ).setOnClickListener( (View view) -> {
                AppUtils.btnClickAnimation( view );
                backAnimateSnapShotLayout( decorLayout );
        } );
    }

    private void fillCompanyMenu() {
        LinearLayout companyMenuLayout = getView().findViewById( R.id.filterCompanyMenuId );
        for ( MenuTypeModel menuType : mCompanyMenus ) {
            companyMenuLayout.addView( createKitchenMenu( menuType.getDisplayName() ) );
            for ( MenuCategoryModel menuCategory : menuType.getMenuCategories() ) {
                companyMenuLayout.addView( createDishMenu( menuType.getId(), menuCategory.getId(),
                        menuCategory.getDisplayName() ) );
            }
        }
    }

    private void animateDecorLayout( final RelativeLayout decorLayout ) {
        decorLayout.setOutlineProvider( new ViewOutlineProvider() {
            @Override
            public void getOutline( View view, Outline outline ) {
                outline.setRoundRect( 0, 0, view.getWidth(), view.getHeight(), DECOR_CORNER_RADIUS );
            }
        } );
        decorLayout.setClipToOutline( true );
        final FrameLayout.LayoutParams layoutParams = ( FrameLayout.LayoutParams ) decorLayout.getLayoutParams();
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
                decorLayout.setLayoutParams( layoutParams );
            }
        } );
        valAnimator.setDuration( 200 );
        valAnimator.start();
    }

    private void backAnimateSnapShotLayout( final RelativeLayout snapShotLayout ) {
        snapShotLayout.setClipToOutline( false );
        mScrollView.setVisibility( View.GONE );
        final FrameLayout.LayoutParams layoutParams = ( FrameLayout.LayoutParams ) snapShotLayout.getLayoutParams();
        final int end = -( int ) ConvertUtils.convertDpToPixel( 4 );
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

    public void applyDishFilter() {
        if ( mListener != null ) {
            mListener.onApplyDishFiler();
        }
    }

    @Override
    public void onAttach( Context context ) {
        super.onAttach( context );
        if ( context instanceof OnApplyDishFilterListener ) {
            mListener = ( OnApplyDishFilterListener ) context;
        } else {
            throw new RuntimeException( context.toString()
                    + " must implement OnApplyDishFilterListener" );
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        ( ( CompanyDishActivity ) getActivity() ).setHeaderFooterVisibilty( View.VISIBLE );
        getActivity().findViewById( R.id.dishContainerId )
                .setBackground( EdaNymaApp.getAppContext().getResources()
                        .getDrawable( R.drawable.main_background_light ) );
    }

    private TextView createKitchenMenu( String kitchen ) {
        TextView kitchenView = new TextView( getActivity() );
        kitchenView.setTypeface( AppConstants.ROBOTO_BLACK );
        kitchenView.setTextColor( getActivity().getResources().getColor( R.color.lightGrayColor ) );
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT );
        params.setMargins( 0, MENU_TYPE_TOP_MARGIN, 0, MENU_TYPE_BOTTOM_MARGIN );
        kitchenView.setLayoutParams( params );
        kitchenView.setCompoundDrawables( EdaNymaApp.getAppContext().getResources().getDrawable( R.drawable.ic_room_service_outline_white_18dp ),
                null, null, null );
        kitchenView.setGravity( Gravity.LEFT );
        kitchenView.setTextSize( TypedValue.COMPLEX_UNIT_DIP, 18 );
        kitchenView.setText( kitchen );
        return kitchenView;
    }

    private TextView createDishMenu( String menuTypeId, String menuCategoryId, String dish ) {
        MenuTextView dishView = new MenuTextView( getActivity() );
        dishView.setTypeface( AppConstants.ROBOTO_CONDENCED );
        dishView.setTextColor( getActivity().getResources().getColor( R.color.darkWhite ) );
        dishView.setPadding( MENU_CATEGORY_PADDING_LEFT, MENU_CATEGORY_PADDING_TOP,
                MENU_CATEGORY_PADDING_RIGHT, MENU_CATEGORY_PADDING_BOTTOM );
        dishView.setTextSize( TypedValue.COMPLEX_UNIT_DIP, 16 );

        if ( getDishFilter() != null
                && getDishFilter().getKitchenId().equals( menuTypeId )
                && getDishFilter().getDishId().equals( menuCategoryId ) ) {
            dishView.setBackground( getActivity().getResources().getDrawable( R.drawable.border_light_blue_r18 ) );
        }
        dishView.setText( dish );
        dishView.setMenuCategoryId( menuCategoryId );
        dishView.setMenuTypeId( menuTypeId );
        dishView.setOnClickListener( this );
        return dishView;
    }


    @Override
    public void onClick( View view ) {
        if ( view instanceof MenuTextView ) {
            AppUtils.clickAnimation( view );
            MenuTextView menuTextView = ( MenuTextView ) view;
            setDishFilter( new FilterDishModel( menuTextView.getMenuTypeId(),
                    menuTextView.getMenuCategoryId(), menuTextView.getText().toString() ) );
            new Handler().postDelayed( new Runnable() {
                @Override
                public void run() {
                    backAnimateSnapShotLayout( ( RelativeLayout ) getView().findViewById( R.id.decorLayoutId ) );
                }
            }, 100 );

        }
    }


    public interface OnApplyDishFilterListener {
        void onApplyDishFiler();
    }
}
