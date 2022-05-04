package com.anastasia.recipeapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.anastasia.recipeapp.Adapters.RecipeListAdapter;
import com.anastasia.recipeapp.Database.RoomDB;
import com.anastasia.recipeapp.Models.Recipes;
import com.anastasia.recipeapp.Models.RecipesClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{
    RecyclerView recyclerView;
    RecipeListAdapter recipeListAdapter;
    List<Recipes> recipes = new ArrayList<>();
    RoomDB database;
    FloatingActionButton add_recipe;
    SearchView searchView_home;
    Recipes selectedRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_home);
        add_recipe = findViewById(R.id.add_recipe);
        searchView_home = findViewById(R.id.searchView_home);

        database = RoomDB.getInstance(this);
        recipes = database.mainDAO().getAll();

        updateRecycler(recipes);

        add_recipe.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RecipesTakerActivity.class);
          // startActivity(intent);
             startActivityForResult(intent, 101);

        });

        searchView_home.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });
    }

    private void filter(String newText) {
        List<Recipes> filteredList = new ArrayList<>();
        for (Recipes singleRecipe : recipes) {
            if (singleRecipe.getTitle().toLowerCase().contains(newText.toLowerCase())
                    || singleRecipe.getRecipe().toLowerCase().contains(newText.toLowerCase())) {
                filteredList.add(singleRecipe);
                //now we have to pass it to our recipeList Adapter
            }
        }
        // pass the filteres list that we've got
        recipeListAdapter.filterList(filteredList);
    }
//receive the data sent RecipeTakerActivity  in the main activity

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101) {
            if (resultCode == Activity.RESULT_OK) {
                Recipes new_recipe = (Recipes) data.getSerializableExtra("Recipe");
                database.mainDAO().insert(new_recipe);
                recipes.clear();
                recipes.addAll(database.mainDAO().getAll());
                recipeListAdapter.notifyDataSetChanged();
              //  recipeListAdapter.notifyItemRangeInserted();
            }
        }
        else if (requestCode == 102) {
            if (resultCode == Activity.RESULT_OK) {
                Recipes new_recipe = (Recipes) data.getSerializableExtra("Recipe");
                database.mainDAO().update(new_recipe.getID(), new_recipe.getTitle(), new_recipe.getRecipe());
                recipes.clear();
                recipes.addAll(database.mainDAO().getAll());
                recipeListAdapter.notifyDataSetChanged();
            }

        }
    }

    private void updateRecycler(List<Recipes> recipes) {
        recyclerView.setHasFixedSize(true);
     // recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recipeListAdapter = new RecipeListAdapter(MainActivity.this, recipes, recipesClickListener);

        recyclerView.setAdapter(recipeListAdapter);
    }
    private final RecipesClickListener recipesClickListener = new RecipesClickListener() {
        @Override
        public void onClick(Recipes recipes) {
            Intent intent = new Intent(MainActivity.this, RecipesTakerActivity.class);
            intent.putExtra("old_recipe", recipes);
            startActivityForResult(intent, 102);

        }

        @Override
        public void onLongClick(Recipes recipes, CardView cardView) {
            selectedRecipe = new Recipes();
            //pass the recipe that we got from he parameter
            selectedRecipe = recipes;
            showPopup(cardView);
        }
    };

    private void showPopup(CardView cardView) {
        PopupMenu popupMenu = new PopupMenu(this, cardView);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public boolean onMenuItemClick(MenuItem item) {
       switch (item.getItemId()) {
           case R.id.pin:
           if (selectedRecipe.isPinned()) {
                database.mainDAO().pin(selectedRecipe.getID(), false);
               Toast.makeText(MainActivity.this, "Unpinned", Toast.LENGTH_SHORT).show();

           }
           else { database.mainDAO().pin(selectedRecipe.getID(), true);
           Toast.makeText(MainActivity.this, "Pinned", Toast.LENGTH_SHORT).show();

           }
           recipes.clear();
           recipes.addAll(database.mainDAO().getAll());
           recipeListAdapter.notifyDataSetChanged();
           return true;

           case R.id.delete:
           database.mainDAO().delete(selectedRecipe);
           recipes.remove(selectedRecipe);
           recipeListAdapter.notifyDataSetChanged();
           Toast.makeText(MainActivity.this, "Recipe deleted", Toast.LENGTH_SHORT).show();
           return true;
           default:
               return false;

       }

    }
}