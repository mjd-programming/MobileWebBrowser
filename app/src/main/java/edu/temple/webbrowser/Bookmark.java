package edu.temple.webbrowser;

public class Bookmark {

    private String title;
    private String url;

    public Bookmark(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public void setTitle(String s) {
        title = s;
    }

    public void setUrl(String s) {
        url = s;
    }
}
