package com.example.myfamily

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// InviteAdapter class for the RecyclerView, taking a list of ContactModel as a parameter
class InviteAdapter(private val listContacts: List<ContactModel>) :
    RecyclerView.Adapter<InviteAdapter.ViewHolder>() {

    // Called when RecyclerView needs a new ViewHolder of the given type to represent an item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)  // Create a LayoutInflater to inflate the item layout
        val item = inflater.inflate(R.layout.item_invite, parent, false)  // Inflate the item layout
        return ViewHolder(item)  // Return a new ViewHolder instance
    }

    // Called by RecyclerView to display the data at the specified position
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listContacts[position]  // Get the contact at the specified position
        holder.name.text = item.name  // Set the name of the contact in the ViewHolder's TextView
    }

    // Returns the total number of items in the data set held by the adapter
    override fun getItemCount(): Int {
        return listContacts.size  // Return the size of the contact list
    }

    // ViewHolder class that holds the view for each item
    class ViewHolder(private val item: View) : RecyclerView.ViewHolder(item) {
        val name: TextView = item.findViewById(R.id.name)  // Find the TextView in the item layout and assign it to the 'name' property
    }
}
