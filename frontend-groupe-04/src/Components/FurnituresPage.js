import callAPI from '../utils/api';

const API_FURNITURE_URL = "/api/furnitures/";
const API_PHOTO_URL = "/api/photos/visible";
const API_PREF_PHOTO_URL = "/api/photos/furniture/$/prefered/"
const API_TYPE_FURNITURE_URL = "/api/type-furnitures";

let page = document.querySelector("#page");

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

const loadFurnitures = async() => {

    let furnitures = await callAPI(
        API_FURNITURE_URL,
        "GET",
        undefined,
        undefined,
    );

    let visiblePhotos = await callAPI(
        API_PHOTO_URL,
        "GET",
        undefined,
        undefined,
    );

    let furnituresPage = `
        <div class="container-fluid h-100 bg-light text-dark">
            <div class="row justify-content-center align-items-center">
                <h4 id="pageTitle" class="pt-3">Nos Meubles</h4>
            </div>
            <hr/>`;
    let modalHtml = ``;
    
        for(let i = 0; i < furnitures.length; i++) {
            let furniture = furnitures[i];
        if(furniture.sellingPrice > 0 && furniture.condition === "propose") {
            furnituresPage += `
    <div class="row justify-content-center align-items-center h-100 pb-4">
        <div class="container py-1 px-4 d-flex">
            <div class="col col-sm-6 col-md-4 col-lg-3 furnitures-photo-border col d-flex align-items-center justify-content-center">`;
            let prefPhoto = await callAPI(
                API_PREF_PHOTO_URL.replace("$", furniture.idFurniture),
                "GET",
                undefined,
                undefined,
            );
            if(prefPhoto.prefered == true) {
                furnituresPage += `
                <img src="${prefPhoto.base64Value}" alt="meuble${furniture.idFurniture}" class="img-fluid">`;
            }
            furnituresPage += `
            </div>
            <div class="col col-sm-15 col-md-10 col-lg-7 furnitures-description-border">
                <h5 class="px-2 pt-3">Description :</h5>
                <p class="px-2">${furniture.description}</p>
                <h5 class="px-2 pt-3">Type :</h5>
                <p class="px-2">${monType.get(furniture.idType)}</p>
            </div>
            <div class="col col-sm-4 col-md-3 col-lg-2 furnitures-price-border text-center">
                <div>
                    <h5 class="px-2 pt-3">Prix :</h5>
                    <p class="px-2"> ${furniture.sellingPrice.toFixed(2)}€</p>
                </div>
                <button class="btn btn-dark my-5" data-toggle="modal" data-target="#detailsModal${furniture.idFurniture}">Plus de photos</button>
            </div>
        </div>
    </div>`;

            modalHtml += `
    <div class="modal fade" id="detailsModal${furniture.idFurniture}">
        <div class="modal-dialog modal-dialog-centered modal-xl">
            <div class="modal-content">
                <!-- Header -->
                <div class="modal-header">
                    <h4 class="modal-title">${furniture.description}</h4>
                    <button class="close" data-dismiss="modal">&times;</button>
                </div>
                <!-- Body -->
                <div class="modal-body">
                    <div id="carousel_id${furniture.idFurniture}" class="carousel slide" data-ride="carousel">
                        <ul class="carousel-indicators">`;
                        let i = 0;
                        visiblePhotos.forEach(photo => {
                            if(photo.idFurniture == furniture.idFurniture) {
                                modalHtml += `
                                <li data-target="#carousel_id${furniture.idFurniture}" data-slide-to="${i}" class="`; if(i == 0) { modalHtml += `active`;} modalHtml +=`"></li>`
                                i++;
                            }
                        });
                        modalHtml += `
                        </ul>
                        <div class="carousel-inner">`;
                            let j = 1;
                            visiblePhotos.forEach(photo => {
                                if(photo.idFurniture == furniture.idFurniture) {
                                    modalHtml += `
                                    <div class="carousel-item `; if(j == 1) { modalHtml += `active`;} modalHtml +=`">
                                        <div class="parent d-flex justify-content-center">
                                            <img src="${photo.base64Value}" alt="photo_${j}" width="1100" height="700">
                                        </div>
                                    </div>`;
                                    j++;
                                }
                            });
                            modalHtml += `
                        </div>
                        <a class="carousel-control-prev" href="#carousel_id${furniture.idFurniture}" data-slide="prev">
                            <span class="carousel-control-prev-icon"></span>
                        </a>
                        <a class="carousel-control-next" href="#carousel_id${furniture.idFurniture}" data-slide="next">
                            <span class="carousel-control-next-icon"></span>
                        </a>
                    </div>
                </div>
                <!-- Footer -->
                <div class="modal-footer">
                    <h3>Prix : ${furniture.sellingPrice.toFixed(2)} €</h3>
                    <button class="btn btn-dark disabled">Acheter</button>
                </div>
            </div>
        </div>
    </div>
    `;
        }
        }
        let pageHtml = furnituresPage + modalHtml + `
        </div>   
        `;
        page.innerHTML = pageHtml;
}


const FurnituresPage = () => {  
    loadFurnitures();
};

export default FurnituresPage;