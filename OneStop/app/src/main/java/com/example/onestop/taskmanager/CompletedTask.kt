package com.example.onestop.taskmanager

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.onestop.R
import com.example.onestop.databinding.FragmentCompletedTaskBinding
import com.example.onestop.databinding.FragmentIncompleteTaskBinding

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
        database = Room.databaseBuilder(
            requireContext().applicationContext, myDatabase::class.java, "To_Do"
        ).fallbackToDestructiveMigration().build()

        setRecycler()
    }

    private fun setRecycler() {
        binding.recyclerView.adapter = Adapter(DataObject.getCompletedtask())
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}