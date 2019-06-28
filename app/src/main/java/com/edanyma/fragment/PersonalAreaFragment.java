package com.edanyma.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edanyma.AppConstants;
import com.edanyma.AppPreferences;
import com.edanyma.R;
import com.edanyma.activity.PersonActivity;
import com.edanyma.model.ApiResponse;
import com.edanyma.model.OurClientModel;
import com.edanyma.model.PayType;
import com.edanyma.owncomponent.ModalMessage;
import com.edanyma.owncomponent.OwnRadioGroup;
import com.edanyma.rest.RestController;
import com.edanyma.utils.AppUtils;
import com.edanyma.utils.GlideClient;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Response;

import static com.edanyma.manager.GlobalManager.getClient;
import static com.edanyma.manager.GlobalManager.getInstance;

public class PersonalAreaFragment extends BaseFragment implements View.OnClickListener {


    private OnPersonalAreaActionListener mListener;

    private OwnRadioGroup mPayTypeContainer;

    private TextView mPayTypeText;
    private TextView mPersonalEdit;

    private boolean mPayTypeOpen;

    public PersonalAreaFragment() {
    }

    public static PersonalAreaFragment newInstance() {
        PersonalAreaFragment fragment = new PersonalAreaFragment();
        return fragment;
    }

    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState ) {
        return inflater.inflate( R.layout.fragment_personal_area, container, false );
    }

    @Override
    public void onActivityCreated( @Nullable Bundle savedInstanceState ) {
        super.onActivityCreated( savedInstanceState );
        OurClientModel client = getClient();
        mPayTypeOpen = false;
        initTextView( R.id.personalTitleId, AppConstants.B52 );
        String avatarName = getActivity().getResources().getString( R.string.prompt_name );
        if ( client != null ) {
            avatarName = client.getNickName();
        }
        int avatarLayoutVisibility = View.GONE;
        LinearLayout avatarLayout = getView().findViewById( R.id.personalAreaLayoutAvatarId );
        TextView firstLatter = initTextView( R.id.personalAreaFirstLetterId, AppConstants.ROBOTO_BLACK );
        if ( AppUtils.nullOrEmpty( client.getPhoto() ) ) {
            avatarLayoutVisibility = View.VISIBLE;
            int blueIndex = client.getNickName().hashCode() % 256;
            int color = Color.argb( 180, 83, 91, blueIndex );
            avatarLayout.setBackgroundColor( color );
            firstLatter.setText( client.getNickName().substring( 0, 1 ).toUpperCase() );
        } else {
            String url = client.getPhoto() + "?time=" + ( new Date() ).getTime();
            GlideClient.downloadImage( getActivity(), url,
                    getView().findViewById( R.id.personalAreaImageAvatarId ) );
        }
        avatarLayout.setVisibility( avatarLayoutVisibility );

        initTextView( R.id.avatarNameId, AppConstants.B52, avatarName );
        initTextView( R.id.accountId, AppConstants.SANDORA, Typeface.BOLD, null );
        initTextView( R.id.personalMenuPasswordId, AppConstants.ROBOTO_CONDENCED );
        initTextView( R.id.personalMenuAddressId, AppConstants.ROBOTO_CONDENCED );
        initTextView( R.id.personalMenuOrderId, AppConstants.ROBOTO_CONDENCED );
        initTextView( R.id.personalMenuBonusId, AppConstants.ROBOTO_CONDENCED );
        initTextView( R.id.signOutId, AppConstants.ROBOTO_CONDENCED );
        initTextView( R.id.avatarAuthId, AppConstants.ROBOTO_CONDENCED,
                getClient().getEmail() != null
                        ? getClient().getEmail()
                        : getClient().getPhone() );

        mPersonalEdit = initTextView( R.id.personalMenuEditId, AppConstants.ROBOTO_CONDENCED );
        mPayTypeText = initTextView( R.id.personalMenuPayTypeId, AppConstants.ROBOTO_CONDENCED );

        mPayTypeContainer = getView().findViewById( R.id.selectPayTypeContainerId );

        setThisOnClickListener( R.id.personalMenuEditId, R.id.signOutId, R.id.personalAreaBackBtnId
                , R.id.personalMenuPasswordId, R.id.personalMenuAddressId, R.id.personalMenuPayTypeId
                , R.id.personalMenuBonusId, R.id.personalMenuOrderId );

        if( getClient().getPayType() != null  ){
            mPayTypeContainer.setPayType( getClient().getPayType() );
        }
        mPayTypeContainer.setOnPayTypeSelectListener( ( PayType payType ) -> {
            getClient().setPayType( payType.name() );
            new UpdateClientPayType().execute( getClient() );
        } );
    }

    private void showPayTypeContainer(){
        if ( mPayTypeOpen ){
            mPayTypeText.setBackground( getActivity().getResources().getDrawable( R.drawable.border_bottom ) );
            mPayTypeContainer.setVisibility( View.GONE );
            mPersonalEdit.setVisibility( View.VISIBLE );
        } else {
            mPayTypeText.setBackground( null );
            mPayTypeContainer.setVisibility( View.VISIBLE );
            mPersonalEdit.setVisibility( View.GONE );
        }
        mPayTypeOpen = !mPayTypeOpen;
    }



    @Override
    public void onAttach( Context context ) {
        super.onAttach( context );
        if ( context instanceof OnPersonalAreaActionListener ) {
            mListener = ( OnPersonalAreaActionListener ) context;
        } else {
            throw new RuntimeException( context.toString()
                    + " must implement OnPersonalAreaActionListener" );
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        ( ( PersonActivity ) getActivity() ).getHeader().setVisibility( View.GONE );
    }

    @Override
    public void onPause() {
        super.onPause();
        ( ( PersonActivity ) getActivity() ).getHeader().setVisibility( View.VISIBLE );
    }

    @Override
    public void onClick( View view ) {
        if ( mListener == null ) {
            return;
        }
        AppUtils.clickAnimation( view );
        switch ( view.getId() ) {
            case R.id.signOutId:
                mListener.onSignOutAction();
                break;
            case R.id.personalMenuEditId:
                mListener.onEditProfileAction();
                break;
            case R.id.personalMenuPasswordId:
                mListener.onChangePasswordAction();
                break;
            case R.id.personalMenuAddressId:
                mListener.onChangePrimaryAddress();
                break;
            case R.id.personalMenuBonusId:
                mListener.onShowBonusesAction();
                break;
            case R.id.personalMenuOrderId:
                mListener.onShowOrdersAction();
                break;
            case R.id.personalMenuPayTypeId:
                showPayTypeContainer();
                break;
            case R.id.personalAreaBackBtnId:
                getActivity().onBackPressed();
                break;
        }
    }


    class UpdateClientPayType extends AsyncTask< OurClientModel, Void, String > {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground( OurClientModel... ourClientModel) {
            String result = null;
            try {
                Call< ApiResponse > updateCall = RestController.getInstance()
                        .getApi().updateClientPayType( AppConstants.AUTH_BEARER
                                + getInstance().getUserToken(), ourClientModel[ 0 ] );
                Response< ApiResponse > responseUpdate = updateCall.execute();
                if ( responseUpdate.body() != null ) {
                    if ( responseUpdate.body().getStatus() == 200 ) {
                        result = responseUpdate.body().getMessage();
                        AppPreferences.setPreference( AppConstants.OUR_CLIENT_PREF, getClient() );
                    } else {
                        result = responseUpdate.body().getMessage();
                    }
                } else {
                    result = getResources().getString( R.string.internal_error );
                }
            } catch ( Exception e ) {
                result = getResources().getString( R.string.internal_error );
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute( String result ) {
            super.onPostExecute( result );
            mPayTypeContainer.enabledPayTypeButton();
            if( result != null ){
                ModalMessage.show( getActivity(), getString( R.string.title_notifications ),
                        new String[]{ result }, 1000 );
            }

        }
    }

    public interface OnPersonalAreaActionListener {
        void onSignOutAction();

        void onEditProfileAction();

        void onChangePasswordAction();

        void onChangePrimaryAddress();

        void onShowBonusesAction();

        void onShowOrdersAction();
    }
}
