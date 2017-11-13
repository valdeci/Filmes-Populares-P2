package br.com.valdecipedroso.filmesfamosos;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Valdecipti on 28/09/2017.
 */

public class Filme implements Parcelable {
    private final Integer id;
    private final String tituloOriginal;
    private final String cartaz;
    private final String sinopse;
    private final double avaliacao;
    private final String dataLancamento;
    Filme[] mObjList;

    public Filme(Integer id, String tituloOriginal, String cartaz, String sinopse, Double avaliacao, String dataLancamento){
        this.id = id;
        this.tituloOriginal = tituloOriginal;
        this.cartaz = cartaz;
        this.sinopse = sinopse;
        this.avaliacao = avaliacao;
        this.dataLancamento = dataLancamento;
    }

    private Filme(Parcel parcel){
        this.id = parcel.readInt();
        this.tituloOriginal = parcel.readString();
        this.cartaz = parcel.readString();
        this.sinopse = parcel.readString();
        this.avaliacao = parcel.readDouble();
        this.dataLancamento = parcel.readString();
    }

    public static final Creator<Filme> CREATOR = new Creator<Filme>() {
        @Override
        public Filme createFromParcel(Parcel in) {
            return new Filme(in);
        }

        @Override
        public Filme[] newArray(int size) {
            return new Filme[size];
        }
    };

    public Integer getId() {
        return id;
    }

    public String getTituloOriginal() {
        return tituloOriginal;
    }

    public String getCartaz() {
        return cartaz;
    }

    public String getSinopse() {
        return sinopse;
    }

    public double getAvaliacao() {
        return avaliacao;
    }

    public String getDataLancamento() {
        return dataLancamento;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
       parcel.writeInt(id);
       parcel.writeString(tituloOriginal);
       parcel.writeString(cartaz);
       parcel.writeString(sinopse);
       parcel.writeDouble(avaliacao);
       parcel.writeString(dataLancamento);
    }
}
