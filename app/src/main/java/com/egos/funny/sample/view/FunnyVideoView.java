package com.egos.funny.sample.view;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.TextureView;
import android.widget.FrameLayout;

/**
 * Created by Egos on 2017/11/26.
 *
 * 封装播放View。
 */
public class FunnyVideoView extends FrameLayout {
  private final static String TAG = "FunnyVideoView";

  private TextureView textureView;
  private Callback mCallback;
  private SurfaceTexture mSurfaceTexture;

  private int mVideoWidth;
  private int mVideoHeight;

  public FunnyVideoView(@NonNull Context context) {
    this(context, null);
  }

  public FunnyVideoView(@NonNull Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public FunnyVideoView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initView();
  }

  private void initView() {
    textureView = new TextureView(getContext());
    LayoutParams layoutParams =
        new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    layoutParams.gravity = Gravity.CENTER;
    addView(textureView, layoutParams);
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    int width = MeasureSpec.getSize(widthMeasureSpec);
    int height = MeasureSpec.getSize(heightMeasureSpec);
    if (mVideoWidth != 0 && mVideoHeight != 0) {
      if (mVideoHeight * width > height * mVideoWidth) {
        width = height * mVideoWidth / mVideoHeight;
      } else if (mVideoHeight * width < height * mVideoWidth) {
        height = mVideoHeight * width / mVideoWidth;
      }
    }
    textureView.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
        MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
  }

  public void init(Callback callback) {
    mCallback = callback;
    textureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
      @Override
      public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
        mSurfaceTexture = surfaceTexture;
        mCallback.onSurfaceAvailable(surfaceTexture);
        Log.e(TAG, width + ", " + height);
      }

      @Override public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int width,
          int height) {
      }

      @Override public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        mSurfaceTexture = null;
        mCallback.onSurfaceDestroyed();
        return false;
      }

      @Override public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

      }
    });
  }

  public void setVideoSize(int width, int height) {
    mVideoWidth = width;
    mVideoHeight = height;
    requestLayout();
  }

  public SurfaceTexture getSurfaceTexture() {
    return mSurfaceTexture;
  }

  public interface Callback {
    void onSurfaceAvailable(SurfaceTexture surfaceTexture);

    void onSurfaceDestroyed();
  }
}
