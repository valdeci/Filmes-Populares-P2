package br.com.valdecipedroso.filmesfamosos;

/**
 * Created by valdecipti on 05/11/2017.
 */

public class Review {

    private final String id;
    private final String author;
    private final String content;
    private final String url;

    public Review(String id, String author, String content, String url) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }
}
