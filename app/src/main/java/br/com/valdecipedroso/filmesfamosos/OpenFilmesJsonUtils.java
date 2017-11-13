package br.com.valdecipedroso.filmesfamosos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Valdecipti on 01/10/2017.
 */

class OpenFilmesJsonUtils {
    public static List<Filme> getFilmesStringsFromJson(String trailerJsonStr)
            throws JSONException {

        final String OWM_LIST = "results";

        final String OWM_ID = "id";
        final String OWM_TITULO = "title";
        final String OWM_CARTAZ = "poster_path";
        final String OWM_SINOPSE = "overview";
        final String OWM_AVALIACAO = "vote_average";
        final String OWM_DATA_LANCAMENTO = "release_date";

        final String OWM_MESSAGE_CODE = "cod";

        JSONObject forecastJson = new JSONObject(trailerJsonStr);

        if (forecastJson.has(OWM_MESSAGE_CODE)) {
            int errorCode = forecastJson.getInt(OWM_MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    /* Location invalid */
                    return null;
                default:
                    return null;
            }
        }

        JSONArray moviesArray = forecastJson.getJSONArray(OWM_LIST);

        List<Filme> parsedFilmesData = new ArrayList<>();

        for (int i = 0; i < moviesArray.length(); i++) {
            Integer id;
            String tituloOriginal;
            String cartaz;
            String sinopse;
            Double avaliacao;
            String dataLancamento;

            JSONObject movie = moviesArray.getJSONObject(i);

            id = movie.getInt(OWM_ID);
            tituloOriginal = movie.getString(OWM_TITULO);
            cartaz = movie.getString(OWM_CARTAZ);
            sinopse = movie.getString(OWM_SINOPSE);
            avaliacao = movie.getDouble(OWM_AVALIACAO);
            dataLancamento = movie.getString(OWM_DATA_LANCAMENTO);

            Filme newFilme = new Filme(id, tituloOriginal, cartaz, sinopse, avaliacao, dataLancamento);
            parsedFilmesData.add(newFilme);
        }

        return parsedFilmesData;
    }

    public static List<Trailer> getTrailersStringsFromJson(String trailerJsonStr)
            throws JSONException {

        final String OWM_LIST = "results";

        final String OWM_ID = "id";
        final String OWM_NAME = "name";
        final String OWM_KEY = "key";
        final String OWM_SITE = "site";

        final String OWM_MESSAGE_CODE = "cod";

        JSONObject forecastJson = new JSONObject(trailerJsonStr);

        if (forecastJson.has(OWM_MESSAGE_CODE)) {
            int errorCode = forecastJson.getInt(OWM_MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    /* Location invalid */
                    return null;
                default:
                    return null;
            }
        }

        JSONArray trailersArray = forecastJson.getJSONArray(OWM_LIST);

        List<Trailer> parsedTrailerData = new ArrayList<>();

        for (int i = 0; i < trailersArray.length(); i++) {
            String id;
            String key;
            String name;
            String site;

            JSONObject trailer = trailersArray.getJSONObject(i);

            id = trailer.getString(OWM_ID);
            key = trailer.getString(OWM_KEY);
            site = trailer.getString(OWM_SITE);
            name = trailer.getString(OWM_NAME);

            Trailer newTrailer = new Trailer(id, name, key, site);
            parsedTrailerData.add(newTrailer);
        }

        return parsedTrailerData;
    }

    public static List<Review> getReviewsStringsFromJson(String reviewJsonStr)
            throws JSONException {

        final String OWM_LIST = "results";

        final String OWM_ID = "id";
        final String OWM_AUTHOR = "author";
        final String OWM_CONTENT = "content";
        final String OWM_URL = "content";

        final String OWM_MESSAGE_CODE = "cod";

        JSONObject forecastJson = new JSONObject(reviewJsonStr);

        if (forecastJson.has(OWM_MESSAGE_CODE)) {
            int errorCode = forecastJson.getInt(OWM_MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    /* Location invalid */
                    return null;
                default:
                    return null;
            }
        }

        JSONArray reviewsArray = forecastJson.getJSONArray(OWM_LIST);

        List<Review> parsedReviewData = new ArrayList<>();

        for (int i = 0; i < reviewsArray.length(); i++) {
            String id;
            String author;
            String content;
            String url;

            JSONObject trailer = reviewsArray.getJSONObject(i);

            id = trailer.getString(OWM_ID);
            author = trailer.getString(OWM_AUTHOR);
            content = trailer.getString(OWM_CONTENT);
            url = trailer.getString(OWM_URL);

            Review newReview = new Review(id, author, content, url);
            parsedReviewData.add(newReview);
        }

        return parsedReviewData;
    }
}
