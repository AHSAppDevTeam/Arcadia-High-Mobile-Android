package com.hsappdev.ahs;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class ArticleViewModel extends AndroidViewModel {
    private MutableLiveData<Float> textScalar = new MutableLiveData<>();
    public void setTextScalar(float newValue) {
        textScalar.setValue(newValue);
        SettingsManager settingsManager = SettingsManager.getInstance(getApplication().getApplicationContext());
        settingsManager.updateTextScalar(newValue);
    }
    public LiveData<Float> getTextScalar() {
        return textScalar;
    }
    public ArticleViewModel(Application application) {
        super(application);
        SettingsManager settingsManager = SettingsManager.getInstance(application.getApplicationContext());
        textScalar.setValue(settingsManager.getTextScalar());

    }
}
