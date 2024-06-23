package com.example.myfamily

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView

// Adapter class for managing a list of MemberModel objects in a RecyclerView
class MemberAdapter(private val listMembers: List<MemberModel>) : RecyclerView.Adapter<MemberAdapter.ViewHolder>() {

    // Called when RecyclerView needs a new ViewHolder of the given type to represent an item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)  // Get the LayoutInflater from the parent context
        val item = inflater.inflate(R.layout.item_member, parent, false)  // Inflate the item layout
        return ViewHolder(item)  // Return a new ViewHolder instance
    }

    // Returns the total number of items in the data set held by the adapter
    override fun getItemCount(): Int {
        return listMembers.size  // Return the size of the member list
    }

    // Called by RecyclerView to display the data at the specified position
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listMembers[position]  // Get the member at the specified position
        holder.nameUser.text = item.name  // Set the name of the member in the ViewHolder's TextView
        holder.address.text = item.address  // Set the address of the member
        holder.battery.text = item.battery  // Set the battery percentage of the member
        holder.distance.text = item.distance  // Set the distance of the member
    }

    // ViewHolder class that holds the view for each item
    class ViewHolder(private val item: View) : RecyclerView.ViewHolder(item) {
        // Initialize the views that will hold the member data
        val nameUser: TextView = item.findViewById(R.id.name_user)  // Find and assign the TextView for the user's name
        val address: TextView = item.findViewById(R.id.name_location)  // Find and assign the TextView for the user's address
        val battery: TextView = item.findViewById(R.id.txt_percentange)  // Find and assign the TextView for the battery percentage
        val distance: TextView = item.findViewById(R.id.txt_send)  // Find and assign the TextView for the distance
    }
}
