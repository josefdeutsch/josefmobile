package com.josef.mobile.ui.main.post;


import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class GridEndlessRecyclerViewScrollListener extends RecyclerView.OnScrollListener {

    private final GridLayoutManager gridLayoutManager;
    private final DataLoader dataLoader;
    private int previousItemCount;
    private boolean loading;

    public GridEndlessRecyclerViewScrollListener(GridLayoutManager gridLayoutManager, DataLoader dataLoader) {
        this.gridLayoutManager = gridLayoutManager;
        this.dataLoader = dataLoader;
        reset();
    }


    @Override
    public void onScrolled(RecyclerView view, int dx, int dy) {
        if (dy > 0) {

            int itemCount = gridLayoutManager.getItemCount();

            if (itemCount != previousItemCount) {
                loading = false;
            }

            if (!loading && gridLayoutManager.findLastVisibleItemPosition() >= itemCount - 1) {
                previousItemCount = itemCount;
                dataLoader.onLoadMore();
            }
        }
    }

    public void reset() {
        this.loading = false;
        this.previousItemCount = -1;
    }

    public interface DataLoader {
        void onLoadMore();
    }
}

