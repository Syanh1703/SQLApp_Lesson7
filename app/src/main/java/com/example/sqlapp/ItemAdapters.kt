package com.example.sqlapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ItemAdapters (val context: Context, val items :ArrayList<DataModels>)
    : RecyclerView.Adapter<ItemAdapters.ViewHolder>(){
    class ViewHolder (view:View) :RecyclerView.ViewHolder(view) {
        val name :TextView = view.findViewById(R.id.txtViewFolderName)
        val des :TextView = view.findViewById(R.id.txtViewFolderDescription)
        val renameBtn :ImageView = view.findViewById(R.id.imgViewRename)
        val deleteBtn :ImageView = view.findViewById(R.id.imgViewDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemAdapters.ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.folder_item
        ,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var item = items[position]
        if(holder is ViewHolder)
        {
            holder.name.text = item.folderName
            holder.des.text = item.folderDes
            holder.renameBtn.setOnClickListener {
                if(context is MainActivity)
                {
                    context.updateFolderDialog(item)
                }
            }

            holder.deleteBtn.setOnClickListener {
                if(context is MainActivity)
                {
                    context.deleteFolderDialog(item)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
