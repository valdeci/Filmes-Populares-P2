package br.com.valdecipedroso.filmesfamosos;

/**
 * Created by valdecipti on 13/11/2017.
 */

public class AsyncTaskComplete {
    public interface AsyncTaskCompleteListener<T>
    {
        void onTaskComplete(T result);
    }
}
