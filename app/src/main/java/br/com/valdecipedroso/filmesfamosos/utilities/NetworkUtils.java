/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package br.com.valdecipedroso.filmesfamosos.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import br.com.valdecipedroso.filmesfamosos.R;
import br.com.valdecipedroso.filmesfamosos.Trailer;

public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String format_popular = "popular";
    private static final String format_top_rated = "top_rated";

    public static URL buildUrl(Uri builtUri) {
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    public static Uri getUriMovies(Context context, Integer orderedBy) {
        String param_order = (orderedBy == context.getResources().getInteger(R.integer.key_popular)) ? format_popular : format_top_rated;

        return Uri.parse(context.getResources().getString(R.string.MOVIE_BASE_URL)).buildUpon()
                .appendPath(param_order)
                .appendQueryParameter(context.getResources().getString(R.string.PARAM_API_KEY), context.getString(R.string.API_KEY))
                .build();
    }

    public static Uri getUriTrailer(Context context, Trailer trailer) {

        return Uri.parse(context.getResources().getString(R.string.YOUTUBE_URL)).buildUpon()
                .appendPath(context.getResources().getString(R.string.PATH_YOUTUBE))
                .appendQueryParameter(context.getResources().getString(R.string.PARAM_ID_VIDEO), trailer.getKey())
                .build();
    }

    public static Uri getUriTrailers(Context context, Integer idFilme) {
        return Uri.parse(context.getResources().getString(R.string.MOVIE_BASE_URL)).buildUpon()
                .appendPath(idFilme.toString())
                .appendPath(context.getResources().getString(R.string.PATH_VIDEOS))
                .appendQueryParameter(context.getResources().getString(R.string.PARAM_API_KEY), context.getString(R.string.API_KEY))
                .build();
    }

    public static Uri getUriReviews(Context context, Integer idFilme) {
        return Uri.parse(context.getResources().getString(R.string.MOVIE_BASE_URL)).buildUpon()
                .appendPath(idFilme.toString())
                .appendPath(context.getResources().getString(R.string.PATH_REVIEW))
                .appendQueryParameter(context.getResources().getString(R.string.PARAM_API_KEY), context.getString(R.string.API_KEY))
                .build();
    }
}