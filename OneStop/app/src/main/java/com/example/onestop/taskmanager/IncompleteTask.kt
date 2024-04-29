package com.example.onestop.taskmanager

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.onestop.databinding.FragmentIncompleteTaskBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.ArrayList


class IncompleteTask : Fragment() {

    private var _binding: FragmentIncompleteTaskBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: Adapter
    private lateinit var temp: List<Task>
    private lateinit var database: myDatabase


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIncompleteTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        database = myDatabase.getInstance(requireContext().applicationContext)
        binding.recyclerViewin.layoutManager = LinearLayoutManager(requireContext())
        binding.add.setOnClickListener {
            val intent = Intent(requireContext(), CreateCard::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
        viewLifecycleOwner.lifecycleScope.launch {
            setItemsInRecyclerView()
        }
    }

    private suspend fun setItemsInRecyclerView() {
        temp = database.dao().getTasks()
        if (temp.isNotEmpty()) {
            binding.recyclerViewin.visibility = View.VISIBLE
        }

        temp.forEach { card ->
            DataObject.parallelArrayOfIds.add(card.id)
        }
        adapter = Adapter(temp)
        binding.recyclerViewin.adapter = adapter
        adapter.setOnItemClickListener(object : Adapter.OnItemClickListener {
            override suspend fun onDeleteClick(pos: Int) {
                withContext(Dispatchers.Main) {
                    val t=database.dao().getObjectUsingID(DataObject.parallelArrayOfIds[pos])
                    database.dao().deleteTask(t)
                    setItemsInRecyclerView()
                }
            }

            override suspend fun onMarkClick(pos: Int) {
                withContext(Dispatchers.Main) {
                    val t=Task()
                    t.id=pos+1
                    t.title= temp[pos].title
                    t.priority= temp[pos].priority
                    t.done=false
                    GlobalScope.launch{

                        database.dao().updateTask(t)
                    }
                }

            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
