package com.example.onestop.quicknotes

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.onestop.quicknotes.databinding.ActivityEditNoteBinding
import com.example.onestop.quicknotes.viewmodel.NoteViewModel

class EditNote : AppCompatActivity() {

    private lateinit var binding: ActivityEditNoteBinding
    private lateinit var viewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(NoteViewModel::class.java)

        val selectedNote: Note = intent.getSerializableExtra("EditSelectedNote") as Note

        // Set up back button functionality
        binding.ivBackButton.setOnClickListener {
            finish()
        }

        // Set up views with selected note data
        binding.etEditNoteTitle.setText(selectedNote.title ?: "")
        binding.etEditNoteDes.setText(selectedNote.des ?: "")
        binding.llEditBg.setBackgroundColor(selectedNote.color ?: Color.WHITE)

        // Save button click listener
        binding.btnSave.setOnClickListener {
            val editedTitle = binding.etEditNoteTitle.text.toString()
            val editedDes = binding.etEditNoteDes.text.toString()

            if (editedTitle.isNotBlank() || editedDes.isNotBlank()) {
                val editedNote = selectedNote.copy(title = editedTitle, des = editedDes)
                viewModel.updateNote(editedNote)
                Toast.makeText(this@EditNote, "Updated the note", Toast.LENGTH_SHORT).show()

                val mainIntent = Intent(this@EditNote, MainActivity::class.java)
                startActivity(mainIntent)
            } else {
                Toast.makeText(this@EditNote, "Please enter title or description", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
