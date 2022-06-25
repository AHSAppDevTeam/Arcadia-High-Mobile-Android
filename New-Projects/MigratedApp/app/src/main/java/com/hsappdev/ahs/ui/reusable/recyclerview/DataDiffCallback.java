package com.hsappdev.ahs.ui.reusable.recyclerview;

import android.view.View;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.SortedList;

import com.hsappdev.ahs.newCache.DataType;

import java.util.ArrayList;
import java.util.List;

public class DataDiffCallback<T extends DataType> extends DiffUtil.Callback {
    private final List<T> oldDataList;
    private final List<T> newDataList;

    public DataDiffCallback(List<T> oldDataList, List<T> newDataList) {
        this.oldDataList = oldDataList;
        this.newDataList = newDataList;
    }

    @Override
    public int getOldListSize() {
        return oldDataList.size();
    }

    @Override
    public int getNewListSize() {
        return newDataList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldDataList.get(oldItemPosition).getDataId().equals(newDataList.get(newItemPosition).getDataId());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldDataList.get(oldItemPosition).getDataHash().equals(newDataList.get(newItemPosition).getDataHash());
    }




    /**
     * A little bit of history
     * From the old repo:
     */
    /** programmerboy "programmer and harry styles fan"
     The algorithm used here is optimized and I have tested it on very large lists ranging from ~40,000 to ~100,000 objects.
     The main optimization is in detecting articles to remove.
     (code below)
    **/

//    // Perform filtering
//    SortedList<Article> sortedListRef = adapter.getArticleSortedList();
//    SortedList<Article> comingUpSortedListRef = comingUpAdapter.getArticleSortedList();
//
//        sortedListRef.beginBatchedUpdates();
//    List<Article> filteredList = new ArrayList<>();
//        if(!areAnyCategoriesChecked()){
//        filteredList.addAll(articleList);
//    } else {
//        for (Article articleToCheck : articleList) {
//            for (CategoryState state : categoryList) {
//                if (articleToCheck.getCategoryID().equals(state.getCategory().getCategoryID())) {
//                    if (state.isSelected()) {
//                        filteredList.add(articleToCheck);
//                    }
//                }
//            }
//        }
//    }
//        if(filteredList.size() <= 4) {
//        // There is no default section, hide it
//        defaultHeader.setVisibility(View.GONE);
//    } else {
//        defaultHeader.setVisibility(View.VISIBLE);
//    }
//
//    compareAndUpdateArticleList(sortedListRef, filteredList);
//
//    // Get top 4 articles
//    List<Article> upComingArticles = new ArrayList<>();
//        for (int i = 0; i < 4; i++) {
//        if(sortedListRef.size()>0) {
//            upComingArticles.add(sortedListRef.removeItemAt(0));
//        }
//    }
//        sortedListRef.endBatchedUpdates();
//
//        comingUpSortedListRef.beginBatchedUpdates();
//
//    compareAndUpdateArticleList(comingUpSortedListRef, upComingArticles);
//
//        comingUpSortedListRef.endBatchedUpdates();
}
