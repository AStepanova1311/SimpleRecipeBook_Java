package com.anastasia.recipeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.anastasia.recipeapp.Models.Recipes;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RecipesTakerActivity extends AppCompatActivity {

    EditText editText_title, editText_recipe;
    ImageView imageView_save;
    Recipes recipes;
    boolean isOldRecipe = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes_taker);

        imageView_save = findViewById(R.id.imageView_save);
        editText_title = findViewById(R.id.editText_title);
        editText_recipe = findViewById(R.id.editText_recipe);

        recipes = new Recipes();
        try {
            recipes = (Recipes) getIntent().getSerializableExtra("old_recipe");
            editText_title.setText(recipes.getTitle());
            editText_recipe.setText(recipes.getRecipe());
            isOldRecipe = true;
        } catch (Exception e) {
            e.printStackTrace();
        }


        imageView_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = editText_title.getText().toString();
                String description = editText_recipe.getText().toString();
                    if (description.isEmpty()) {
                        Toast.makeText(RecipesTakerActivity.this, "Please enter your recipe", Toast.LENGTH_SHORT).show();
                        return;
                    }
                SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm a");
                    Date date = new Date();

                   if (!isOldRecipe) {
                       recipes = new Recipes();
                   }


                    recipes.setTitle(title);
                    recipes.setRecipe(description);
                    recipes.setDate(formatter.format(date));

//sending the data back to main activity, dont forget to make your class serializable inside the Model class
                Intent intent = new Intent();
                intent.putExtra("Recipe", recipes);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }
}