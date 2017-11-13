package br.com.valdecipedroso.filmesfamosos;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import java.net.URL;
import java.util.List;

import br.com.valdecipedroso.filmesfamosos.utilities.NetworkUtils;

/**
 * Created by valdecipti on 04/11/2017.
 */

class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailersAdapterViewHolder> {

    private final TrailersAdapterOnClickHandler mClickHandler;
    private Context mContext;
    private List<Trailer> mTrailerData;

    public TrailersAdapter(Context context, TrailersAdapterOnClickHandler mClickHandler) {
        mContext = context;
        this.mClickHandler = mClickHandler;
    }

    @Override
    public TrailersAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.trailer_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new TrailersAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailersAdapterViewHolder holder, int position) {
        Trailer trailer = mTrailerData.get(position);
        TextView title = holder.mTextViewTrailer;
        title.setText(trailer.getName());
    }

    @Override
    public int getItemCount() {
        if (null == mTrailerData){return 0;}
        return mTrailerData.size();
    }

    public interface TrailersAdapterOnClickHandler {
        void onClick(Trailer filmeSelected);
    }

    public class TrailersAdapterViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        private final TextView mTextViewTrailer;

        private TrailersAdapterViewHolder(View view) {
            super(view);
            mTextViewTrailer = (TextView) view.findViewById(R.id.tv_trailer_position);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Trailer trailerSelected = mTrailerData.get(adapterPosition);
            mClickHandler.onClick(trailerSelected);

            URL trailerUrl = NetworkUtils.buildUrl(NetworkUtils.getUriTrailer(mContext, trailerSelected));

            Intent it = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(trailerUrl.toString()));
            mContext.startActivity(it);
        }
    }

    public void setTrailerData(List<Trailer> trailerData) {
        mTrailerData = trailerData;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {

        return super.getItemViewType(position);
    }
}
