package com.danielme.android.recyclerview.endless;

import android.content.Context;

import androidx.annotation.ColorRes;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

import com.danielme.android.recyclerview.endless.model.Color;

import java.util.ArrayList;
import java.util.List;

public final class Colors {

  public static List<Color> buildColors(Context context) {
    List<Color> colors = new ArrayList<>(13);

    colors.add(newColor(R.string.blue, R.color.blue, context));
    colors.add(newColor(R.string.indigo, R.color.indigo, context));
    colors.add(newColor(R.string.red, R.color.red, context));
    colors.add(newColor(R.string.green, R.color.green, context));
    colors.add(newColor(R.string.orange, R.color.orange, context));
    colors.add(newColor(R.string.grey, R.color.bluegrey, context));
    colors.add(newColor(R.string.amber, R.color.teal, context));
    colors.add(newColor(R.string.deeppurple, R.color.deeppurple, context));
    colors.add(newColor(R.string.bluegrey, R.color.bluegrey, context));
    colors.add(newColor(R.string.yellow, R.color.yellow, context));
    colors.add(newColor(R.string.cyan, R.color.cyan, context));
    colors.add(newColor(R.string.brown, R.color.brown, context));
    colors.add(newColor(R.string.teal, R.color.teal, context));

    return colors;
  }

  private static Color newColor(@StringRes int string, @ColorRes int colorId, Context context) {
    String text = context.getString(string);
    String color = "#" + Integer.toHexString(ContextCompat.getColor(context, colorId)).toUpperCase()
            .substring(2);
    return new Color(text, color);
  }

}
