package com.example.onestop.quicknotes

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.onestop.R
import com.example.onestop.databinding.FragmentQuickNotesBinding

import com.google.android.material.snackbar.Snackbar

class QuickNotes : Fragment(){

    private var _binding: FragmentQuickNotesBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: NoteViewModel
    private lateinit var noteRVAdapter: NoteRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuickNotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fabAddNote.setOnClickListener {
            val addNoteIntent = Intent(requireActivity(), AddNote::class.java)
            startActivity(addNoteIntent)
        }

        binding.recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        // Instantiate the correct adapter
        noteRVAdapter = NoteRVAdapter(requireContext(), this)
        binding.recyclerView.adapter = noteRVAdapter

        setUpSwipeToDeleteItem()

        viewModel = ViewModelProvider(requireActivity()).get(NoteViewModel::class.java)

        viewModel.allNotes.observe(viewLifecycleOwner, Observer {
            Log.d("TAGYOYO", "$it")
            noteRVAdapter.submitList(it)
        })

        // Run the animation after setting up the adapter
        runRecyclerViewAnimation(binding.recyclerView)
    }

    private fun setUpSwipeToDeleteItem() {
        val swipeToDelete = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val itemPosition = viewHolder.adapterPosition
                val currentList =  noteRVAdapter.currentList.toMutableList()
                val swipedItem = currentList[itemPosition]
                currentList.removeAt(itemPosition)

                viewModel.removeNote(swipedItem)

                noteRVAdapter.submitList(currentList)

                val snackbar = Snackbar.make(binding.root, "Note removed", Snackbar.LENGTH_LONG)
                snackbar.setAction("UNDO") {
                    val newCurrentList  = noteRVAdapter.currentList.toMutableList()
                    newCurrentList.add(itemPosition, swipedItem)

                    viewModel.addNote(swipedItem)
                    noteRVAdapter.submitList(newCurrentList)
                }
                snackbar.setActionTextColor(Color.YELLOW)
                snackbar.show()
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDelete)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }

    private fun runRecyclerViewAnimation(recyclerView: RecyclerView) {
        val context = recyclerView.context
        val controller = AnimationUtils.loadLayoutAnimation(context, R.anim.recyclerview_animation)
        recyclerView.layoutAnimation = controller
        recyclerView.scheduleLayoutAnimation()
    }

    fun onCardClicked(note: Note) {
        // Handle click event on a note
        val openNoteIntent = Intent(requireActivity(), OpenNote::class.java)
        openNoteIntent.putExtra("SelectedNote", note)
        startActivity(openNoteIntent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
