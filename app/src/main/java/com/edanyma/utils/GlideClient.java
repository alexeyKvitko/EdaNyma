package com.edanyma.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.edanyma.R;

public class GlideClient {

    public static void downloadImage(Context c, String url, ImageView img)
    {
        if(url != null && url.length()>0)
        {
            Glide.with(c).load(url).into(img);
        }else {
            Glide.with(c).load(R.drawable.loading).into(img);
        }
    }

    public static void downloadImage(Context c, String url, ImageView img, int width, int height)
    {
        if(url != null && url.length()>0)
        {
            Glide.with(c).load(url).override( width, height ).centerCrop().into(img);
        }else {
            Glide.with(c).load(R.drawable.loading).into(img);
        }
    }

}
