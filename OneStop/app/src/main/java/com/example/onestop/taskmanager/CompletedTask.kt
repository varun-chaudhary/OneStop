package com.example.onestop.taskmanager


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.onestop.databinding.FragmentCompletedTaskBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CompletedTask : Fragment() {


    private var _binding: FragmentCompletedTaskBinding? = null
    private val binding get() = _binding!!

    private lateinit var database: myDatabase

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdapterComplete
    private lateinit var temp: List<CardInfo>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCompletedTaskBinding.inflate(inflater, container, false)
        recyclerView=binding.recyclerView
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        database = Room.databaseBuilder(
            requireContext().applicationContext, myDatabase::class.java, "To_Do"
        ).build()

        setRecycler()
    }

    private fun setRecycler() {
        temp = DataObject.getCompletedtask()
        adapter =AdapterComplete(temp)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        adapter.setOnItemClickListener(object : AdapterComplete.OnItemClickListener {


            override suspend fun onDeleteClick(position: Int) {
                val cur=temp[position]
                val taskId = position + 1
                val newData = temp.toMutableList()
                newData.removeAt(position)
                temp = newData
                DataObject.deleteData(position)
                GlobalScope.launch {
                    database.dao().deleteTask(Entity(taskId, cur.title, cur.priority, false))
                }
                viewLifecycleOwner.lifecycleScope.launch {
                    setRecycler()
                }
            }
        })
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}