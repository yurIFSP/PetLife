package com.example.petlife

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {

    private var pet = Pet(
        name = "Buddy",
        birthDate = "01/01/2020",
        type = "Cão",
        color = "Marrom",
        size = "Médio",
        lastVetVisit = "02/08/2024",
        lastVaccination = "15/07/2024",
        lastPetShopVisit = "20/09/2024"
    )

    private val editPetLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val updatedPet = result.data?.getParcelableExtra<Pet>("updatedPet")
            if (updatedPet != null) {
                pet = updatedPet
                setContent { PetDashboard(pet) }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PetDashboard(pet)
        }
    }

    @Composable
    fun PetDashboard(pet: Pet) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Nome: ${pet.name}", style = MaterialTheme.typography.titleLarge)
            Text(text = "Data de Nascimento: ${pet.birthDate}")
            Text(text = "Tipo: ${pet.type}")
            Text(text = "Cor: ${pet.color}")
            Text(text = "Porte: ${pet.size}")
            Text(text = "Última ida ao veterinário: ${pet.lastVetVisit}")
            Text(text = "Última vacinação: ${pet.lastVaccination}")
            Text(text = "Última ida ao petshop: ${pet.lastPetShopVisit}")

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { editPetData(pet) }) {
                Text("Editar Dados")
            }
        }
    }

    private fun editPetData(pet: Pet) {
        val intent = Intent(this, EditPetActivity::class.java)
        intent.putExtra("pet", pet)
        editPetLauncher.launch(intent)
    }
}
