package com.hsappdev.ahs.dataTypes;

public class Article {

    private String articleId;
    private int articleImage;

    public Article(String articleId, int articleImage) {
        this.articleId = articleId;
        this.articleImage = articleImage;
    }


    // GETTERS AND SETTERS
    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public int getArticleImage() {
        return articleImage;
    }

    public void setArticleImage(int articleImage) {
        this.articleImage = articleImage;
    }
}
