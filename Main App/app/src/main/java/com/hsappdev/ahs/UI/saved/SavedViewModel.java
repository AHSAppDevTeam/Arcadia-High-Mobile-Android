package com.hsappdev.ahs.UI.saved;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.hsappdev.ahs.dataTypes.Article;
import com.hsappdev.ahs.localdb.ArticleRepository;

import java.util.List;

public class SavedViewModel extends AndroidViewModel {
    private final LiveData<List<Article>> allArticles;
    private final LiveData<List<Article>> savedArticles;
    private final ArticleRepository repository;

    public SavedViewModel(@NonNull Application application) {
        super(application);
        repository = new ArticleRepository(application);
        allArticles = repository.getAllArticles();
        savedArticles = repository.getAllSavedArticles();
    }

    public LiveData<List<Article>> getAllArticles() {
        return allArticles;
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public LiveData<List<Article>> getAllSavedArticles() {
        return savedArticles;
    }
}