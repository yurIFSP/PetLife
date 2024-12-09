package com.example.petlife

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {

    private val petList = mutableStateListOf(
        Pet(
            name = "Buddy",
            birthDate = "01/01/2020",
            type = "Cão",
            color = "Marrom",
            size = "Médio",
            lastVetVisit = "02/08/2024",
            lastVaccination = "15/07/2024",
            lastPetShopVisit = "20/09/2024",
            clinicPhone = "16998865982",
            clinicWebsite = "https://www.petlove.com.br/"
        ),
        Pet(
            name = "Mimi",
            birthDate = "15/05/2021",
            type = "Gato",
            color = "Cinza",
            size = "Pequeno",
            lastVetVisit = "10/06/2024",
            lastVaccination = "25/05/2024",
            lastPetShopVisit = "15/07/2024",
            clinicPhone = "15988775432",
            clinicWebsite = "https://www.petlove.com.br/"
        )
    )

    private val editPetLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val updatedPet = result.data?.getParcelableExtra<Pet>("updatedPet")
            val petIndex = petList.indexOfFirst { it.name == updatedPet?.name }
            if (updatedPet != null && petIndex != -1) {
                petList[petIndex] = updatedPet
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PetDashboard()
        }
    }

    private fun dialClinic(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }
        startActivity(intent)
    }

    private fun openClinicWebsite(websiteUrl: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(websiteUrl)
        }
        startActivity(intent)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun PetDashboard() {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("PetLife") }
                )
            },
            content = { padding ->
                Column(modifier = Modifier.padding(padding)) {
                    Text(
                        text = "Lista de Pets",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(16.dp)
                    )
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(petList.size) { index ->
                            val pet = petList[index]
                            PetItem(
                                pet = pet,
                                onEdit = { editPetData(pet) },
                                onDelete = { removePet(pet) }
                            )
                        }
                    }
                }
            }
        )
    }

    @Composable
    fun PetItem(pet: Pet, onEdit: () -> Unit, onDelete: () -> Unit, onSelect: () -> Unit) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clickable { onSelect() },
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Nome: ${pet.name}", style = MaterialTheme.typography.bodyLarge)
                Text(text = "Tipo: ${pet.type}", style = MaterialTheme.typography.bodyMedium)

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Button(onClick = onEdit) {
                        Text("Editar")
                    }
                    Button(onClick = onDelete) {
                        Text("Remover")
                    }
                }
            }
        }
    }

    private fun editPetData(pet: Pet) {
        val intent = Intent(this, EditPetActivity::class.java)
        intent.putExtra("pet", pet)
        editPetLauncher.launch(intent)
    }

    private fun removePet(pet: Pet) {
        petList.remove(pet)
    }


}
