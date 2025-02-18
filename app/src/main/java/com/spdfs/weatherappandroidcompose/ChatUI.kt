package com.spdfs.weatherappandroidcompose

import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spdfs.weatherappandroidcompose.dao.Message
import java.text.SimpleDateFormat
import java.util.*

class ChatUI : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { ChatScreen() }
    }


    val automaticReplies = mapOf(
        "hi" to "Hello! How can I assist you today?",
        "location" to "Your location is Pune, India.",
        "temperature" to "The current temperature is 28°C.",
        "humidity" to "The current humidity is 65%.",
        "aqi" to "The AQI is 80, which is considered good."
    )

    fun getReply(userMessage: String): String {
        return automaticReplies[userMessage.toLowerCase()] ?: "Sorry, I didn't understand that. Try asking about location, temperature, humidity, or AQI."
    }

    @Preview
    @Composable
    fun ChatScreen() {

        var messages by remember { mutableStateOf(listOf<Message>()) }
        var messageText by remember { mutableStateOf("") }
        val keyboardController = LocalSoftwareKeyboardController.current
        val context = LocalContext.current
        val micIcon = ImageVector.vectorResource(id = R.drawable.baseline_mic_24)
        val micOffIcon = ImageVector.vectorResource(id = R.drawable.baseline_mic_none_24)

        var currentIcon by remember { mutableStateOf(micOffIcon) }

        fun handleSend(){
            if (messageText.isNotBlank()) {
                val userMessage = messageText.trim()

                messages = messages + Message(
                    text = messageText,
                    isSent = true,
                    timestamp = SimpleDateFormat(
                        "hh:mm a",
                        Locale.getDefault()
                    ).format(Date())
                )

                val autoReply = getReply(userMessage)
                messages = messages + Message(
                    text = autoReply,
                    isSent = false,
                    timestamp = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
                )

                messageText = ""
                keyboardController?.hide()
            }
        }

        fun startSpeechToText() {
            val speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now...")

            speechRecognizer.setRecognitionListener(object : RecognitionListener {
                override fun onResults(results: Bundle?) {
                    results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.firstOrNull()?.let {
                        messageText = it
                        currentIcon = micOffIcon
                    }
                }
                override fun onError(error: Int) {
                    currentIcon = micOffIcon
                    Toast.makeText(context, "Error: $error", Toast.LENGTH_SHORT).show() }
                override fun onReadyForSpeech(params: Bundle?) {
                    currentIcon = micIcon
                }
                override fun onBeginningOfSpeech() {}
                override fun onRmsChanged(rmsdB: Float) {}
                override fun onBufferReceived(buffer: ByteArray?) {}
                override fun onEndOfSpeech() {}
                override fun onPartialResults(partialResults: Bundle?) {}
                override fun onEvent(eventType: Int, params: Bundle?) {}
            })

            speechRecognizer.startListening(intent)
        }

        Column(modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F2F2))) {
            TopAppBar(
                title = { Text("Chat", color = Color.White) },
                backgroundColor = Color(0xFF3700B3),
                navigationIcon = {
                    IconButton(onClick = { /* Handle back press */ }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                }
            )

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                reverseLayout = true
            ) {
                items(messages.reversed()) { message ->
                    ChatBubble(message, onSwipeToDelete = {
                        messages = messages - message
                    })
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = messageText,
                    onValueChange = { messageText = it },
                    modifier = Modifier
                        .weight(1f)
                        .shadow(4.dp, RoundedCornerShape(24.dp)),
                    placeholder = { Text("Type a message...") },
                    shape = RoundedCornerShape(24.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Send,
                        keyboardType = KeyboardType.Text
                    ),
                    keyboardActions = KeyboardActions(
                        onSend = {
                            handleSend()
                        }
                    ),
                    colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White)
                )
                IconButton(onClick = { startSpeechToText() }) {
                    Icon(imageVector = currentIcon, contentDescription = "Voice Input", tint = Color(0xFF3700B3))
                }
                IconButton(
                    onClick = {
                        handleSend()
                    },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send",
                        tint = Color(0xFF3700B3)
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun ChatBubble(message: Message, onSwipeToDelete: () -> Unit) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .pointerInput(Unit) {
//                    detectHorizontalDragGestures(
//                        onHorizontalDrag = { change, dragAmount -> onSwipeToDelete() }
//                    )
                },
            horizontalArrangement = if (message.isSent) Arrangement.End else Arrangement.Start
        ) {
            Column(
                modifier = Modifier
                    .padding(vertical = 4.dp, horizontal = 8.dp)
                    .background(
                        if (message.isSent) Color(0xFF6200EE) else Color(0xFFE0E0E0),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(12.dp)
                    .widthIn(min = 50.dp, max = 250.dp)
            ) {
                Text(
                    text = message.text,
                    color = if (message.isSent) Color.White else Color.Black,
                    fontSize = 16.sp
                )
                Text(
                    text = message.timestamp,
                    color = Color.LightGray,
                    fontSize = 12.sp,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
