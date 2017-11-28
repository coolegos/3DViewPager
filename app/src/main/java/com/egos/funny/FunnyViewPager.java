package com.egos.funny;

import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.animation.Interpolator;
import android.widget.Scroller;
import com.egos.funny.transform.FunnyPageTransformer;
import java.lang.reflect.Field;

/**
 * Created by Egos on 2017/11/14.
 *
 * 1. 切换的时候暂停
 * 2. 切换选中以后，重新播放选中的那个
 * 3. 播放完以后播放下一个
 * </p>
 * 要求：
 * 1. 需要使用TextureView
 * 2. 需要保证有且仅有一个TextureView
 * </p>
 * 实现：
 * 1. 封装
 * </p>
 * ViewPager 没有必要每一个Item 都使用相同的TextureView，可以在每个Page 里面使用不同的TextureView
 */
public class FunnyViewPager extends ViewPager {
  private final static String TAG = "FunnyViewPager";

  private static final int DURATION = 600;

  public FunnyViewPager(Context context) {
    this(context, null);
  }

  public FunnyViewPager(Context context, AttributeSet attrs) {
    super(context, attrs);

    setPageTransformer(true, new FunnyPageTransformer());
    init();
  }

  private void init() {
    new ViewPagerScroller(getContext()).initViewPagerScroll(this);
  }

  public class ViewPagerScroller extends Scroller {

    public ViewPagerScroller(Context context) {
      this(context, null);
    }

    public ViewPagerScroller(Context context, Interpolator interpolator) {
      this(context, interpolator,
          context.getApplicationInfo().targetSdkVersion >= Build.VERSION_CODES.HONEYCOMB);
    }

    public ViewPagerScroller(Context context, Interpolator interpolator, boolean flywheel) {
      super(context, interpolator, flywheel);
    }

    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
      super.startScroll(startX, startY, dx, dy, DURATION);
    }

    public void startScroll(int startX, int startY, int dx, int dy) {
      super.startScroll(startX, startY, dx, dy, DURATION);
    }

    public void initViewPagerScroll(ViewPager viewPager) {
      try {
        Field mScroller = ViewPager.class.getDeclaredField("mScroller");
        mScroller.setAccessible(true);
        mScroller.set(viewPager, this);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
