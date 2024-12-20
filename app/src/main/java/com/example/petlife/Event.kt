package com.example.petlife

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Event(
    val id: Int = 0,
    val petId: Int,
    val eventCategory: String,
    val eventType: String,
    val eventDate: String,
    val time: String
) : Parcelable


