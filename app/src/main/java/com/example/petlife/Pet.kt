package com.example.petlife

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Pet(
    val name: String,
    val birthDate: String,
    val type: String,
    val color: String,
    val size: String,
    val lastVetVisit: String,
    val lastVaccination: String,
    val lastPetShopVisit: String
) : Parcelable