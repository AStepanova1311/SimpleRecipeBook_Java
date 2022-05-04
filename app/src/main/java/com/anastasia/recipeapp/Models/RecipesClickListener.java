package com.anastasia.recipeapp.Models;

import androidx.cardview.widget.CardView;

public interface RecipesClickListener {
    void onClick(Recipes recipes);
    void onLongClick(Recipes recipes, CardView cardView);

}
