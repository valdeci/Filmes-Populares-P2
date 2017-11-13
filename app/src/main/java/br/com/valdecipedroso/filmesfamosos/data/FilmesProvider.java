package br.com.valdecipedroso.filmesfamosos.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import br.com.valdecipedroso.filmesfamosos.data.FilmesContract.FilmeEntry;

/**
 * Created by valdecipti on 06/11/2017.
 */

public class FilmesProvider extends ContentProvider {
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private FilmesDbHelper mOpenHelper;

    static final int FILMES = 100;
    static final int FILMES_ID_MOVIE = 101;

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        final String authority = FilmesContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, FilmesContract.PATH_FILME, FILMES);
        matcher.addURI(authority, FilmesContract.PATH_FILME+"/#", FILMES_ID_MOVIE);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new FilmesDbHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {

            case FILMES_ID_MOVIE: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FilmeEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case FILMES: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FilmeEntry.TABLE_NAME,
                        null,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case FILMES: {
                long _id = db.insert(FilmeEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = FilmeEntry.buildMovieUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        if ( null == selection ) selection = "1";
        switch (match) {
            case FILMES_ID_MOVIE:
                rowsDeleted = db.delete(
                        FilmeEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }


    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
