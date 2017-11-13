package br.com.valdecipedroso.filmesfamosos.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by valdecipti on 06/11/2017.
 */

public class FilmesContract {

    public static final String CONTENT_AUTHORITY = "br.com.valdecipedroso.filmesfamosos";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_FILME = "filme";

    public static final class FilmeEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_FILME)
                .build();

        public static final String TABLE_NAME = "filme";
        public static final String COLUMN_ID_API = "idApi";
        public static final String COLUMN_TITULO = "tituloOriginal";
        public static final String COLUMN_CARTAZ = "cartaz";
        public static final String COLUMN_SINOPSE = "sinopse";
        public static final String COLUMN_AVALIACAO = "avaliacao";
        public static final String COLUMN_DATA_LANCAMENTO = "dataLancamento";

        public static Uri buildMovieUri(long paramId) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Long.toString(paramId))
                    .build();
        }
    }
}
