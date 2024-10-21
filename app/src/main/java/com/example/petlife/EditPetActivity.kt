package com.example.petlife

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import android.util.Log

class EditPetActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pet: Pet? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("pet", Pet::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("pet")
        }

        if (pet != null) {
            setContent {
                EditPetScreen(pet)
            }
        } else {
            Log.e("EditPetActivity", "Erro: Pet é nulo.")
            setContent {
                Text("Erro: Não foi possível carregar as informações do pet.")
            }
        }
    }

    @Composable
    fun EditPetScreen(pet: Pet) {
        var updatedName by remember { mutableStateOf(pet.name) }
        var updatedBirthDate by remember { mutableStateOf(pet.birthDate) }
        var updatedType by remember { mutableStateOf(pet.type) }
        var updatedColor by remember { mutableStateOf(pet.color) }
        var updatedSize by remember { mutableStateOf(pet.size) }
        var updatedLastVetVisit by remember { mutableStateOf(pet.lastVetVisit) }
        var updatedLastVaccination by remember { mutableStateOf(pet.lastVaccination) }
        var updatedLastPetShopVisit by remember { mutableStateOf(pet.lastPetShopVisit) }

        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Editar informações do Pet", style = MaterialTheme.typography.titleLarge)

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = updatedName,
                onValueChange = { updatedName = it },
                label = { Text("Nome") }
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = updatedBirthDate,
                onValueChange = { updatedBirthDate = it },
                label = { Text("Data de Nascimento") }
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = updatedType,
                onValueChange = { updatedType = it },
                label = { Text("Tipo") }
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = updatedColor,
                onValueChange = { updatedColor = it },
                label = { Text("Cor") }
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = updatedSize,
                onValueChange = { updatedSize = it },
                label = { Text("Porte") }
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = updatedLastVetVisit,
                onValueChange = { updatedLastVetVisit = it },
                label = { Text("Última ida ao veterinário") }
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = updatedLastVaccination,
                onValueChange = { updatedLastVaccination = it },
                label = { Text("Última vacinação") }
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = updatedLastPetShopVisit,
                onValueChange = { updatedLastPetShopVisit = it },
                label = { Text("Última ida ao petshop") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                val updatedPet = pet.copy(
                    name = updatedName,
                    birthDate = updatedBirthDate,
                    type = updatedType,
                    color = updatedColor,
                    size = updatedSize,
                    lastVetVisit = updatedLastVetVisit,
                    lastVaccination = updatedLastVaccination,
                    lastPetShopVisit = updatedLastPetShopVisit
                )

                val resultIntent = Intent()
                resultIntent.putExtra("updatedPet", updatedPet)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }) {
                Text("Salvar")
            }
        }
    }
}
