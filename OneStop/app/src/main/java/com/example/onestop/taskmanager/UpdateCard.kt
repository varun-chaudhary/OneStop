package com.example.onestop.taskmanager



import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.onestop.LandingPage
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import com.example.onestop.databinding.ActivityUpdateCardBinding


class UpdateCard : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateCardBinding
    private lateinit var database: myDatabase

    override  fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateCardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = myDatabase.getInstance(applicationContext)


        val pos = intent.getIntExtra("id", -1)
        if (pos != -1) {
            var en:Task=Task()

            GlobalScope.launch {
                 en=database.dao().getTask()
            }
            binding.createTitle.setText(en.title)
            binding.createPriority.setText(en.priority)

            binding.doneupdate.setOnClickListener {
            val t=Task()
                t.id=en.id
                t.title= binding.createTitle.text.toString()
                t.priority= binding.createPriority.text.toString()
                t.done=en.done
                GlobalScope.launch {
                    database.dao().updateTask(t)
                }
                myIntent()
            }

            binding.updateButton.setOnClickListener {
                val t=Task()
                t.id=en.id
                t.title= binding.createTitle.text.toString()
                t.priority= binding.createPriority.text.toString()
                t.done=en.done
                GlobalScope.launch {
                    database.dao().updateTask(t)
                }
                myIntent()
            }
        }
    }
    fun myIntent() {
        val intent = Intent(this, LandingPage::class.java)
        intent.putExtra("id","tm")
        startActivity(intent)
    }
}
