package com.egos.funny.sample;

import android.graphics.SurfaceTexture;

/**
 * Created by Egos on 2017/11/26.
 */
public interface IPlayer {
  void start();

  void stop();

  void pause();

  void release();

  boolean isPlaying();

  void openVideo(SurfaceTexture surfaceTexture, String url, int position);

  boolean playNext();
}
