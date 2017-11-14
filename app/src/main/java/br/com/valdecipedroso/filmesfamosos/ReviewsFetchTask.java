package br.com.valdecipedroso.filmesfamosos;

import android.content.Context;
import android.os.AsyncTask;

import java.net.URL;
import java.util.List;

import br.com.valdecipedroso.filmesfamosos.utilities.NetworkUtils;

/**
 * Created by valdecipti on 13/11/2017.
 */

public class ReviewsFetchTask extends AsyncTask<Integer, Void, List<Review>> {

    private Context mContext;
    private AsyncTaskComplete.AsyncTaskCompleteListener<List<Review>> mListener;

    public ReviewsFetchTask(Context context, AsyncTaskComplete.AsyncTaskCompleteListener<List<Review>> listener) {
        mContext = context;
        mListener = listener;
    }

    @Override
    protected List<Review> doInBackground(Integer... idParam) {
        URL reviewsRequestUrl = NetworkUtils.buildUrl(NetworkUtils.getUriReviews(mContext, idParam[0]));

        try {
            String jsonReviewResponse = NetworkUtils.getResponseFromHttpUrl(reviewsRequestUrl);

            return  OpenFilmesJsonUtils.getReviewsStringsFromJson(jsonReviewResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<Review> reviews) {
        super.onPostExecute(reviews);
        mListener.onTaskComplete(reviews);
    }
}

