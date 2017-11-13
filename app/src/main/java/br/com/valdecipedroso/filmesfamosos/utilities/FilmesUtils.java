package br.com.valdecipedroso.filmesfamosos.utilities;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import br.com.valdecipedroso.filmesfamosos.Filme;
import br.com.valdecipedroso.filmesfamosos.R;
import br.com.valdecipedroso.filmesfamosos.data.FilmesPreferences;

/**
 * Created by valdecipti on 11/11/2017.
 */

public class FilmesUtils {

    public static List<Filme> ConvertCursorToListFilmes(Cursor cursor) {
        List<Filme> filmesData = new ArrayList<>();

        if (cursor == null) return null;
        cursor.moveToFirst();

        for (int i = 0; i < cursor.getCount(); i++) {
            Filme newFilme = new Filme(cursor.getInt(1), cursor.getString(6), cursor.getString(3), cursor.getString(5), cursor.getDouble(2), cursor.getString(4));
            filmesData.add(newFilme);
            cursor.moveToNext();
        }
        return  filmesData;
    }


    public static boolean IsFavoriteMovie(Context mContext) {
        Integer orderedBy = FilmesPreferences.getPreferredFilmesOrderPopular(mContext);
        return  (orderedBy == mContext.getResources().getInteger(R.integer.key_favorite));
    }

    public static Bitmap getBitmapFromURL(String url) {
        try {
            URL src = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) src.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
