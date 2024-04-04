package com.example.onestop.reminder

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.onestop.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ReminderFragment : Fragment() {

    private lateinit var add: FloatingActionButton
    private lateinit var appDatabase: AppDatabase
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdapterReminders
    private lateinit var temp: List<Reminders>
    private lateinit var empty: TextView
    private lateinit var parallelArrayOfIds: ArrayList<Int>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_reminder, container, false)

        appDatabase = AppDatabase.getInstance(requireContext())

        add = view.findViewById(R.id.floatingButton)
        empty = view.findViewById(R.id.empty)

        add.setOnClickListener {
            addReminder()
        }

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = linearLayoutManager

        viewLifecycleOwner.lifecycleScope.launch {
            setItemsInRecyclerView()
        }

        return view
    }

    @SuppressLint("ScheduleExactAlarm")
    private fun addReminder() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.floating_popup)

        val textView = dialog.findViewById<TextView>(R.id.date)
        val select = dialog.findViewById<Button>(R.id.selectDate)
        val add = dialog.findViewById<Button>(R.id.addButton)
        val cancel = dialog.findViewById<Button>(R.id.cancelButton)
        val message = dialog.findViewById<EditText>(R.id.reminderMessage)

        val newCalender = Calendar.getInstance()
        select.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    val newDate = Calendar.getInstance()
                    val newTime = Calendar.getInstance()
                    TimePickerDialog(
                        requireContext(),
                        { _, hourOfDay, minute ->
                            newDate.set(year, month, dayOfMonth, hourOfDay, minute, 0)
                            val tem = Calendar.getInstance()
                            if (newDate.timeInMillis - tem.timeInMillis > 0)
                                textView.text = newDate.time.toString()
                            else
                                showToast("Invalid time")
                        },
                        newTime.get(Calendar.HOUR_OF_DAY),
                        newTime.get(Calendar.MINUTE),
                        true
                    ).show()
                },
                newCalender.get(Calendar.YEAR),
                newCalender.get(Calendar.MONTH),
                newCalender.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.datePicker.minDate = System.currentTimeMillis()
            datePickerDialog.show()
        }

        add.setOnClickListener {
            val roomDAO = appDatabase.getRoomDAO()
            val reminders = Reminders()
            reminders.message = message.text.toString().trim()

            val remind: Date
            try {
                val format = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)
                remind = format.parse(textView.text.toString().trim())
            } catch (pe: ParseException) {
                showToast("Please Enter Date and Time Properly")
                return@setOnClickListener
            }

            reminders.remindDate = remind
            viewLifecycleOwner.lifecycleScope.launch {
                roomDAO.Insert(reminders)

                withContext(Dispatchers.Main) {
                    val l = roomDAO.orderThetable()
                    val lastReminder = l.last()
                    val calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"))
                    calendar.time = lastReminder.remindDate
                    calendar.set(Calendar.SECOND, 0)
                    val intent = Intent(requireContext(), NotifierAlarm::class.java)
                    intent.putExtra("Message", lastReminder.message)
                    intent.putExtra("RemindDate", lastReminder.remindDate.toString())
                    intent.putExtra("id", lastReminder.id)
                    val intent1 = PendingIntent.getBroadcast(requireContext(), lastReminder.id, intent, PendingIntent.FLAG_MUTABLE)
                    val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, intent1)

                    showToast("Inserted Successfully")
                    setItemsInRecyclerView()
                    dialog.dismiss()
                }
            }
        }

        cancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    private suspend fun setItemsInRecyclerView() {
        val dao = appDatabase.getRoomDAO()
        temp = dao.orderThetable()
        if (temp.isNotEmpty()) {
            empty.visibility = View.INVISIBLE
            recyclerView.visibility = View.VISIBLE
        }
        parallelArrayOfIds = ArrayList()
        temp.forEach { reminders ->
            parallelArrayOfIds.add(reminders.id)
        }

        adapter = AdapterReminders(temp)
        recyclerView.adapter = adapter
        adapter.setOnItemClickListener(object : AdapterReminders.OnItemClickListener {
            override suspend fun onDeleteClick(position: Int) {
                withContext(Dispatchers.Main) {
                    alarmRemover(parallelArrayOfIds[position])
                    val appDatabase2 = AppDatabase.getInstance(requireContext())
                    val dao1 = appDatabase2.getRoomDAO()
                    val reminder = dao1.getObjectUsingID(parallelArrayOfIds[position])
                    dao1.Delete(reminder)
                    showToast("Deleted")
                    setItemsInRecyclerView()
                }
            }

        })
    }

    private fun alarmRemover(id: Int) {
        val intent = Intent(requireContext(), NotifierAlarm::class.java)
        val intent1 = PendingIntent.getBroadcast(requireContext(), id, intent, PendingIntent.FLAG_MUTABLE)
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(intent1)
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
