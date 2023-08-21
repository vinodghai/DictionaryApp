package com.example.dictionaryapp.core.components

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import java.io.Serializable


data class DialogState(
    val title: String = "",
    val message: String = "",
    val isVisible: Boolean = false,
    val onDismiss: () -> Unit = {}
) : Serializable

@Composable
fun DictionaryAlertDialog(dialogState: DialogState) {
    AlertDialog(
        onDismissRequest = dialogState.onDismiss,
        title = { Text(text = dialogState.title) },
        text = { Text(text = dialogState.message) },
        confirmButton = {
            Button(onClick = dialogState.onDismiss) {
                Text(text = "Ok")
            }
        }
    )
}