package com.example.onestop.taskmanager

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.onestop.R
import com.example.onestop.databinding.TaskViewBinding
import com.example.onestop.taskmanager.CardInfo
import com.example.onestop.taskmanager.Entity
import com.example.onestop.taskmanager.UpdateCard
import com.example.onestop.taskmanager.myDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AdapterIn(private val context: Context, private var data: List<CardInfo>) : RecyclerView.Adapter<AdapterIn.ViewHolder>() {

    private lateinit var database: myDatabase

    inner class ViewHolder(private val binding: TaskViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CardInfo) {
            binding.apply {
                titlein.text = item.title
                priorityin.text = item.priority

                when (item.priority) {
                    "1" -> mylayoutin.setBackgroundColor(Color.parseColor("#F05454"))
                    "2" -> mylayoutin.setBackgroundColor(Color.parseColor("#EDC988"))
                    else -> mylayoutin.setBackgroundColor(Color.parseColor("#00917C"))
                }

                taskimg.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val currentTask = data[position]
                        val taskId = position + 1
                        GlobalScope.launch(Dispatchers.IO) {
                            DataObject.updateState(position, true)
                            database.dao().updateTask(Entity(taskId, currentTask.title, currentTask.priority, true))
                        }
                    }
                }

                root.setOnClickListener {
                    val intent = Intent(context, UpdateCard::class.java)
                    intent.putExtra("id", adapterPosition)
                    context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = TaskViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        database = Room.databaseBuilder(
            context.applicationContext, myDatabase::class.java, "To_Do"
        ).build()
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }
}
