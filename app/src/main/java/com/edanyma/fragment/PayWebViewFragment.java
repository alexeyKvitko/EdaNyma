package com.edanyma.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;

import com.edanyma.AppConstants;
import com.edanyma.R;
import com.edanyma.activity.BasketActivity;

public class PayWebViewFragment extends BaseFragment {

    private static final String PAY_URL_PARAM = "payUrlParam";

    private String mPayUrl;

    public PayWebViewFragment() {}

    public static PayWebViewFragment newInstance( String payUrl ) {
        PayWebViewFragment fragment = new PayWebViewFragment();
        Bundle args = new Bundle();
        args.putString( PAY_URL_PARAM, payUrl );
        fragment.setArguments( args );
        return fragment;
    }

    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        if ( getArguments() != null ) {
            mPayUrl = getArguments().getString( PAY_URL_PARAM );
        }
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState ) {
        // Inflate the layout for this fragment
        return inflater.inflate( R.layout.fragment_web_view, container, false );
    }

    @Override
    public void onActivityCreated( @Nullable Bundle savedInstanceState ) {
        super.onActivityCreated( savedInstanceState );
        WebView webView = getView().findViewById( R.id.webView );
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled( true );
        webView.setWebViewClient( new WebViewClient() );
        webView.loadUrl( mPayUrl );
        initTextView( R.id.webBtnBackId, AppConstants.ROBOTO_CONDENCED );
        getView().findViewById( R.id.webViewBackBtnId ).setOnClickListener( ( View view )->{
            if( getActivity() instanceof BasketActivity ){
                (( BasketActivity ) getActivity()).setPayOnlineUrl( null );
            }
            getActivity().onBackPressed();
        } );
    }
}
