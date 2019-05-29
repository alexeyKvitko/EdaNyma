package com.edanyma.activity;


import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.edanyma.AppConstants;
import com.edanyma.AppPreferences;
import com.edanyma.R;
import com.edanyma.fragment.BonusFragment;
import com.edanyma.fragment.CameraFragment;
import com.edanyma.fragment.ChangePasswordFragment;
import com.edanyma.fragment.EditProfileFragment;
import com.edanyma.fragment.OwnMapFragment;
import com.edanyma.fragment.PersonalAreaFragment;
import com.edanyma.fragment.SignInFragment;
import com.edanyma.fragment.SignUpFragment;
import com.edanyma.manager.BasketOrderManager;
import com.edanyma.model.ActivityState;
import com.edanyma.model.ApiResponse;
import com.edanyma.model.OurClientModel;
import com.edanyma.owncomponent.ModalDialog;
import com.edanyma.owncomponent.ModalMessage;
import com.edanyma.pixelshot.PixelShot;
import com.edanyma.rest.RestController;
import com.edanyma.utils.AppUtils;

import retrofit2.Call;
import retrofit2.Response;

import static com.edanyma.manager.GlobalManager.getClient;
import static com.edanyma.manager.GlobalManager.getUserToken;
import static com.edanyma.manager.GlobalManager.isSignedIn;
import static com.edanyma.manager.GlobalManager.setActionConfirmed;
import static com.edanyma.manager.GlobalManager.setClient;
import static com.edanyma.manager.GlobalManager.setClientLocation;

