package com.example.app_f


import android.content.Intent
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
        holder.speciesName.text = name.nameSpeciesLiked
        holder.speciesName.setOnClickListener {
            val intent = Intent(holder.itemView.context,InfoSpecies::class.java)
            intent.putExtra("documentId", name.documentId)
            holder.itemView.context.startActivity(intent)
        }
    }
    override fun getItemCount(): Int {
        return nameList.size
    }
    class MyViewHolder(item: View): RecyclerView.ViewHolder(item){
        val speciesName : TextView = item.findViewById(R.id.item_species_liked_name)
    }
}