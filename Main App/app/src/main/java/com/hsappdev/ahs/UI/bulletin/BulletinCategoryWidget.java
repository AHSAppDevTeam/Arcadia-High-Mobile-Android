package com.hsappdev.ahs.UI.bulletin;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.BlendMode;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.drawable.DrawableCompat;

import com.hsappdev.ahs.R;
import com.hsappdev.ahs.dataTypes.Category;
import com.hsappdev.ahs.util.ImageUtil;

import java.util.List;

public class BulletinCategoryWidget extends CardView implements CategoryLoadedCallback, View.OnClickListener{
    final private Resources r;
    final private String categoryId;
    private CategoryState categoryState;

    final private TextView labelTextView;
    final private ImageView bgImageView;
    final private ImageView iconImageView;


    final BulletinFragment bulletinFragment;

    private boolean isSelected;

    public BulletinCategoryWidget(@NonNull Context context, String categoryId, BulletinFragment bulletinFragment) {
        super(context);
        this.r = getResources();
        this.categoryId = categoryId;
        this.bulletinFragment = bulletinFragment;
        View view = inflate(context, R.layout.bulletin_category_widget, this);
        labelTextView = view.findViewById(R.id.bulletin_icon_text);
        bgImageView = view.findViewById(R.id.bulletin_icon_bg_imageView);
        iconImageView = view.findViewById(R.id.bulletin_icon_imageView);
        setRadius(r.getDimension(R.dimen.padding));
        TypedValue outValue = new TypedValue();
        getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
        setForeground(r.getDrawable(outValue.resourceId, getContext().getTheme()));
        setCardElevation(10);
        setClickable(true);
        setFocusable(true);
        setDetails();
    }

    private void setDetails() {
        setOnClickListener(this);
        new BulletinIconCategoriesLoader(getContext()).loadCategoryData(categoryId, this);
    }

    @Override
    public void onCategoryDataLoaded(Category category, List<String> articleIds) {
        categoryState = new CategoryState(category, isSelected);
        categoryState.setArticleIds(articleIds);
        labelTextView.setText(category.getTitle());
        setBackground();
        ImageUtil.setImageToView(category.getIconURL(), iconImageView);
        bulletinFragment.registerCategory(this.categoryState);
        bulletinFragment.updateView();
    }

    public void setBackground(){
        Category category = categoryState.getCategory();
        GradientDrawable gradientDrawable;

        if(isSelected){
            bgImageView.setPadding(0,0,0,0);

            gradientDrawable =
                    new GradientDrawable(
                            GradientDrawable.Orientation.BR_TL,
                            new int[] {category.getColor(),
                                    manipulateColor(category.getColor(), 0.7f)}
                    );
            labelTextView.setTextColor(r.getColor(R.color.white, getContext().getTheme()));
            iconImageView.setColorFilter(r.getColor(R.color.white, getContext().getTheme()));
            bgImageView.setImageDrawable(gradientDrawable);
            bgImageView.setImageTintList(null);

        } else {
//            gradientDrawable =
//                    new GradientDrawable(
//                            GradientDrawable.Orientation.BR_TL,
//                            new int[] {manipulateColor(category.getColor(), 0.25f),
//                                    manipulateColor(category.getColor(), 0.5f)}
//                    );
            labelTextView.setTextColor(category.getColor());
            iconImageView.setColorFilter(category.getColor());

            //bgImageView.setBackground();
            bgImageView.setBackgroundColor(category.getColor());

            //bgImageView.setBackgroundTintBlendMode(BlendMode.MULTIPLY);
            bgImageView.setPadding(10,10,10,10);
            bgImageView.setBackgroundTintList(ColorStateList.valueOf(category.getColor()));

            bgImageView.setImageDrawable(r.getDrawable(R.drawable.bulletin_category_icon_unselected_border, getContext().getTheme()));
        }
        //bgImageView.setTin(category.getColor());
    }




    @Override
    public void onClick(View v) {
        isSelected = !isSelected;
        categoryState.setSelected(isSelected);
        bulletinFragment.registerCategory(categoryState);
        bulletinFragment.updateView();
        setBackground();
    }

    /**
     * https://stackoverflow.com/questions/33072365/how-to-darken-a-given-color-int
     * @param color
     * @param factor
     * @return
     */
    public static int manipulateColor(int color, float factor) {
        int a = Color.alpha(color);
        int r = Math.round(Color.red(color) * factor);
        int g = Math.round(Color.green(color) * factor);
        int b = Math.round(Color.blue(color) * factor);
        return Color.argb(a,
                Math.min(r,255),
                Math.min(g,255),
                Math.min(b,255));
    }
}
