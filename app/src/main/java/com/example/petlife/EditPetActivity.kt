package com.example.petlife

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class EditPetActivity : ComponentActivity() {

    private lateinit var database: PetLifeDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        database = PetLifeDatabase(this)

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

        val petTypes = listOf("Cão", "Gato")
        val petSizes = listOf("Pequeno", "Médio", "Grande")

        Column(modifier = Modifier.padding(16.dp)) {
            Text("Editar informações do Pet", style = MaterialTheme.typography.titleLarge)

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = updatedName,
                onValueChange = { updatedName = it },
                label = { Text("Nome") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = updatedBirthDate,
                onValueChange = { updatedBirthDate = it },
                label = { Text("Data de Nascimento") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            DropdownMenuField(
                label = "Tipo",
                options = petTypes,
                selectedOption = updatedType,
                onOptionSelected = { updatedType = it }
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = updatedColor,
                onValueChange = { updatedColor = it },
                label = { Text("Cor") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            DropdownMenuField(
                label = "Porte",
                options = petSizes,
                selectedOption = updatedSize,
                onOptionSelected = { updatedSize = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val updatedPet = pet.copy(
                        name = updatedName,
                        birthDate = updatedBirthDate,
                        type = updatedType,
                        color = updatedColor,
                        size = updatedSize
                    )

                    val isUpdated = database.updatePet(updatedPet)

                    if (isUpdated) {
                        val resultIntent = Intent()
                        resultIntent.putExtra("updatedPet", updatedPet)
                        setResult(Activity.RESULT_OK, resultIntent)
                        finish()
                    } else {
                        Toast.makeText(this@EditPetActivity, "Erro ao salvar alterações", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Salvar")
            }
        }
    }

    @Composable
    fun DropdownMenuField(
        label: String,
        options: List<String>,
        selectedOption: String,
        onOptionSelected: (String) -> Unit
    ) {
        var expanded by remember { mutableStateOf(false) }

        Column {
            OutlinedTextField(
                value = selectedOption,
                onValueChange = {},
                label = { Text(label) },
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = null
                        )
                    }
                }
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onOptionSelected(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
