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
import com.example.onestop.databinding.FragmentIncompleteTaskBinding
import com.example.onestop.databinding.FragmentTaskManagerBinding


class IncompleteTask : Fragment() {


    private var _binding: FragmentIncompleteTaskBinding? = null
    private val binding get() = _binding!!

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
        database = Room.databaseBuilder(
            requireContext().applicationContext, myDatabase::class.java, "To_Do"
        ).fallbackToDestructiveMigration().build()

        binding.add.setOnClickListener {
            val intent = Intent(requireContext(), CreateCard::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)

        }

        setRecycler()
    }

    private fun setRecycler() {

        val ls=DataObject.getAllData()
//        print(ls)
        binding.recyclerView.adapter = Adapter(DataObject.getIncompletetask())
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}