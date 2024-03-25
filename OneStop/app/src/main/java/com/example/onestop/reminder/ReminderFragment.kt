package com.example.onestop.reminder

import android.app.*
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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.onestop.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_reminder, container, false)

        appDatabase = AppDatabase.getInstance(requireContext())
        add = view.findViewById(R.id.floatingButton)
        empty = view.findViewById(R.id.empty)
        add.setOnClickListener { addReminder() }
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = linearLayoutManager
        GlobalScope.launch {
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
        select = dialog.findViewById<Button>(R.id.selectDate)
        add = dialog.findViewById<Button>(R.id.addButton)
        cancel = dialog.findViewById<Button>(R.id.cancelButton)
        val message = dialog.findViewById<EditText>(R.id.reminderMessage)
        val newCalender = Calendar.getInstance()
        select.setOnClickListener {
            val dialog = DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    val newDate = Calendar.getInstance()
                    val newTime = Calendar.getInstance()
                    val time = TimePickerDialog(
                        requireContext(),
                        { _, hourOfDay, minute ->
                            newDate.set(year, month, dayOfMonth, hourOfDay, minute, 0)
                            val tem = Calendar.getInstance()
                            if (newDate.timeInMillis - tem.timeInMillis > 0) textView.text =
                                newDate.time.toString() else Toast.makeText(
                                requireContext(),
                                "Invalid time",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        newTime.get(Calendar.HOUR_OF_DAY),
                        newTime.get(Calendar.MINUTE),
                        true
                    )
                    time.show()
                },
                newCalender.get(Calendar.YEAR),
                newCalender.get(Calendar.MONTH),
                newCalender.get(Calendar.DAY_OF_MONTH)
            )
            dialog.datePicker.minDate = System.currentTimeMillis()
            dialog.show()
        }
        add.setOnClickListener {
            val roomDAO: RoomDAO? = appDatabase.getRoomDAO()
            val reminders = Reminders()
            reminders.message = message.text.toString().trim()
            val remind: Date
            remind = try {
                val format = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)
                format.parse(textView.text.toString().trim())
            } catch (pe: ParseException) {
                Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                throw IllegalArgumentException(pe)
            }
            reminders.remindDate = remind
            GlobalScope.launch {
                if (roomDAO != null) {
                    roomDAO.Insert(reminders)
                }
            }
            requireActivity().runOnUiThread {
                Toast.makeText(requireContext(), "Inserted Successfully", Toast.LENGTH_SHORT).show()
            }

            GlobalScope.launch {
                setItemsInRecyclerView()
            }

            dialog.dismiss()
        }
        cancel.setOnClickListener { dialog.dismiss() }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }


    private suspend fun setItemsInRecyclerView() {
        val dao: RoomDAO? = appDatabase?.getRoomDAO()
        if (dao != null) {
            temp = dao.orderThetable()
        }
        if (!temp.isNullOrEmpty()) {
            empty?.visibility = View.INVISIBLE
            recyclerView?.visibility = View.VISIBLE
        }
        val parallelArrayOfIds = ArrayList<Int>()
        temp?.forEach { reminders ->
            parallelArrayOfIds.add(reminders.id)
        }

        requireActivity().runOnUiThread {
            adapter = AdapterReminders(temp)
            recyclerView?.adapter = adapter
            adapter?.setOnItemClickListener(object : AdapterReminders.OnItemClickListener {
                suspend fun onItemClick(position: Int) {
                    alarmRemover(parallelArrayOfIds.getOrNull(position) ?: return)
                    val appDatabase2: AppDatabase = AppDatabase.getInstance(requireContext())
                    val dao1: RoomDAO? = appDatabase2.getRoomDAO()
                    val reminder: Reminders? =
                        dao1?.getObjectUsingID(parallelArrayOfIds.getOrNull(position) ?: return)
                    if (reminder != null) {
                        dao1?.Delete(reminder)
                    }
                    requireActivity().runOnUiThread {
                        Toast.makeText(requireContext(), "Deleted", Toast.LENGTH_SHORT).show() // Stuff that updates the UI
                    }

                    setItemsInRecyclerView()
                }

                override fun onDeleteClick(position: Int) {

                }
            })
        }
    }

    private fun alarmRemover(id: Int) {
        requireActivity().runOnUiThread {
            val intent = Intent(requireContext(), NotifierAlarm::class.java)
            val intent1 = PendingIntent.getBroadcast(
                requireContext(),
                id,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(intent1)
        }
    }

    private fun confirmDeleteAll() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Do you want to delete all reminders?")
            .setPositiveButton("Yes") { _, _ ->
                GlobalScope.launch {
                    deleteAll()
                }
            }
            .setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private suspend fun deleteAll() {
        val appDatabase2: AppDatabase? = AppDatabase.getInstance(requireContext())
        val dao1: RoomDAO? = appDatabase2?.getRoomDAO()
        dao1?.DeleteAll()

        Toast.makeText(requireContext(), "All deleted", Toast.LENGTH_SHORT).show()
        setItemsInRecyclerView()
    }

}
