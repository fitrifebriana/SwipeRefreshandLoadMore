package id.or.codelabs.swiperefreshandloadmore;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

/**
 * Created by FitriFebriana on 10/3/2016.
 */
public class LoadMoreListView extends ListView implements AbsListView.OnScrollListener {

    private static final String TAG = LoadMoreListView.class.getSimpleName();
    private OnScrollListener onScrollListener;
    private LayoutInflater inflater;
    private View footerView;
    private View loadMoreStatusView;
    private OnLoadMoreListener onLoadMoreListener;
    private boolean isLoadingMore;
    private int currentScrollState;
    
    
    public LoadMoreListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LoadMoreListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        footerView = (RelativeLayout)inflater.inflate(R.layout.loadmore_view, this, false);
        loadMoreStatusView = footerView.findViewById(R.id.load_more_progress_bar);
        addFooterView(footerView);
        setLoading(false);

        super.setAdapter(adapter);
    }

    private void init(Context context) {
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        super.setOnScrollListener(this);
    }

    public void setLoadMoreStatusView(View view, int statusViewId){
        removeFooterView(footerView);

        footerView = view;
        loadMoreStatusView = footerView.findViewById(statusViewId);
        addFooterView(footerView);
    }

    public void setLoadMoreStatusView(View view){
        removeFooterView(footerView);
        footerView = view;
        loadMoreStatusView = footerView.findViewById(R.id.load_more_progress_bar);
        addFooterView(footerView);
    }

    @Override
    public void setOnScrollListener(AbsListView.OnScrollListener onScrollListener){
        this.onScrollListener = onScrollListener;
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
        currentScrollState = scrollState;

        if (onScrollListener != null){
            onScrollListener.onScrollStateChanged(absListView, scrollState);
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (onScrollListener != null){
            onScrollListener.onScroll(absListView, firstVisibleItem, visibleItemCount, totalItemCount);
        }
        if (visibleItemCount == totalItemCount){
            if (loadMoreStatusView != null){
                loadMoreStatusView.setVisibility(View.GONE);
            }
            return;
        }

        if (onLoadMoreListener != null){
            boolean loadMore = firstVisibleItem + visibleItemCount >= totalItemCount;
            if (!isLoadingMore && loadMore && currentScrollState != SCROLL_STATE_IDLE){
                setLoading(true);
                onLoadMore();
            }
        }

    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener){
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public void onLoadMore() {
        Log.d(TAG, "onLoadMore");
        if (onLoadMoreListener != null){
            onLoadMoreListener.onLoadMore();
        }

    }

    public void setLoading(boolean loading) {
        Log.d(TAG, "setLoading" + loading);
        isLoadingMore = loading;
        loadMoreStatusView.setVisibility(loading?View.VISIBLE:View.GONE);
    }

    public void onLoadMoreComplete(){
        setLoading(false);
    }

    public interface OnLoadMoreListener{
        void onLoadMore();
    }
}
