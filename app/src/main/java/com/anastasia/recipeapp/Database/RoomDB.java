package com.anastasia.recipeapp.Database;
//initialize it with a database and add entities

import android.content.Context;

import androidx.room.ColumnInfo;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.anastasia.recipeapp.Models.Recipes;

@Database(entities = {Recipes.class}, version = 1, exportSchema = false)
public abstract class RoomDB extends RoomDatabase {
    private static RoomDB database;
    private static String DATABASE_NAME = "RecipeApp";

//create a method to get the instance of database
    public synchronized static RoomDB getInstance(Context context) {
       if (database == null) {
            database = Room.databaseBuilder(context.getApplicationContext(),
                    RoomDB.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
       }
       return database;
    }
    //create instance for MAinDAO
    public abstract MainDAO mainDAO();
}
