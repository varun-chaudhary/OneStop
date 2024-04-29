package com.example.onestop.taskmanager
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.onestop.databinding.TaskViewBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Adapter(private val data: List<Task>) : RecyclerView.Adapter<Adapter.ViewHolder>() {

    inner class ViewHolder(private val binding: TaskViewBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.taskimg.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    GlobalScope.launch {
                        mListener?.onMarkClick(position)
                    }
                }
            }
//            binding.deltask.setOnClickListener {
//                val position = adapterPosition
//                if (position != RecyclerView.NO_POSITION) {
//                    GlobalScope.launch {
//                        mListener?.onDeleteClick(position)
//                    }
//                }
//            }
        }

        fun bind(cardInfo:Task) {
            binding.apply {
                titlein.text = cardInfo.title
                priorityin.text = cardInfo.priority
                when (cardInfo.priority) {
                    "1" -> mylayoutin.setBackgroundColor(Color.parseColor("#F05454"))
                    "2" -> mylayoutin.setBackgroundColor(Color.parseColor("#EDC988"))
                    else -> mylayoutin.setBackgroundColor(Color.parseColor("#00917C"))
                }
                root.setOnClickListener {
                    val intent = Intent(root.context, UpdateCard::class.java)
                    intent.putExtra("id", adapterPosition)
                    root.context.startActivity(intent)
                }
            }
        }
    }

    private var mListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = TaskViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task: Task=data[position]
        holder.bind(task)
    }

    override fun getItemCount(): Int = data.size

    interface OnItemClickListener {
        suspend fun onDeleteClick(position: Int)
        suspend fun onMarkClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mListener = listener
    }
}
