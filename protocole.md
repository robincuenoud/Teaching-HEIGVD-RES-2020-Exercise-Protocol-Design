## Protocole

#### port utilis�
Le port qu'on va utiliser est le port 2022. Donc le client utilise le port 2022 pour se connecteur au serveur qui �coute ce dernier port TCP

#### format
Les 4 op�rations support�es sont addition, soustraction, multiplication, division. on utilisera ces mot-r�serv�s `ADD`, `SUB`, `MUL`, `DIV`


Le client, pour demander son calcul envoit un message au format (OPERATION OP1 OP2)


Nous prennons juste des entiers en arguments, et ne retournous que des entier (on utilise donc une division enti�re)

#### feuille de route

* le client envoit au serveur un message pour initier la connexion: client -> serveur (connexion TCP)
* le serveur r�pond au client pour lui dire qu'il a bien re�u, que la connexion est �tablie: serveur -> client (ack + hello)
* le serveur envoit au client la liste des op�rations support�es: serveur -> client 
* le client envoit sa demande de calcul: client -> serveur (OPERATION OP1 OP2)
* le serveur retourne le r�sultat: serveur -> client (result)

#### fermeture

En cas de non respect de la proc�dure, la connexion est ferm�e. Sinon elle est ferm�e par le client qui coupe son socket