package com.spdfs.weatherappandroidcompose.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.spdfs.weatherappandroidcompose.model.ChatModel
import com.spdfs.weatherappandroidcompose.model.Message
import com.spdfs.weatherappandroidcompose.ui.theme.PurpleGrey80

@Preview
@Composable
fun Chatbot() {
    val message: Message = Message("Hiii..........")
    Box(
        modifier = Modifier
            //.align(if (message.isFromMe) Alignment.End else Alignment.Start)
            .clip(
                RoundedCornerShape(
                    topStart = 48f,
                    topEnd = 48f,
                    bottomStart = if (message.isFromMe) 48f else 0f,
                    bottomEnd = if (message.isFromMe) 0f else 48f
                )
            )
            .background(PurpleGrey80)
            .padding(16.dp)
    ) {
        Text(text = message.text)
    }
}

@Preview
@Composable
fun demoScreen() {
    val itemList = mutableListOf<ChatModel>()

    itemList.add(1, ChatModel("Hi sonali", addressee = 23))
    itemList.add(2, ChatModel("Hi", addressee = 23))

    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        items(
            items = itemList
        ) {
            Text(it.messages)
        }
    }
}

@Preview
@Composable
fun ChatScreen2() {
    Text(
        text = "Holla"
    )
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (messages, chatBox) = createRefs()
        val itemList = mutableListOf<ChatModel>()

        itemList.add(1, ChatModel("Hi sonali", addressee = 23))
        itemList.add(2, ChatModel("Hi Naitik", addressee = 23))

        Text(
            text = "Holla"
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(messages) {
                    top.linkTo(parent.top)
                    bottom.linkTo(chatBox.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    height = Dimension.fillToConstraints
                }
        ) {
            items(itemList.size) { item ->
                //ChatItem(item)
            }
        }

        ChatBox(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(chatBox) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )
    }
}

@Composable
fun ChatBox(modifier: Modifier) {
    var chatBoxValue by remember { mutableStateOf(TextFieldValue("")) }
    Row {
        TextField(
            value = chatBoxValue,
            onValueChange = { newText ->
                chatBoxValue = newText
            },
            placeholder = {
                Text(text = "Type something")
            }
        )
        IconButton(
            onClick = {

            }
        ) {

        }
    }
}


@Composable
fun ChatItem(item: String) {
    //Text()
}
