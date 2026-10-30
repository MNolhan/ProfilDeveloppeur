# Profil développeur Compose

## Description

Cette application Android affiche une fiche « Profil développeur » réalisée avec Jetpack Compose.
L'écran présente un nom, un rôle, une courte description, une liste de compétences et une information de disponibilité.
L'utilisateur peut cliquer sur la zone de disponibilité ou sur le bouton « Contacter ».
L'interface est découpée en plusieurs composables réutilisables, paramétrés et prévisualisables avec deux jeux de données différents.

## Notions utilisées

- Fonctions composables (`@Composable`)
- Paramètres
- `Modifier` et chaînage de modifiers
- Padding externe / padding interne
- Callbacks (`() -> Unit`)
- `Surface`, `Column`, `Text`, `Button`
- `Scaffold` et `innerPadding`
- Previews (`@Preview`)
- Accessibilité de base (`semantics`, `Role.Button`, `contentDescription`)

## Explication

**1. Quels composables avez-vous créés ?**
`ProfileScreen`, `ProfileCard`, `ProfileHeader`, `SkillItem`, `AvailabilityInfo` et `ContactButton`, ainsi que deux previews.

**2. Quel composable représente l'écran principal ?**
`ProfileScreen`. Il contient les données du profil et place la carte dans l'écran. Il est appelé dans `MainActivity` à l'intérieur du `Scaffold`, avec le `innerPadding`.

**3. Où utilisez-vous un padding externe ?**
Dans `ProfileScreen`, qui passe `modifier.fillMaxWidth().padding(24.dp)` à `ProfileCard` : c'est l'écran (le parent) qui décide de l'espace autour de la carte. De la même façon, `ProfileCard` passe un padding à chaque `SkillItem` et au `ContactButton` pour les espacer entre eux.

**4. Où utilisez-vous un padding interne ?**
Dans `ProfileCard`, la `Column` placée dans la `Surface` a un `Modifier.padding(16.dp)` : c'est la carte elle-même qui espace son contenu par rapport à ses bords. `AvailabilityInfo` applique aussi un padding interne en fin de chaîne pour agrandir sa zone cliquable autour du texte.

**5. Quel composant reçoit un callback ?**
`ContactButton` et `AvailabilityInfo` reçoivent un callback `onClick`. Ils signalent le clic sans décider de la logique : celle-ci est fournie par `ProfileScreen` et transmise via `ProfileCard` (`onContactClick`, `onAvailabilityClick`).

**6. Quelle information d'accessibilité avez-vous ajoutée ?**
Sur la zone cliquable personnalisée `AvailabilityInfo`, j'ai ajouté `semantics` avec `role = Role.Button` et une `contentDescription`. Un lecteur d'écran l'annonce donc comme un bouton avec une action claire. Le `ContactButton` utilise un `Button` Material 3, qui expose déjà son rôle de bouton et son texte aux outils d'accessibilité, contrairement à une simple zone rendue cliquable avec `clickable`.

**Chaîne de modifiers :** dans `AvailabilityInfo`, `fillMaxWidth()` → `clickable()` → `semantics {}` → `padding()`. Le padding est placé après `clickable` pour que l'espace autour du texte soit aussi cliquable.

**7. Pourquoi vos composants peuvent-ils être considérés comme réutilisables ?**
Ils reçoivent leurs données en paramètres (nom, rôle, compétence, disponibilité, texte du bouton, callback) au lieu d'afficher des valeurs fixes, et chacun accepte un `modifier: Modifier = Modifier` qui laisse le parent décider de son placement. Les deux previews affichent par exemple deux profils différents (disponible / non disponible, compétences différentes) avec les mêmes composables.

## Capture d'écran

![Capture de l'application](screenshot.png)

## Lien du dépôt

[Lien du dépôt GitHub](https://github.com/TON_PSEUDO/TON_DEPOT)
