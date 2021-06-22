package com.hsappdev.ahs;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hsappdev.ahs.UI.home.CommunityArticleUnit;
import com.hsappdev.ahs.dataTypes.Article;
import com.hsappdev.ahs.dataTypes.CommunitySection;
import com.hsappdev.ahs.util.Helper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CommunityActivity extends AppCompatActivity implements OnItemClick, View.OnClickListener{
    public static final String DATA_KEY = "DATA_INDEX";

    private final int gridNum = 2;

    private CommunitySection communitySection;
    private Resources r;

    private ImageButton backButton;
    private TextView categoryTitle;
    private TextView dateText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.community_activity);
        communitySection = getIntent().getParcelableExtra(DATA_KEY);
        r = getResources();

        backButton = findViewById(R.id.community_activity_home_button);
        categoryTitle = findViewById(R.id.community_activity_category_name);
        dateText = findViewById(R.id.community_date);
        backButton.setOnClickListener(this);
        Helper.setBoldRegularText(categoryTitle, communitySection.getCategoryDisplayName(), " News");
        loadArticles();
    }

    private void loadArticles() {
        DatabaseReference ref = FirebaseDatabase.getInstance(FirebaseApp.getInstance()).getReference()
                .child(r.getString(R.string.db_categories))
                .child(communitySection.getCategoryId());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                List<String> articleIds = new ArrayList<>();
                for (DataSnapshot articleId : snapshot.child(r.getString(R.string.db_categories_articleIds)).getChildren()) {
                    articleIds.add(articleId.getValue(String.class));
                }

                setUpView(articleIds);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setUpView(List<String> articleIds) {
        String date = new SimpleDateFormat("MMMM d, yyyy", Locale.US).format(Calendar.getInstance().getTimeInMillis());
        dateText.setText(date);

        LinearLayout linearLayout = findViewById(R.id.community_activity_article_linearLayout);

        if(articleIds.size() > 0) {
            CommunityArticleUnit articleUnit = new CommunityArticleUnit(getApplicationContext(), articleIds.get(0), this, this);
            linearLayout.addView(articleUnit);
            articleIds.remove(0);

        }
        if(articleIds.size() > 0) {
            for (int i = 0; i < articleIds.size(); i += gridNum) {
                LinearLayout intermediateLinearLayout = new LinearLayout(getApplicationContext());
                intermediateLinearLayout.setWeightSum(gridNum);
                for (int j = i; j < i + gridNum; j++) {
                    if (j < articleIds.size()) {
                        CommunityArticleUnit secondaryArticleUnit = new CommunityArticleUnit(getApplicationContext(), articleIds.get(j), this, this);
                        secondaryArticleUnit.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                1.0f
                        ));
                        intermediateLinearLayout.addView(secondaryArticleUnit);
                    }
                }
                linearLayout.addView(intermediateLinearLayout);
            }
        }

    }

    @Override
    public void onArticleClicked(Article article) {
        Intent intent = new Intent(CommunityActivity.this, ArticleActivity.class);
        intent.putExtra(ArticleActivity.data_KEY, article);
        startActivity(intent);
        overridePendingTransition(R.anim.enter_from_right, R.anim.empty_animation);
    }

    @Override
    public void onClick(View v) {
        finish();
        overridePendingTransition(R.anim.empty_animation, R.anim.exit_to_right);
    }
}
