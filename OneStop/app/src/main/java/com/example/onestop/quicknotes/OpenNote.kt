package com.example.onestop.quicknotes

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.onestop.quicknotes.databinding.ActivityOpenNoteBinding

class OpenNote : AppCompatActivity() {

    private var binding: ActivityOpenNoteBinding? = null
    private var selectedNote: Note? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOpenNoteBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        // Retrieve the selected note from the intent extras
        selectedNote = intent.getSerializableExtra("SelectedNote") as? Note

        // Check if selectedNote is not null before accessing its properties
        selectedNote?.let { note ->
            binding?.llBg?.setBackgroundColor(note.color ?: Color.WHITE)
            binding?.tvTitleOpen?.text = note.title
            binding?.tvDesOpen?.text = note.des
        }

        // Set up back button functionality
        binding?.ivBackButton?.setOnClickListener {
            finish()
        }

        // Set up edit button functionality
        binding?.ivEditButton?.setOnClickListener {
            selectedNote?.let { note ->
                val editNoteIntent = Intent(this, EditNote::class.java)
                editNoteIntent.putExtra("EditSelectedNote", note)
                startActivity(editNoteIntent)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}
