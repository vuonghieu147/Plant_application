package com.example.app_f

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UsersFlowerAdapter (private val nameList: List<UsersFlowerData>) :RecyclerView.Adapter<UsersFlowerAdapter.MyViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersFlowerAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_usersflower,parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UsersFlowerAdapter.MyViewHolder, position: Int) {
        val name: UsersFlowerData = nameList[position]
        holder.flowerName.text = name.flowerName

        holder.flowerName.setOnClickListener {
            val intent = Intent(holder.itemView.context, UserFlowerInfo::class.java)
            intent.putExtra("documentId", name.documentId)
            holder.itemView.context.startActivity(intent)
        }

    }
    override fun getItemCount(): Int {
        return nameList.size
    }
    class MyViewHolder(item: View): RecyclerView.ViewHolder(item){
        val flowerName : Button = item.findViewById(R.id.item_usersname)

    }
}