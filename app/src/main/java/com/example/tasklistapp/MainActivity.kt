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
                    TaskListApp()
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
                if (taskText.text.isNotBlank()) {
                    taskList = taskList + Task(taskText.text)
                    taskText = TextFieldValue("")
                }
            }) {
                Text("Adicionar")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(taskList) { task ->
                TaskItem(
                    task = task,
                    onToggle = {
                        taskList = taskList.map {
                            if (it == task) it.copy(isDone = !it.isDone) else it
                        }.filter { !(autoDeleteDone && it.isDone) }
                    },
                    onRemove = {
                        taskList = taskList - task
                    },
                    onEdit = {
                        editingTask = task
                    },
                    isEditing = (editingTask == task),
                    onDetailsChange = { newDetails ->
                        task.details = newDetails
                        taskList = taskList.toList()
                    },
                    onDoneEditing = {
                        editingTask = null
                    }
                )
            }
        }
    }
}
@Composable
fun TaskItem(
    task: Task,
    onToggle: () -> Unit,
    onRemove: () -> Unit,
    onEdit: () -> Unit,
    isEditing: Boolean,
    onDetailsChange: (String) -> Unit,
    onDoneEditing: () -> Unit
) {

}