public class PersonActivity extends BaseActivity implements SignInFragment.OnSignInListener,
        SignUpFragment.OnSignUpListener, PersonalAreaFragment.OnPersonalAreaActionListener,
        EditProfileFragment.OnEditFragmentActionListener {

    private final String TAG = "PersonActivity";

    private String mSign;
    private Activity mThis;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_person );
        initBaseActivity( new ActivityState( AppConstants.LOGIN_BOTTOM_INDEX ) );
        mThis = this;
        if ( !isSignedIn() ) {
            findViewById( R.id.navigation_login ).setBackground( getResources().getDrawable( R.drawable.login_navigation ) );
            mSign = this.getIntent().getStringExtra( AppConstants.SIGN_TYPE );
            if ( mSign == null || AppConstants.SIGN_IN.equals( mSign ) ) {
                addReplaceFragment( SignInFragment.newInstance() );
            } else {
                addReplaceFragment( SignUpFragment.newInstance( AppConstants.NEW_CLIENT ) );
            }
        } else {
            findViewById( R.id.navigation_login ).setBackground( getResources().getDrawable( R.drawable.person_navigation ) );
            addReplaceFragment( PersonalAreaFragment.newInstance() );
        }
        ( ( ImageButton ) findViewById( R.id.navButtonId ) ).setImageDrawable( getResources().getDrawable( R.drawable.ic_chevron_left_black_24dp ) );
        findViewById( R.id.mainHeaderId ).setVisibility( View.GONE );
    }

    protected void addReplaceFragment( Fragment newFragment ) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations( R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out );
        if ( getSupportFragmentManager().getFragments().size() == 0 ) {
            fragmentTransaction.add( R.id.personFragmentContainerId, newFragment );
        } else {
            fragmentTransaction.replace( R.id.personFragmentContainerId, newFragment );
            fragmentTransaction.addToBackStack( null );
        }
        fragmentTransaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        changeStatusProfileBtn( true );
    }

    @Override
    public void onSignInAction() {
        if ( mSign != null ) {
            changeClientStatus();
            setActionConfirmed( true );
            onBackPressed();
        } else {
            NavUtils.navigateUpFromSameTask( this );
        }
    }

    @Override
    public void onStartSignUpAction() {
        addReplaceFragment( SignUpFragment.newInstance( AppConstants.NEW_CLIENT ) );
    }

    private void signOut() {
        if( BasketOrderManager.isBasketEmpty() ){
            exactlySignOut();
            return;
        }
        ModalDialog.DialogParams params = ModalDialog.getDialogParms();
        params.setTitle( "Подтверждение" )
                .setMessage( "У Вас остался незавершенный заказ !" )
                .setBlueButtonText( getResources().getString( R.string.continue_order ) )
                .setBlueButtonId( R.drawable.ic_basket_fill_white_24dp )
                .setWhiteButtonText( getResources().getString( R.string.sign_out ) )
                .setWhiteButtonId( R.drawable.ic_logout_grey600_24dp );
        ModalDialog.execute( this, params ).setOnModalBtnClickListener( new ModalDialog.OnModalBtnClickListener() {
            @Override
            public void onBlueButtonClick() {
            }
            @Override
            public void onWhiteBtnClick() {
                exactlySignOut();
            }
        } );
    }

    @Override
    public void onRemoveClientFullyAction() {
        ModalDialog.DialogParams params = ModalDialog.getDialogParms();
        params.setTitle( "Подтверждение" )
                .setMessage( "Вы уверены, что хотите удалить профиль" )
                .setBlueButtonText( getResources().getString( R.string.no_it_joke ) )
                .setBlueButtonId( R.drawable.ic_emoticon_wink_outline_white_24dp )
                .setWhiteButtonText( getResources().getString( R.string.yes_sure ) )
                .setWhiteButtonId( R.drawable.ic_account_off_outline_grey600_24dp );
        ModalDialog.execute( this, params ).setOnModalBtnClickListener( new ModalDialog.OnModalBtnClickListener() {
            @Override
            public void onBlueButtonClick() {
            }
            @Override
            public void onWhiteBtnClick() {
                new RemoveOurClient().execute( );
            }
        } );
    }

    private void exactlySignOut(){
        setClient( null );
        AppPreferences.removePreference( AppConstants.OUR_CLIENT_PREF );
        AppPreferences.removePreference( AppConstants.BASKET_PREF );
        BasketOrderManager.clearBasket();
        NavUtils.navigateUpFromSameTask( this );
    }

    @Override
    public void onSignUpAction() {
        NavUtils.navigateUpFromSameTask( this );
        overridePendingTransition( R.anim.fade_in, R.anim.fade_out );
    }

    @Override
    public void onChangePasswordAction() {
        addReplaceFragment( ChangePasswordFragment.newInstance() );
    }

    @Override
    public void onChangePrimaryAddress() {
        setHeaderFooterVisibilty( View.GONE );
        boolean useGlobalLocation = false;
        if( !AppUtils.nullOrEmpty( getClient().getClientLocationModel().getStreet() ) ){
            setClientLocation( getClient().getClientLocationModel() );
            useGlobalLocation = true;
        }
        addReplaceFragment( OwnMapFragment.newInstance( useGlobalLocation ) );
    }

    @Override
    public void onSignInListener() {
        addReplaceFragment( SignInFragment.newInstance() );
    }

    @Override
    public void onSignOutAction() {
        signOut();
    }

    @Override
    public void onEditProfileAction() {
        addReplaceFragment( EditProfileFragment.newInstance() );
    }

    @Override
    public void onForgotPasswordAction() {
        addReplaceFragment( SignUpFragment.newInstance( AppConstants.FORGOT_PASSWORD ) );
    }

    @Override
    public void onBackPressed() {
        setHeaderFooterVisibilty( View.VISIBLE );
        if ( getSupportFragmentManager().getFragments().size() == 0 ) {
            NavUtils.navigateUpFromSameTask( this );
            overridePendingTransition( R.anim.fade_in, R.anim.fade_out );
        }
        super.onBackPressed();
        overridePendingTransition( R.anim.fade_in, R.anim.fade_out );
    }

    @Override
    public void onSignOutFromEditAction() {
        signOut();
    }

    @Override
    public void onGetAvatarClick() {
        addReplaceFragment( CameraFragment.newInstance() );
    }

    @Override
    public void onShowBonusesAction() {
        addReplaceFragment( BonusFragment.newInstance() );
    }

    private class RemoveOurClient extends AsyncTask< Void, Void, String > {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground( Void... arg0 ) {
            String result = null;
            try {
                OurClientModel client = getClient();
                Call< ApiResponse > removeClientCall = RestController
                        .getApi().removeClient(AppConstants.AUTH_BEARER
                                + getUserToken(), client.getUuid()  );
                Response< ApiResponse > removeClientResponse = removeClientCall.execute();
                if ( removeClientResponse.body() != null ) {
                    if( removeClientResponse.body().getStatus() == 200 ){
                        String name = client.getPhone() != null ? client.getPhone() : client.getEmail();
                        result = String.format( getResources().getString( R.string.removed_from_system ), name );
                    } else {
                        result = removeClientResponse.body().getMessage();
                    }
                } else {
                    result = getResources().getString( R.string.internal_error );
                }
            } catch ( Exception e ) {
                result = getResources().getString( R.string.internal_error );
                Log.i( TAG, e.getMessage() );
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute( String result ) {
            super.onPostExecute( result );
            ModalMessage.show( mThis, "Сообщение", new String[] {result} );
            exactlySignOut();
        }
    }
}
