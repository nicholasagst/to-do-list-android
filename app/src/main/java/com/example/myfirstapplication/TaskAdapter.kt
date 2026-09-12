package com.example.myfirstapplication

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstapplication.databinding.ItemTaskBinding

class TaskAdapter(
    private val tasks: MutableList<Task>,
    private val onTaskCheckedChange: (Task) -> Unit,
    private val onTaskDeleteClick: (Task) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {


    // 1. O ViewHolder guarda as referências de cada item da lista
    inner class TaskViewHolder(val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root)

    // 2. Cria a visualização para cada item (carrega o layout item_task.xml)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemTaskBinding.inflate(layoutInflater, parent, false)
        return TaskViewHolder(binding)
    }

    // 3. Conecta os dados de uma tarefa específica aos elementos visuais da linha
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]

        with(holder.binding) {
            // 1. DESLIGA o ouvinte antes de mexer na tela para evitar cliques fantasmas da rolagem
            cbTaskCompleted.setOnCheckedChangeListener(null)

            // Define o texto e se o CheckBox está marcado
            cbTaskCompleted.text = task.title
            cbTaskCompleted.isChecked = task.isCompleted

            // Dica de UI: Riscado no texto quando a tarefa estiver concluída
            if (task.isCompleted) {
                cbTaskCompleted.paintFlags =
                    cbTaskCompleted.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                cbTaskCompleted.paintFlags =
                    cbTaskCompleted.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }

            // 2. LIGA o ouvinte de volta agora que tudo já foi configurado
            cbTaskCompleted.setOnCheckedChangeListener { _, isChecked ->
                task.isCompleted = isChecked
                onTaskCheckedChange(task)

                // 3. O PULO DO GATO: Espera o CheckBox terminar de animar para notificar o adapter
                cbTaskCompleted.post {
                    // Dica extra: bindingAdapterPosition é mais seguro que 'position' para animações!
                    notifyItemChanged(holder.bindingAdapterPosition)
                }
            }

            // Clique no botão de deletar
            btnDeleteTask.setOnClickListener {
                onTaskDeleteClick(task)
            }
        }
    }
    // 4. Diz ao RecyclerView quantas tarefas temos na lista
    override fun getItemCount(): Int = tasks.size
}