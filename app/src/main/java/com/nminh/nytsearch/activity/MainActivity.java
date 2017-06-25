package com.nminh.nytsearch.activity;

import android.content.Intent;
import android.media.session.PlaybackState;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.nminh.nytsearch.R;
import com.nminh.nytsearch.adapter.ArticleAdapter;
import com.nminh.nytsearch.article_api.ArticleApi;
import com.nminh.nytsearch.article_api.SearchResult;
import com.nminh.nytsearch.models.Article;
import com.nminh.nytsearch.models.SearchRequest;
import com.nminh.nytsearch.utils.RetrofitUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements ArticleAdapter.Listener, ArticleAdapter.onClickHandler {

    private SearchRequest mSearchRequest;
    private ArticleAdapter mArticleAdapter;
    private ArticleApi mArticleApi;

    @BindView(R.id.rvListArticles) RecyclerView rvListArticles;
    @BindView(R.id.pbLoading) ProgressBar pbLoading;
    @BindView(R.id.pbLoadMore) ProgressBar pbLoadMore;

    @Override
    public void onItemClick(Article articleClicked) {
        launchURL(articleClicked.getWebUrl());
    }

    private void launchURL(String webUrl) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(webUrl)));
    }


    public interface Listener {
        void onResult(SearchResult searchResult);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        setUpApi();
        setupListArticlesView();
    }

    private void setUpApi() {
        ButterKnife.bind(this);
        mSearchRequest = new SearchRequest();
        mArticleApi = RetrofitUtils.get().create(ArticleApi.class);
    }

    private void setupListArticlesView() {
        mArticleAdapter = new ArticleAdapter(this, this);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        rvListArticles.setAdapter(mArticleAdapter);
        rvListArticles.setLayoutManager(layoutManager);
        //setSupportActionBar(toolbar);
        //fetchArticles();
        loadSearchResults();
    }

    // Executes an API call to the OpenLibrary search endpoint, parses the results
    // Converts them into an array of book objects and adds them to the adapter
    private void fetchArticles() {
        mArticleApi.search(mSearchRequest.toQueryMap()).enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                if (response.body() != null) {
                    Log.d("Minh","success");


                    mArticleAdapter.appendData(response.body().getArticles());
                    mArticleAdapter.notifyDataSetChanged();

                    pbLoading.setVisibility(View.GONE);
                    pbLoadMore.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                Log.e("Minh Error", t.getMessage());
            }
        });
    }

    private void LoadMore(){
        Log.d("Minh2"," LoadMore");
        mSearchRequest.nextPage();
        pbLoadMore.setVisibility(View.VISIBLE);
        fetchArticles();
    }

    private void setupSearchMenu(MenuItem item) {
        SearchView searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                mSearchRequest.setQuery(query);
                loadSearchResults();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void search(final Listener listener){
        mArticleApi.search(mSearchRequest.toQueryMap()).enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                if (response.body() != null) {
                    listener.onResult(response.body());
                    mArticleAdapter.appendData(response.body().getArticles());
                    mArticleAdapter.notifyDataSetChanged();
                    pbLoading.setVisibility(View.GONE);
                    pbLoadMore.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                Log.e("Minh Error", t.getMessage());
            }
        });
    }

    private void loadSearchResults() {
        Log.d("Minh3"," loadSearchResults");
        mSearchRequest.resetPage();
        pbLoading.setVisibility(View.VISIBLE);
        search((SearchResult searchResult) -> {
            mArticleAdapter.setData(searchResult.getArticles());
            rvListArticles.scrollToPosition(0);
        });
    }

    @Override
    public void onLoadMore() {
        LoadMore();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        setupSearchMenu(menu.findItem(R.id.action_search));
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_sort:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                showSetting();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public void showSetting(){
        //to be implemented!!!!!! @@@@@@
    };


}
