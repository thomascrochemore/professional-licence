## Projet ACRObATT - Overkill Legacy

#### Dépôt front mobile

Bienvenue sur le dépôt de l'application mobile du projet ACRObATT, nommé "PROUST" (PRévisions météo Optimisées par Utilisation de SiTes)

#### Commandes de base ionic :

créer un projet : ionic start nom_projet

lancer le projet (option --lab pour mobile) : ionic serve --lab

lancer sur mobile : 

1) ajouter plateforme : ionic cordova platform add android

2) construire : cordova build android --verbos

3) lancer : ionic cordova run 

voir les templates de base : ionic start --list

ajouter un composant (d'angular web par exemple) : ionic g component meteo_api

générer resources adaptées : ionic cordova resources (--splash --icon)