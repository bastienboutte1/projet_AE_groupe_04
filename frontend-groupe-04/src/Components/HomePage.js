import { getUserSessionData } from '../utils/session';
import callAPI from '../utils/api';

let page = document.querySelector("#page");

const API_PHOTO_URL = "/api/photos/visible";
const API_FURNITURE_URL ="/api/furnitures/";

const loadData = async () => {
  let visiblePhotos = await callAPI(
    API_PHOTO_URL,
    "GET",
    undefined,
    undefined,
  );

  let homePage = `
    <div class="container-fluid h-100 bg-light text-dark">
      <div class="row justify-content-center align-items-center">
        <h4 id="pageTitle" class="pt-3">Bienvenue sur Satchold</h4>
      </div>
      <div class="row justify-content-center align-items-center">
        <h5>Trouvez le meuble qui vous correspond</h5>
      </div>
      <hr/>
      </br>
      <div class="row justify-content-center align-items-center">
        <div id="carousel_id" class="carousel slide" data-ride="carousel" data-interval="5000">
          <ul class="carousel-indicators">`;
      
          displayphoto(visiblePhotos,homePage);
          

};

const HomePage = () => {
  loadData();
}


  
const displayphoto = async (visiblePhotos,homePage) =>{
  let i = 0;
  visiblePhotos.forEach(photo => {
    homePage += `
    <li data-target="#carousel_id" data-slide-to="${i}" class="`; if(i == 0) { homePage += `active`;} homePage +=`"></li>`;
    i++;
  });
  homePage += `
  </ul>
  <div class="carousel-inner">`;
    for (let j = 0; j< visiblePhotos.length; j++){
      let meuble = await callAPI (
        API_FURNITURE_URL+visiblePhotos[j].idFurniture,
        "GET",
        undefined,
        undefined
      )
      homePage += `
      <div class="carousel-item `; if(j == 0) { homePage += `active`;} homePage +=`">
        <div class="parent d-flex justify-content-center">
          <img src="${visiblePhotos[j].base64Value}" alt="meuble_vis_${j}" width="1100" height="600">
          <div class="carousel-caption">
            <h3 class="text-light bg-dark ">${meuble.description}</h3>
          </div>
        </div>
      </div>`;
    }
    homePage += `
  </div>
  <a class="carousel-control-prev" href="#carousel_id" data-slide="prev">
    <span class="carousel-control-prev-icon"></span>
  </a>
  <a class="carousel-control-next" href="#carousel_id" data-slide="next">
    <span class="carousel-control-next-icon"></span>
  </a>
</div>
</div>
</div>
`;
page.innerHTML = homePage;
}
 


export default HomePage;