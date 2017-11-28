package com.egos.funny.transform;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

/**
 * Created by Egos on 2017/11/14.
 */

public class FunnyPageTransformer implements ViewPager.PageTransformer {
  private final static String TAG = "FunnyPageTransformer";
  private final static float DEFAULT_MAX_ROTATION = 30.0f;

  @Override public void transformPage(View page, float position) {
    /**
     * 左滑
     * </p>
     * 第一页：-1.xx
     * 第二页：-0.xx
     * 第三页：0.xx
     */

    /**
     * 右滑
     * </p>
     * 第一页：-0.xx
     * 第二页：0.xx
     * 第三页：1.xx
     */

    /**
     * 似乎没有办法知道是什么情况过来的页面。
     * 1. 可否根据page 来判断。getLeft 判断？ 就是这样计算的。
     * 2. 可否根据ViewPager 判断。getScrollX?
     *
     * 角度是0-(+90)/0-(-90)
     * 旋转坐标x 轴坐标0-getWidth()/getWidth()-0
     */
    int pageWidth = page.getWidth();
    int pageHeight = page.getHeight();

    Log.d(TAG, "pageWidth " + pageWidth + ", pageHeight " + pageHeight);
    if (position < -1) { // (-Infinity, -1)
      // This page is way off-screen to the left.
      page.setRotationY(0.0f);
    } else if (position <= 0) { // [-1, 0]
      page.setPivotX(pageWidth);
      page.setPivotY(pageHeight / 2);
      page.setRotationY(DEFAULT_MAX_ROTATION * position);
    } else if (position <= 1) { // (0, 1]
      page.setPivotX(0);
      page.setPivotY(pageHeight / 2);
      page.setRotationY(DEFAULT_MAX_ROTATION * position);
    } else { // [1, +Infinity)
      // This page is way off-screen to the right.
      page.setRotationY(0.0f);
    }
  }
}
