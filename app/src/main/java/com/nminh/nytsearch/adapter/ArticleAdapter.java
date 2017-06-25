package com.nminh.nytsearch.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.nminh.nytsearch.R;
import com.nminh.nytsearch.models.Article;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ArticleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int NO_IMAGE = 0;
    private static final int HAS_IMAGE = 1;
    private final List<Article> articles;
    private final Context context;
    public Listener listener;
    private final onClickHandler clickHandler;


    public interface onClickHandler {
        void onItemClick(Article articleClicked);
    }


    public interface Listener {
        void onLoadMore();
    }


    public void setListener(Listener listener) {
        this.listener = listener;
    }


    public ArticleAdapter(Context context, onClickHandler clickHandler) {
        this.clickHandler = clickHandler;
        this.articles = new ArrayList<>();
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {

        if (hasImages(position)) {
            return HAS_IMAGE;
        }
        return NO_IMAGE;
    }

    private boolean hasImages(int position) {
        Article a = articles.get(position);
        return a.getMultimedia() != null && a.getMultimedia().size() > 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        //final ArticleAdapter.Listener activity = (Listener) context;

//        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(context);

        final View itemView;

        if (viewType == NO_IMAGE) {
            return new ViewHolder(inflater.inflate(R.layout.item_article_without_image, viewGroup, false));
        } else {
            return new ViewHolderImage(inflater.inflate(R.layout.item_article_with_image, viewGroup, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Article a = articles.get(position);

        if (holder instanceof ViewHolder) {
            bindNoImage((ViewHolder) holder, a);
        } else if (holder instanceof ViewHolderImage) {
            bindHasImage((ViewHolderImage) holder, a);
        }

/*        if (position == articles.size() - 1){
            ArticleAdapter.Listener activity = (Listener) context;
            activity.onLoadMore();
        }*/
        ArticleAdapter.Listener activity = (Listener) context;

        if ((position == (articles.size() - 1)) && (activity != null)) {
            activity.onLoadMore();
        }
    }

    private void bindHasImage(ViewHolderImage holder, Article a) {
        holder.headline.setText(a.getHeadline().getMain());
        holder.snippet.setText(a.getSnippet());

        // Get the thumbnail image link
        Article.Multimedia media = a.getMultimedia().get(0);

        int imgHeight = getScaledHeight(media);

        ViewGroup.LayoutParams layoutParams = holder.thumbnail.getLayoutParams();
        layoutParams.height = imgHeight;

        holder.thumbnail.setLayoutParams(layoutParams);

        Glide.with(context)
                .load(media.getUrl())
                .into(holder.thumbnail);
    }

    private int getScaledHeight(Article.Multimedia media) {
        int halfWidth = getScreenWidth(context) / 2;
        return media.getHeight() * halfWidth / media.getWidth();
    }

    public static int getScreenWidth(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    private void bindNoImage(ViewHolder holder, Article a) {
        holder.headline.setText(a.getHeadline().getMain());
        holder.snippet.setText(a.getSnippet());
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public void setData(List<Article> newArticles) {
        articles.clear();
        articles.addAll(newArticles);

        notifyDataSetChanged();
    }

    public void appendData(List<Article> newArticles) {
        int nextPos = articles.size();
        articles.addAll(articles.size(), newArticles);
        notifyItemRangeChanged(nextPos, newArticles.size());
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tvHeadline)
        TextView headline;

        @BindView(R.id.tvSnippet)
        TextView snippet;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Article articleClicked = articles.get(adapterPosition);
            clickHandler.onItemClick(articleClicked);
        }
    }

    public class ViewHolderImage extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tvHeadline)
        TextView headline;

        @BindView(R.id.tvSnippet)
        TextView snippet;

        @BindView(R.id.ivThumbnail)
        ImageView thumbnail;

        public ViewHolderImage(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Article articleClicked = articles.get(adapterPosition);
            clickHandler.onItemClick(articleClicked);
        }
    }
}
