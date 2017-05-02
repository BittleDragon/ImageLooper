package com.gtrsp.myimagelooper;

import android.content.Context;
import android.widget.ImageView;

/**
 * Created by raoxuting on 2017/4/1.
 */

public abstract class ImageLoader implements DisplayImage {

    public abstract void displayImage(Context context, Object imageData, ImageView imageView);

    @Override
    public void loadImage(Context context, Object imageData, ImageView imageView) {
        displayImage(context, imageData, imageView);
    }
}
