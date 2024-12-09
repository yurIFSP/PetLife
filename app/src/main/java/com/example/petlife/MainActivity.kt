package com.example.petlife

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {

    private lateinit var database: PetLifeDatabase
    private var petList = mutableStateListOf<Pet>()

    private val editPetLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val updatedPet = result.data?.getParcelableExtra<Pet>("updatedPet")
            if (updatedPet != null) {
                database.updatePet(updatedPet)
                loadPets()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = PetLifeDatabase(this)

        loadPets()

        setContent {
            PetDashboard()
        }
    }

    private fun loadPets() {
        petList.clear()
        petList.addAll(database.getAllPets())
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
                if (petList.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding),
                        contentAlignment = androidx.compose.ui.Alignment.Center
                    ) {
                        Text("Nenhum pet cadastrado. Clique em adicionar!")
                    }
                } else {
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
            },
            floatingActionButton = {
                FloatingActionButton(onClick = { addNewPet() }) {
                    Text("+")
                }
            }
        )
    }

    @Composable
    fun PetItem(pet: Pet, onEdit: () -> Unit, onDelete: () -> Unit) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clickable {  },
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Nome: ${pet.name}", style = MaterialTheme.typography.bodyLarge)
                Text(text = "Tipo: ${pet.type}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Porte: ${pet.size}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Cor: ${pet.color}", style = MaterialTheme.typography.bodyMedium)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
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
        database.deletePet(pet.id)
        loadPets()
    }

    private fun addNewPet() {
        val newPet = Pet(
            id = 0,
            name = "Novo Pet",
            birthDate = "01/01/2023",
            type = "cão",
            color = "Preto",
            size = "pequeno"
        )
        database.insertPet(newPet)
        loadPets()
    }
}
