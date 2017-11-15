package br.com.valdecipedroso.filmesfamosos;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import br.com.valdecipedroso.filmesfamosos.data.FilmesContract;
import br.com.valdecipedroso.filmesfamosos.data.FilmesPreferences;
import br.com.valdecipedroso.filmesfamosos.utilities.FilmesUtils;
import br.com.valdecipedroso.filmesfamosos.utilities.ImageFetchTask;
import br.com.valdecipedroso.filmesfamosos.utilities.NetworkUtils;

public class FilmesActivity extends AppCompatActivity implements LoaderCallbacks<Cursor>, FilmesAdapter.FilmesAdapterOnClickHandler {

    private TextView mErrorMessageDisplay;
    private TextView mNoMovieToDisplay;
    private TextView mOfflineMessage;
    private RecyclerView mRecyclerView;
    private ProgressBar mLoadingIndicator;
    private FilmesAdapter mFilmesAdapter;
    private Context mContext;
    private FilmesFetchTask mFilmesFetchTask;
    private static final String ON_SAVE_INSTANCE_STATE = "savedInstanceState";

    private static final int FILMES_LOADER_ID = 1264;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filmes);

        mContext = this;
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
        mNoMovieToDisplay = (TextView) findViewById(R.id.tv_no_movie_to_display);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        mOfflineMessage= (TextView) findViewById(R.id.tv_offline_message_display);
        mFilmesAdapter = new FilmesAdapter(this, FilmesActivity.this);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns() , GridLayoutManager.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mFilmesAdapter);

        if ((savedInstanceState != null)){
            if (savedInstanceState.containsKey(ON_SAVE_INSTANCE_STATE)){
                Parcelable[] parcelables = savedInstanceState.getParcelableArray(ON_SAVE_INSTANCE_STATE);
                if(parcelables != null){
                    mFilmesAdapter.setFilmesData(convertParcelable(parcelables));
                }else{
                    showFilmesDataView(null);
                }
            }
        }else {
            startLoadMovies();
        }

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            File directory = new File(Environment.getExternalStorageDirectory()+File.separator+"/"+getApplicationContext().getPackageName()+mContext.getString(R.string.path_image));
            directory.mkdirs();
        }
    }

    private List<Filme> convertParcelable(Parcelable[] parcelables) {
        if (parcelables == null) return null;
        Filme[] filmes = Arrays.copyOf(parcelables, parcelables.length, Filme[].class);
        return Arrays.asList(filmes);
    }

    private void startLoadMovies() {
        Integer orderedBy = FilmesPreferences.getPreferredFilmesOrderPopular(mContext);

        if ((orderedBy == getBaseContext().getResources().getInteger(R.integer.key_favorite)) ||
                !NetworkUtils.isNetworkConnected(mContext)){
            LoaderCallbacks<Cursor> callback = FilmesActivity.this;
            getSupportLoaderManager().initLoader(FILMES_LOADER_ID, null, callback);
        }else{
            mFilmesFetchTask = new FilmesFetchTask(this, new FetchMyDataTaskCompleteListener());
            mFilmesFetchTask.execute();
        }
    }

    private void restartLoadMovies() {
        if (FilmesUtils.IsFavoriteMovie(mContext)){
            LoaderCallbacks<Cursor> callback = FilmesActivity.this;
            getSupportLoaderManager().restartLoader(FILMES_LOADER_ID, null, callback);
        }else{
            if(NetworkUtils.isNetworkConnected(mContext)){
                 mFilmesFetchTask = new FilmesFetchTask(this, new FetchMyDataTaskCompleteListener());
                 mFilmesFetchTask.execute();
            }else{
                loadFinished(null);
            }
        }
    }

    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        // You can change this divider to adjust the size of the poster
        int widthDivider = 400;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 2) return 2;
        return nColumns;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Parcelable[] parcelables = ReturnMoviesParcelabled();
        outState.putParcelableArray(ON_SAVE_INSTANCE_STATE, parcelables);
        super.onSaveInstanceState(outState);
    }

    private Parcelable[] ReturnMoviesParcelabled() {
        if (FilmesAdapter.mFilmesData == null) return null;

        Filme[] filmes = new Filme[FilmesAdapter.mFilmesData.size()];
        filmes = FilmesAdapter.mFilmesData.toArray(filmes);
        for (Filme filme: filmes) {
            ImageFetchTask imageFetchTask = new ImageFetchTask(mContext,filme.getId().toString(),filme.getCartaz());
            imageFetchTask.execute();
        }
        return filmes;
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem item;
        getMenuInflater().inflate(R.menu.discovery, menu);
        Integer itemChecked = FilmesPreferences.getPreferredFilmesOrderPopular(this);

        if (itemChecked == mContext.getResources().getInteger(R.integer.key_popular)){
            item  = menu.findItem(R.id.action_check_popular);
        }
        else if (itemChecked == mContext.getResources().getInteger(R.integer.key_rated)){
            item = menu.findItem(R.id.action_check_rate);
        }else{
            item = menu.findItem(R.id.action_check_favorite);
        }

        item.setChecked(true);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.action_check_popular:
            case R.id.action_check_rate:
            case R.id.action_check_favorite:
                item.setChecked(!item.isChecked());
                FilmesPreferences.setPreferredFilmesOrderPopular(this, id);
                loadFilmesData();
                break;
           default:
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadFilmesData() {
        mLoadingIndicator.setVisibility(View.VISIBLE);
        restartLoadMovies();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return RetornarFilmesFavorite();
    }

    private CursorLoader RetornarFilmesFavorite() {
        return new CursorLoader(getApplicationContext(),
                FilmesContract.FilmeEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (FilmesUtils.IsFavoriteMovie(mContext)) {
            List<Filme> listFilmes = FilmesUtils.ConvertCursorToListFilmes(data);
            loadFinished(listFilmes);
        }else{
            showFilmesDataView(null);
        }
    }

    private void loadFinished(List<Filme> data) {
        mFilmesAdapter.setFilmesData(data);
        mLoadingIndicator.setVisibility(View.INVISIBLE);

        showFilmesDataView(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void showFilmesDataView(List<Filme> filmes) {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        if ((filmes == null) || (filmes.size() != 0)) {
            mNoMovieToDisplay.setVisibility(View.INVISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);

            if((filmes != null) && (filmes.size() > 0)){
                mOfflineMessage.setVisibility(View.INVISIBLE);
            }else{
                mOfflineMessage.setVisibility(View.VISIBLE);
            }
        }else{
            mOfflineMessage.setVisibility(View.INVISIBLE);
            mNoMovieToDisplay.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(Filme filmeSelected) {
        Intent it = new Intent(getBaseContext(), DetailActivity.class);
        it.putExtra(Intent.EXTRA_TEXT, filmeSelected);
        startActivity(it);
    }

    public class FetchMyDataTaskCompleteListener implements AsyncTaskComplete.AsyncTaskCompleteListener<List<Filme>>
    {
        @Override
        public void onTaskComplete(List<Filme> result) {
            loadFinished(result);
        }
    }
}
