package com.egos.funny.sample;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.Surface;
import java.io.IOException;

/**
 * Created by Egos on 2017/11/26.
 *
 * 播放与TextureView 分离
 */
public class FunnyMediaPlayer implements IPlayer {
  private final MediaPlayer mMediaPlayer;
  private final Context mContext;
  /**
   * 播放的index
   */
  private int mPlayPosition = -1;

  private SurfaceTexture mSurfaceTexture;
  private Surface mSurface;

  public FunnyMediaPlayer(Context context, MediaPlayer.OnPreparedListener onPreparedListener,
      MediaPlayer.OnVideoSizeChangedListener onVideoSizeChangedListener,
      MediaPlayer.OnErrorListener onErrorListener,
      MediaPlayer.OnCompletionListener onCompletionListener) {
    mContext = context;
    mMediaPlayer = new MediaPlayer();
    mMediaPlayer.setOnPreparedListener(onPreparedListener);
    mMediaPlayer.setOnVideoSizeChangedListener(onVideoSizeChangedListener);
    mMediaPlayer.setOnErrorListener(onErrorListener);
    mMediaPlayer.setOnCompletionListener(onCompletionListener);
  }

  private void play(String url) {
    mMediaPlayer.reset();
    mSurface = new Surface(mSurfaceTexture);
    mMediaPlayer.setSurface(mSurface);
    try {
      mMediaPlayer.setDataSource(mContext, Uri.parse(url));
    } catch (IOException e) {
      e.printStackTrace();
    }
    mMediaPlayer.prepareAsync();
  }

  @Override public void start() {
    mMediaPlayer.start();
  }

  @Override public void stop() {
    mMediaPlayer.stop();
  }

  @Override public void pause() {

  }

  @Override public void release() {
    mMediaPlayer.release();
  }

  @Override public boolean isPlaying() {
    return false;
  }

  @Override public void openVideo(SurfaceTexture surfaceTexture, String url, int position) {
    if (surfaceTexture != null) {
      if (mPlayPosition != -1 && mPlayPosition != position) {
        stop();
      }
      mPlayPosition = position;
      mSurfaceTexture = surfaceTexture;
      play(url);
    }
  }

  @Override public boolean playNext() {
    return false;
  }
}
