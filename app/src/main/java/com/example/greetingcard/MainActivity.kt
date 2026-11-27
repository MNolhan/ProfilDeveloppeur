package com.example.greetingcard

import android.os.Bundle
import androidx.compose.material3.Button
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.greetingcard.ui.theme.GreetingCardTheme
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.Card
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalUriHandler
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.text.contains

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GreetingCardTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ProfileContainer(
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }
        }
    }
}

// --- Data Class & Sample

data class Developer(
    val name: String,
    val role: String,
    val description: String,
    val skills: List<String>,
    val email: String,
    val location: String,
    val isAvailable: Boolean
)

data class Project(
    val id: Int,
    val titre: String,
    val description: String,
    val imageRes: Int
)

data class ProfileUiState(
    val projects: List<Project> = emptyList(),
    val searchQuery: String = "",
    val favoriteProjectIds: Set<Int> = emptySet(),
    val showOnlyFavorites: Boolean = false
)

fun sampleDeveloper(): Developer = Developer(
    name = "Nolhan Marteau",
    role = "Étudiant à l'ESGI",
    description = "Développeur full-stack passionné, je passe du front au back sans problème : React et Compose pour les interfaces, Node.js, Java et Laravel pour le back. J'aime construire un projet de la maquette jusqu'à la mise en production.",
    skills = listOf("JavaScript", "React", "SQL", "Node", "Java", "PHP", "Laravel"),
    email = "nolhan.marteau@gmail.com",
    location = "Tours",
    isAvailable = true
)

fun sampleProjects(): List<Project> = listOf(
    Project(1, "Portfolio", "Site perso en React / Node.js, déployé avec Docker", android.R.drawable.ic_menu_gallery),
    Project(2, "Product Explorer", "Application Android en Jetpack Compose", android.R.drawable.ic_menu_gallery),
    Project(3, "Site e-commerce", "Projet ESGI en PHP / Laravel", android.R.drawable.ic_menu_gallery)
)

// --- ViewModel ---

class ProfileViewModel : ViewModel() {
    private val allProjects: List<Project> = sampleProjects()

    private val _uiState = MutableStateFlow(
        ProfileUiState(
            projects = allProjects
        )
    )

    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private fun filterProjects(
        searchQuery: String,
        showOnlyFavorites: Boolean,
        favoriteProjectIds: Set<Int>
    ): List<Project> {
        return allProjects.filter { project ->
            val matchesSearch =
                project.titre.contains(searchQuery, ignoreCase = true) ||
                        project.description.contains(searchQuery, ignoreCase = true)
            val matchesFavorite =
                !showOnlyFavorites || project.id in favoriteProjectIds
            matchesSearch && matchesFavorite
        }
    }

    fun onSearchQueryChange(newValue: String) {
        _uiState.update { currentState ->
            currentState.copy(
                searchQuery = newValue,
                projects = filterProjects(
                    searchQuery = newValue,
                    showOnlyFavorites = currentState.showOnlyFavorites,
                    favoriteProjectIds = currentState.favoriteProjectIds
                )
            )
        }
    }

    fun onToggleFavoritesFilter() {
        _uiState.update { currentState ->
            val newValue = !currentState.showOnlyFavorites
            currentState.copy(
                showOnlyFavorites = newValue,
                projects = filterProjects(
                    searchQuery = currentState.searchQuery,
                    showOnlyFavorites = newValue,
                    favoriteProjectIds = currentState.favoriteProjectIds
                )
            )
        }
    }

    fun onToggleFavorite(projectId: Int) {
        _uiState.update { currentState ->
            val newFavorites =
                if (projectId in currentState.favoriteProjectIds) {
                    currentState.favoriteProjectIds - projectId
                } else {
                    currentState.favoriteProjectIds + projectId
                }
            currentState.copy(
                favoriteProjectIds = newFavorites,
                projects = filterProjects(
                    searchQuery = currentState.searchQuery,
                    showOnlyFavorites = currentState.showOnlyFavorites,
                    favoriteProjectIds = newFavorites
                )
            )
        }
    }
}

// --- Screen ---

@Composable
fun ProfileScreen(
    developer: Developer,
    projects: List<Project>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    favoriteProjectIds: Set<Int>,
    onFavoriteClick: (Int) -> Unit,
    showOnlyFavorites: Boolean,
    onToggleFavoritesFilter: () -> Unit,
    onProjectClick: (Project) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.spacedBy(30.dp)
    ) {
        item {
            ProfileHeader(name = developer.name, role = developer.role)
        }

        item {
            Info(description = developer.description)
        }

        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Skills", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(developer.skills) { skill ->
                            SkillItem(skill = skill)
                        }
                    }
                }
            }
        }

        item {
            Column (verticalArrangement = Arrangement.spacedBy(8.dp)){
                Text(
                    text = "Projets",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    label = { Text(text = "Rechercher un projet") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Button(onClick = onToggleFavoritesFilter) {
                    Text(if (showOnlyFavorites) "Afficher tous les projets" else "★ Favoris uniquement")
                }
                val count = projects.size
                Text(
                    text = if (count > 1) "$count projets affichés" else "$count projet affiché",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(
                        items = projects,
                        key = { project -> project.id }
                    ) { project ->
                        ProjetItem(
                            project = project,
                            isFavorite = project.id in favoriteProjectIds,
                            onFavoriteClick = { onFavoriteClick(project.id) },
                            onClick = { onProjectClick(project) },
                            modifier = Modifier.width(325.dp)
                        )
                    }
                }
            }
        }

        item {
            ContactSection(
                name = developer.name,
                email = developer.email,
                location = developer.location,
                isAvailable = developer.isAvailable,
                onContactClick = {},
                onAvailabilityClick = {}
            )
        }
    }
}

