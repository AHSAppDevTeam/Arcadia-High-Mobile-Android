package com.hsappdev.ahs.UI.bulletin;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SortedList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.hsappdev.ahs.OnItemClick;
import com.hsappdev.ahs.R;
import com.hsappdev.ahs.cache.ArticleLoaderBackend;
import com.hsappdev.ahs.cache.callbacks.ArticleLoadableCallback;
import com.hsappdev.ahs.dataTypes.Article;
import com.hsappdev.ahs.dataTypes.Category;
import com.hsappdev.ahs.util.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

public class BulletinFragment extends Fragment implements CategoriesLoadedCallback, ArticleLoadableCallback {
    private static final String TAG = "BulletinFragment";

    private LinearLayout categoryLinearLayout;
    private List<CategoryState> categoryList;
    private List<Article> articleList = new ArrayList<>();

    private RecyclerView recyclerView;
    private RecyclerView comingUpRecyclerView;
    private BulletinRecyclerAdapter adapter;
    private BulletinRecyclerAdapter comingUpAdapter;
    private OnItemClick onArticleClick;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bulletin, container, false);
        categoryLinearLayout = view.findViewById(R.id.bulletin_categories_linearLayout);
        recyclerView = view.findViewById(R.id.bulletin_articles_recycler_view);
        comingUpRecyclerView = view.findViewById(R.id.bulletin_articles_coming_up_recycler_view);
        loadRecyclerView();
        loadLinearLayoutView();
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        onArticleClick = (OnItemClick) context;
    }

    private void loadRecyclerView() {
        adapter = new BulletinRecyclerAdapter(onArticleClick, false);
        comingUpAdapter = new BulletinRecyclerAdapter(onArticleClick, true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        LinearLayoutManager comingUpLinearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        comingUpRecyclerView.setLayoutManager(comingUpLinearLayoutManager);
        comingUpRecyclerView.setAdapter(comingUpAdapter);

        addPadding();
    }

    private void addPadding() {
        int padding = (int) getResources().getDimension(R.dimen.padding);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.right = padding;
                outRect.left = padding;
                if(parent.getChildAdapterPosition(view) == 0){
                    outRect.top = padding;
                }
                outRect.bottom = padding;

            }
        });
        comingUpRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.right = padding;
                outRect.left = padding;
                if(parent.getChildAdapterPosition(view) == 0){
                    outRect.top = padding;
                }
                outRect.bottom = padding;

            }
        });
    }

    private void loadLinearLayoutView() {
        new BulletinIconCategoriesLoader(getContext()).loadCategories(this);

    }

    public void registerCategory(CategoryState categoryState) {
        Category newCategory = categoryState.getCategory();
        if(categoryList == null){
            categoryList = new ArrayList<>();
        }
        boolean isUpdate = false;
        boolean matchingArticleIds = false;
        for (int i = 0; i < categoryList.size(); i++) {
            Category categoryStateToCheck = categoryList.get(i).getCategory();
            if(newCategory.getCategoryID().equals(categoryStateToCheck.getCategoryID())){ // if is equal, then update
                categoryList.set(i, categoryState);
                matchingArticleIds = categoryState.getArticleIds().equals(categoryList.get(i).getArticleIds());
                isUpdate = true;
            }
        }
        if(!isUpdate) {
            categoryList.add(categoryState);
        }
        // Perform list update
        if(isUpdate && matchingArticleIds) { // Performance enchantment: only load articles if there is a change
            return;
        }
        // TODO: 6/13/2021 handle deletions
        // Selective Update
        for (int i = 0; i < categoryState.getArticleIds().size(); i++) {
            boolean isArticleUpdate = false;
            for (int j = 0; j < articleList.size(); j++) {
                if(articleList.get(j).getArticleID().equals(categoryState.getArticleIds().get(i))) {
                    // if existing, then update
                    // if we set an article loader again here, we will have duplicate db listeners for one article
                    isArticleUpdate = true;
                }
            }
            if(!isArticleUpdate){ // Only set an article loader if it is not set before
                //Log.d(TAG, "registerCategory: loadedArticle "+ categoryState.getArticleIds().get(i));
                ArticleLoaderBackend.getInstance(getActivity().getApplication()).getCacheObject(
                        categoryState.getArticleIds().get(i),
                        getResources(),
                        this);
            }
        }

    }

    @Override
    public void onLoad(List<String> categories) {
        //Log.d(TAG, "onLoad: ");
        //categoryLinearLayout.setWeightSum(categories.size());

        for (int i = 0; i < categories.size(); i++) {
            BulletinCategoryWidget category = new BulletinCategoryWidget(getContext(), categories.get(i), this);
            int p = (int) ScreenUtil.dp_to_px(getResources().getDimension(R.dimen.padding), getContext())/4;
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    (int) ScreenUtil.dp_to_px(100, getContext()),
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1.0f
            );
            layoutParams.setMargins(p, p, p, p);
            category.setLayoutParams(layoutParams);
            categoryLinearLayout.addView(category);
        }
    }

    public void updateView() {
        /*
        Jeffrey Aaron Jeyasingh
        The algorithm used here is optimized and I have tested it on very large lists ranging from ~40,000 to ~100,000 objects.
        The main optimization is in detecting articles to remove.
        Explained:
        First we create a list called deleteList, this will store possible articles that will be deleted (most of them wont be).
        Then, we use a for loop to populate the list because SortedList does not support conversion to ArrayList.
        see compareAndUpdateArticleList()
        We prepare a filteredList which contains only the articles that should be displayed.
        The optimization trick is not to track which articles have been deleted but which articles have been found.
        If an article is found (whether update or new), we know it is not deleted so we remove from the delete list.
        This allows us to skip one double-forLoop and reduces computation time.

        Although, computation time does not matter too much for the small number of articles we have to load,
        I have used this algorithm elsewhere and running time was cut from 3 minutes to 30 seconds!
         */

        // Perform filtering
        SortedList<Article> sortedListRef = adapter.getArticleSortedList();
        SortedList<Article> comingUpSortedListRef = comingUpAdapter.getArticleSortedList();

        sortedListRef.beginBatchedUpdates();
        List<Article> filteredList = new ArrayList<>();
        for (Article articleToCheck : articleList) {
            for (CategoryState state : categoryList) {
                if(articleToCheck.getCategoryID().equals(state.getCategory().getCategoryID())){
                    if(state.isSelected()){
                        filteredList.add(articleToCheck);
                    }
                }
            }
        }

        compareAndUpdateArticleList(sortedListRef, filteredList);

        // Get top 4 articles
        List<Article> upComingArticles = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            if(sortedListRef.size()>0) {
                upComingArticles.add(sortedListRef.removeItemAt(0));
            }
        }
        sortedListRef.endBatchedUpdates();

        comingUpSortedListRef.beginBatchedUpdates();

        compareAndUpdateArticleList(comingUpSortedListRef, upComingArticles);

        comingUpSortedListRef.endBatchedUpdates();
    }

    public void compareAndUpdateArticleList(SortedList<Article> sortedListRef, List<Article> filteredList){
        // Adjust sortedListRef using filteredList
        List<Article> deleteList = new ArrayList<>();
        for (int i = 0; i < sortedListRef.size(); i++) {
            deleteList.add(sortedListRef.get(i));
        }
        for (int i = 0; i < filteredList.size(); i++) {
            Article filteredArticle = filteredList.get(i);
            boolean isUpdate = false;
            for (int j = 0; j < sortedListRef.size(); j++) {
                Article sortedArticle = sortedListRef.get(j);
                if(filteredArticle.getArticleID().equals(sortedArticle.getArticleID())){
                    isUpdate = true;
                    sortedListRef.updateItemAt(j, filteredArticle);
                    //Log.d(TAG, "updateView1: " + filteredArticle.getTitle());
                    deleteList.remove(filteredArticle);
                }
            }
            if(!isUpdate){
                // The article is not yet in the sorted list, we need to add it
                sortedListRef.add(filteredArticle);
                deleteList.remove(filteredArticle);
            }
        }
        // Deleted the articles in the delete list
        for(Article a : deleteList){
            sortedListRef.remove(a);
        }
    }

    @Override
    public void onLoaded(Article articleN) {
        Article article = (Article) articleN;
        String articleId = article.getArticleID();
        boolean isArticleUpdate = false;
        for (int j = 0; j < articleList.size(); j++) {
            if(articleList.get(j).getArticleID().equals(articleId)){
                isArticleUpdate = true;
                articleList.set(j, article);
            }
        }
        if(!isArticleUpdate) {
            articleList.add(article);
        }

        updateView();
    }


    @Override
    public boolean isActivityDestroyed() {
        if(getActivity() == null){
            return true;
        }
        return getActivity().isDestroyed();
    }
}
class CategoryState {
    private Category category;
    private boolean isSelected;
    private List<String> articleIds;

    public CategoryState(Category category, boolean isSelected) {
        this.category = category;
        this.isSelected = isSelected;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public List<String> getArticleIds() {
        return articleIds;
    }

    public void setArticleIds(List<String> articleIds) {
        this.articleIds = articleIds;
    }
}