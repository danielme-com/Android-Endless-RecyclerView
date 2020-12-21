package com.danielme.android.recyclerview.endless;

import android.os.AsyncTask;
import android.util.Log;

import com.danielme.android.recyclerview.endless.model.Color;

import java.lang.ref.WeakReference;
import java.util.List;

public class LoadColorsAsyncTask extends AsyncTask<Void, Void, Void> {

  private static final int FAKE_DELAY = 3000;

  private final WeakReference<MainActivity> activityWR;
  private List<Color> colors;

  public LoadColorsAsyncTask(MainActivity activity) {
    this.activityWR = new WeakReference<>(activity);
  }

  @Override
  protected void onPreExecute() {
    if (activityWR.get() != null) {
      activityWR.get().displayLoadingIndicator();
    }
  }

  @Override
  protected Void doInBackground(Void... params) {
    try {
      Thread.sleep(FAKE_DELAY);
      colors = Colors.buildColors(activityWR.get());
    } catch (Exception e) {
      Log.e(this.getClass().toString(), e.getMessage());
    }
    return null;
  }

  @Override
  protected void onPostExecute(Void aVoid) {
    if (activityWR.get() != null) {
      if (colors == null || activityWR.get().forceError()) {
        activityWR.get().onError();
      } else {
        activityWR.get().displayData(colors);
      }
    }
  }

}
