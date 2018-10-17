# Gawo
# A18-team8
# Projet de session – Automne 2018

Développement de logiciels dans un environnement Agile


## Effectué par

###### Larissa Manirambona
###### Lisa Steccy Kaneza
###### Talal Mansour
###### Ussel Sabbat

## À quoi sert le programme ?

Au début de l'exécution, l'utilisateur doit rentrer le répertoire où se trouve le fichier IN et où est créé le fichier OUT.
 Exemple pour un *PC= c:\répertoire\Fichier* et pour le *Mac = \Users\NomOrdinateur\Fichier*. Aprés, le programme s'occupe de faire la lecture du fichier IN et saisir les données qui sont dans le fichier entré. Par la suite, le programme va pouvoir faire les calculs pour la valeur foncière en additionnant la somme du montant de la valeur du lots, le montant pour les droits de passage et le montant pour les services. Le fichier Out va afficher les resultats de l'évaluation foncière de chaque terrain.

### But 

Le logiciel calcule l'évaluation foncière d'un terrain qui posséde un ou plusieurs lots. C'est un des moyens de finacement
 du coût des services municipaux et scolaires que génère l’évaluation foncière. 

## Qu'elles sont les classes
Nous allons utiliser quelque classes pour l'execution du programme:
* Agricole
* Calcul_Agricol
* Commercial
* EcritureJson
* EvaluationFonnciere
* LectureJson
* Lot
* Residentiel
* Terrain
* Tp (main)
* Traitement
* ValeurLot


## Quels sont les formats d'entrées et de sorties
Les formats de fichier d'entré et de sortie sont en format JSON.

## Gestion des erreurs
Il est **nécessaire** de mettre un fichier d'entrée, sinon une exception est lévée.
Il faut **au moins** un lot par terrain, sinon une exception est lévée.
