package com.anastasia.recipeapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.anastasia.recipeapp.Models.Recipes;
import com.anastasia.recipeapp.Models.RecipesClickListener;
import com.anastasia.recipeapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.PrimitiveIterator;
import java.util.Random;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeViewHolder>{
    //create an object for the context
    Context context;
    List<Recipes> list;
    RecipesClickListener listener;
    //create constructor for cliskListener

    public RecipeListAdapter(Context context, List<Recipes> list, RecipesClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }


    //create the object for the listener


    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecipeViewHolder(LayoutInflater.from(context).inflate(R.layout.recipes_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        holder.textView_title.setText(list.get(position).getTitle());
        holder.textView_title.setSelected(true);

        holder.textView_recipe.setText((list.get(position).getRecipe()));

        holder.textView_date.setText((list.get(position).getDate()));
        holder.textView_date.setSelected(true);

        if (list.get(position).isPinned()) {
            holder.imageView_pin.setImageResource(R.drawable.ic_pin);
        }
        else {
            holder.imageView_pin.setImageResource(0);
        }

        int color_code = getRandomColor();
        holder.recipe_container.setCardBackgroundColor(holder.itemView.getResources().getColor(color_code, null));

        holder.recipe_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(list.get(holder.getAdapterPosition()));
            }
        });
        holder.recipe_container.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                listener.onLongClick(list.get(holder.getAdapterPosition()), holder.recipe_container);
                return true;
            }
        });
    }

    // create a new method to change colors
    private int getRandomColor(){
        List<Integer> colorCode = new ArrayList<>();
        colorCode.add(R.color.color1);
        colorCode.add(R.color.color2);
        colorCode.add(R.color.color3);
        colorCode.add(R.color.color4);
        colorCode.add(R.color.color5);

        //call an object of the random class to pick a different color every time
        Random random = new Random();
        int random_color = random.nextInt(colorCode.size());
        return colorCode.get(random_color);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    //create a method to pass recipes that match search request
    public void filterList(List<Recipes> filteredList) {
        //replaces global list with filtered
        list = filteredList;
        notifyDataSetChanged();
    }
}
class RecipeViewHolder extends RecyclerView.ViewHolder {

    CardView recipe_container;
    TextView textView_title, textView_recipe, textView_date;
    ImageView imageView_pin;

    public RecipeViewHolder(@NonNull View itemView) {
        super(itemView);
        recipe_container = itemView.findViewById(R.id.recipe_container);
        textView_title = itemView.findViewById(R.id.textView_title);
        textView_recipe = itemView.findViewById(R.id.textView_recipe);
        textView_date = itemView.findViewById(R.id.textView_date);
        imageView_pin = itemView.findViewById(R.id.imageView_pin);
    }
}