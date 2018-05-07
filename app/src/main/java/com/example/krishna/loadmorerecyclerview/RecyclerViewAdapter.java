package com.example.krishna.loadmorerecyclerview;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private RecyclerView mRecyclerView;
    private  Activity activity;
    private List<User> mUsers;
    private int lastVisibleItem, totalItemCount;
    private boolean isLoading;
    private OnLoadMoreListener mOnLoadMoreListener;
    private int visibleThreshold = 5;

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    public RecyclerViewAdapter(RecyclerView mRecyclerView, List<User> mUsers, Activity activity) {
        this.mRecyclerView=mRecyclerView;
        this.mUsers=mUsers;
        this.activity=activity;


        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (mOnLoadMoreListener != null) {
                        mOnLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener){
        this.mOnLoadMoreListener=mOnLoadMoreListener;

    }

    @Override
    public int getItemViewType(int position) {
        return mUsers.get(position)==null? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(activity).inflate(R.layout.layout_user_item, parent, false);
            return new LoadMoreViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(activity).inflate(R.layout.layout_loading_item, parent, false);
            return new ProggressBarViewHolder(view);
        }
        return null;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof LoadMoreViewHolder) {
        User user = mUsers.get(position);
            LoadMoreViewHolder userViewHolder = (LoadMoreViewHolder) holder;
        userViewHolder.tvName.setText(user.getName());
        userViewHolder.tvEmailId.setText(user.getEmail());
    } else if (holder instanceof ProggressBarViewHolder) {
            ProggressBarViewHolder loadingViewHolder = (ProggressBarViewHolder) holder;
        loadingViewHolder.progressBar.setIndeterminate(true);
    }

    }

    @Override
    public int getItemCount() {
        return mUsers == null ? 0 : mUsers.size();
    }


    public void setLoaded() {
        isLoading = false;
    }
    public class LoadMoreViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName;
        public TextView tvEmailId;
        public LoadMoreViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);

            tvEmailId = (TextView) itemView.findViewById(R.id.tvEmailId);
        }
    }
    public class ProggressBarViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;
        public ProggressBarViewHolder(View itemView) {
            super(itemView);

                progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar1);
            }

    }
}
