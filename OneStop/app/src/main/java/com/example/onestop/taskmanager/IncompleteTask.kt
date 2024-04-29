package com.example.onestop.taskmanager



import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.onestop.databinding.FragmentIncompleteTaskBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class IncompleteTask : Fragment() {

    private var _binding: FragmentIncompleteTaskBinding? = null
    private val binding get() = _binding!!

    private lateinit var database: myDatabase
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapterTask: AdapterTask
    private lateinit var temp: List<CardInfo>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIncompleteTaskBinding.inflate(inflater, container, false)
        recyclerView = binding.recyclerViewin
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        database = Room.databaseBuilder(
            requireContext().applicationContext, myDatabase::class.java, "To_Do"
        ).build()

        binding.add.setOnClickListener {
            val intent = Intent(requireContext(), CreateCard::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
        viewLifecycleOwner.lifecycleScope.launch {
            setRecycler()
        }
    }

    private fun setRecycler() {
        temp = DataObject.getIncompletetask()
        adapterTask =AdapterTask(temp)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapterTask

        adapterTask.setOnItemClickListener(object : AdapterTask.OnItemClickListener {


            override suspend fun onMarkClick(position: Int) {


                val cur=temp[position]
                val taskId = position + 1
                val newData = temp.toMutableList()
                newData.removeAt(position)
                temp = newData
                DataObject.updateState(position,true)
                GlobalScope.launch {
                    database.dao().updateTask(Entity(taskId, cur.title, cur.priority, true))
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
