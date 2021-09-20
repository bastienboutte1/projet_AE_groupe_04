import callAPI from "../utils/api.js";
import { getUserSessionData } from "./../utils/session.js";
import { RedirectUrl } from "./Router.js";

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

let idFurniture;

const UploadPage = async () => {
    idFurniture = location.search.substring(1);
    if(idFurniture==""){
        RedirectUrl("/admin","Aucun id n'a été fourni");
    }
    let userSession = getUserSessionData();
    if(!userSession.token){
        RedirectUrl("/login","Vous devez être connecté pour accéder à cette page!");
    } else {
        if(userSession.user.role!=="patron"){
            RedirectUrl("/login","Vous n'avez pas le droit d'accéder à cette page");
        }
        else {
            let page = document.getElementById("page");
            page.innerHTML = uploadPage;
            document.getElementById("uploadImage").addEventListener("change",imageUploaded);
            document.getElementById("uploadForm").addEventListener("submit",validForm);
        }
    }
};

const validForm = async (e) => {
    e.preventDefault();
    if(e.target[0].files.length > 0){ // files have been uploaded

        let photo = {
            id_furniture: idFurniture,
            base64_value: document.getElementById("uploadPreview").src,
            visible: e.target[1].checked,
            prefered: e.target[2].checked
        }
        await callAPI("/api/photos","POST",getUserSessionData().token,photo);
        RedirectUrl("/admin","L'upload de l'image s'est déroulé avec succès");
    }else {
        document.getElementById("alertDiv").innerText= "Vous n'avez pas uploadé de photo";
        document.getElementById("alertDiv").classList.remove("d-none");
    }
};

const imageUploaded = async (e) => {
    e.preventDefault();
    let file = e.target.files[0];
    document.getElementById('labelUpload').innerText = file.name;
    let reader = new FileReader();
    reader.onloadend = function() {
        let img = document.getElementById("uploadPreview");
        img.src=reader.result;
        document.getElementById("btnSubmit").attributes.removeNamedItem("disabled");
    }
    reader.readAsDataURL(file);
}


let uploadPage = `
    <div class="container">
        <div class="row justify-content-center mb-4" id ="titleUpload">
            <h2><u>Upload</u></h2>
        </div>
        <div class="alert alert-danger d-none" role="alert" id="alertDiv">
            Une erreur s'est produite
        </div>
        <div class="alert alert-success d-none" role="alert" id="successDiv">
            L'image a été ajouté avec succès
        </div>
        <div class="container">
            <form id="uploadForm">
                <div class="row h-100">
                    <div class="custom-file col">
                        <input type="file" class="custom-file-input" id="uploadImage" accept=".jpg, .jpeg, .png">
                        <label class="custom-file-label" for="uploadImage" id ="labelUpload">Choisir une image</label>
                        <br>
                        <div class="custom-file col">
                            <img src="" id="uploadPreview" class="img-fluid mt-2">
                        </div>
                    </div>
                        <input class="form-check-input" type="hidden" value="" id="visibleCheckbox">
                        <input class="form-check-input" type="hidden" id="visibleRadio" value="visible">
                    <div class="col-2">
                        <button class="btn btn-success" type="submit" id="btnSubmit" disabled>Submit form</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
`;

export default UploadPage;