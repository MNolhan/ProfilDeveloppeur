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
import androidx.compose.ui.platform.LocalUriHandler

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GreetingCardTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ProfileScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

// --- Data Class

data class Project(
    val id: Int,
    val titre: String,
    val description: String,
    val imageRes: Int
)

fun sampleProjects(): List<Project> = listOf(
    Project(1, "Portfolio", "Site perso en React / Node.js, déployé avec Docker", android.R.drawable.ic_menu_gallery),
    Project(2, "Product Explorer", "Application Android en Jetpack Compose", android.R.drawable.ic_menu_gallery),
    Project(3, "Site e-commerce", "Projet ESGI en PHP / Laravel", android.R.drawable.ic_menu_gallery)
)

// --- Screen ---

@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {
    ProfileCard(
        name = "Nolhan Marteau",
        role = "Etudiant de l'ESGI",
        description = "Développeur full-stack passionné, je passe du front au back sans problème : React et Compose pour les interfaces, Node.js, Java et Laravel pour le back. J'aime construire un projet de la maquette jusqu'à la mise en production.",
        skills = listOf("JavaScript", "React", "SQL", "Node", "Java", "PHP", "Laravel"),
        projects = sampleProjects(),
        isAvailable = true,
        onContactClick = {},
        onAvailabilityClick = {},
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@Composable
fun ProfileCard(
    name: String,
    role: String,
    description: String,
    skills: List<String>,
    projects: List<Project>,
    isAvailable: Boolean,
    onContactClick: () -> Unit,
    onAvailabilityClick : () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface (modifier = modifier) {
        LazyColumn (
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ){
            item{
                ProfileHeader(name = name, role = role)
            }

            item{
                Info(description = description)
            }

            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Skills",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(skills) { skill ->
                                SkillItem(skill = skill)
                            }
                        }
                    }
                }
            }

            item{
                Column {
                    Text(
                        text = "Projets",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(projects, key = { it.id }) { project ->
                            ProjetItem(
                                titre = project.titre,
                                description = project.description,
                                imageRes = project.imageRes,
                                onClick = {},
                                modifier = Modifier
                                    .width(300.dp)
                                    .height(175.dp)
                            )
                        }
                    }
                }
            }

            item {
                ContactSection(
                    name = name,
                    email = "nolhan.marteau@gmail.com",
                    location = "Tours",
                    isAvailable = isAvailable,
                    onAvailabilityClick = onAvailabilityClick,
                    onContactClick = onContactClick,
                )
            }
        }
    }
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
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Info :", style = MaterialTheme.typography.titleMedium)
            Text(text = description, style = MaterialTheme.typography.bodyMedium)
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
    titre: String,
    description: String,
    imageRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = "Image du projet $titre",
                modifier = Modifier.size(56.dp)
            )
            Text(text = titre, style = MaterialTheme.typography.titleMedium)
            Text(text = description, style = MaterialTheme.typography.bodyMedium)
            Text(
                text = "Détails du projet",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable(onClick = onClick)
            )
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
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Contact",
                style = MaterialTheme.typography.titleMedium
            )

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
                ContactButton(text = "Portfolio", onClick = { uriHandler.openUri("https://www.nolhandev.fr") })
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
        ProfileCard(
            name = "Nolhan Marteau",
            role = "Etudiant à l'ESGI",
            description = "Développeur full-stack passionné, je passe du front au back sans problème : React et Compose pour les interfaces, Node.js, Java et Laravel pour le back. J'aime construire un projet de la maquette jusqu'à la mise en production.",
            skills = listOf("JavaScript", "React", "SQL", "Node", "Java", "PHP", "Laravel"),
            projects = sampleProjects(),
            isAvailable = true,
            onContactClick = {},
            onAvailabilityClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenUnavailablePreview() {
    GreetingCardTheme(darkTheme = false) {
        ProfileCard(
            name = "Nolhan Marteau",
            role = "Etudiant à l'ESGI",
            description = "Développeur full-stack passionné, je passe du front au back sans problème : React et Compose pour les interfaces, Node.js, Java et Laravel pour le back. J'aime construire un projet de la maquette jusqu'à la mise en production.",
            skills = listOf("JavaScript", "React", "SQL", "Node", "Java", "PHP", "Laravel"),
            projects = sampleProjects(),
            isAvailable = false,
            onContactClick = {},
            onAvailabilityClick = {}
        )
    }
}