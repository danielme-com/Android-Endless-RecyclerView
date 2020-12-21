package com.danielme.android.recyclerview.endless.rv;

import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.danielme.android.recyclerview.endless.model.Color;
import com.danielme.android.recyclerview.endless.R;

class PaletteViewHolder extends RecyclerView.ViewHolder implements View
        .OnClickListener {

  private final View circleView;
  private final TextView titleTextView;
  private final TextView subtitleTextView;
  private final RecyclerViewOnItemClickListener listener;

  PaletteViewHolder(@NonNull View itemView, @NonNull RecyclerViewOnItemClickListener listener) {
    super(itemView);
    circleView = itemView.findViewById(R.id.circleView);
    titleTextView = itemView.findViewById(R.id.titleTextView);
    subtitleTextView = itemView.findViewById(R.id.subtitleTextView);
    this.listener = listener;
    itemView.setOnClickListener(this);
  }

  void bindRow(@NonNull Color color) {
    titleTextView.setText(color.getName());
    subtitleTextView.setText(color.getHex());
    GradientDrawable gradientDrawable = (GradientDrawable) circleView.getBackground();
    int colorId = android.graphics.Color.parseColor(color.getHex());
    gradientDrawable.setColor(colorId);
  }

  @Override
  public void onClick(View v) {
    listener.onClick(v, getAdapterPosition());
  }

}