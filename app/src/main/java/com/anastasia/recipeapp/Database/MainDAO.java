package com.anastasia.recipeapp.Database;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.anastasia.recipeapp.Models.Recipes;

import java.util.List;

@Dao
public interface MainDAO {
    //create a method to insert data in our DB
    @Insert(onConflict = REPLACE)
    void insert(Recipes recipes);

//CREATE A METHOD TO GET THE DATA
    @Query("SELECT * FROM Recipes")
    List<Recipes> getAll();

    //Create  a method to update all the items
    @Query("UPDATE Recipes SET title = :title, recipe = :recipe WHERE ID = :id")
    void update(int id, String title, String recipe);

    @Delete
    void delete(Recipes recipes);

    @Query("UPDATE Recipes SET pinned =:pin WHERE ID = :id")
    void pin(int id, boolean pin);
}
