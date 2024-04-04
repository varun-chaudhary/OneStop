package com.example.onestop.reminder

import android.app.*
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.onestop.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ReminderFragment : Fragment() {

    private lateinit var add: FloatingActionButton
    private lateinit var dialog: Dialog
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

        GlobalScope.launch(Dispatchers.Main) {
            setItemsInRecyclerView()
        }

        return view
    }

    private fun addReminder() {
        dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.floating_popup)

        val textView = dialog.findViewById<TextView>(R.id.date)
        val select: Button
        val add: Button
        val cancel: Button
        val message = dialog.findViewById<EditText>(R.id.reminderMessage)

        val newCalender = Calendar.getInstance()

        select = dialog.findViewById(R.id.selectDate)
        select.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
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
                                Toast.makeText(
                                    requireContext(),
                                    "Invalid time",
                                    Toast.LENGTH_SHORT
                                ).show()
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

        add = dialog.findViewById(R.id.addButton)
        add.setOnClickListener {
            val roomDAO = appDatabase.getRoomDAO()
            val reminders = Reminders()
            reminders.message = message.text.toString().trim()

            val remind: Date
            try {
                val format = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)
                remind = format.parse(textView.text.toString().trim())
            } catch (pe: ParseException) {
                Toast.makeText(requireContext(), "Please Enter Date and Time Properly", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            reminders.remindDate = remind

            GlobalScope.launch {
                roomDAO.Insert(reminders)
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Inserted Successfully", Toast.LENGTH_SHORT).show()
                    setItemsInRecyclerView()
                }
            }
            dialog.dismiss()
        }

        cancel = dialog.findViewById(R.id.cancelButton)
        cancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

    }

    private suspend fun setItemsInRecyclerView() {
        val dao = appDatabase.getRoomDAO()
        temp = dao.orderThetable()
        withContext(Dispatchers.Main) {
            if (temp.isNotEmpty()) {
                empty.visibility = View.INVISIBLE
            } else {
                empty.visibility = View.VISIBLE
            }
            parallelArrayOfIds = ArrayList()
            for (i in temp.indices)
                parallelArrayOfIds.add(temp[i].id)

            adapter = AdapterReminders(temp)
            recyclerView.adapter = adapter
            adapter.setOnItemClickListener(object : AdapterReminders.OnItemClickListener {
                override suspend fun onDeleteClick(position: Int) {
                    val roomDAO = appDatabase.getRoomDAO()
                    val reminder = roomDAO.getObjectUsingID(parallelArrayOfIds[position])
                    reminder.let {
                        GlobalScope.launch {
                            roomDAO.Delete(it)
                            withContext(Dispatchers.Main) {
                                Toast.makeText(requireContext(), "Deleted", Toast.LENGTH_SHORT).show()
                                setItemsInRecyclerView()
                            }
                        }
                    }
                }
            })
        }
    }

}

