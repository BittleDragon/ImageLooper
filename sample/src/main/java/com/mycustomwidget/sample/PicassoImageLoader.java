package com.mycustomwidget.sample;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.gtrsp.myimagelooper.ImageLoader;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * 轮播图需要的图片加载器
 * Created by raoxuting on 2017/4/1.
 */

public class PicassoImageLoader extends ImageLoader {

    @Override
    public void displayImage(Context context, Object imageData, ImageView imageView) {
        if(imageData instanceof String) {
            Picasso.with(context).load((String) imageData).into(imageView);
        }else if (imageData instanceof Integer) {
            Picasso.with(context).load((int) imageData).into(imageView);
        }else if (imageData instanceof Uri) {
            Picasso.with(context).load((Uri) imageData).into(imageView);
        }else if (imageData instanceof File) {
            Picasso.with(context).load((File) imageData).into(imageView);
        }
    }
}
