package com.spdfs.weatherappandroidcompose.model

data class Message(
    val text: String,
) {
    class Author {
        companion object {
            val bot: Any = 0
        }

    }

    val isFromMe: Boolean = false
}
