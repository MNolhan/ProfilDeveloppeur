package com.example.greetingcard

import android.os.Bundle
import androidx.compose.material3.Button
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.greetingcard.ui.theme.GreetingCardTheme

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


@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {
    ProfileCard(
        name = "Nolhan Marteau",
        role = "Etudiant de l'ESGI",
        description = "Étudiant à l'ESGI, je me forme au développement mobile avec Kotlin et Jetpack Compose.",
        skills = listOf("Kotlin", "Jetpack Compose", "Git"),
        isAvailable = true,
        onContactClick = {},
        onAvailabilityClick = {},
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp)
    )
}

@Composable
fun ProfileCard(
    name: String,
    role: String,
    description: String,
    skills: List<String>,
    isAvailable: Boolean,
    onContactClick: () -> Unit,
    onAvailabilityClick : () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface (modifier = modifier) {
        Column(modifier = Modifier.padding(16.dp)){
            ProfileHeader(name = name, role = role)
            Text(text = description)
            Text(
                text = "Skills : ",
                Modifier.padding(top = 16.dp))
            for (skill in skills){
                SkillItem(
                    skill = skill,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }   
            AvailabilityInfo(
                isAvailable,
                onClick = onAvailabilityClick
            )
            ContactButton(
                text = "Contacter",
                onContactClick,
                modifier = Modifier.padding(top = 16.dp)
            )

        }
    }
}

@Composable
fun ProfileHeader(
    name : String,
    role : String,
    modifier: Modifier = Modifier
){
    Surface (modifier = modifier) {
        Column(modifier = Modifier.padding(bottom = 8.dp)){
            Text(text = name)
            Text(text = role)
        }
    }
}

@Composable
fun SkillItem(
    skill: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = skill,
        modifier = modifier
    )
}

@Composable
fun AvailabilityInfo(
    isAvailable: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Text(
        text = if (isAvailable){
            "Disponible"
        } else {
            "Non disponible"
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
fun ContactButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(onClick = onClick, modifier = modifier) {
        Text(text)
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenAvailablePreview() {
    GreetingCardTheme {
        ProfileCard(
            name = "Nolhan Marteau",
            role = "Etudiant à l'ESGI",
            description = "Encore Etudiant je recherche une Alternance en Développement Web et Mobile",
            skills = listOf("JavaScript", "React", "SQL, Node, Java, PHP, Laravel"),
            isAvailable = true,
            onContactClick = {},
            onAvailabilityClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenUnavailablePreview() {
    GreetingCardTheme {
        ProfileCard(
            name = "Nolhan Marteau",
            role = "Etudiant à l'ESGI",
            description = "Encore Etudiant je recherche une Alternance en Développement Web et Mobile",
            skills = listOf("JavaScript", "React", "SQL, Node, Java, PHP, Laravel"),
            isAvailable = false,
            onContactClick = {},
            onAvailabilityClick = {}
        )
    }
}

// -------------- TP --------------

@Composable
fun ProductDemoScreen(modifier: Modifier = Modifier) {
    ProductCard(
        name = "Sac à Dos",
        price = 49.99,
        isAvailable = true,
        isFavorite = false,
        onFavoriteClick = {},
        modifier = modifier.padding(24.dp)
    )
}

@Composable
fun ProductCard(
    name : String,
    price : Double,
    isAvailable: Boolean,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(modifier = modifier){
        Column(modifier = Modifier.padding(16.dp)){
            ProductTitle(name = name)
            ProductPrice(price = price)
            ProductAvailability(isAvailable)
            FavoriteButton(
                isFavorite = isFavorite,
                onFavoriteClick = onFavoriteClick
            )
        }
    }
}

@Composable
fun ProductTitle(name: String) {
    Text(text = name)
}

@Composable
fun ProductPrice(price: Double) {
    Text(text = "$price €")
}

@Composable
fun ProductAvailability(isAvailable : Boolean) {
    Text(
        text = if (isAvailable) {
            "Dispo"
        } else {
            "Pas Dispo"
        }
    )
}

@Composable
fun FavoriteButton(
    isFavorite : Boolean,
    onFavoriteClick : () -> Unit
) {
    Button(onClick = onFavoriteClick) {
        Text(
            if (isFavorite) {
                "Favori"
            } else {
                "Ajouter au Favori"
            }
        )
    }
}
