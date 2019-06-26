#Project de INF2015TP

A18-team10

Premier projet de session

INF2015-30 Développement de logiciels dans un environnement Agile

Contributeurs

N'TSOUAGLO Kokou Gawonou
GAVIDIA, Cristian
MEDEIROS Ricardo Julio

Objectif du project
Le programme ou logiciel écrit en Java sert à evaluer une valeur foncière d’un terrain avec un ou plusieurs lots.
Cette évaluation foncièreva nous permettre aussi de calculer taux des services municipaux et scolaires. L'utilisateur
doit avoir un fichier d'entré en format json et précisé le nom de fichier de sortie en format json. Le programme 
va lire le fichier d'entré, faire les calcules et produire un fichier de sortie. Le calcul va se faire selon 
le type du terrain (Agricol, residentiel ou commercial)
la valeur d'un lot est calculé par la formule suivante:
Valeur_par_lot2 = Superfice * ((PrixMin +PrixMax)/2) + (500 - droit_de_passage * 0.1 (Superfice * ((PrixMin +PrixMax)/2))) + 500(Nbr_de_service+2)
Le prix du terrain dépend de beaucoup de paramètre tels que droit de passage, Nombre de services et le prix minimum ou maximun.
Nous avons les classes suivantes:
Agricole
Commercial
EvaluationFonnciere
ReadWritetoFile
Residentiel
Terrain
Remarque
Le nom de fichier d'éntré doit être obligatoirement en format .json sinon le programme sortira erreurs à l'exécution Les deux fichiers doivent être précisé au console