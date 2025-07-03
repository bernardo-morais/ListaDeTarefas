package com.example.tasklistapp
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.tasklistapp.ui.theme.TaskListAppTheme

data class Task(
    val title: String,
    var isDone: Boolean = false,
    var details: String = ""
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TaskListAppTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {

                }
            }
        }
    }
}
@Composable
fun TaskListApp() {

    var taskText by remember { mutableStateOf(TextFieldValue("")) }
    var taskList by remember { mutableStateOf(listOf<Task>()) }
    var editingTask by remember { mutableStateOf<Task?>(null) }
    var autoDeleteDone by remember { mutableStateOf(false) }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Text(text = "📝 Minha To-Do List", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Switch(
                checked = autoDeleteDone,
                onCheckedChange = { autoDeleteDone = it }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Apagar tarefas automaticamente ao concluir")
        }

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
                    taskList = taskList + Task(taskText.text) // Adiciona tarefa nova
                    taskText = TextFieldValue("") // Limpa o input
                }
            }) {
                Text("Adicionar")
            }
        }
    }
}