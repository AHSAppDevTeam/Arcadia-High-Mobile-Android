package com.example.carousel;

import android.graphics.drawable.Drawable;

public class FeaturedArticle {

    private String articleId;
    private int articleImage;

    public FeaturedArticle(String articleId, int articleImage) {
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
