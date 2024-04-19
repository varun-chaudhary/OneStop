package com.example.onestop.taskmanager


import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.onestop.databinding.FragmentTaskManagerBinding

import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.onestop.R
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Adapter(var data: List<CardInfo>) : RecyclerView.Adapter<Adapter.viewHolder>() {
    private lateinit var binding: FragmentTaskManagerBinding
    private lateinit var database: myDatabase

    class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.title)
        var priority: TextView = itemView.findViewById(R.id.priority)
        var layout: View = itemView.findViewById(R.id.mylayout)
        var img:ImageView =itemView.findViewById(R.id.taskimg)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        var itemView = LayoutInflater.from(parent.context).inflate(R.layout.task_view, parent, false)
        return viewHolder(itemView)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        when (data[position].priority.toLowerCase()) {
            "1" -> holder.layout.setBackgroundColor(Color.parseColor("#F05454"))
            "2" -> holder.layout.setBackgroundColor(Color.parseColor("#EDC988"))
            else -> holder.layout.setBackgroundColor(Color.parseColor("#00917C"))
        }
        when (data[position].done) {
            false -> holder.img.setImageResource(R.drawable.ic_baseline_delete_24)
            true -> holder.img.setImageResource(R.drawable.ic_marktick)
        }


        holder.title.text = data[position].title
        holder.priority.text = data[position].priority
        holder.img.setOnClickListener {
            database = Room.databaseBuilder(
                holder.itemView.context.applicationContext, myDatabase::class.java, "To_Do"
            ).fallbackToDestructiveMigration().build()
            if(data[position].done)
            {
                DataObject.deleteData(position)
                GlobalScope.launch {
                    database.dao().updateTask(Entity(position+1, data[position].title, data[position].priority,false))
                }
            }
            else
            {
                DataObject.updateState(position,true)
                GlobalScope.launch {
                    database.dao().deleteTask(Entity(position+1, data[position].title, data[position].priority,false))
                }

            }
        }
        holder.itemView.setOnClickListener{
            val intent= Intent(holder.itemView.context,UpdateCard::class.java)
            intent.putExtra("id",position)
            holder.itemView.context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return data.size
    }
}