package com.example.poyh

import java.io.Serializable

data class WordResult(
    val word: String,
    val isGuessed: Boolean
) : Serializable
