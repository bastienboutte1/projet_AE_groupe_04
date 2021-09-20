let navBar = document.querySelector("#navBar");
import {getUserSessionData} from "../utils/session.js";
import logo_image from "../images/icons/ImageDevanturePourLogo.ico";

// destructuring assignment

const Navbar = () => {
  document.getElementById("logo_footer").setAttribute("src", logo_image);
  let navbar;
  let user = getUserSessionData();
  if (user) {
    if(user.user.role==="patron"){
      navbar = `
        <nav class="navbar navbar-expand-sm navbar-light bg-light mb-2" id="navBar">
          <div class="container-fluid">
            <div>
              <img src="${logo_image}" class="logo_satcho" alt="Logo Satcho">
              <a class="navbar-brand pl-4" href="#" data-uri="/">Satchold</a>
            </div>
            
            <div class="d-flex">
              <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#connectionNavbar" aria-controls="connectionNavbar" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
              </button>
              <div class="collapse navbar-collapse" id="connectionNavbar">
                <div class="navbar-nav">

                  <a class="nav-item nav-link px-4" href="#" data-uri="/meubles">Nos meubles</a>
                
                  <div class="btn-group">
                    <button type="button" class="btn btn-secondary dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                      ${user.user.pseudo}
                    </button>
                    <div class="dropdown-menu dropdown-menu-right">
                      <a class="dropdown-item" href="#" data-uri="/admin">Panel d'administration</a>
                      <a class="dropdown-item" href="#" data-uri="/logout">Déconnexion</a>
                    </div>
                  </div>
                
                </div>
              </div>
            </div>
          </div>
        </nav>
      `;
    }
    else{
      navbar = `
        <nav class="navbar navbar-expand-sm navbar-light bg-light mb-2" id="navBar">
          <div class="container-fluid">
            <div>
              <img src="${logo_image}" class="logo_satcho" alt="Logo Satcho">
              <a class="navbar-brand pl-4" href="#" data-uri="/">Satchold</a>
            </div>
            
            <div class="d-flex">
              <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#connectionNavbar" aria-controls="connectionNavbar" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
              </button>
              <div class="collapse navbar-collapse" id="connectionNavbar">
                <div class="navbar-nav">

                  <a class="nav-item nav-link px-4" href="#" data-uri="/meubles">Nos meubles</a>
                
                  <div class="btn-group">
                    <button type="button" class="btn btn-secondary dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                      ${user.user.pseudo}
                    </button>
                    <div class="dropdown-menu dropdown-menu-right">
                      <a class="dropdown-item" href="#" data-uri="/compte">Mon compte</a>
                      <a class="dropdown-item" href="#" data-uri="/logout">Déconnexion</a>
                    </div>
                  </div>
                
                </div>
              </div>
            </div>
          </div>
        </nav>
      `;
    }
  } else {
    navbar = `
    <nav class="navbar navbar-expand-sm navbar-light bg-light mb-2" id="navBar">
      <div class="container-fluid">
        <div>
          <img src="${logo_image}" class="logo_satcho" alt="Logo Satcho">
          <a class="navbar-brand pl-4" href="#" data-uri="/">Satchold</a>
        </div>
        
        <div class="d-flex">
          <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#connectionNavbar" aria-controls="connectionNavbar" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
          </button>
          <div class="collapse navbar-collapse" id="connectionNavbar">
            <div class="navbar-nav">
              <a class="nav-item nav-link px-4" href="#" data-uri="/meubles">Nos meubles</a>
              <a class="nav-item nav-link border rounded-pill" href="#" data-uri="/login">Se connecter / S'inscrire</a> 
            </div>
          </div>
        </div>
      </div>
    </nav>
    `;
  }

  return (navBar.innerHTML = navbar);
};

export default Navbar;
