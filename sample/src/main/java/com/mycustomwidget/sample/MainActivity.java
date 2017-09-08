package com.mycustomwidget.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.customwidget.myimagelooper.DepthPageTransformer;
import com.customwidget.myimagelooper.ImageLooper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.imagelooper)
    ImageLooper imagelooper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        List<Integer> imageList = new ArrayList<>();
        imageList.add(R.mipmap.emulate_fruits_big);
        imageList.add(R.mipmap.emulate_fruits_big);
        imageList.add(R.mipmap.emulate_fruits_big);
        imageList.add(R.mipmap.emulate_fruits_big);
        imagelooper.setAnimation(new DepthPageTransformer())
                .setImageLoader(new PicassoImageLoader())
                .setScrollTime(1000)
                .setList(imageList);

    }

    @Override
    protected void onStart() {
        super.onStart();
        imagelooper.setCurrentPage(0);
        imagelooper.startAutoPlay();
    }

    @Override
    protected void onStop() {
        super.onStop();
        imagelooper.stopAutoPlay();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        imagelooper.removeAllListners();
    }
}
