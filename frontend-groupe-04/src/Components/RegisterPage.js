import { getUserSessionData, setUserSessionData } from "../utils/session.js";
import { RedirectUrl } from "./Router.js";
import callAPI from "../utils/api.js";

const API_BASE_URL = "/api/auths/";
const API_USER_URL = "/api/users/";

let registerPage = `
  <div class="container-fluid h-100 bg-light text-dark">
    <div class="row justify-content-center align-items-center">
      <h4 id="pageTitle" class="pt-3">Inscription</h4>
    </div>
    <hr/>
    <div class="row justify-content-center align-items-center h-100">
      <div class="col col-sm-12 col-md-8 col-lg-6">
      <form id="registerForm">
        <div class="row pb-3">
          <div class="col">
            <label for="fNameInput">Prénom* :</label>
            <input type="text" class="form-control" id="fNameInput" placeholder="Prénom" required="" />
          </div>
          <div class="col">
            <label for="lNameInput">Nom* :</label>
            <input type="text" class="form-control" id="lNameInput" placeholder="Nom" required="" />
          </div>
        </div>

        <div class="row pb-3">
          <div class="col">
            <label for="pseudoInput">Pseudo* :</label>
            <input class="form-control" id="pseudoInput" type="text" placeholder="Pseudo" required="" />
          </div>
          <div class="col">
            <label for="emailInput">Email* :</label>
            <input class="form-control" id="emailInput" type="text" placeholder="Email" required="" />
          </div>
        </div>

        <div class="row pb-3">
          <div class="col">
            <label for="passwordInput">Mot de passe* :</label>
            <input class="form-control" id="passwordInput" type="password" name="password" placeholder="Mot de passe" required=""  />
          </div>
          <div class="col">
            <label for="passwordConfirmationInput">Confirmation mot de passe* :</label>
            <input class="form-control" id="passwordConfirmationInput" type="password" name="password" placeholder="Mot de passe" required=""  />
          </div>
        </div>

        <div class="row pb-3">
          <div class="col">
            <label for="streetInput">Rue* :</label>
            <input class="form-control" id="streetInput" type="text" placeholder="Rue" required="" />
          </div>
          <div class="col">
            <label for="numberInput">Numéro* :</label>
            <input class="form-control" id="numberInput" type="text" placeholder="Numéro" required="" />
          </div>
          <div class="col">
            <label for="box">Boîte :</label>
            <input class="form-control" id="boxInput" type="text" placeholder="Boîte"/>
          </div>
        </div>
        <div class="row pb-3">
          <div class="col">
            <label for="zipInput">Code postal* :</label>
            <input class="form-control" id="zipInput" type="text" placeholder="Code postal" required="" />
          </div>
          <div class="col">
            <label for="cityInput">Commune* :</label>
            <input class="form-control" id="cityInput" type="text" placeholder="Commune" required="" />
          </div>
          <div class="col">
            <label for="countryInput">Pays* :</label>
            <input class="form-control" id="countryInput" type="text" placeholder="Pays" required="" />
          </div>
        </div>

        <div class="row justify-content-center align-items-center h-100">
          <button class="btn btn-success" id="btn" type="submit">S'inscrire</button>
        </div>
        <!-- Create an alert component with bootstrap that is not displayed by default-->
        <div class="alert alert-danger mt-2 d-none" id="messageBoard"></div><br>
        
      </form>
      </div>
    </div>
  </div> 
`;


const RegisterPage = () => {
  if(!getUserSessionData){
    RedirectUrl("/");
  }

  let page = document.querySelector("#page");
  page.innerHTML = registerPage;

  let registerForm = document.querySelector("#registerForm");
  let passwordValidationForm = document.getElementById("passwordConfirmationInput");
  passwordValidationForm.addEventListener("input",checkPasswordMatch);
  registerForm.addEventListener("submit", onRegister);
};


