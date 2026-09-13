package com.example.myfirstapplication

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myfirstapplication.databinding.ActivityAddTaskBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AddTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    }

    override fun onPause() {
        super.onPause()
        var title = binding.etTaskTitle.text.toString().trim()

        val description = binding.etTaskDescription.text.toString().trim()
        if (title.isEmpty() || description.isEmpty()) {
            if (title.isEmpty()) {
                title = description.lines().first().take(50)
            }

        }
        val newTask = Task(title = title, description = description)

        saveTaskToDatabase(newTask)

    }


    private fun saveTaskToDatabase(task: Task) {
        //Abre acesso ao armazenamento interno do celular
        val sharedPreferences = getSharedPreferences("task_prefs", MODE_PRIVATE)

        // Lê a String JSON armazenada (retorna uma lista vazia se for a primeira vez abrindo o app)
        val jsonString = sharedPreferences.getString("key_tasks", "[]")

        // O TypeToken diz ao Gson que a String representa uma MutableList<Task>
        val type = object : TypeToken<MutableList<Task>>() {}.type

        val tasklist = Gson().fromJson<MutableList<Task>>(jsonString, type)
        tasklist.add(0,task)
        val newJsonString = Gson().toJson(tasklist)
        sharedPreferences.edit().putString("key_tasks", newJsonString).apply()
    }

}
