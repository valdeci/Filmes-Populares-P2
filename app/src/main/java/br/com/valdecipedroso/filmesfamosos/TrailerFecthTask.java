package br.com.valdecipedroso.filmesfamosos;

import android.content.Context;
import android.os.AsyncTask;

import java.net.URL;
import java.util.List;

import br.com.valdecipedroso.filmesfamosos.utilities.NetworkUtils;

/**
 * Created by valdecipti on 13/11/2017.
 */

public class TrailerFecthTask extends AsyncTask<Integer , Void, List<Trailer>> {

    private Context mContext;
    private AsyncTaskComplete.AsyncTaskCompleteListener<List<Trailer>> mListener;

    public TrailerFecthTask(Context context, AsyncTaskComplete.AsyncTaskCompleteListener<List<Trailer>> listener) {
        mContext = context;
        mListener = listener;
    }

    @Override
    protected List<Trailer> doInBackground(Integer... param) {
        try {
            URL trailersRequestUrl = NetworkUtils.buildUrl(NetworkUtils.getUriTrailers(mContext, param[0]));

            String jsonTrailersResponse = NetworkUtils
                    .getResponseFromHttpUrl(trailersRequestUrl);

            return OpenFilmesJsonUtils
                    .getTrailersStringsFromJson(jsonTrailersResponse);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<Trailer> trailers) {
        super.onPostExecute(trailers);
        mListener.onTaskComplete(trailers);
    }
}