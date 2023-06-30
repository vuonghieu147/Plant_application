package com.example.app_f

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView


class SpeciesAdapter(private val nameList: List<FlowerName>) :RecyclerView.Adapter<SpeciesAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpeciesAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_species,parent,false)
        val holder = MyViewHolder(itemView)
        return holder
    }

    override fun onBindViewHolder(holder: SpeciesAdapter.MyViewHolder, position: Int) {
        val name: FlowerName = nameList[position]
        holder.flowerName.text = name.flowerName

        holder.flowerName.setOnClickListener {
            val intent = Intent(holder.itemView.context, InfoSpecies::class.java)
            intent.putExtra("documentId", name.documentId)
            holder.itemView.context.startActivity(intent)
        }
    }
    override fun getItemCount(): Int {
        return nameList.size
    }
    class MyViewHolder(item: View):RecyclerView.ViewHolder(item){
        val flowerName :Button = itemView.findViewById(R.id.item_name)
/*
        init {
            flowerName.setOnClickListener {
                val intent = Intent(itemView.context, InfoSpecies::class.java)
                itemView.context.startActivity(intent)
            }
        }
 */
    }
}
