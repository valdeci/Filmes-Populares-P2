package br.com.valdecipedroso.filmesfamosos;

/**
 * Created by valdecipti on 04/11/2017.
 */

public class Trailer {
    private final String id;
    private final String name;
    private final String key;
    private final String site;

    public Trailer(String id,String name, String key, String site) {
        this.id = id;
        this.name = name;
        this.key = key;
        this.site = site;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public String getSite() {
        return site;
    }
}
