package br.com.valdecipedroso.filmesfamosos;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by valdecipti on 05/11/2017.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsAdapterViewHolder>  {

    private List<Review> mReviewData;

    public ReviewsAdapter() {
    }

    @Override
    public ReviewsAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.review_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new ReviewsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewsAdapterViewHolder holder, int position) {
        Review review = mReviewData.get(position);
        holder.mTextViewAuthor.setText(review.getAuthor());
        holder.mTextViewContent.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        if (null == mReviewData){return 0;}
        return mReviewData.size();
    }

    public class ReviewsAdapterViewHolder extends RecyclerView.ViewHolder{
        private final TextView mTextViewAuthor;
        private final TextView mTextViewContent;

        private ReviewsAdapterViewHolder(View itemView) {
            super(itemView);
            mTextViewAuthor = (TextView) itemView.findViewById(R.id.tv_review_author);
            mTextViewContent = (TextView) itemView.findViewById(R.id.tv_review_content);
        }
    }

    public void setReviewData(List<Review> ReviewData) {
        mReviewData = ReviewData;
        notifyDataSetChanged();
    }
}