const onRegister = async (e) => {
  e.preventDefault();
  let errorBox = document.getElementById("messageBoard");

  //verify if inputs are not empty
  if(!validInputs()){
    let err = {
      "message" : "Veuillez remplir tous les champs obligatoires"
    }
    console.error("RegisterPage::onRegister", err);
    errorBox.innerText = err.message;
    errorBox.classList.remove("d-none");
    return;
  }
  errorBox.classList.add("d-none");

  if(!isPseudoAvailable()){
    invalidInput("pseudoInput");
    let err = {
      "message" : "Le pseudo est déjà utilisé par un autre utilisateur"
    }
    errorBox.innerText = err.message;
    errorBox.classList.remove("d-none");
    return;
  }
  else{
    validInput("pseudoInput");
    errorBox.classList.add("d-none");
  
    if(!isEmailAvailable()){
      invalidInput("emailInput");
      let err = {
        "message" : "L'email est déjà utilisé par un autre utilisateur"
      }
      console.error("RegisterPage::onRegister", err);
      errorBox.innerText = err.message;
      errorBox.classList.remove("d-none");
      return;
    }
    else{
      validInput("emailInput");
      errorBox.classList.add("d-none");
      
      let data = {
        //user data
        fName: document.getElementById("fNameInput").value.trim(),
        lName: document.getElementById("lNameInput").value.trim(),
        pseudo: document.getElementById("pseudoInput").value.trim(),
        email: document.getElementById("emailInput").value.trim(),
        password: document.getElementById("passwordInput").value,
        
        //address data
        street: document.getElementById("streetInput").value.trim(),
        number: document.getElementById("numberInput").value.trim(),
        box: document.getElementById("boxInput").value.trim(),
        zip: document.getElementById("zipInput").value.trim(),
        city: document.getElementById("cityInput").value.trim(),
        country: document.getElementById("countryInput").value.trim()
      }

      try {
        let newUser = await callAPI(
          API_BASE_URL+"register",
          "POST",
          undefined,
          data
        );
        onUserRegister(newUser);
      } catch(err) {
        console.error(err);
      }
      
    }
  }
  return;
};

const checkPasswordMatch = () => {
  let passwordForm = document.getElementById("passwordInput");
  let passwordValidationForm = document.getElementById("passwordConfirmationInput");
  if(passwordForm.value.trim() === passwordValidationForm.value.trim()){
    validInput("passwordInput");
    validInput("passwordConfirmationInput");
    return true;
  } else{
    invalidInput("passwordInput");
    invalidInput("passwordConfirmationInput");
    return false;
  }
};

const validInputs = () => {
  let isValid = true;
  let inputs = document.querySelectorAll("input");
  inputs.forEach(element => {
    if(element.value.trim()=="") {
      if(element.id!=="boxInput") {
        invalidInput(element.id);
        isValid = false;
      }
    }
    else {
      validInput(element.id);
    }
  });  
  return isValid;
}

const isPseudoAvailable = async () => {
  let pseudoInput = document.getElementById("pseudoInput");
  let pseudo = {
    "pseudo": pseudoInput.value.trim()
  };
  try {
    fetch(API_USER_URL + "pseudoAvailable", {
      method: "POST",
      body: JSON.stringify(pseudo),
      headers: {
        "Content-Type": "application/json",
      },
    })
    .then((response) => {
      if (response.ok){
        return true;
      }
      else {
        throw new Error("Error code : " + response.status + " : " + response.statusText);
      }
    });
  } catch(err) {
    console.error("RegisterPage::onRegister", err);
    let errorBox = document.getElementById("messageBoard");
    errorBox.innerText = "Le pseudo est déjà utilisé par un autre utilisateur";
    errorBox.classList.remove("d-none");
    invalidInput("pseudoInput");
    return false;
  }
}

const isEmailAvailable = async () => {
  let emailInput = document.getElementById("emailInput");
  let email = {
    "email": emailInput.value.trim()
  };
  try {
    fetch(API_USER_URL + "emailAvailable", {
      method: "POST",
      body: JSON.stringify(email),
      headers: {
        "Content-Type": "application/json",
      },
    })
    .then((response) => {
      if (response.ok){
        return true;
      } 
      else {
        throw new Error(response.status);
      }
    });

  } catch(err) {
    console.error("RegisterPage::onRegister", err);
    let errorBox = document.getElementById("messageBoard");
    if(err == "Error: 409"){
      errorBox.innerText = "L'email est déjà utilisé par un autre utilisateur";
    }
    else{
      errorBox.innerText = "Le format de l'email est incorrect";
    }
    errorBox.classList.remove("d-none");
    invalidInput("emailInput");
    return false;
  }
}

const onUserRegister = (userData) => {
  console.log("onUserRegister:", userData);
  //const user = { ...userData, isAutenticated: true };
  //setUserSessionData(user);
  // re-render the navbar for the authenticated user
  //Navbar();
  RedirectUrl("/login");
};

const validInput = (id) => {
  let elt = document.getElementById(id);
  elt.classList.remove('border-danger');
  elt.classList.add('border');
  elt.classList.add('border-success');
}

const invalidInput = (id) => {
  let elt = document.getElementById(id);
  elt.classList.remove('border-success');
  elt.classList.add('border');
  elt.classList.add('border-danger');
}

export default RegisterPage;
