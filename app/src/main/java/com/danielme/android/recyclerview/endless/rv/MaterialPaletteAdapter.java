package com.danielme.android.recyclerview.endless.rv;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.danielme.android.recyclerview.endless.model.Color;
import com.danielme.android.recyclerview.endless.model.Footer;
import com.danielme.android.recyclerview.endless.R;
import com.danielme.android.recyclerview.endless.model.ItemRv;

import java.util.List;

/**
 * @author danielme.com
 */
public class MaterialPaletteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  private final List<ItemRv> data;
  private final RecyclerViewOnItemClickListener recyclerViewOnItemClickListener;

  private static final int TYPE_COLOR = 0;
  private static final int TYPE_FOOTER = 1;

  public MaterialPaletteAdapter(@NonNull List<ItemRv> data, RecyclerViewOnItemClickListener
          recyclerViewOnItemClickListener) {
    this.data = data;
    this.recyclerViewOnItemClickListener = recyclerViewOnItemClickListener;
  }

  @Override
  public int getItemViewType(int position) {
    if (data.get(position) instanceof Color) {
      return TYPE_COLOR;
    } else if (data.get(position) instanceof Footer) {
      return TYPE_FOOTER;
    } else {
      throw new RuntimeException("ItemViewType unknown");
    }
  }

  @Override
  @NonNull
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    if (viewType == TYPE_COLOR) {
      View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
      return new PaletteViewHolder(row, recyclerViewOnItemClickListener);
    } else {
      View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_footer,
              parent, false);
      return new FooterViewHolder(row);
    }
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    if (holder instanceof PaletteViewHolder) {
      Color color = (Color) data.get(position);
      ((PaletteViewHolder) holder).bindRow(color);
    }
    //FOOTER: nothing to do

  }

  @Override
  public int getItemCount() {
    return data.size();
  }

}