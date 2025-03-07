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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spdfs.weatherappandroidcompose.dao.Message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class ChatUI : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { ChatScreen() }
    }

    suspend fun fetchLocationData(): String {
        return withContext(Dispatchers.IO) {
            val client = OkHttpClient()
            val url = "https://nominatim.openstreetmap.org/reverse?format=json&lat=${LocationInfo.latitude}&lon=${LocationInfo.longitude}"
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            val responseData = response.body?.string() ?: return@withContext "Unknown location"

            val json = JSONObject(responseData)
            return@withContext json.optString("display_name", "Unknown location")
        }
    }

    suspend fun fetchWeatherData(type: String): String {
        return withContext(Dispatchers.IO) {

            val client = OkHttpClient()
            val url =
                "https://api.open-meteo.com/v1/forecast?latitude=${LocationInfo.latitude}&longitude=${LocationInfo.longitude}&current=temperature_2m,relative_humidity_2m"
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            val responseData = response.body?.string() ?: return@withContext "N/A"

            val json = JSONObject(responseData)
            val currentWeather = json.getJSONObject("current")

            android.util.Log.d("CompleteAPI", responseData)
            return@withContext when (type) {
                "temperature" -> currentWeather.getDouble("temperature_2m").toString()
                "humidity" -> currentWeather.getDouble("relative_humidity_2m").toString()
                else -> "N/A"
            }
        }
    }

    suspend fun fetchAQIData(): String {
        return withContext(Dispatchers.IO) {
            val client = OkHttpClient()
            val url = "https://api.waqi.info/feed/geo:${LocationInfo.latitude};${LocationInfo.longitude}/?token=demo"
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            val responseData = response.body?.string() ?: return@withContext "N/A"

            val json = JSONObject(responseData)
            return@withContext json.getJSONObject("data").getInt("aqi").toString()
        }
    }


    suspend fun getReply(userMessage: String): String {
        return withContext(Dispatchers.IO) {

            return@withContext when (userMessage.lowercase()) {
                "temperature" -> fetchWeatherData("temperature")
                "hi" -> {
                    "Hello! How can I assist you today?"
                }

                "location" -> {
                    fetchLocationData()
                }

                "humidity" -> fetchWeatherData("humidity")
                "aqi" -> fetchAQIData()
                else -> "Sorry, I didn't understand that. Try asking about location, temperature, humidity, or AQI."
            }
        }
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
        val coroutineScope = rememberCoroutineScope()


        var currentIcon by remember { mutableStateOf(micOffIcon) }

        suspend fun handleSend(){
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
                title = { Text("Chat", color = Color.Black) },
                backgroundColor = Color.White,
                navigationIcon = {
                    IconButton(onClick = { onBackPressedDispatcher.onBackPressed() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black
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
                            coroutineScope.launch {
                                handleSend()
                            }
                        }
                    ),
                    colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White)
                )
                IconButton(onClick = { startSpeechToText() }) {
                    Icon(imageVector = currentIcon, contentDescription = "Voice Input", tint = colorResource(id = R.color.dark_blue))
                }
                IconButton(
                    onClick = {
                        coroutineScope.launch {
                            handleSend()
                        }
                    },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send",
                        tint = colorResource(id = R.color.dark_blue)
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
                        if (message.isSent) colorResource(id = R.color.dark_blue) else Color(0xFFE0E0E0),
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
