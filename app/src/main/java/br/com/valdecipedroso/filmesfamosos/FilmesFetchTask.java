package br.com.valdecipedroso.filmesfamosos;

import android.content.Context;
import android.os.AsyncTask;

import java.net.URL;
import java.util.List;

import br.com.valdecipedroso.filmesfamosos.data.FilmesPreferences;
import br.com.valdecipedroso.filmesfamosos.utilities.NetworkUtils;

/**
 * Created by valdecipti on 13/11/2017.
 */

public class FilmesFetchTask extends AsyncTask<Void, Void, List<Filme>> {
    private Context mContext;

    private AsyncTaskComplete.AsyncTaskCompleteListener<List<Filme>> mListener;

    public FilmesFetchTask(Context context,  AsyncTaskComplete.AsyncTaskCompleteListener<List<Filme>> listener) {
        mContext = context;
        mListener = listener;
    }

    @Override
    protected List<Filme> doInBackground(Void... voids) {
        Integer orderedBy = FilmesPreferences.getPreferredFilmesOrderPopular(mContext);
        return ConsultarJSONURL(orderedBy);
    }

    @Override
    protected void onPostExecute(List<Filme> filmes) {
        super.onPostExecute(filmes);
        mListener.onTaskComplete(filmes);
    }

    private List<Filme> ConsultarJSONURL(Integer orderedBy) {
        URL filmesRequestUrl = NetworkUtils.buildUrl(NetworkUtils.getUriMovies(mContext, orderedBy));

        try {
            String jsonFilmesResponse = NetworkUtils
                    .getResponseFromHttpUrl(filmesRequestUrl);

            return OpenFilmesJsonUtils
                    .getFilmesStringsFromJson(jsonFilmesResponse);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}