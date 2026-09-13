package com.example.myfirstapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myfirstapplication.databinding.ActivityMainBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : AppCompatActivity() {

    // 1. ViewBinding para acessar os componentes da tela
    private lateinit var binding: ActivityMainBinding

    // 2. Lista de tarefas em memória
    private val taskList = mutableListOf<Task>()

    // 3. O nosso adaptador
    private lateinit var taskAdapter: TaskAdapter

    // 4. Instância do Gson para conversão de Objetos <-> JSON
    private val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inicializa o ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ajuste para não cobrir com a barra de status/navegação do sistema
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Carrega as tarefas salvas no dispositivo antes de exibir a tela
        loadTasksFromPreferences()

        // Configura a lista e os botões
        setupRecyclerView()
        setupListeners()
    }

    private fun setupRecyclerView() {
        // Inicializa o Adapter passando as lambdas para ações de marcar/desmarcar e excluir
        taskAdapter = TaskAdapter(
            tasks = taskList,
            onTaskCheckedChange = { task ->
                if (task.isCompleted){
                    Toast.makeText(this, "Concluído!", Toast.LENGTH_SHORT).show()
                }
                // Salva a lista sempre que o status de uma tarefa mudar
                saveTasksToPreferences()
            },
            onTaskDeleteClick = { task ->
                deleteTask(task)
            }
        )

        // O LinearLayoutManager organiza os itens em formato de lista de cima para baixo
        binding.rvTasks.layoutManager = LinearLayoutManager(this)
        binding.rvTasks.adapter = taskAdapter
    }

    private fun setupListeners() {
        // Configura o clique no botão "Adicionar"
        binding.btnAddTask.setOnClickListener {
            //1. Cria a intenção, apontando para a nova tela.
            val intent = Intent(this, AddTaskActivity::class.java)
            //2. Inicia a nova tela.
            startActivity(intent)

           // val title = binding.etTaskTitle.text.toString().trim()

            /*if (title.isNotEmpty()) {
                addNewTask(title)
            } else {
                Toast.makeText(this, "Digite o nome da tarefa!", Toast.LENGTH_SHORT).show()
            }*/
        }
    }

    private fun addNewTask(title: String) {
        val newTask = Task(title = title)
        taskList.add(newTask)

        // Salva a lista atualizada no armazenamento interno do celular
        saveTasksToPreferences()

        // Avisa o Adapter que um item foi adicionado no final da lista
        taskAdapter.notifyItemInserted(taskList.size - 1)

        // Limpa o campo de texto
        binding.etTaskTitle.text?.clear()

        // Rola a lista automaticamente para o novo item
        binding.rvTasks.smoothScrollToPosition(taskList.size - 1)
    }

    private fun deleteTask(task: Task) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Excluir Tarefa")
        builder.setMessage("Tem certeza que deseja excluir esta tarefa?")
        builder.setPositiveButton("Excluir"){ dialog, _ ->
            val index = taskList.indexOf(task)
            if (index != -1) {
                taskList.removeAt(index)

                // Salva a lista atualizada após remover a tarefa
                saveTasksToPreferences()

                // Avisa o Adapter que o item na posição 'index' foi removido
                taskAdapter.notifyItemRemoved(index)
                taskAdapter.notifyItemRangeChanged(index, taskList.size)
                Toast.makeText(this, "Tarefa excluída!", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("Cancelar"){ dialog, _ ->
            dialog.dismiss()
        }
        builder.show()

    }

    // --- MÉTODOS DE PERSISTÊNCIA DE DADOS ---

    private fun saveTasksToPreferences() {
        // Obtém o arquivo de preferências "task_prefs"
        val sharedPreferences = getSharedPreferences("task_prefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Converte a lista de Tasks em um texto formatado em JSON
        val jsonString = gson.toJson(taskList)

        // Grava a String JSON na chave "key_tasks"
        editor.putString("key_tasks", jsonString)
        editor.apply() // Salva as alterações de forma assíncrona
    }

    private fun loadTasksFromPreferences() {
        val sharedPreferences = getSharedPreferences("task_prefs", MODE_PRIVATE)

        // Lê a String JSON armazenada (retorna null se for a primeira vez abrindo o app)
        val jsonString = sharedPreferences.getString("key_tasks", null)

        if (jsonString != null) {
            // O TypeToken diz ao Gson que a String representa uma List<Task>
            val type = object : TypeToken<List<Task>>() {}.type
            val savedTasks: List<Task> = gson.fromJson(jsonString, type)

            // Limpa a lista atual e adiciona todas as tarefas restauradas
            taskList.clear()
            taskList.addAll(savedTasks)
        }
    }
}
