package com.edanyma.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edanyma.AppConstants;
import com.edanyma.AppPreferences;
import com.edanyma.EdaNymaApp;
import com.edanyma.R;
import com.edanyma.activity.PersonActivity;
import com.edanyma.manager.GlobalManager;
import com.edanyma.model.OurClientModel;
import com.edanyma.utils.AppUtils;
import com.edanyma.utils.GlideClient;
import com.google.gson.Gson;

import java.util.Date;

import retrofit2.converter.gson.GsonConverterFactory;

public class PersonalAreaFragment extends BaseFragment implements View.OnClickListener {


    private OnPersonalAreaActionListener mListener;

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
        initTextView( R.id.personalTitleId, AppConstants.SANDORA, Typeface.BOLD, null );
        String avatarName = getActivity().getResources().getString( R.string.prompt_name );
        int avatarLayoutVisibility = View.GONE;
        LinearLayout avatarLayout = getView().findViewById( R.id.personalAreaLayoutAvatarId );
        TextView firstLatter = initTextView( R.id.personalAreaFirstLetterId, AppConstants.ROBOTO_BLACK );
        OurClientModel client = GlobalManager.getInstance().getClient();
        if( AppUtils.nullOrEmpty( client.getPhoto() ) ){
            avatarName = client.getNickName();
            avatarLayoutVisibility = View.VISIBLE;
            int blueIndex = client.getNickName().hashCode() % 256;
            int color = Color.argb( 180, 83,91, blueIndex );
            avatarLayout.setBackgroundColor( color );
            firstLatter.setText( client.getNickName().substring( 0,1 ).toUpperCase() );
        } else {
            String url = client.getPhoto()+"?time="+( new Date() ).getTime();
            GlideClient.downloadImage( getActivity(), url,
                    getView().findViewById( R.id.personalAreaImageAvatarId ) );
        }
        avatarLayout.setVisibility( avatarLayoutVisibility );

        initTextView( R.id.avatarNameId, AppConstants.B52, avatarName );
        initTextView( R.id.accountId, AppConstants.SANDORA, Typeface.BOLD, null );
        initTextView( R.id.personalMenuPasswordId, AppConstants.ROBOTO_CONDENCED );
        initTextView( R.id.personalMenuAddressId, AppConstants.ROBOTO_CONDENCED );
        initTextView( R.id.personalMenuPayTypeId, AppConstants.ROBOTO_CONDENCED );
        initTextView( R.id.personalMenuBonusId, AppConstants.ROBOTO_CONDENCED );
        initTextView( R.id.personalMenuEditId, AppConstants.ROBOTO_CONDENCED );
        initTextView( R.id.signOutId, AppConstants.ROBOTO_CONDENCED );
        initTextView( R.id.avatarAuthId , AppConstants.ROBOTO_CONDENCED,
                                                GlobalManager.getClient().getEmail() != null
                                                    ? GlobalManager.getClient().getEmail()
                                                            : GlobalManager.getClient().getPhone() );
        setThisOnClickListener( new int[]{ R.id.personalMenuEditId ,R.id.signOutId,
                                            R.id.personalAreaBackBtnId} );
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
        (( PersonActivity) getActivity()).getHeader().setVisibility( View.GONE );
    }

    @Override
    public void onPause() {
        super.onPause();
        (( PersonActivity) getActivity()).getHeader().setVisibility( View.VISIBLE );
    }

    @Override
    public void onClick( View view ) {
        if ( mListener == null ){
            return;
        }
        AppUtils.clickAnimation( view );
        switch ( view.getId() ){
            case R.id.signOutId :
                mListener.onSignOutAction();
                break;
            case R.id.personalMenuEditId:
                mListener.onEditProfileAction();
                break;
            case R.id.personalAreaBackBtnId:
                getActivity().onBackPressed();
                break;
        }
    }

    public interface OnPersonalAreaActionListener {
        void onSignOutAction();
        void onEditProfileAction();
    }
}
