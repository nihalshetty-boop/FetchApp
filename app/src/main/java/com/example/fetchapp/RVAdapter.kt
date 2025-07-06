package com.example.fetchapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RVAdapter (private val requestList: List<RequestGroup>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class RequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val idText: TextView = itemView.findViewById(R.id.idtv)
        val listIdText: TextView = itemView.findViewById(R.id.listidtv)
        val nameText: TextView = itemView.findViewById(R.id.nametv)
    }

    class GroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val headerText: TextView = itemView.findViewById(R.id.groupText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_group, parent, false)
            GroupViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_row, parent, false)
            RequestViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = requestList[position]

        if (holder is GroupViewHolder && item is RequestGroup.GroupItem) {
            holder.headerText.text = item.header
        } else if (holder is RequestViewHolder && item is RequestGroup.DataItem) {
            val req = item.request
            holder.idText.text = "ID: ${req.id}"
            holder.listIdText.text = "List ID: ${req.listId}"
            holder.nameText.text = "Name: ${req.name}"
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (requestList[position]) {
            is RequestGroup.GroupItem -> 0
            is RequestGroup.DataItem -> 1
            else -> throw IllegalArgumentException("Unknown ViewHolder type")
        }
    }

    override fun getItemCount(): Int = requestList.size
}