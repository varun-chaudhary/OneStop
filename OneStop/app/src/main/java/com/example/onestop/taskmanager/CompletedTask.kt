package com.example.onestop.taskmanager


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.onestop.databinding.FragmentCompletedTaskBinding
import kotlinx.coroutines.launch

class CompletedTask : Fragment() {


    private var _binding: FragmentCompletedTaskBinding? = null
    private val binding get() = _binding!!

    private lateinit var database: myDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCompletedTaskBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        database = myDatabase.getInstance(requireContext().applicationContext)


        viewLifecycleOwner.lifecycleScope.launch {
            setItemsInRecyclerView()
        }
    }

    private suspend fun  setItemsInRecyclerView() {
        binding.recyclerView.adapter = Adapter(database.dao().getTasks())
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}