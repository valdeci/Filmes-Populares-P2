package br.com.valdecipedroso.filmesfamosos.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import br.com.valdecipedroso.filmesfamosos.R;

import static br.com.valdecipedroso.filmesfamosos.utilities.FilmesUtils.getBitmapFromURL;

/**
 * Created by valdecipti on 12/11/2017.
 */

public class ImageFetchTask extends AsyncTask<Void, Void, Void>{
    private String mIdparam;
    private String mCartaz;
    private Context mContext;

    public ImageFetchTask(Context context, String id, String cartaz) {
        mIdparam = id;
        mContext = context;
        mCartaz = cartaz;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        if (ImageSaver.isExternalStorageWritable()) {
            Bitmap bitmap = getBitmapFromURL(mContext.getResources().getString(R.string.URL_IMAGE) + mCartaz);

            new ImageSaver(mContext).
                    setFileName(mIdparam).
                    setDirectoryName("images").
                    save(bitmap);
        }
        return null;
    }
}
