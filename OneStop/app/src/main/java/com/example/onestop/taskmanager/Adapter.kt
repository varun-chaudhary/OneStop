import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.onestop.databinding.TaskViewCompleteBinding
import com.example.onestop.taskmanager.CardInfo
import com.example.onestop.taskmanager.Entity
import com.example.onestop.taskmanager.UpdateCard
import com.example.onestop.taskmanager.myDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Adapter(private val context: Context, private var data: List<CardInfo>) : RecyclerView.Adapter<Adapter.ViewHolder>() {

    private lateinit var database: myDatabase

    inner class ViewHolder(private val binding: TaskViewCompleteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CardInfo) {
            binding.apply {
                title.text = item.title
                priority.text = item.priority

                when (item.priority) {
                    "1" -> mylayout.setBackgroundColor(Color.parseColor("#F05454"))
                    "2" -> mylayout.setBackgroundColor(Color.parseColor("#EDC988"))
                    else -> mylayout.setBackgroundColor(Color.parseColor("#00917C"))
                }

                deltask.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val taskId = position + 1
                        val newData = data.toMutableList()
                        newData.removeAt(position)
                        data = newData
                        notifyItemRemoved(position)
                        GlobalScope.launch(Dispatchers.IO) {
                            database.dao().deleteTask(Entity(taskId, item.title, item.priority, false))
                        }
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = TaskViewCompleteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
