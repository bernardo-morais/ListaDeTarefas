package com.example.tasklistapp
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.tasklistapp.ui.theme.TaskListAppTheme
import androidx.compose.ui.unit.sp

// Modelo de dados para uma tarefa
data class Task(
    val title: String,
    var isDone: Boolean = false,
    var details: String = ""
)

// Activity principal que cria a UI utilizando o Compose
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TaskListAppTheme { // Aplica o tema
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    TaskListApp() // Componente principal da app
                }
            }
        }
    }
}
@Composable
fun TaskListApp() {

    // Estado para o texto da nova tarefa
    var taskText by remember { mutableStateOf(TextFieldValue("")) }
    // Estado para a lista de tarefas
    var taskList by remember { mutableStateOf(listOf<Task>()) }
    // Estado para a tarefa que está a ser editada (se houver)
    var editingTask by remember { mutableStateOf<Task?>(null) }
    // Estado para o switch de apagar tarefas feitas automaticamente
    var autoDeleteDone by remember { mutableStateOf(false) }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Text(text = "📝 Minha To-Do List", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        // Linha com o switch para ativar/desativar apagar automático
        Row(verticalAlignment = Alignment.CenterVertically) {
            Switch(
                checked = autoDeleteDone,
                onCheckedChange = { autoDeleteDone = it }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Apagar tarefas automaticamente ao concluir")
        }

        // Linha para input de nova tarefa e botão adicionar
        Row(modifier = Modifier.fillMaxWidth()) {
            TextField(
                value = taskText,
                onValueChange = { taskText = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Escreva o titulo da tarefa") }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                if (taskText.text.isNotBlank()) { // Só adiciona se não estiver vazio
                    taskList = taskList + Task(taskText.text)
                    taskText = TextFieldValue("")
                }
            }) {
                Text("Adicionar")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Lista de tarefas com LazyColumn (scroll)
        LazyColumn {
            items(taskList) { task ->
                TaskItem(
                    task = task,
                    onToggle = {
                        // Alterna isDone e remove se autoDeleteDone está ativo
                        taskList = taskList.map {
                            if (it == task) it.copy(isDone = !it.isDone) else it
                        }.filter { !(autoDeleteDone && it.isDone) }
                    },
                    onRemove = {
                        taskList = taskList - task // Remove a tarefa
                    },
                    onEdit = {
                        editingTask = task // Define tarefa para editar detalhes
                    },
                    isEditing = (editingTask == task),// Se esta tarefa está a ser editada
                    onDetailsChange = { newDetails ->
                        task.details = newDetails // Atualiza os detalhes da tarefa
                        taskList = taskList.toList() // Força recomposição
                    },
                    onDoneEditing = {
                        editingTask = null // Sai do modo de edição
                    }
                )
            }
        }
    }
}
@Composable
fun TaskItem(
    task: Task,
    onToggle: () -> Unit, //função sem argumento e sem retorno
    onRemove: () -> Unit,
    onEdit: () -> Unit,
    isEditing: Boolean,
    onDetailsChange: (String) -> Unit,
    onDoneEditing: () -> Unit
) {
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)

    ){
        Row (
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ){
            // Checkbox para marcar a tarefa como concluída
            Checkbox(
                checked = task.isDone,
                onCheckedChange = { onToggle() }
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Título da tarefa, clicável para editar detalhes
            Text(
                text = task.title,
                color = if (task.isDone) Color.Gray else Color.Black,
                fontSize = 22.sp,
                modifier = Modifier
                    .weight(1f)
                    .clickable { onEdit() }
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Ícone de remover tarefa
            Text(
                text = "🗑",
                modifier = Modifier
                    .clickable { onRemove() }
            )
        }
        // Se estiver a editar, mostra campo para detalhes e botão fechar
        if (isEditing) {
            var tempDetails by remember { mutableStateOf(task.details) }

            TextField(
                value = tempDetails,
                onValueChange = {
                    tempDetails = it
                    onDetailsChange(it) // Atualiza detalhes no estado
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                placeholder = { Text("Adicione detalhes à tarefa...") }
            )

            Button(
                onClick = onDoneEditing,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .align(Alignment.End)
            ) {
                Text("Fechar")
            }
        } else if (task.details.isNotBlank()) {

            // Mostra detalhes da tarefa (se existirem) quando não está em edição
            Text(
                text = "📝 ${task.details}",
                color = Color.DarkGray,
                fontSize = 19.sp,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 48.dp, top = 4.dp)
            )
        }
    }
}
