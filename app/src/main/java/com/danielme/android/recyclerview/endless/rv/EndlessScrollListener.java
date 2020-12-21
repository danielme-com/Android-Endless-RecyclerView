package com.danielme.android.recyclerview.endless.rv;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class EndlessScrollListener extends RecyclerView.OnScrollListener {

  private static final int MIN_POSITION_TO_END = 2;

  private final ScrollerClient scrollerClient;
  private boolean loadMore;

  public interface ScrollerClient {
    void loadData();
  }

  public EndlessScrollListener(ScrollerClient scrollerClient) {
    this.scrollerClient = scrollerClient;
    this.loadMore = true;
  }

  @Override
  public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
    super.onScrolled(recyclerView, dx, dy);
    if (loadMore) {
      LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
      //position starts at 0
      if (layoutManager.findLastCompletelyVisibleItemPosition()
              >= layoutManager.getItemCount() - MIN_POSITION_TO_END) {
        loadMore = false;
        scrollerClient.loadData();
      }
    }
  }

  public void loadMore() {
    loadMore = true;
  }

}
