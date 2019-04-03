package com.edanyma.fragment;

import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.widget.TextView;

public class BaseFragment extends Fragment {

    protected TextView initTextView( int textId, Typeface typeface, Integer style, String text ){
        TextView textView = getView().findViewById( textId );
        if( typeface != null ){
            if( style != null ){
                textView.setTypeface( typeface, style );
            } else {
                textView.setTypeface( typeface );
            }
        }
        if ( text != null ){
            textView.setText( text );
        }
        return textView;
    }
    protected TextView initTextView( int textId, Typeface typeface,  String text ){
        return initTextView( textId, typeface, null, text );
    }

    protected TextView initTextView( int textId,  String text ){
        return initTextView( textId, null, text );
    }

    protected TextView initTextView( int textId,  Typeface typeface ){
        return initTextView( textId, typeface, null );
    }
    protected TextView initTextView( int textId  ){
        return initTextView( textId, null, null );
    }
}
