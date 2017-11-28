package com.egos.funny.sample;

import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import com.egos.funny.sample.view.FunnyVideoView;
import com.egos.funny.transform.FunnyPageTransformer;

/**
 * Created by Egos on 2017/11/14.
 *
 * 触发播放的时机：TextureView Available && Page 完整Visible
 * 1. onPageScrolled 的时候
 * 2. onSurfaceTextureAvailable 的时候
 *
 * 触发停止播放的时机：TextureView Destroyed || Page Item 变化
 */
public class MainActivity extends AppCompatActivity {
  private final static String TAG = "MainActivity";

  private ViewPager mViewPager;
  private MyAdapter mAdapter;
  private IPlayer mPlayer;
  private FunnyVideoView mPlayingView;

  private String[] videos;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    setContentView(R.layout.activity_main);
    mViewPager = findViewById(R.id.viewPager);
    mViewPager.setPageTransformer(true, new FunnyPageTransformer());
    mAdapter = new MyAdapter();
    mViewPager.setAdapter(mAdapter);
    mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
      }

      @Override public void onPageSelected(int position) {
        mPlayingView = mAdapter.getVideoView(position);
        if (mPlayingView != null) {
          mPlayer.openVideo(mPlayingView.getSurfaceTexture(), videos[position % videos.length],
              position);
        }
      }

      @Override public void onPageScrollStateChanged(int state) {
      }
    });

    initVideoData();
    initPlayer();
  }

  private void initVideoData() {
    videos = new String[3];
    videos[0] = "android.resource://" + getPackageName() + "/" + R.raw.video1;
    videos[1] = "android.resource://" + getPackageName() + "/" + R.raw.video2;
    videos[2] = "android.resource://" + getPackageName() + "/" + R.raw.video3;
  }

  private void initPlayer() {
    PlayerListener playerListener = new PlayerListener();
    mPlayer =
        new FunnyMediaPlayer(this, playerListener, playerListener, playerListener, playerListener);
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    mPlayer.release();
  }

  class MyAdapter extends PagerAdapter {

    private SparseArray<FunnyVideoView> mVideoViews = new SparseArray<>();

    private final static String TAG = "MyAdapter";

    @Override public int getCount() {
      return 10;
    }

    @Override public boolean isViewFromObject(View view, Object object) {
      return view == object;
    }

    @Override public Object instantiateItem(ViewGroup container, final int position) {
      View view =
          LayoutInflater.from(container.getContext()).inflate(R.layout.item_page, container, false);
      final FunnyVideoView funnyVideoView = view.findViewById(R.id.videoView);
      funnyVideoView.init(new FunnyVideoView.Callback() {
        @Override public void onSurfaceAvailable(SurfaceTexture surfaceTexture) {
          if (mViewPager.getCurrentItem() == position) {
            mPlayingView = funnyVideoView;
            mPlayer.openVideo(surfaceTexture, videos[position % videos.length], position);
            Log.e(TAG, "onSurfaceAvailable " + position);
          }
        }

        @Override public void onSurfaceDestroyed() {
          if (mViewPager.getCurrentItem() == position) {
            Log.e(TAG, "onSurfaceDestroyed " + position);
          }
        }
      });
      mVideoViews.put(position, funnyVideoView);
      container.addView(view);
      return view;
    }

    @Override public void destroyItem(ViewGroup container, int position, Object object) {
      mVideoViews.remove(position);
      container.removeView((View) object);
    }

    public FunnyVideoView getVideoView(int position) {
      return mVideoViews.get(position);
    }
  }

  class PlayerListener
      implements MediaPlayer.OnPreparedListener, MediaPlayer.OnVideoSizeChangedListener,
      MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {

    @Override public void onCompletion(MediaPlayer mediaPlayer) {
      Log.e(TAG, "onCompletion");
      if (mAdapter.getCount() - 1 > mViewPager.getCurrentItem()) {
        mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, true);
      }
    }

    @Override public boolean onError(MediaPlayer mediaPlayer, int what, int extra) {
      return false;
    }

    @Override public void onPrepared(MediaPlayer mediaPlayer) {
      mPlayer.start();
    }

    @Override public void onVideoSizeChanged(MediaPlayer mediaPlayer, int width, int height) {
      if (mPlayingView != null) {
        mPlayingView.setVideoSize(width, height);
      }
    }
  }
}
