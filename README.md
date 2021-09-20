# Projet Application d'Entreprise groupe 4

## RESTful API : opérations disponibles

### Opérations associées à la gestion des utilisateurs et l'authentification

<br>
<table style="caption-side: top">
<caption>Opérations sur les ressources de type "Authentification"</caption>
<tr>
    <th>URI</th>
    <th>Méthode</th>
    <th>Auths?</th>
    <th>Opération</th>
</tr>

<tr>
    <td>auths/login</td>
    <td>POST</td>
    <td>Non</td>
    <td>
    Vérifier les « credentials » d’un User et renvoyer le User et un token JWT s’ils sont OK
    </td>
</tr>
<tr>
    <td>auths/register</td>
    <td>POST</td>
    <td>Non</td>
    <td>
    Créer une ressource User et un token JWT et les renvoyer
    </td>
</tr>
<tr>
    <td>auths/me</td>
    <td>POST</td>
    <td>JWT</td>
    <td>
    Lire la ressource identifiée par le biais du token donné dans le header de la requête
    </td>
</tr>

</table>

<br>

<table style="caption-side: top">
<caption>Opérations sur les ressources de type "User"</caption>
<tr>
    <th>URI</th>
    <th>Méthode</th>
    <th>Auths?</th>
    <th>Opération</th>
</tr>

<tr>
    <td>users/pseudo-available/{pseudo}</td>
    <td>GET</td>
    <td>Non</td>
    <td>
    Renvoie HTTP_CODE 409 si pseudo deja utilisé, sinon 200
    </td>
</tr>
<tr>
    <td>users/email-available/{email}</td>
    <td>GET</td>
    <td>Non</td>
    <td>
    Renvoie HTTP_CODE 409 si email deja utilisé, HTTP_CODE 401 si email mauvais format, sinon 200
    </td>
</tr>
<tr>
    <td>users</td>
    <td>GET</td>
    <td>JWT</td>
    <td>
    Renvoie la liste de tous les utilisateurs
    </td>
</tr>

</table>

<table style="caption-side: top">
<caption>Opérations sur les ressources de type "Furniture"</caption>
<tr>
    <th>URI</th>
    <th>Méthode</th>
    <th>Auths?</th>
    <th>Opération</th>
</tr>

<tr>
    <td>furnitures/</td>
    <td>GET</td>
    <td>Non</td>
    <td>
    GET ALL : Renvoie une liste de tous les meubles dans la bd
    </td>
</tr>
<tr>
    <td>furnitures/{id}</td>
    <td>GET</td>
    <td>Non</td>
    <td>
    GET ALL : Renvoie le meuble ayant l'id spéficié dans l'url
    </td>
</tr>
<tr>
    <td>furnitures/</td>
    <td>PUT</td>
    <td>JWT</td>
    <td>
    Met a jour le meuble avec id_furniture du meuble passé dans le header avec le meuble qui est envoyé
    </td>
</tr>

</table>

<table style="caption-side: top">
<caption>Opérations sur les ressources de type "Photos"</caption>
<tr>
    <th>URI</th>
    <th>Méthode</th>
    <th>Auths?</th>
    <th>Opération</th>
</tr>

<tr>
    <td>photos</td>
    <td>GET</td>
    <td>Non</td>
    <td>
    Renvoie une list de toutes les photos contenu dans la DB
    </td>
</tr>
<tr>
    <td>photos/{id}</td>
    <td>GET</td>
    <td>Non</td>
    <td>
    Renvoie une list de toutes les photos qui sont pour un meuble spécifique ayant l'id spécifié dans l'url
    </td>
</tr>
<tr>
    <td>photos</td>
    <td>POST</td>
    <td>JWT</td>
    <td>
    Créée un nouveau meuble en bd et le renvoie
    </td>
</tr>
<tr>
    <td>photos</td>
    <td>PUT</td>
    <td>JWT</td>
    <td>
    Met a jour un une photo et renvoie la photo modifié
    </td>
</tr>
<tr>
    <td>photos/{id}</td>
    <td>DELETE</td>
    <td>JWT</td>
    <td>
    Supprime la photo ayant l'id spécifié dans l'url et renvoie la photo supprimée
    </td>
</tr>
</table>

<table style="caption-side: top">
<caption>Opérations sur les ressources de type "Adresses"</caption>
<tr>
    <th>URI</th>
    <th>Méthode</th>
    <th>Auths?</th>
    <th>Opération</th>
</tr>

<tr>
    <td>addresses/{id}</td>
    <td>GET</td>
    <td>JWT</td>
    <td>
    Renvoie l'adresse ayant l'id spécifié dans l'url
    </td>
</tr>
</table>