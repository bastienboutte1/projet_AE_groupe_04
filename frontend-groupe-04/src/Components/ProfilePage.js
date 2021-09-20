import callAPI from "../utils/api.js";
import { getUserSessionData } from "./../utils/session.js";
import { RedirectUrl } from "./Router.js";
const API_AUTH_URI = "/api/auths/";
const API_TYPE_FURNITURE_URL = "/api/type-furnitures/";

const valEtat = [
    ["visite","En attente de visite"],
    ["refuse","Meuble refusé d'achat"],
    ["restaure","Meuble en restauration"],
    ["depose","Meuble déposé en magasin"],
    ["propose","Meuble proposé à la vente"],
    ["achete","Meuble a été acheté par le magasin"],
    ["retire","Meuble retiré de la vente"],
    ["vendu","Meuble a été vendu"],
];
const monEtat = new Map(valEtat);

let monType;
let typesList= [];
let types = callAPI(
    API_TYPE_FURNITURE_URL,
    "GET",
    undefined,
    undefined
);
types.then((value)=> {
    value.forEach((val) => {
        let type = [parseInt(val.idTypeFurniture), val.type];
        typesList.push(type);
    });
    monType = new Map(typesList)
});

let profilePage = `
    <div class="container-fluid">
        <!-- partie profil + adresse -->
        <div class="row">
            <div class="col-12 col-md-6" id="colProfil">
                <div class="container border border-dark rounded py-2 h-100">
                    <div class="row">
                        <h2 class="mx-auto"><u>Profil</u></h2>
                    </div>
                    <hr>
                    <div class="container align-items-center">
                        <div class="row">
                            <div class="col">
                                <div id="nom" class="bg-light p-1 rounded">Nom : </div>
                            </div>
                            <div class="col">
                                <div id="prenom" class="bg-light p-1 rounded">Prénom : </div>
                            </div>
                        </div>
                        <br>
                        <div class="row">
                            <div class="col">
                                <div id="pseudo" class="bg-light p-1 rounded">Pseudo : </div>
                            </div>
                            <div class="col">
                                <div id="email" class="bg-light p-1 rounded">Email : </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-12 col-md-6" id="colAdresse">
                <div class="container border border-dark rounded py-2 h-100">
                    <div class="row">
                        <h2 class="mx-auto"><u>Adresse</u></h2>
                    </div>
                    <hr>
                    <div class="container align-items-center">
                        <div class="row">
                            <div class="col">
                                <div id="rue" class="bg-light p-1 rounded">Rue : </div>
                            </div>
                            <div class="col">
                                <div id="num" class="bg-light p-1 rounded">Numéro : </div>
                            </div>
                        </div>
                        <br>
                        <div class="row">
                            <div class="col">
                                <div id="boite" class="bg-light p-1 rounded">Boite : </div>
                            </div>
                            <div class="col">
                                <div id="pays" class="bg-light p-1 rounded">Pays : </div>
                            </div>
                        </div>
                        <br>
                        <div class="row">
                            <div class="col">
                                <div id="zip" class="bg-light p-1 rounded">Code Postal : </div>
                            </div>
                            <div class="col">
                                <div id="commune" class="bg-light p-1 rounded">Commune : </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!-- FIN partie profil + adresse -->

        <br>
        <hr class="py-2">
        
        <!-- partie meubles vendus + achetes -->
        <div class="row">
            <div class="container-fluid border border-dark rounded py-2 h-100">
                <div class="row">
                    <h2 class="mx-auto"><u id="totalSales">Mes Meubles Achetés : </u></h2>
                </div>
                <br>
                <div class="justify-content-center align-items-center w-100" id="furnituresBought">
                </div>
            </div>
        </div>
        <!-- FIN partie meubles vendus + achetes -->

    </div>
`;

const ProfilePage = async () => {
    if(!getUserSessionData()){
        RedirectUrl("/login","Vous devez être connecté pour accéder à cette page!");
    } else {
        let user = getUserSessionData().user;
        let adresse = await callAPI(
            "/api/addresses/"+user.address,
            "GET",
            getUserSessionData().token,
            undefined
        );

        let sales = await callAPI(
            "/api/sales/user/"+user.id,
            "GET",
            getUserSessionData().token,
            undefined
        );
        
        let page = document.getElementById("page");
        page.innerHTML = profilePage;
        
        //input profil info
        document.getElementById("nom").innerText = "Nom : "+user.name;
        document.getElementById("prenom").innerText = "Prénom : "+user.firstName;
        document.getElementById("pseudo").innerText = "Pseudo : "+user.pseudo;
        document.getElementById("email").innerText = "Email : "+user.email;

        //input adresse info
        document.getElementById("rue").innerText = "Rue : "+adresse.street;
        document.getElementById("num").innerText = "Numéro : "+adresse.number;
        if(adresse.box){
            document.getElementById("boite").innerText = "Boite : "+adresse.box;
        }else {
            document.getElementById("boite").innerText = "Boite : aucune";
        }
        document.getElementById("pays").innerText = "Pays : "+adresse.country;
        document.getElementById("zip").innerText = "Code Postal : "+adresse.postalCode;
        document.getElementById("commune").innerText = "Commune : "+adresse.municipality;

        document.getElementById("totalSales").innerText += " "+sales.length;

        if(sales.length <= 0){
            document.getElementById('furnituresBought').innerHTML = `<p>Vous n'avez acheté aucun meuble</p>`;
        }else{
            for (let i = 0; i < sales.length; i++) {
                const sale = sales[i];
                let furniture = await callAPI("/api/furnitures/"+sale.idFurniture,"GET",getUserSessionData().token,undefined);
                let photos = await callAPI("/api/photos/furniture/"+furniture.idFurniture,"GET",undefined,undefined);
                document.getElementById('furnituresBought').innerHTML += getFurnitureFragment(furniture,photos,sale);
            }
        }
    }
};

const getFurnitureFragment = (furniture,photos,sale) => {
    let frag = `
    <div class="container-fluid">
        <div class="justify-content-center align-items-center h-100">
            <form class="container pt-3 d-flex">
                <div class="col-3 col-sm-3 col-md-4 col-lg-3 furnitures-photo-border align-items-center">`;
    photos.forEach(photo => {
        if(photo.prefered){
            frag+=`<img id="image_test" src="${photo.base64Value}" alt="photo_meuble${photo.idFurniture}" class="img-fluid">`;
        }
    });
    frag+=`
                </div>
                <div class="col-5 col-sm-5 col-md-6 col-lg-7 furnitures-description-border">
                    <h5 class="px-2 pt-3">Description :</h5>
                    <p class="px-2"> ${furniture.description}</p>

                    <h5 class="px-2 pt-3">Type : ${monType.get(furniture.idType)}</h5>

                    <h5 class="px-2 pt-3">État : ${monEtat.get(furniture.condition)}</h5>
                </div>
                <div class="col-4 col-sm-4 col-md-2 col-lg-2 furnitures-price-border text-center">
                    <div>
                        <h5 class="px-2 pt-3">Prix d'achat:</h5>
                        <p class="px-2 text-wrap">${sale.lastSellingPrice}€</p>
                    </div>
                </div>
            </form>
        </div>
    </div>
    <br>
    `;
    return frag;
}

export default ProfilePage;