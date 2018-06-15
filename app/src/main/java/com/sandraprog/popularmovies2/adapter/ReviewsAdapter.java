package com.sandraprog.popularmovies2.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sandraprog.popularmovies2.R;
import com.sandraprog.popularmovies2.model.Review;

import java.util.List;

/**
 * Created by sandrapog on 11.06.2018.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsViewHolder> {
    private List<Review> mReviewList;
    private Context mContext;

    public ReviewsAdapter(Context context, List<Review> mReviewList) {
        this.mReviewList = mReviewList;
        mContext = context;
    }

    @Override
    public ReviewsAdapter.ReviewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_row, parent, false);
        return new ReviewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewsAdapter.ReviewsViewHolder viewHolder, int position) {
        viewHolder.mReviewContent.setText(mReviewList.get(position).getContent());
        viewHolder.mAuthor.setText(mReviewList.get(position).getAuthor() + ":");
    }

    @Override
    public int getItemCount() {
        return mReviewList.size();
    }

    public class ReviewsViewHolder extends RecyclerView.ViewHolder {
        private TextView mReviewContent;
        private TextView mAuthor;

        public ReviewsViewHolder(View itemView) {
            super(itemView);
            mReviewContent = (TextView) itemView.findViewById(R.id.review_text);
            mAuthor = (TextView) itemView.findViewById(R.id.author_text);

        }
    }
}
