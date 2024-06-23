package com.example.myfamily

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.example.myfamily.R

// Adapter for displaying invite emails in a RecyclerView
class InviteMailAdapter(
    private val listInvites: List<String>,        // List of invite emails
    private val onActionClick: OnActionClick     // Listener for accept/deny actions
) : RecyclerView.Adapter<InviteMailAdapter.ViewHolder>() {

    // Called when RecyclerView needs a new ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflate the item_invite_mail layout
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_invite_mail, parent, false)
        return ViewHolder(view)
    }

    // Called by RecyclerView to display data at a specific position
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Get the invite email at the current position
        val item = listInvites[position]
        // Set the email text
        holder.name.text = item

        // Set click listeners for accept and deny buttons
        holder.accept.setOnClickListener {
            onActionClick.onAcceptClick(item)
        }

        holder.deny.setOnClickListener {
            onActionClick.onDenyClick(item)
        }
    }

    // Returns the total number of items in the list
    override fun getItemCount(): Int {
        return listInvites.size
    }

    // ViewHolder class to hold the views of each item
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Views within the item layout
        val name: TextView = itemView.findViewById(R.id.mail)
        val accept: Button = itemView.findViewById(R.id.accept)
        val deny: Button = itemView.findViewById(R.id.deny)
    }

    // Interface for handling accept/deny actions
    interface OnActionClick {
        fun onAcceptClick(mail: String)
        fun onDenyClick(mail: String)
    }
}