// --- Container ---

@Composable
fun ProfileContainer(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    ProfileScreen(
        developer = sampleDeveloper(),
        projects = uiState.projects,
        searchQuery = uiState.searchQuery,
        onSearchQueryChange = viewModel::onSearchQueryChange,
        favoriteProjectIds = uiState.favoriteProjectIds,
        onFavoriteClick = viewModel::onToggleFavorite,
        showOnlyFavorites = uiState.showOnlyFavorites,
        onToggleFavoritesFilter = viewModel::onToggleFavoritesFilter,
        onProjectClick = {},
        modifier = modifier
    )
}

// --- Composant ---

@Composable
fun ProfileHeader(
    name: String,
    role: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.primary,
        contentColor = Color.White,
    ) {
        Column(
            modifier =
                Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(contentAlignment = Alignment.Center) {
                Image(
                    painter = painterResource(id = R.drawable.nolhan_pdp),
                    contentDescription = "Image de $name",
                    modifier = Modifier.size(150.dp)
                )
                Text(
                    text = name,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.BottomEnd)
                )
            }
            Text(
                text = role,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
fun Info(
    description: String,
    modifier: Modifier = Modifier
){
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
    ) {
        Column(
            modifier = Modifier.padding(vertical = 16.dp),
        ) {
            Text(
                text = "Info :",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun SkillItem(
    skill: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary
    ) {
        Text(
            text = skill,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun ProjetItem(
    project: Project,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = project.imageRes),
                    contentDescription = "Image du projet ${project.titre}",
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = project.titre,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = project.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = onClick, modifier = Modifier) {
                    Text(text = "Voir le projet")
                }
                Button(onClick = onFavoriteClick, modifier = Modifier) {
                    Text(text = if (isFavorite){
                        "★ Retirer des favoris"
                    } else {
                        "Ajouter aux favoris"
                    })
                }
            }
        }
    }
}

@Composable
fun AvailabilityInfo(
    name: String,
    isAvailable: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Text(
        text = if (isAvailable){
            "$name est Disponible pour de nouvelles missions"
        } else {
            "$name n'est pas disponible en ce moment"
        },
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(top = 12.dp)
            .semantics {
                role = Role.Button
                contentDescription = "Voir ma disponibilité"
            }
    )
}

@Composable
fun ContactSection(
    name: String,
    email: String,
    location: String,
    isAvailable: Boolean,
    onContactClick: () -> Unit,
    onAvailabilityClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {

        var showContact by rememberSaveable {
            mutableStateOf(false)
        }

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Contact",
                style = MaterialTheme.typography.titleMedium
            )

            Button(onClick = { showContact = !showContact }) {
                if (showContact) {
                    Text(text = "Masquer Contact")
                } else
                    Text(text = "Afficher Contact")
            }

            if (showContact) {
                OutlinedTextField(
                    value = email,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Email") },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = location,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Localisation") },
                    leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth()
                )

                AvailabilityInfo(name = name, isAvailable, onClick = onAvailabilityClick)

                val uriHandler = LocalUriHandler.current
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ContactButton(text = "Contacter", onClick = onContactClick)
                    ContactButton(
                        text = "Portfolio",
                        onClick = { uriHandler.openUri("https://www.nolhandev.fr") })
                }
            }
        }
    }
}

@Composable
fun ContactButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(onClick = onClick, modifier = modifier) {
        Icon(
            imageVector = Icons.Default.Send,
            contentDescription = null
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text)
    }
}

// --- Preview ---

@Preview(showBackground = true)
@Composable
fun ProfileScreenAvailablePreview() {
    GreetingCardTheme(darkTheme = true) {
        ProfileScreen(
            developer = sampleDeveloper(),
            projects = sampleProjects(),
            searchQuery = "",
            onSearchQueryChange = {},
            favoriteProjectIds = emptySet(),
            onFavoriteClick = {},
            showOnlyFavorites = false,
            onToggleFavoritesFilter = {},
            onProjectClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenUnavailablePreview() {
    GreetingCardTheme(darkTheme = false) {
        ProfileScreen(
            developer = sampleDeveloper(),
            projects = sampleProjects(),
            searchQuery = "Portfolio",
            onSearchQueryChange = {},
            favoriteProjectIds = setOf(1,3),
            onFavoriteClick = {},
            showOnlyFavorites = true,
            onToggleFavoritesFilter = {},
            onProjectClick = {}
        )
    }
}