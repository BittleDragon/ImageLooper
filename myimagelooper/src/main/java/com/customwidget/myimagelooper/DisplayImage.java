package com.customwidget.myimagelooper;

import android.content.Context;
import android.widget.ImageView;

/**
 * 设置图片接口
 * Created by raoxuting on 2017/4/1.
 */

public interface DisplayImage {
    void loadImage(Context context, Object imageData, ImageView imageView);
}
