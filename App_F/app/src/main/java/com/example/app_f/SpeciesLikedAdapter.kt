package com.example.app_f


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SpeciesLikedAdapter(private val nameList: List<SpeciesLikedData>) : RecyclerView.Adapter<SpeciesLikedAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpeciesLikedAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_species_liked,parent,false)
        return MyViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val name: SpeciesLikedData = nameList[position]
        holder.flowerName.text = name.nameSpeciesLiked
    }
    override fun getItemCount(): Int {
        return nameList.size
    }
    class MyViewHolder(item: View): RecyclerView.ViewHolder(item){
        val flowerName : TextView = item.findViewById(R.id.item_species_liked_name)
    }
}