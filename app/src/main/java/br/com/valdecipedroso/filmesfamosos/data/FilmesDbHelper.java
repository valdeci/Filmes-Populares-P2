package br.com.valdecipedroso.filmesfamosos.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import br.com.valdecipedroso.filmesfamosos.data.FilmesContract.FilmeEntry;

/**
 * Created by valdecipti on 06/11/2017.
 */

public class FilmesDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "filmesfamosos.db";

    private static final int DATABASE_VERSION = 1;

    private static final String TEXT_TYPE = " TEXT ";
    private static final String DOUBLE_TYPE = " DOUBLE ";
    private static final String INTEGER_TYPE = " INTEGER ";
    private static final String COMMA_SEP = " , ";
    private static final String PRIMARY_KEY  = " PRIMARY KEY AUTOINCREMENT ";

    public FilmesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_FILME_TABLE =
                "CREATE TABLE " + FilmeEntry.TABLE_NAME + " ( " +
                                  FilmeEntry._ID     + INTEGER_TYPE + PRIMARY_KEY  + COMMA_SEP +
                                  FilmeEntry.COLUMN_ID_API          + INTEGER_TYPE + COMMA_SEP +
                                  FilmeEntry.COLUMN_AVALIACAO       + DOUBLE_TYPE  + COMMA_SEP +
                                  FilmeEntry.COLUMN_CARTAZ          + TEXT_TYPE    + COMMA_SEP +
                                  FilmeEntry.COLUMN_DATA_LANCAMENTO + TEXT_TYPE    + COMMA_SEP +
                                  FilmeEntry.COLUMN_SINOPSE         + TEXT_TYPE    + COMMA_SEP +
                                  FilmeEntry.COLUMN_TITULO          + TEXT_TYPE    +" ) ";

        sqLiteDatabase.execSQL(SQL_CREATE_FILME_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + FilmeEntry.TABLE_NAME);
        onCreate(db);
    }
}
