# Prototypes de recommendations Steam

=======

Ce projet à pour but de créer des prototypes de logiciel de recommendations.

## Premier prototype : Python

Ce prototype utilise l'api python steamapi (https://github.com/smiley/steamapi.git).
Il utilise également requests (https://github.com/kennethreitz/requests).
Pour le lancer, il suffit de se placer dans prototypePython et de faire : 
##### python prototypePython.py


## Second prototype : Java

Ce prototype utilise le framework spring (http://spring.io/) et un de ces modules, spring-MVC.

Pour le lancer, il suffit de se placer dans java et de faire :
##### mvn tomcat:run 

## Où trouver des id steam ? 

Sur la page d'un utilisateur, la dernière partie de l'url est soit un pseudo (inutilisable pour l'instant), soit une longue suite de caractères. Cette longue suite est l'id de l'utilisateur.

Voici deux ids d'exemple: 

- 76561198003848262
- 76561197980002497
