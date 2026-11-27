# Profil Développeur v3.0.0

## Description

C'est une petite application Android faite avec Jetpack Compose qui affiche mon profil de développeur.
On y trouve ma photo, mon nom, une courte description, mes compétences, quelques projets et une partie contact.

- **v1** : Première version, avec le nom, la description et les compétences. Elle a servi à prendre en main Android Studio et le développement mobile.
- **v2** : Interface améliorée, et ajout des projets et de la section contact.
- **v3** : Application interactive, avec la recherche de projets, les favoris, le filtre « favoris uniquement », le compteur de projets et l'affichage du contact au clic. L'état est géré par un ViewModel avec StateFlow.

## États gérés par l'application

L'état principal de l'écran est regroupé dans `ProfileUiState`, géré par le `ProfileViewModel` :

- **Texte de recherche** (`searchQuery`) : ce que l'utilisateur tape pour filtrer les projets.
- **Filtre favoris** (`showOnlyFavorites`) : affiche tous les projets ou seulement les favoris.
- **Projets favoris** (`favoriteProjectIds`) : les identifiants des projets marqués comme favoris.
- **Liste affichée** (`projects`) : les projets visibles, recalculés à partir de la recherche et du filtre

## État Local

# Quel état avez-vous conservé directement dans un composable et pourquoi ?

- **Affichage du contact** (`showContact`) : affiche ou masque les informations de contact.

On a gardé l'affichage du contact dans un composable car il ne change aucune donnée, il n'a aucun lien avec le ViewModel.

## State Hoisting

# Pourquoi votre composable d'écran principal peut-il être considéré comme stateless ?

Il peut être considéré comme stateless car il affiche uniquement des informations que le container lui transmet, il se contente 
d'afficher sans calculer ni regarder quoi que ce soit

## ViewModel

#  Quel est le rôle de votre ViewModel dans cette application ?

Le ViewModel stocke l'état. Au lancement de l'application, il contient l'état de base (par exemple "" pour
la recherche ou false pour le filtre). Ensuite, il reçoit les actions de l'utilisateur, qui viennent de l'écran
en passant par le container. Avec ces actions, il recalcule l'état et stock ce nouvelle état. Le container l'observe et se charge de le redonner à l'écran.

## Stateflow

#  Comment l'interface est-elle informée lorsqu'un état change ?

Dans mon code, dès que la variable uiState est modifié (quand le ViewModel a finit de recalculer) alors container
donne les valeurs de uiState à la View en paramètre

## Organisation du code

```
MainActivity.kt
├── MainActivity
├── Data Class & Sample
│   ├── Developer
│   ├── Project
│   ├── ProfileUiState
│   ├── sampleDeveloper()
│   └── sampleProjects()
├── ViewModel
│   └── ProfileViewModel
├── Screen
│   └── ProfileScreen
├── Container
│   └── ProfileContainer
├── Composant
│   ├── ProfileHeader
│   ├── Info
│   ├── SkillItem
│   ├── ProjetItem
│   ├── AvailabilityInfo
│   ├── ContactSection
│   └── ContactButton
└── Preview
    ├── ProfileScreenAvailablePreview
    └── ProfileScreenUnavailablePreview
```

## Capture d'écran

<img src="./Images/Final1.jpg" alt="Final1" width="1440"> 
<img src="./Images/Final2.jpg" alt="Final2" width="1440">
<img src="./Images/Final3.jpg" alt="Final3" width="1440">

## Auteur

Développé par Nolhan Marteau, M1 ESGI