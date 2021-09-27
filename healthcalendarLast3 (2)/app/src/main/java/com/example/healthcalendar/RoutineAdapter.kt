package com.example.healthcalendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import calenderClass.Routine
import kotlin.collections.ArrayList

class RoutineAdapter(val RoutineList: ArrayList<Routine>) : RecyclerView.Adapter<RoutineAdapter.CustomVieHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoutineAdapter.CustomVieHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.routine_listitem,parent,false)
        return CustomVieHolder(view)
    }

    override fun onBindViewHolder(holder: RoutineAdapter.CustomVieHolder, position: Int) {
        holder.name.text=RoutineList.get(position).name
        holder.set.text=RoutineList.get(position).set.toString()
        holder.num.text=RoutineList.get(position).num.toString()
        holder.bodyPart.text=RoutineList.get(position).bodyPart

        return
    }

    override fun getItemCount(): Int {
        return RoutineList.size
    }

    class CustomVieHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val name= itemView.findViewById<TextView>(R.id.routine_name_added);
        val set= itemView.findViewById<TextView>(R.id.routine_set_added)
        val num= itemView.findViewById<TextView>(R.id.routine_per_set_added)
        val bodyPart=itemView.findViewById<TextView>(R.id.routine_bodypart_added)

    }}