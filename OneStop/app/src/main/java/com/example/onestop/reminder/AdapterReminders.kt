package com.example.onestop.reminder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.onestop.R

class AdapterReminders(private val allReminders: List<Reminders>) :
    RecyclerView.Adapter<AdapterReminders.MyViewHolder>() {

    private var mListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.reminder_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val reminders: Reminders = allReminders[position]
        holder.message.text = if (reminders.message?.isNotEmpty() == true) reminders.message else "No Message"
        holder.time.text = reminders.remindDate.toString()
    }

    override fun getItemCount(): Int {
        return allReminders.size
    }

    interface OnItemClickListener {
        fun onDeleteClick(position: Int)
    }

    fun setOnItemClickListener(listener: Any) {
//        mListener = listener
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val message: TextView = itemView.findViewById(R.id.textView1)
        val time: TextView = itemView.findViewById(R.id.textView21)
        private val deleteReminder: ImageView = itemView.findViewById(R.id.ReminderDelete)

        init {
            deleteReminder.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    mListener?.onDeleteClick(position)
                }
            }
        }
    }
}
