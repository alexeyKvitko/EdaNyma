package com.edanyma.fragment;

import android.graphics.Typeface;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.widget.Button;
import android.widget.TextView;

import com.edanyma.EdaNymaApp;

public class BaseFragment extends Fragment {


    //TEXT VIEW
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

    //EDIT TEXT

    protected AppCompatEditText initEditText( int editId, Typeface typeface, Integer style ){
        AppCompatEditText editText = getView().findViewById( editId );
        if( typeface != null ){
            if( style != null ){
                editText.setTypeface( typeface, style );
            } else {
                editText.setTypeface( typeface );
            }
        }
        return editText;
    }

    protected AppCompatEditText initEditText( int editId, Typeface typeface ){
        return initEditText( editId, typeface, null  );
    }

    protected AppCompatEditText initEditText( int editId  ){
        return initEditText( editId, null, null );
    }

    //TEXT LAYOUT
    protected TextInputLayout initTextInputLayout( int textLayoutId, Typeface typeface ){
        TextInputLayout textLayout = getView().findViewById( textLayoutId );
        if( typeface != null ){
                textLayout.setTypeface( typeface );
        }
        return textLayout;
    }

    protected TextInputLayout initTextInputLayout( int textLayoutId ){
        return initTextInputLayout( textLayoutId , null );
    }

    //BUTTON
    protected Button initButon( int buttonId, Typeface typeface ){
        Button button = getView().findViewById( buttonId );
        if( typeface != null ){
            button.setTypeface( typeface );
        }
        return button;
    }


}
