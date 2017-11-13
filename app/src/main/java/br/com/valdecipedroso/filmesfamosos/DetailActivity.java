package br.com.valdecipedroso.filmesfamosos;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

import br.com.valdecipedroso.filmesfamosos.data.FilmesContract.FilmeEntry;
import br.com.valdecipedroso.filmesfamosos.utilities.ImageSaver;
import br.com.valdecipedroso.filmesfamosos.utilities.NetworkUtils;

import static android.R.drawable.btn_star_big_off;
import static android.R.drawable.btn_star_big_on;


public class DetailActivity extends AppCompatActivity  implements TrailersAdapter.TrailersAdapterOnClickHandler{
    private Filme mFilmes;
    private boolean mIsFavoriteMovie;
    private Integer mIdFilme;
    private Context mContext;
    private TextView mTextViewTitleTrailer;
    private TextView mTextViewTitleReview;
    private TrailersAdapter mTrailersAdapter;
    private ReviewsAdapter mReviewsAdapter;
    private TextView mButtonFavorite;
    private String mLinkTrailerShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ImageView cartaz;
        TextView tituloOriginal;
        TextView sinopse;
        TextView avaliacao;
        TextView dataLancamento;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mContext = this;

        mTrailersAdapter = new TrailersAdapter(this, DetailActivity.this);
        RecyclerView recyclerViewTrailers = (RecyclerView) findViewById(R.id.recycler_view_trailers);
        recyclerViewTrailers.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerViewTrailers.setHasFixedSize(true);
        recyclerViewTrailers.setAdapter(mTrailersAdapter);

        mReviewsAdapter = new ReviewsAdapter();
        RecyclerView recyclerViewReviews = (RecyclerView) findViewById(R.id.recycler_view_reviews);
        recyclerViewReviews.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerViewReviews.setHasFixedSize(true);
        recyclerViewReviews.setAdapter(mReviewsAdapter);

        mTextViewTitleTrailer = (TextView) findViewById(R.id.tv_title_trailer);
        mTextViewTitleReview = (TextView) findViewById(R.id.tv_title_reviews);
        mButtonFavorite = (Button) findViewById(R.id.bt_favorite);
        tituloOriginal = (TextView) findViewById(R.id.tv_titulo_original);
        cartaz = (ImageView) findViewById(R.id.tv_cartaz);
        sinopse = (TextView) findViewById(R.id.tv_sinopse);
        avaliacao = (TextView) findViewById(R.id.tv_avaliacao);
        dataLancamento = (TextView) findViewById(R.id.tv_data_lancamento);

        Intent it = getIntent();

        if (it.resolveActivity(getPackageManager()) !=  null){
            if(it.hasExtra(Intent.EXTRA_TEXT)){
                mFilmes = it.getParcelableExtra(Intent.EXTRA_TEXT);
                if (mFilmes != null){
                   tituloOriginal.setText(mFilmes.getTituloOriginal());
                   sinopse.setText(mFilmes.getSinopse());
                   avaliacao.setText(String.format(getResources().getString(R.string.string_avaliacao), mFilmes.getAvaliacao()));
                   dataLancamento.setText(mFilmes.getDataLancamento());
                   cartaz.setAdjustViewBounds(true);
                   mIdFilme = mFilmes.getId();

                   if (NetworkUtils.isNetworkConnected(this)){
                       Picasso.with(this)
                              .load(getResources().getString(R.string.PARAM_URL_IMAGE)+mFilmes.getCartaz())
                              .error(R.drawable.ic_photo_black_24dp)
                              .into(cartaz);
                   }else{
                       try{
                           ImageSaver imageSaver = new ImageSaver(mContext);
                           Bitmap bitmap = imageSaver.setDirectoryName("images").setFileName(mFilmes.getId().toString()).loadImageFromStorage();
                           if (bitmap != null) cartaz.setImageBitmap(bitmap);
                           else cartaz.setBackgroundResource(R.drawable.ic_photo_black_24dp);
                       }catch (Exception e){
                           e.printStackTrace();
                       }
                   }
                }
            }
        }

        mIsFavoriteMovie = IsFavoriteMovie();

        changeBackgroundStarFavorite();

        mButtonFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mIsFavoriteMovie){
                    RemoveMovie();
                    Toast.makeText(mContext, getResources().getText(R.string.info_removed_favorite), Toast.LENGTH_SHORT).show();
                }
                else{
                    AddNewMovie();
                    Toast.makeText(mContext, getResources().getText(R.string.info_add_favorite), Toast.LENGTH_SHORT).show();
                }

                mIsFavoriteMovie = !mIsFavoriteMovie;
                changeBackgroundStarFavorite();
            }
        });

        if(NetworkUtils.isNetworkConnected(mContext)){
            TrailersFetchTask trailersFetchTask = new TrailersFetchTask();
            ReviewsFetchTask reviewsFetchTask = new ReviewsFetchTask();

            trailersFetchTask.execute();
            reviewsFetchTask.execute();
        }else{
            mTextViewTitleTrailer.setText(getText(R.string.info_trailer_off_line));
            mTextViewTitleReview.setText(getText(R.string.info_reviews_off_line));
        }
    }

    private void changeBackgroundStarFavorite() {
        mButtonFavorite.setBackgroundResource( mIsFavoriteMovie ? btn_star_big_on : btn_star_big_off);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        MenuItem menuShare = menu.findItem(R.id.action_share);
        menuShare.setVisible(NetworkUtils.isNetworkConnected(mContext));
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private Intent createShareIntent() {
        return ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText(mLinkTrailerShare)
                .getIntent();
    }

    private void RemoveMovie() {
        Uri uri = FilmeEntry.buildMovieUri(mFilmes.getId());
        String[] selectionArgs = new String[]{mFilmes.getId().toString()};
        String selection = FilmeEntry.TABLE_NAME+"."+FilmeEntry.COLUMN_ID_API +" = ?";
        mContext.getContentResolver().delete(uri, selection, selectionArgs);
    }

    private void AddNewMovie() {

        ContentValues movieValue = new ContentValues();
        movieValue.put(FilmeEntry.COLUMN_ID_API,  mFilmes.getId());
        movieValue.put(FilmeEntry.COLUMN_TITULO,  mFilmes.getTituloOriginal());
        movieValue.put(FilmeEntry.COLUMN_SINOPSE, mFilmes.getSinopse());
        movieValue.put(FilmeEntry.COLUMN_AVALIACAO, mFilmes.getAvaliacao());
        movieValue.put(FilmeEntry.COLUMN_CARTAZ, mFilmes.getCartaz());
        movieValue.put(FilmeEntry.COLUMN_DATA_LANCAMENTO, mFilmes.getDataLancamento());

        mContext.getContentResolver().insert(FilmeEntry.CONTENT_URI, movieValue);
    }

    private boolean IsFavoriteMovie() {
        Uri uri = FilmeEntry.buildMovieUri(mFilmes.getId());
        String[] selectionArgs = new String[]{mFilmes.getId().toString()};
        String selection = FilmeEntry.TABLE_NAME+"."+FilmeEntry.COLUMN_ID_API +" = ?";

        Cursor cur = mContext.getContentResolver().query(
                uri,
                new String[]{},
                selection,
                selectionArgs,
                null);

        boolean existsFavoriteMovie = (cur != null) && (cur.getCount() > 0);

        if (existsFavoriteMovie) cur.close();

        return existsFavoriteMovie;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_share){
            startActivity(createShareIntent());
        }

        return super.onOptionsItemSelected(item);
    }

    private class TrailersFetchTask extends AsyncTask<Void, Void, List<Trailer>> {

        @Override
        protected List<Trailer> doInBackground(Void... voids) {
            try {
                URL trailersRequestUrl = NetworkUtils.buildUrl(NetworkUtils.getUriTrailers(mContext, mIdFilme));

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

            if (trailers == null){
                mTextViewTitleTrailer.setText(NetworkUtils.isNetworkConnected(mContext) ? R.string.info_trailer :R.string.info_trailer_off_line);
            }else{
                try {
                    mLinkTrailerShare = NetworkUtils.buildUrl(NetworkUtils.getUriTrailer(mContext, trailers.get(1))).toString();
                }catch (Exception e){
                    e.printStackTrace();
                }

                mTrailersAdapter.setTrailerData(trailers);
            }
        }
    }

    private class ReviewsFetchTask extends AsyncTask<Void, Void, List<Review>> {

        @Override
        protected List<Review> doInBackground(Void... voids) {
            URL reviewsRequestUrl = NetworkUtils.buildUrl(NetworkUtils.getUriReviews(mContext, mIdFilme));

            try {
                String jsonReviewResponse = NetworkUtils.getResponseFromHttpUrl(reviewsRequestUrl);

                return  OpenFilmesJsonUtils.getReviewsStringsFromJson(jsonReviewResponse);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Review> data) {
            super.onPostExecute(data);
            if (data == null){
                mTextViewTitleReview.setText(mContext.getResources().getString(NetworkUtils.isNetworkConnected(mContext) ? R.string.info_reviews:R.string.info_reviews_off_line));
            }else{
                mReviewsAdapter.setReviewData(data);
            }
        }
    }

    @Override
    public void onClick(Trailer trailerSelected) {
    }
}
