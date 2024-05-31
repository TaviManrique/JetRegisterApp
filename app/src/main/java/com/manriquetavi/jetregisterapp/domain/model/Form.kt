package com.manriquetavi.jetregisterapp.domain.model

import androidx.room.PrimaryKey
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp

data class Form(
    @PrimaryKey(autoGenerate = false)
    var id: String = "",
    @ServerTimestamp
    var date: Timestamp? = null,
    val service: String,
    val address: String,
    val problem: String,
    val description: String,
)
