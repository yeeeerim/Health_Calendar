package com.example.healthcalendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import calenderClass.Food
import kotlin.collections.ArrayList

class FoodAdapter(val FoodList: ArrayList<Food>) : RecyclerView.Adapter<FoodAdapter.CustomVieHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodAdapter.CustomVieHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.food_listitem,parent,false)
        return CustomVieHolder(view)
    }

    override fun onBindViewHolder(holder: FoodAdapter.CustomVieHolder, position: Int) {
        holder.image.setImageResource(FoodList.get(position).image)
        holder.foodName.text=FoodList.get(position).name
        holder.foodCalPer100g.text=FoodList.get(position).cal_per_100g.toString()
        holder.howMany.text=FoodList.get(position).how_many.toString()

        return
    }

    override fun getItemCount(): Int {
        return FoodList.size
    }

    class CustomVieHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val image= itemView.findViewById<ImageView>(R.id.rv_food);
        val foodName= itemView.findViewById<TextView>(R.id.food_name_added)
        val foodCalPer100g= itemView.findViewById<TextView>(R.id.cal_per_100g_added)
        val howMany=itemView.findViewById<TextView>(R.id.food_num_added)

    }

}