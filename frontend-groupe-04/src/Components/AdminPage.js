import { getUserSessionData } from "./../utils/session.js";
import { RedirectUrl } from "./Router.js";
import { getDefaultAdminPage, getUserFragmentShort } from "./AdminFragments";
import callAPI from "../utils/api.js";

const API_FURNITURE_URL = "/api/furnitures/";
const API_SELLED_FURNITURE_URL = "/api/furnitures/selled/";
const API_BOUGHT_FURNITURE_URL = "/api/furnitures/bought/";
const API_USER_URL = "/api/users/";
const API_DESC_USER_URL = "/api/users/sorted-desc/";
const API_PHOTO_URL = "/api/photos/";
const API_PREF_PHOTO_URL = "/api/photos/furniture/$/prefered/"
const API_SALE_URL ="/api/sales/";
const API_TYPE_FURNITURE_URL = "/api/type-furnitures";

const valEtat = [
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

const AdminPage = () => {
    let user = getUserSessionData();
    if(user && user.user.role==="patron"){
        document.querySelector("#page").innerHTML = getDefaultAdminPage();
    
        let btnGeneral = document.getElementById("nav-general-tab");
        let btnUser = document.getElementById("nav-user-tab");
        let btnFurniture = document.getElementById("nav-furniture-tab");
        
        btnGeneral.addEventListener("click", onGeneral);
        btnUser.addEventListener("click", onUser);
        btnFurniture.addEventListener("click", onFurniture);
    
        onGeneral();
    }else{
        RedirectUrl("/", "Vous n'avez pas accès a cette partie");
    }
};

const onGeneral = async () => {
    console.log("onGeneral");
    let innerPage = document.getElementById("nav-general");

    let selledFurnitures = await getAllSelledFurnitures();
    let boughtFurnitures = await getAllBoughtFurnitures();
    let users = await getAllUsersDesc();
    
    let newPage = `
    <div class="container-fluid">
        <div class="row justify-content-center align-items-center h-100">
            <div class="col col-sm-12 col-md-8 col-lg-12">
                <div class="row pt-4">
                    <div class="col-md-12 col-lg-6 py-3">
                        <u><h4>Derniers meubles vendus</h4></u>
                        <div class="card border-dark scrollbar scrollbar-dark overflow-auto" style="max-height: 20em">
                            <div class="card-body">`;
                            for(let i = 0; i < selledFurnitures.length; i++) {
                                newPage +=  await getSelledGeneralFragment(selledFurnitures[i]);
                            }
                            newPage += `
                            </div>
                        </div>
                    </div>
                    <div class="col-md-12 col-lg-6 py-3">
                        <u><h4>Derniers meubles achetés</h4></u>
                        <div class="card border-dark overflow-auto" style="max-height: 20em">
                            <div class="card-body">
                            `;
                            for(let i = 0; i < boughtFurnitures.length; i++) {
                                newPage +=  await getBoughtGeneralFragment(boughtFurnitures[i]);
                            }
                            newPage += `
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row justify-content-center pb-5">
                    <div class="col-md-12 col-lg-6 py-3">
                        <u><h4>Derniers utilisateurs inscrits</h4></u>
                        <div class="card border-dark overflow-auto" style="max-height: 20em">
                            <div class="card-body">
    `;
    users.forEach((user) => {
        newPage += getUsersGeneralFragment(user);
    });
    newPage += `
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    `;

    innerPage.innerHTML = newPage;
};

const onUser = async () => {
    console.log("onUser");
    let innerPage = document.getElementById("nav-user");

    let users = await getAllUsers();
    
    let newPage = `<div class="container">`;

    for(let i=0; i<users.length; i++){
        let sales = await callAPI(
            API_SALE_URL+"user/"+users[i].id,
            "GET",
            getUserSessionData().token,
            undefined
        );
        newPage += `
        <div class="row justify-content-center align-items-center h-100 border border-dark rounded my-4">
            <div class="container-fluid">
                <div class="row m-2">
                    <div class="col mx-1">
                        <input class="form-control-plaintext " type="text" placeholder="Prénom : ${users[i].firstName}" readonly>
                    </div>
                    <div class="col mx-1">
                        <input class="form-control-plaintext " type="text" placeholder="Nom : ${users[i].name}" readonly>
                    </div>
                    <div class="col mx-1">
                        <input class="form-control-plaintext " type="text" placeholder="Pseudo : ${users[i].pseudo}" readonly>
                    </div>
                </div>
                <div class="row m-2">
                    <div class="col mx-1">
                        <input class="form-control-plaintext " type="text" placeholder="Email : ${users[i].email}" readonly>
                    </div>
                    <div class="col mx-1">
                        <input class="form-control-plaintext " type="text" placeholder="Date d'inscription : ${users[i].registrationDate.dayOfMonth}/${users[i].registrationDate.monthValue}/${users[i].registrationDate.year} " readonly>
                    </div>
                </div>
                <div class="row m-2">
                    <!-- récuperer tous les meubles pour compter combien vendus et achetés par l'utilisateur courant -->
                    <div class="col">Nombre de meubles achetés : ${sales.length}</div>
                    <button class="btn btn-info" type="button" data-toggle="collapse" data-target="#collapseExample${users[i].pseudo}" aria-expanded="false" aria-controls="collapseExample${users[i].pseudo}">
                        Plus d'info
                    </button>
                </div>
                <div class="row justify-content-center align-items-center">
                    <div class="collapse w-75 pb-3" id="collapseExample${users[i].pseudo}">
                        
                        <p class="align-items-left">Meubles Achetes :</p>
                        <!-- debut meuble -->
                        `;
        if(sales.length>0){
            for(let i = 0; i<sales.length; i++){

                let furniture = await callAPI(
                    "/api/furnitures/"+sales[i].idFurniture,
                    "GET",
                    undefined,
                    undefined
                );
                newPage += `
                        <div class="row justify-content-center align-items-center h-100">
                            <form class="container-fluid pt-3 d-flex">
                                <div class="col-3 col-sm-3 col-md-4 col-lg-3 furnitures-photo-border">`;
                let photos = await callAPI("/api/photos/furniture/"+furniture.idFurniture,"GET",getUserSessionData().token,undefined);
                photos.forEach(photo => {
                    if(photo.prefered){
                        newPage+=`<img id="image_test" src="${photo.base64Value}" alt="photo_meuble${photo.id_furniture}" class="img-fluid">`;
                    }
                });      
                newPage+=`
                                </div>
                                <div class="col-5 col-sm-5 col-md-6 col-lg-7 furnitures-description-border">
                                    <h5 class="px-2 pt-3">Description :</h5>
                                    <p class="px-2"> ${furniture.description}</p>
                
                                    <h5 class="px-2 pt-3">Type : ${monType.get(furniture.idType)}</h5>
                
                                    <h5 class="px-2 pt-3">État : ${monEtat.get(furniture.condition)}</h5>
                                </div>
                                <div class="col-4 col-sm-4 col-md-2 col-lg-2 furnitures-price-border text-center">
                                    <div>
                                        <h5 class="px-2 pt-3">Prix de vente:</h5>
                                        <p class="px-2 text-wrap">${furniture.sellingPrice.toFixed(2)}€</p>
                                    </div>
                                    <div>
                                        <h5 class="px-2 pt-3">Prix de vente final:</h5>
                                        <p class="px-2 text-wrap">${sales[i].lastSellingPrice}€</p>
                                    </div>
                                </div>
                            </form>
                        </div>
                `;
            }
        } else {
            newPage +=`<p>Aucun meubles n'a été acheté par cet utilisateur</p>`
        }
        newPage +=`
                            <!-- fin meuble -->
                        </div>
                    </div>
                </div>
            </div>
        `;
    }
    
    newPage+=`</div>`;
    innerPage.innerHTML = newPage;
};

const onFurniture = async () => {
    console.log("onFurniture");
    let innerPage = document.getElementById("nav-furniture");

    let furnitures = await getAllFurnitures();
    let photos = await getAllPhotos();
    
    let newPage = `<div class="container">`;
    for(let i = 0; i < furnitures.length; i++) {
        newPage += await getFurnitureFragment(furnitures[i], photos);
    }
    newPage+=`</div>`;
    
    innerPage.innerHTML = newPage;
    let forms = document.querySelectorAll(".saveFormClass");
    forms.forEach(form => {
        form.addEventListener("submit",onSave);
    });
    
    let saleForms = document.querySelectorAll(".saleFormClass");
    saleForms.forEach(form => {
        form.addEventListener("submit",saleApprouved);
    });

    let photoForms = document.querySelectorAll(".photosFormClass");
    photoForms.forEach(form => {
        form.addEventListener("submit",photosChanges);
    })
};

/*
*Creation d'une vente.
*/
const saleApprouved = async (e) => {
    e.preventDefault();
    let user = await getSpecificUserById(e.target[0].value);
    let furniture = await getSpecificFurnitureById(e.target[2].dataset.idFurniture);
    if((user.role=="client") && (e.target[2].dataset.sellingPrice>e.target[1].value)){
        let errorBox = document.getElementById("messageBoard"+e.target[2].dataset.idFurniture);
        errorBox.innerText = "Le client n'est pas un antiquaire, il n'a donc pas droit à une ristourne";
        errorBox.classList.remove("d-none");
        console.log(errorBox); 
    }else if(user.role=="client" && furniture.condition!="propose"){
        let errorBox = document.getElementById("messageBoard"+e.target[2].dataset.idFurniture);
        errorBox.innerText = "Un client ne peut acheter que des meubles proposés a la vente";
        errorBox.classList.remove("d-none");
        console.log(errorBox); 
    }else if (user.role=="antiquaire" && furniture.condition!="restaure" && furniture.condition!="depose" && furniture.condition!="propose" && furniture.condition!="achete"){
        let errorBox = document.getElementById("messageBoard"+e.target[2].dataset.idFurniture);
        errorBox.innerText = "Un antiquaire ne peut acheter un meuble dans cet état";
        errorBox.classList.remove("d-none");
        console.log(errorBox); 
    }else if(e.target[1].value<=0) {
        let errorBox = document.getElementById("messageBoard"+e.target[2].dataset.idFurniture);
        errorBox.innerText = "Le prix de vente doit etre superieur a 0";
        errorBox.classList.remove("d-none");
        console.log(errorBox);
    }
    else{
        let sale = {
            idUser : user.id,
            idFurniture : e.target[2].dataset.idFurniture,  
            lastSellingPrice : e.target[1].value
        }
        let returnedSale = await callAPI (
            API_SALE_URL,
            "POST",
            getUserSessionData().token,
            sale
        );
        document.getElementById("detailsModal"+e.target[2].dataset.idFurniture).classList.remove("show");
        document.querySelector("body").classList.remove("modal-open");
        document.querySelector(".modal-backdrop").remove();
        onFurniture();
    }    
}

const photosChanges = async (e) => {
    e.preventDefault();
    console.log(e);
    let inputVisible = document.querySelectorAll(".checkboxClass"+e.target[0].value);
    let inputPrefere = document.querySelectorAll(".radioClass"+e.target[0].value);
    for(let i=0; i<inputVisible.length; i++) {
        let photo = {
            id_photo : inputVisible[i].dataset.idphoto,
            visible : inputVisible[i].checked,
            prefered : inputPrefere[i].checked
        }
        await updatePhoto(photo);
        document.getElementById("photosModal"+e.target[0].value).classList.remove("show");
        document.querySelector("body").classList.remove("modal-open");
        document.querySelector(".modal-backdrop").remove();
        onFurniture();
    }
    
}

const onSave = async (e) => {
    e.preventDefault();
    if(e.target[1].value < 0){
        e.target[1].classList.add("border-danger");
    }else{
        let furniture = {
            id_furniture : e.target[3].dataset.idFurniture,
            id_type : e.target[3].dataset.idType,
            description : e.target[3].dataset.description,
            condition : e.target[1].value,
            purchase_price : e.target[2].dataset.purchasePrice,
            selling_price : e.target[2].value,
        }
        await updateFurniture(furniture);
        onFurniture();
    }
};

const getSelledGeneralFragment = async (dataFurniture) => {
    let selledFrag = `
        <div class="row py-2">
            <div class="col col-sm-6 col-md-4 col-lg-3 furnitures-photo-border col d-flex align-items-center justify-content-center">`;
                let photo = await callAPI(
                    API_PREF_PHOTO_URL.replace("$", dataFurniture.idFurniture),
                    "GET",
                    undefined,
                    undefined,
                );
                if(photo.prefered == true) {
                    selledFrag += `
                    <img src="${photo.base64Value}" alt="meuble${dataFurniture.idFurniture}" class="img-fluid">`;
                }
                selledFrag += `
            </div>
            <div class="col col-sm-15 col-md-10 col-lg-7 furnitures-description-border">
                <h5 class="px-2 pt-3">Description :</h5>
                <p class="px-2"> ${dataFurniture.description}</p>
                <h5 class="px-2 pt-3">Type :</h5>
                <p class="px-2"> ${monType.get(dataFurniture.idType)}</p>
            </div>
            <div class="col col-sm-4 col-md-3 col-lg-2 furnitures-price-border text-center">
                <div>
                    <h5 class="px-2 pt-3">Prix de vente :</h5>
                    <p class="px-2"> ${dataFurniture.sellingPrice.toFixed(2)}€</p>
                </div>
            </div>
        </div>
    `;

    return selledFrag;
}

const getBoughtGeneralFragment = async (dataFurniture) => {
    let boughtFrag = `
        <div class="row py-2">
            <div class="col col-sm-6 col-md-4 col-lg-3 furnitures-photo-border col d-flex align-items-center justify-content-center">`;
                let photo = await callAPI(
                    API_PREF_PHOTO_URL.replace("$", dataFurniture.idFurniture),
                    "GET",
                    undefined,
                    undefined,
                );
                if(photo.prefered == true) {
                    boughtFrag += `
                    <img src="${photo.base64Value}" alt="meuble${dataFurniture.idFurniture}" class="img-fluid">`;
                }
                boughtFrag += `
            </div>
            <div class="col col-sm-15 col-md-10 col-lg-7 furnitures-description-border">
                <h5 class="px-2 pt-3">Description :</h5>
                <p class="px-2"> ${dataFurniture.description}</p>
                <h5 class="px-2 pt-3">Type :</h5>
                <p class="px-2"> ${monType.get(dataFurniture.idType)}</p>
            </div>
            <div class="col col-sm-4 col-md-3 col-lg-2 furnitures-price-border text-center">
                <div>
                    <h5 class="px-2 pt-3">Prix d'achat :</h5>
                    <p class="px-2"> ${dataFurniture.purchasePrice}€</p>
                </div>
            </div>
        </div>  
    `;

    return boughtFrag;
}

const getUsersGeneralFragment = (dataUser) => {
    let usersFrag = `
        <div class="border border-dark rounded my-3">
        <div class="row m-2">
            <div class="col mx-1">
                <input class="form-control-plaintext " type="text" placeholder="Prénom : ${dataUser.firstName}" readonly>
            </div>
            <div class="col mx-1">
                <input class="form-control-plaintext " type="text" placeholder="Nom : ${dataUser.name}" readonly>
            </div>
            <div class="col mx-1">
                <input class="form-control-plaintext " type="text" placeholder="Pseudo : ${dataUser.pseudo}" readonly>
            </div>
        </div>
        <div class="row m-2">
            <div class="col mx-1">
                <input class="form-control-plaintext " type="text" placeholder="Email : ${dataUser.email}" readonly>
            </div>
            <div class="col mx-1">
                <input class="form-control-plaintext " type="text" placeholder="Date d'inscription : ${dataUser.registrationDate.dayOfMonth}/${dataUser.registrationDate.monthValue}/${dataUser.registrationDate.year}" readonly>
            </div>
        </div>
        </div>
    `;

    return usersFrag;
}


/*
*
*
*Page Meuble
*
*
*
*/
const getFurnitureFragment = async (dataFurniture, photos) => {
    /*
    *
    * 
    * Debut affichage
    * 
    * 
    */
    let furnitureFrag = `
    <div class="row justify-content-center align-items-center h-100">
        <form class="container-fluid pt-3 d-flex saveFormClass" >
            <div class="col-3 col-sm-3 col-md-4 col-lg-3 furnitures-photo-border col d-flex align-items-center justify-content-center">`;
                let photoPref = await callAPI(
                    API_PREF_PHOTO_URL.replace("$", dataFurniture.idFurniture),
                    "GET",
                    undefined,
                    undefined,
                );
                if(photoPref.prefered == true) {
                    furnitureFrag += `
                    <img src="${photoPref.base64Value}" alt="meuble${dataFurniture.idFurniture}" class="img-fluid">`;
                }
                furnitureFrag += `
                <button class="btn btn-dark btn-block fix" type="button" data-toggle="modal" data-target="#photosModal${dataFurniture.idFurniture}">Paramètres photos</button>
            </div>
            <div class="col-5 col-sm-5 col-md-6 col-lg-7 furnitures-description-border">
                <h5 class="px-2 pt-3">Description :</h5>
                <p class="px-2"> ${dataFurniture.description}</p>

                <h5 class="px-2 pt-3">Type :</h5>
                <p class="px-2"> ${monType.get(dataFurniture.idType)}</p>

                <h5 class="px-2 pt-3">État : ${monEtat.get(dataFurniture.condition)}</h5>
                <p class="px-2">
                    
                    <select class="custom-select" name="conditionInput" data-idFurniture="${dataFurniture.idFurniture}">
    `;
    valEtat.forEach((etat) => {
        if(etat[0] === dataFurniture.condition){
            furnitureFrag+=`
                        <option value="${etat[0]}" title="${monEtat.get(dataFurniture.condition)}" selected>${etat[1]}</option>
            `;
        }
        else{
            furnitureFrag+=`
                        <option value="${etat[0]}" title="${monEtat.get(dataFurniture.condition)}">${etat[1]}</option>
            `;
        }
        
    });

    furnitureFrag+=`
                    </select>
                </p>
            </div>
            <div class="col-4 col-sm-4 col-md-2 col-lg-2 furnitures-price-border text-center">
                <div>
                    <h5 class="px-2 pt-3">Prix d'achat:</h5>
                    <p class="px-2 text-wrap">${dataFurniture.purchasePrice}€</p>
                </div>
                <div>
                    <h5 class="px-2 pt-3">Prix de vente:</h5>
                    <div class="input-group mb-3">
                        <input type="text" name="sellingPriceInput" class="form-control" placeholder="Prix de vente" value="${dataFurniture.sellingPrice.toFixed(2)}" data-purchase-price="${dataFurniture.purchasePrice}">
                        <div class="input-group-append">
                            <span class="input-group-text" id="basic-addon2">€</span>
                        </div>
                    </div>`;
                    if(dataFurniture.condition=="vendu"){
                        let sale = await callAPI(
                            API_SALE_URL+"furniture/"+dataFurniture.idFurniture,
                            "GET",
                            getUserSessionData().token,
                            undefined
                        )
                        furnitureFrag+=`             
                        <h5 class="px-2 pt-3">Le meuble a été vendu </h5>
                        <p class="px-2"> ${sale.lastSellingPrice.toFixed(2)}€</p>
                        `;
                    }
                    else{
                        furnitureFrag+=`
                        <button class="btn btn-success" type="submit" data-id-furniture="${dataFurniture.idFurniture}" data-id-type="${dataFurniture.idType}" data-description="${dataFurniture.description}">Sauvergarder</button>
                        <button class="btn btn-dark my-2" type="button" data-toggle="modal" data-target="#detailsModal${dataFurniture.idFurniture}">Vendre</button>
                        `;
                    }
                furnitureFrag+=`
                </div>
            </div>
        </form>
    </div>`;
    
    /*
    *
    * 
    * Modal vente
    * 
    * 
    */
    furnitureFrag+=`
    <!-- Modal Vente -->
    <div class="modal fade" id="detailsModal${dataFurniture.idFurniture}" >
        <div class="modal-dialog modal-dialog-centered modal-xl">
            <div class="modal-content">
                <!-- Header -->
                <div class="modal-header">
                    <button class="close" id="btnclose" data-dismiss="modal">&times;</button>
                </div>
                
                <!-- Body -->
                <div class="modal-body">
                    <div class="row justify-content-center align-items-center h-100">
                        <form class="container-fluid pt-3 d-flex saleFormClass">
                            <div class="col-3 col-sm-3 col-md-4 col-lg-3 furnitures-photo-border col d-flex align-items-center justify-content-center">`;
                            if(photoPref.prefered == true) {
                                furnitureFrag += `
                                <img src="${photoPref.base64Value}" alt="meuble${dataFurniture.idFurniture}" class="img-fluid">`;
                            }
                            furnitureFrag += `
                            </div>
                            <div class="col-5 col-sm-5 col-md-6 col-lg-7 furnitures-description-border">
                                <h5 class="px-2 pt-3">Description :</h5>
                                <p class="px-2"> ${dataFurniture.description}</p>
                    
                                <h5 class="px-2 pt-3">Type :</h5>
                                <p class="px-2"> ${monType.get(dataFurniture.idType)}</p>
                                <p>État : ${monEtat.get(dataFurniture.condition)}</p>
                                <h5 class="px-2 pt-3">Acheteur :</h5>
                                <div class="form-group pb-3">
                                    <input class="form-control" type="text" placeholder="Entrez le pseudo de l'acheteur" required="" />
                                </div>
                                <div class="alert alert-danger mt-2 d-none" id="messageBoard${dataFurniture.idFurniture}" role="alert"></div><br> 
                            </div>
                            <div class="col-4 col-sm-4 col-md-2 col-lg-2 furnitures-price-border text-center">
                                <div>
                                    <h5 class="px-2 pt-3">Prix d'achat:</h5>
                                    <p class="px-2 text-wrap">${dataFurniture.purchasePrice}€</p>
                                    <h5 class="px-2 pt-3">Prix de vente:</h5>
                                    <p class="px-2 text-wrap">${dataFurniture.sellingPrice.toFixed(2)}€</p>
                                </div>
                                <div>
                                    <h5 class="px-2 pt-3">Prix de vente final:</h5>
                                    <div class="input-group mb-3">
                                        <input type="text" name="sellingPriceInput" class="form-control" placeholder="Prix de vente final" value="${dataFurniture.sellingPrice}" data-purchase-price="${dataFurniture.purchasePrice}">
                                        <div class="input-group-append">
                                            <span class="input-group-text" id="basic-addon2">€</span>
                                        </div>
                                        <div class="row justify-content-center align-items-center h-100">
                                            <button class="btn btn-success m-5" id="btnConfirmer${dataFurniture.idFurniture}" type="submit" data-sale="dataSale" data-selling-price="${dataFurniture.sellingPrice}" data-id-furniture="${dataFurniture.idFurniture}">Confirmer la vente</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>`;


    /*
    *
    * 
    * Modal photo
    * 
    * 
    */  
    furnitureFrag+=`                     
    <!-- Modal Photos -->
    <div class="modal fade" id="photosModal${dataFurniture.idFurniture}">
        <div class="modal-dialog modal-dialog-centered modal-xl">
        <form class="photosFormClass">
            <input type="hidden" value="${dataFurniture.idFurniture}">
            <div class="modal-content">
                <!-- Header -->
                <div class="modal-header">
                    <button class="close" data-dismiss="modal">&times;</button>
                </div>
                <!-- Body -->
                <div class="modal-body">
                    <div id="carousel_id${dataFurniture.idFurniture}" class="carousel slide" data-ride="carousel">
                        <ul class="carousel-indicators">`;
                        let i = 0;
                        photos.forEach(photo => {
                            if(photo.idFurniture == dataFurniture.idFurniture) {
                                furnitureFrag += `
                                <li data-target="#carousel_id${dataFurniture.idFurniture}" data-slide-to="${i}" class="`; if(i == 0) { furnitureFrag += `active`;} furnitureFrag +=`"></li>`
                                i++;
                            }
                        });
                        furnitureFrag += `
                        </ul>
                        <div class="carousel-inner">`;
                            let j = 1;
                            photos.forEach(photo => {
                                if(photo.idFurniture == dataFurniture.idFurniture) {
                                    furnitureFrag += `
                                    <div class="carousel-item `; if(j == 1) { furnitureFrag += `active`;} furnitureFrag +=`">
                                        <div class="parent d-flex justify-content-center">
                                            <img src="${photo.base64Value}" alt="photo_${j}" width="1100" height="700">
                                            <div class="carousel-caption">
                                                <h6 class="text-light bg-dark">
                                                    <div class="row">
                                                        <div class="col form-check">
                                                            <input type="checkbox" class="form-check-input checkboxClass${dataFurniture.idFurniture}" data-idPhoto="${photo.id}" `;if(photo.visible == true){ furnitureFrag += `checked`; } furnitureFrag +=`><label class="form-check-label pl-2" for="visibleId">Visible</label>
                                                        </div>
                                                        <div class="col form-check">
                                                            <input type="radio" name="radioFurniture${dataFurniture.idFurniture}" class="form-check-input radioClass${dataFurniture.idFurniture}" data-idPhoto="${photo.id}" `;if(photo.prefered == true){ furnitureFrag += `checked`; } furnitureFrag +=`><label class="form-check-label pl-2" for="preferedId">Préférée</label>
                                                        </div>
                                                    </div>
                                                </h6>
                                            </div>
                                        </div>
                                    </div>`;
                                    j++;
                                }
                            });
                            furnitureFrag += `
                        </div>
                        <a class="carousel-control-prev" href="#carousel_id${dataFurniture.idFurniture}" data-slide="prev">
                            <span class="carousel-control-prev-icon"></span>
                        </a>
                        <a class="carousel-control-next" href="#carousel_id${dataFurniture.idFurniture}" data-slide="next">
                            <span class="carousel-control-next-icon"></span>
                        </a>
                    </div>
                </div>
                <!-- Footer -->
                <div class="modal-footer">
                    <a href="/upload?${dataFurniture.idFurniture}" class="btn btn-success">Ajouter une photo</a>
                    <button class="btn btn-dark" id="btnPhotos${dataFurniture.idFurniture}" type="submit" data-id-furniture="${dataFurniture.idFurniture}">Valider</button>
                </div>
            </div>
        </form>
        </div>
    </div>
    `;
    return furnitureFrag;
}

/*
 * API CALLS
*/
const getAllFurnitures = async () => {
    return await callAPI(
        API_FURNITURE_URL,
        "GET",
        undefined,
        undefined
    );
};

const getAllSelledFurnitures = async () => {
    return await callAPI(
        API_SELLED_FURNITURE_URL,
        "GET",
        undefined,
        undefined
    );
};

const getAllBoughtFurnitures = async () => {
    return await callAPI(
        API_BOUGHT_FURNITURE_URL,
        "GET",
        undefined,
        undefined
    );
};

const getAllUsers = async () => {
    return await callAPI(
        API_USER_URL,
        "GET",
        getUserSessionData().token,
        undefined
    );
};

const getAllUsersDesc = async () => {
    return await callAPI(
        API_DESC_USER_URL,
        "GET",
        getUserSessionData().token,
        undefined
    );
};

const getSpecificUserById = async (id) => {
    return await callAPI (
        API_USER_URL+id,
        "GET",
        undefined,
        undefined
    );
};

const getSpecificFurnitureById = async (id) =>{
    return await callAPI (
        API_FURNITURE_URL+id,
        "GET",
        undefined,
        undefined
    );
};

const getAllPhotos = async () => {
    return await callAPI(
        API_PHOTO_URL,
        "GET",
        undefined,
        undefined,
    );
};

const updateFurniture = async (data) => {
    return await callAPI(
        API_FURNITURE_URL,
        "PUT",
        getUserSessionData().token,
        data
    );
}

const updatePhoto = async (data) => {
    return await callAPI(
        API_PHOTO_URL,
        "PUT",
        getUserSessionData().token,
        data
    )
}

export default AdminPage;