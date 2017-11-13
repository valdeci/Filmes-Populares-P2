package br.com.valdecipedroso.filmesfamosos.data;

import android.content.Context;
import android.content.SharedPreferences;

import br.com.valdecipedroso.filmesfamosos.R;

/**
 * Created by Valdecipti on 01/10/2017.
 */

public class FilmesPreferences {
    private static final  String KEY_PREF_MENU = "id_menu_pref";
    private static final  String KEY_ORDER = "order_by";

    public static Integer getPreferredFilmesOrderPopular(Context context) {
        SharedPreferences settings = context.getSharedPreferences(KEY_PREF_MENU, 0);
        return settings.getInt(KEY_ORDER, context.getResources().getInteger(R.integer.key_popular));
    }

    public static void setPreferredFilmesOrderPopular(Context context, int id) {
        SharedPreferences settings = context.getSharedPreferences(KEY_PREF_MENU, 0);
        SharedPreferences.Editor editor = settings.edit();
        Integer optionSelected;
        switch (id){
            case R.id.action_check_popular:
                optionSelected = context.getResources().getInteger(R.integer.key_popular);
                break;
            case R.id.action_check_rate:
                optionSelected = context.getResources().getInteger(R.integer.key_rated);
                break;
            case R.id.action_check_favorite:
                optionSelected = context.getResources().getInteger(R.integer.key_favorite);
                break;
            default:
                optionSelected = context.getResources().getInteger(R.integer.key_popular);
        }

        editor.putInt(KEY_ORDER, optionSelected);
        editor.apply();
    }
}
