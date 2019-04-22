package com.edanyma.fragment;

import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Outline;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.TypedValue;
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
import com.edanyma.activity.DishActivity;
import com.edanyma.manager.GlobalManager;
import com.edanyma.model.CompanyModel;
import com.edanyma.model.MenuCategoryModel;
import com.edanyma.owncomponent.MenuTextView;
import com.edanyma.utils.AppUtils;
import com.edanyma.utils.ConvertUtils;

import static com.edanyma.AppConstants.DECOR_CORNER_RADIUS;
import static com.edanyma.AppConstants.DECOR_HEIGHT;
import static com.edanyma.AppConstants.DECOR_LEFT_MARGIN;
import static com.edanyma.AppConstants.DECOR_WIDTH;
import static com.edanyma.AppConstants.HORIZONTAL_RATIO;
import static com.edanyma.AppConstants.MARGIN_RATIO;
import static com.edanyma.AppConstants.VERTICAL_RATIO;

public class FilterDishFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "FilterCompanyDishFragment";

    private static final int MENU_CATEGORY_PADDING_LEFT = ( int ) ConvertUtils.convertDpToPixel( 12 );
    private static final int MENU_CATEGORY_PADDING_RIGHT = ( int ) ConvertUtils.convertDpToPixel( 8 );
    private static final int MENU_CATEGORY_PADDING_TOP = ( int ) ConvertUtils.convertDpToPixel( 4 );
    private static final int MENU_CATEGORY_PADDING_BOTTOM = ( int ) ConvertUtils.convertDpToPixel( 6 );
    private static final int MENU_EXPAND = 1;
    private static final int MENU_CLOSED = 0;
    private static final int MENU_CLOSED_DISHES = 6;
    private static final int MENU_CLOSED_COMPANIES = 6;

    private OnDishFilterActionListener mListener;

    private ScrollView mScrollView;
    private Integer mSelectedDishId;
    private Integer mSelectedCompanyId;
    private TextView mShowAllDishes;
    private TextView mShowAllCompany;
    private int mDishMenuState = MENU_CLOSED;
    private int mCompanyMenuState = MENU_CLOSED;



    public FilterDishFragment() {
    }

    public static FilterDishFragment newInstance() {
        FilterDishFragment fragment = new FilterDishFragment();
        return fragment;
    }

    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState ) {
        return inflater.inflate( R.layout.fragment_filter_dish, container, false );
    }

    @Override
    public void onActivityCreated( @Nullable Bundle savedInstanceState ) {
        super.onActivityCreated( savedInstanceState );
        final RelativeLayout decorLayout = getView().findViewById( R.id.snapShotDishLayoutId );
        decorLayout.setOnClickListener( ( View view ) -> {
                backAnimateSnapShotLayout( decorLayout, false );
        } );
        mSelectedDishId = ( ( DishActivity ) getActivity() ).getSelectedDishId();
        mSelectedCompanyId = ( ( DishActivity ) getActivity() ).getSelectedCompanyId();
        fillDishFilter();
        decorLayout.setBackground( Drawable.createFromPath( AppUtils.getSnapshotPath() ) );
        initTextView( R.id.filterAllDishesTitleId, AppConstants.B52 );
        initTextView( R.id.filterAllDishesTextId, AppConstants.ROBOTO_BLACK );
        initTextView( R.id.filterCompanyTextId, AppConstants.ROBOTO_BLACK );

        mShowAllDishes = initTextView( R.id.filterBtnShowAllDishesId, AppConstants.ROBOTO_CONDENCED,
                EdaNymaApp.getAppContext().getResources().getString( R.string.show_all_dish ) );
        mShowAllDishes.setOnClickListener( this );

        mShowAllCompany = initTextView( R.id.filterBtnShowAllCompanyId, AppConstants.ROBOTO_CONDENCED,
                EdaNymaApp.getAppContext().getResources().getString( R.string.show_all_company ) );
        mShowAllCompany.setOnClickListener( this );

        mScrollView = getView().findViewById( R.id.filterAllDishesScrollId );
        mScrollView.setVisibility( View.VISIBLE );
        animateDecorLayout( decorLayout );
        getView().findViewById( R.id.filterAllDishesBackBtnId ).setOnClickListener( (View view) -> {
                AppUtils.btnClickAnimation( view );
                backAnimateSnapShotLayout( decorLayout, false );
        } );
    }

    private void fillDishFilter() {
        LinearLayout dishesListLayout = getView().findViewById( R.id.filterDishesListId );
        LinearLayout companyListLayout = getView().findViewById( R.id.filterCompanyListId );
        int idx = 0;
        for ( MenuCategoryModel menuCategory : GlobalManager.getInstance().getBootstrapModel().getDeliveryMenu().getMenuCategories() ) {
            int visibility = idx < MENU_CLOSED_DISHES ? View.VISIBLE : View.GONE;
            dishesListLayout.addView( createDishMenu( null, menuCategory.getId(), menuCategory.getDisplayName(), visibility ) );
            idx++;
        }
        idx = 0;
        for ( CompanyModel company : GlobalManager.getInstance().getBootstrapModel().getCompanies() ) {
            int visibility = idx < MENU_CLOSED_COMPANIES ? View.VISIBLE : View.GONE;
            companyListLayout.addView( createDishMenu( company.getId(), null, company.getDisplayName(), visibility ) );
            idx++;
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

    private void backAnimateSnapShotLayout( final RelativeLayout snapShotLayout, final boolean isAction ) {
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
                    if ( isAction ){
                        mListener.onDishFilterAction();
                    } else {
                        getActivity().onBackPressed();
                    }
                }
            }
        } );
        valAnimator.setDuration( 200 );
        valAnimator.start();
    }


    private TextView createDishMenu( String menuCompanyId, String menuCategoryId, String dish, int visibility ) {
        MenuTextView dishView = new MenuTextView( getActivity() );
        dishView.setVisibility( visibility );
        dishView.setTypeface( AppConstants.ROBOTO_CONDENCED );
        dishView.setTextColor( getActivity().getResources().getColor( R.color.darkWhite ) );
        dishView.setPadding( MENU_CATEGORY_PADDING_LEFT, MENU_CATEGORY_PADDING_TOP,
                MENU_CATEGORY_PADDING_RIGHT, MENU_CATEGORY_PADDING_BOTTOM );
        dishView.setTextSize( TypedValue.COMPLEX_UNIT_DIP, 16 );

        if ( menuCategoryId != null && Integer.valueOf( menuCategoryId ).equals( mSelectedDishId ) ) {
            dishView.setBackground( getActivity().getResources().getDrawable( R.drawable.border_light_blue_r18 ) );
        }
        if ( menuCompanyId != null && Integer.valueOf( menuCompanyId ).equals( mSelectedCompanyId ) ) {
            dishView.setBackground( getActivity().getResources().getDrawable( R.drawable.border_light_blue_r18 ) );
        }
        dishView.setText( dish );
        dishView.setMenuCategoryId( menuCategoryId );
        dishView.setMenuCompanyId( menuCompanyId );
        dishView.setOnClickListener( this );
        return dishView;
    }


    private void showAllDishes() {
        LinearLayout dishesListLayout = getView().findViewById( R.id.filterDishesListId );
        if ( MENU_CLOSED == mDishMenuState ) {
            for ( int idx = 0; idx < dishesListLayout.getChildCount(); idx++ ) {
                dishesListLayout.getChildAt( idx ).setVisibility( View.VISIBLE );
            }
            mShowAllDishes.setText( EdaNymaApp.getAppContext().getResources().getString( R.string.hide_all_dishes ) );
            mDishMenuState = MENU_EXPAND;
        } else {
            for ( int idx = 0; idx < dishesListLayout.getChildCount(); idx++ ) {
                int visibility = idx < MENU_CLOSED_DISHES ? View.VISIBLE : View.GONE;
                dishesListLayout.getChildAt( idx ).setVisibility( visibility );
            }
            mShowAllDishes.setText( EdaNymaApp.getAppContext().getResources().getString( R.string.show_all_dish ) );
            mDishMenuState = MENU_CLOSED;
        }
    }

    private void showAllCompany() {
        LinearLayout companyListLayout = getView().findViewById( R.id.filterCompanyListId );
        if ( MENU_CLOSED == mCompanyMenuState ) {
            for ( int idx = 0; idx < companyListLayout.getChildCount(); idx++ ) {
                companyListLayout.getChildAt( idx ).setVisibility( View.VISIBLE );
            }
            mShowAllCompany.setText( EdaNymaApp.getAppContext().getResources().getString( R.string.hide_all_company ) );
            mCompanyMenuState = MENU_EXPAND;
        } else {
            for ( int idx = 0; idx < companyListLayout.getChildCount(); idx++ ) {
                int visibility = idx < MENU_CLOSED_COMPANIES ? View.VISIBLE : View.GONE;
                companyListLayout.getChildAt( idx ).setVisibility( visibility );
            }
            mShowAllCompany.setText( EdaNymaApp.getAppContext().getResources().getString( R.string.show_all_company ) );
            mCompanyMenuState = MENU_CLOSED;
        }
    }

    @Override
    public void onAttach( Context context ) {
        super.onAttach( context );
        if ( context instanceof OnDishFilterActionListener ) {
            mListener = ( OnDishFilterActionListener ) context;
        } else {
            throw new RuntimeException( context.toString()
                    + " must implement OnDishFilterActionListener" );
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onPause() {
        super.onPause();
        ( ( BaseActivity ) getActivity() ).getHeader().setVisibility( View.VISIBLE );
        ( ( BaseActivity ) getActivity() ).getFooter().setVisibility( View.VISIBLE );
    }

    @Override
    public void onClick( View view ) {
        AppUtils.clickAnimation( view );
        switch ( view.getId() ) {
            case R.id.filterBtnShowAllDishesId:
                showAllDishes();
                break;
            case R.id.filterBtnShowAllCompanyId:
                showAllCompany();
                break;
            default:
                if ( view instanceof MenuTextView ) {
                    AppUtils.clickAnimation( view );
                    MenuTextView menuTextView =  (MenuTextView ) view;
                    if( menuTextView.getMenuCompanyId() != null ){
                        ((DishActivity)getActivity()).setSelectedCompanyId( Integer.valueOf( menuTextView.getMenuCompanyId() ) );
                    }
                    if ( menuTextView.getMenuCategoryId() != null ){
                        ((DishActivity)getActivity()).setSelectedDishId( Integer.valueOf( menuTextView.getMenuCategoryId() ) );
                    }
                    backAnimateSnapShotLayout( ( RelativeLayout ) getView().findViewById( R.id.snapShotDishLayoutId ), true );
                }
                break;

        }
    }

    public interface OnDishFilterActionListener {
        void onDishFilterAction( );
    }

}
