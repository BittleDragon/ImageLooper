# ImageLooper
轮播图控件

##用法

#### 步骤1
引用本地库
```groovy
compile project(':myimagelooper')
```

#### 步骤2
布局
```xml
<com.gtrsp.myimagelooper.ImageLooper
        android:id="@+id/imagelooper"
        android:layout_width="match_parent"
        android:layout_height="高度自己设置"/>

```

#### 步骤3
重写图片加载器,下面是一个使用Picasso加载的例子
```java
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
```

#### 步骤4
##### 1.简单使用
```java
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imagelooper.setImageLoader(new PicassoImageLoader())
                   .setList(imageList);
        imagelooper.startAutoPlay;

    }

}
    

```
##### 2.详细设置方法
```java
1.设置轮播间隔时间
imagelooper.setDelayTime(long delayTime)

2.手动设置显示哪一页
imagelooper.setCurrentPage(int currentPosition)

3.设置动画,目前只提供了DepthPageTransformer和AccordionTransformer两种,你可以自己写PageTransformer来实现你想要的动画
imagelooper.setAnimation(ViewPager.PageTransformer pageTransformer)

4.设置轮播滑动时间,默认1s,单位毫秒
imagelooper.setScrollTime(int scrollTime)

5.更新数据集合
imagelooper.upDataList(List list)

6.启动轮播,建议在onStart()中调用
imagelooper.startAutoPlay();

7.停止轮播, 建议在onStop()中调用
imagelooper.stopAutoPlay();

8.移除所有监听,防止内存泄漏, 建议在onDestroy()中调用
imagelooper.removeAllListners()
```
