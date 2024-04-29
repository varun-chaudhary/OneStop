//package com.example.onestop.taskmanager
//
//import android.content.Context
//import android.content.Intent
//import android.graphics.Color
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//import androidx.room.Room
//import com.example.onestop.databinding.TaskViewBinding
//import com.example.onestop.reminder.AdapterReminders
//import kotlinx.coroutines.GlobalScope
//import kotlinx.coroutines.launch
//
//class AdapterIn(private val context: Context, private var data: MutableList<CardInfo>) : RecyclerView.Adapter<AdapterIn.ViewHolder>() {
//
//
//    private var mListener: AdapterReminders.OnItemClickListener? = null
//    private lateinit var database:myDatabase
//    inner class ViewHolder(private val binding: TaskViewBinding) : RecyclerView.ViewHolder(binding.root) {
//        fun bind(item: CardInfo) {
//            binding.apply {
//                titlein.text = item.title
//                priorityin.text = item.priority
//
//                when (item.priority) {
//                    "1" -> mylayoutin.setBackgroundColor(Color.parseColor("#F05454"))
//                    "2" -> mylayoutin.setBackgroundColor(Color.parseColor("#EDC988"))
//                    else -> mylayoutin.setBackgroundColor(Color.parseColor("#00917C"))
//                }
//                taskimg.setOnClickListener {
//                    val pos = adapterPosition
//
//                    if (pos != RecyclerView.NO_POSITION) {
//                        var currentTask = data[pos]
//
//                        GlobalScope.launch{
//
//                            database.dao().updateTask(
//                                Entity(
//                                    pos + 1,
//                                    binding.titlein.text.toString(),
//                                    binding.priorityin.text.toString(),
//                                    true
//                                )
//                            )
//                        }
////                        data.removeAt(pos)
////                        notifyItemRemoved(pos)
//                    }
//                }
//
//                root.setOnClickListener {
//                    val intent = Intent(context, UpdateCard::class.java)
//                    intent.putExtra("id", adapterPosition)
//                    context.startActivity(intent)
//                }
//            }
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val binding = TaskViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        database = myDatabase.getInstance(context)
//        return ViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.bind(data[position])
//    }
//
//    override fun getItemCount(): Int {
//        return data.size
//    }
//}
