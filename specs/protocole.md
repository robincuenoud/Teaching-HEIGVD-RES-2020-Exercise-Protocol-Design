## Protocole

#### port utilisé
Le port qu'on va utiliser est le port 2022. Donc le client utilise le port 2022 pour se connecteur au serveur qui écoute ce dernier port TCP

#### format
Les 4 opérations supportées sont addition, soustraction, multiplication, division. on utilisera ces mot-réservés `ADD`, `SUB`, `MUL`, `DIV`


Le client, pour demander son calcul envoit un message au format (OPERATION OP1 OP2)


Nous prennons juste des entiers en arguments, et ne retournous que des entier (on utilise donc une division entière)

#### feuille de route

* le client envoit au serveur un message pour initier la connexion: client -> serveur (connexion TCP)
* le serveur répond au client pour lui dire qu'il a bien reçu, que la connexion est établie: serveur -> client (ack + hello)
* le serveur envoit au client la liste des opérations supportées: serveur -> client 
* le client envoit sa demande de calcul: client -> serveur (OPERATION OP1 OP2)
* le serveur retourne le résultat: serveur -> client (result)

#### fermeture

En cas de non respect de la procédure, la connexion est fermée. Sinon elle est fermée par le client qui coupe son socket