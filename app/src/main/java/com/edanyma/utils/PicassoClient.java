package com.edanyma.utils;

import android.content.Context;
import android.widget.ImageView;

import com.edanyma.R;
import com.squareup.picasso.Picasso;

public class PicassoClient {

    public static void downloadImage(Context c, String url, ImageView img)
    {
        if(url != null && url.length()>0)
        {
            Picasso.with(c).load(url).into(img);
        }else {
            Picasso.with(c).load(R.drawable.loading).into(img);
        }
    }

    public static void preloadImage(Context c, String url ){
        if(url != null && url.length() > 0 )
        {
            Picasso.with(c).load( url );

        }
    }

}
