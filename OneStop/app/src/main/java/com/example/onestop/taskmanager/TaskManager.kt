package com.example.onestop.taskmanager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.onestop.R
import com.example.onestop.databinding.FragmentTaskManagerBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TaskManager : Fragment() {
    private var _binding: FragmentTaskManagerBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var database: myDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_task_manager, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        database = myDatabase.getInstance(requireContext().applicationContext)

        GlobalScope.launch{
            database.dao().getTasks() as MutableList<Task>
        }
        val viewPager: ViewPager2 = view.findViewById(R.id.viewPager)
        val fragments = listOf(IncompleteTask(), CompletedTask())
        val adapter = ViewPagerAdapter(fragments, childFragmentManager, lifecycle)
        viewPager.adapter = adapter

        val tabLayout: TabLayout = view.findViewById(R.id.tabLayout)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            if(position==0) {
                tab.text = "Incomplete"
            }
            else {
                tab.text = "Completed"
            }

        }.attach()
    }
}


