package com.hsappdev.ahs.UI.bulletin;

import com.hsappdev.ahs.dataTypes.Category;

import java.util.List;

public interface CategoryLoadedCallback {
    void onCategoryDataLoaded(Category category, List<String> articleIds);
}
