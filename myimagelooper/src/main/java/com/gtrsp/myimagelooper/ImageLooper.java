package com.gtrsp.myimagelooper;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 轮播图控件
 * Created by raoxuting on 2017/3/31.
 */

public class ImageLooper extends RelativeLayout {

    //    private float Default_Height = 150;
    private long Default_delayTime = 3000;
    private int Default_scrollTime = 1000;
    private int indicator_gravity;//默认为居中，1：居左；2：居中；3：居右

    private Context context;
    private LinearLayout llIndicators;
    private int currentPosition = 1;
    private int selectedIndicator = 0;
    private int formerIndicator = 0;
    private List<ImageView> indicatorlist;
    private ViewPager viewPager;
    private List originList;
    private MyOnPageChangeListner pageChangeListner;
    private ImageLoader imageLoader;
    private Myadapter myadapter;
    private ViewPagerScroller scroller;
    private OnPageClickListener onPageClickListener;

    public void setOnPageClickListener(OnPageClickListener onPageClickListener) {
        this.onPageClickListener = onPageClickListener;
    }

    public void setImageLoader(ImageLoader imageLoader) {
        this.imageLoader = imageLoader;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (originList != null) {
                currentPosition = currentPosition % (originList.size() + 1) + 1;
                if (currentPosition == 1) {
                    viewPager.setCurrentItem(currentPosition, false);
                    handler.sendEmptyMessage(0);
                } else {
                    viewPager.setCurrentItem(currentPosition);
                    handler.sendEmptyMessageDelayed(0, Default_delayTime);
                }
            }

        }
    };

    public interface OnPageClickListener {
        void onPageClick(int position);
    }

    /**
     * 设置轮播间隔时间，即时生效
     *
     * @param delayTime
     */
    public ImageLooper setDelayTime(long delayTime) {
        Default_delayTime = delayTime;
        return this;
    }

    /**
     * 设置显示哪一页
     *
     * @param currentPosition
     */
    public void setCurrentPage(int currentPosition) {
        if (viewPager != null)
            viewPager.setCurrentItem(currentPosition + 1);
    }

    public ImageLooper(Context context) {
        this(context, null);
    }

    public ImageLooper(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ImageLooper);
        indicator_gravity = a.getInt(R.styleable.ImageLooper_indicator_gravity, 2);
        a.recycle();
        initChild();
    }

    private void initChild() {
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        viewPager = new ViewPager(context);
        viewPager.setLayoutParams(params);

        //viewpager初始化
        setScrollTime(Default_scrollTime);
        //触摸时停止轮播
        viewPager.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        handler.removeMessages(0);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        handler.removeMessages(0);
                        break;
                    case MotionEvent.ACTION_UP:
                        handler.sendEmptyMessageDelayed(0, 3000);
                        break;
                }
                return false;
            }
        });

        //指示器初始化
        llIndicators = new LinearLayout(context);
        LayoutParams layoutParams1 = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        layoutParams1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        switch (indicator_gravity) {
            case 1:
                layoutParams1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                break;
            case 2:
                layoutParams1.addRule(RelativeLayout.CENTER_HORIZONTAL);
                break;
            case 3:
                layoutParams1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                break;
        }
        llIndicators.setLayoutParams(layoutParams1);

        this.addView(viewPager);
        this.addView(llIndicators);
    }

    public ImageLooper setAnimation(ViewPager.PageTransformer pageTransformer) {
        if (viewPager != null)
            viewPager.setPageTransformer(true, pageTransformer);
        return this;

    }

    /**
     * 设置轮播滑动时间，默认1s
     */
    public ImageLooper setScrollTime(int scrollTime) {
        if (scroller == null)
            scroller = new ViewPagerScroller(context);
        scroller.setScrollDuration(scrollTime);
        scroller.initViewPagerScroll(viewPager);
        return this;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        if (mode == MeasureSpec.AT_MOST) {
            //如果布局中高度是wrap_content,那么把控件高度设置为0
            setMeasuredDimension(widthMeasureSpec, 0);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        //设置指示器距离底部和左右的距离
        LayoutParams layoutParams = (LayoutParams) llIndicators.getLayoutParams();
        switch (indicator_gravity) {
            case 1:
                layoutParams.setMargins(dip2px(20), 0, 0, (int) (b * 0.05f));
                break;
            case 2:
                layoutParams.setMargins(0, 0, 0, (int) (b * 0.05f));
                break;
            case 3:
                layoutParams.setMargins(0, 0, dip2px(20), (int) (b * 0.05f));
                break;
        }
        llIndicators.setLayoutParams(layoutParams);
    }

    /**
     * 设置数据
     */
    public void setList(List list) {
        originList = list;

        List<View> imageList = getImageViewList(list);

        myadapter = new Myadapter(imageList);
        viewPager.setOffscreenPageLimit(list.size() - 1);
        viewPager.setAdapter(myadapter);

        viewPager.setCurrentItem(currentPosition);
        pageChangeListner = new MyOnPageChangeListner();
        viewPager.addOnPageChangeListener(pageChangeListner);

        initPageIndicator();
    }


    @NonNull
    private List<View> getImageViewList(List list) {
        List<View> imageList = new ArrayList<>();
        for (int i = 0; i < list.size() + 2; i++) {
            if (i == 0) {
                //添加最后一张图片
                imageList.add(setImage(list.get(list.size() - 1)));
            } else if (i == list.size() + 1) {
                //最后一张添加第一张图片
                imageList.add(setImage(list.get(0)));
            } else {
                imageList.add(setImage(list.get(i - 1)));
            }
        }
        return imageList;
    }

    public void upDateList(List list) {
        stopAutoPlay();
        originList = list;
        currentPosition = 1;

        List<View> imageList = getImageViewList(list);
        initPageIndicator();

        if (myadapter != null && viewPager != null) {
            myadapter.setImageList(imageList);
            myadapter.notifyDataSetChanged();
            viewPager.setCurrentItem(currentPosition);
            viewPager.setOffscreenPageLimit(list.size() - 1);
            startAutoPlay();
        }

    }

    private View setImage(Object data) {
        ImageView imageView = new ImageView(context);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(params);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        if (imageLoader != null)
            imageLoader.loadImage(context, data, imageView);
        return imageView;
    }

    private void initPageIndicator() {
        llIndicators.removeAllViews();
        indicatorlist = new ArrayList<>();
        for (int i = 0; i < originList.size(); i++) {
            ImageView indicators = new ImageView(context);
            indicators.setImageResource(R.drawable.shape_point_white_alpha);//默认使用白色半透明圆点

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            if (i > 0) {
                layoutParams.leftMargin = dip2px(10);
            }
            indicators.setLayoutParams(layoutParams);

            indicatorlist.add(indicators);

            llIndicators.addView(indicators);
        }
        //选中使用黑色圆点
        indicatorlist.get(selectedIndicator).setImageResource(R.drawable.shape_point_black);
    }


    /**
     * 将 dip 或 dp 转换为 px, 保证尺寸大小不变
     *
     * @param dipValue
     * @return
     */
    public int dip2px(float dipValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    class MyOnPageChangeListner implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            if (position == 0) {
                selectedIndicator = originList.size() - 1;
            }else if (position == originList.size() + 1) {
                selectedIndicator = 0;
            }else {
                currentPosition = position;
                selectedIndicator = position - 1;
            }
            //更改指示器状态
            indicatorlist.get(formerIndicator).setImageResource(R.drawable.shape_point_white_alpha);
            indicatorlist.get(selectedIndicator).setImageResource(R.drawable.shape_point_black);
            formerIndicator = selectedIndicator;
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (originList == null) {
                return;
            }
            currentPosition = viewPager.getCurrentItem();
            if (state == ViewPager.SCROLL_STATE_IDLE || state == ViewPager.SCROLL_STATE_DRAGGING) {
                if (currentPosition == 0) {
                    currentPosition = originList.size();
                    viewPager.setCurrentItem(currentPosition, false);
                }else if (currentPosition == originList.size() + 1) {
                    currentPosition = 1;
                    viewPager.setCurrentItem(currentPosition, false);
                }

            }
        }
    }

    public void startAutoPlay() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                handler.sendEmptyMessageDelayed(0, 3000);
            }
        }.start();
    }

    public void stopAutoPlay() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                handler.removeMessages(0);
            }
        }.start();
    }

    class Myadapter extends PagerAdapter {

        private List<View> imageList;

        public Myadapter(List<View> list) {
            imageList = list;
        }

        public void setImageList(List<View> imageList) {
            this.imageList = imageList;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            imageList.get(position).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onPageClickListener != null)
                        onPageClickListener.onPageClick(selectedIndicator);
                }
            });
            container.addView(imageList.get(position));
            return imageList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return imageList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    public void removeAllListners() {
        handler.removeCallbacksAndMessages(null);
        viewPager.removeOnPageChangeListener(pageChangeListner);
    }
}
