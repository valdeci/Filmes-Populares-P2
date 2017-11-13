package br.com.valdecipedroso.filmesfamosos;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.valdecipedroso.filmesfamosos.utilities.ImageSaver;
import br.com.valdecipedroso.filmesfamosos.utilities.NetworkUtils;


/**
 * Created by Valdecipti on 27/09/2017.
 */

class FilmesAdapter extends RecyclerView.Adapter<FilmesAdapter.FilmesAdapterViewHolder> {

    public static List<Filme> mFilmesData;
    private Context mContext;
    private final FilmesAdapterOnClickHandler mClickHandler;

    public FilmesAdapter(Context context, FilmesAdapterOnClickHandler mClickHandler) {
        mContext = context;
        this.mClickHandler = mClickHandler;
    }

    public interface FilmesAdapterOnClickHandler {
        void onClick(Filme filmeSelected);
    }

    @Override
    public FilmesAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.filme_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        return new FilmesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FilmesAdapterViewHolder convertView, int position) {
        Filme filme = mFilmesData.get(position);
        ImageView image = convertView.mFilmeImageView;
        image.setAdjustViewBounds(true);

        if ((filme != null ? filme.getCartaz() : null) != null) {
            if ((NetworkUtils.isNetworkConnected(mContext)) || !ImageSaver.isExternalStorageReadable()){
                Picasso.with(mContext)
                        .load(mContext.getResources().getString(R.string.URL_IMAGE) + filme.getCartaz())
                        .error(R.drawable.ic_photo_black_24dp)
                        .into(image);
            }else{
                ImageSaver imageSaver = new ImageSaver(mContext);
                Bitmap bitmap = imageSaver.setDirectoryName("images").setFileName(filme.getId().toString()).loadImageFromStorage();
                if (bitmap != null) image.setImageBitmap(bitmap);
                else image.setBackgroundResource(R.drawable.ic_photo_black_24dp);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (null == mFilmesData) return 0;
        return mFilmesData.size();
    }

    public class FilmesAdapterViewHolder extends RecyclerView.ViewHolder implements OnClickListener {

        public final ImageView mFilmeImageView;

        public FilmesAdapterViewHolder(View view) {
            super(view);
            mFilmeImageView = (ImageView) view.findViewById(R.id.list_item_image);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Filme filmeSelected = mFilmesData.get(adapterPosition);
            mClickHandler.onClick(filmeSelected);
        }
    }

    public void setFilmesData(List<Filme> filmesData) {
        mFilmesData = filmesData;
        notifyDataSetChanged();
    }
}
