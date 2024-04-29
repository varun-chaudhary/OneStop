package com.example.onestop.taskmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.onestop.LandingPage
import com.example.onestop.databinding.ActivityCreateCardBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CreateCard : AppCompatActivity() {
    private lateinit var binding: ActivityCreateCardBinding
    private lateinit var database: myDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateCardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = myDatabase.getInstance(applicationContext)


        binding.saveButton.setOnClickListener {
            val title = binding.createTitle.text.toString().trim()
            val priority = binding.createPriority.text.toString().trim()

            if (title.isNotEmpty() && priority.isNotEmpty()) {

                GlobalScope.launch {
                    val t=Task()
                    t.id=0
                    t.title=title
                    t.priority=priority
                    t.done=false
                    database.dao().insertTask(t)
                }
                val intent = Intent(this, LandingPage::class.java)
                intent.putExtra("id","tm")
                startActivity(intent)
            }
        }
    }
}
