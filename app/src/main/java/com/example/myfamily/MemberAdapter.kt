package com.example.myfamily

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView

class MemberAdapter(private val listMembers: List<MemberModel>) : RecyclerView.Adapter<MemberAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val item = inflater.inflate(R.layout.item_member, parent, false)
        return ViewHolder(item)
    }

    override fun getItemCount(): Int {
        return listMembers.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {     // It will tell how many members view we will have on screen
        val item = listMembers[position]
        holder.nameUser.text = item.name
        holder.address.text = item.address
        holder.battery.text = item.battery
        holder.distance.text = item.distance
    }

    class ViewHolder(private val item: View) : RecyclerView.ViewHolder(item) {    // It will hold the views as name also suggests
        val imageUser = item.findViewById<ImageView>(R.id.img_user)
        val nameUser = item.findViewById<TextView>(R.id.name_user)
        val address = item.findViewById<TextView>(R.id.name_location)
        val battery = item.findViewById<TextView>(R.id.txt_percentange)
        val distance = item.findViewById<TextView>(R.id.txt_send)
    }
}