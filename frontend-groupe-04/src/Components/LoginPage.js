import { getUserSessionData, setRememberData, setUserSessionData } from "../utils/session.js";
import { RedirectUrl } from "./Router.js";
import Navbar from "./Navbar.js";
import callAPI from "../utils/api.js";
const API_BASE_URL = "/api/auths/";

let loginPage = `
  <div class="container-fluid h-100 bg-light text-dark">
    <div class="row justify-content-center align-items-center">
      <h4 id="pageTitle" class="pt-3">Se connecter</h4>
    </div>
    <hr/>
    <div class="row justify-content-center align-items-center h-100">
      <div class="col col-sm-12 col-md-8 col-lg-6">
        <form class="">
          <div class="form-group pb-3">
            <label for="pseudo">Pseudo :</label>
            <input class="form-control" id="pseudo" type="text" placeholder="Entrez votre pseudo" required="" />
          </div>
          <div class="form-group pb-3">
            <label for="password">Mot de passe :</label>
            <input class="form-control" id="password" type="password" name="password" placeholder="Entrez votre mot de passe" required=""  />
          </div>
          <div class="form-group form-check">
            <input type="checkbox" class="form-check-input" id="rememberMeBtn">
            <label class="form-check-label" for="rememberMeBtn">Se souvenir de moi</label>
          </div>
          <div>
            <a class="btn btn-primary" id="btnRegister" data-uri="/register">S'inscrire</a>
            <button class="btn btn-success" id="btnLogin" type="submit">Se connecter</button>
          </div>
          <!-- Create an alert component with bootstrap that is not displayed by default-->
          <div class="alert alert-danger mt-2 d-none" id="messageBoard" role="alert"></div><br>
          <!-- Create an alert component with bootstrap that is not displayed by default-->
          <div class="alert alert-success mt-2 d-none" id="successBoard" role="alert">Votre compte a bien été créé, vous pouvez désormais vous connecter.</div><br>
        </form>
      </div>
    </div>
  </div>
  `;

const LoginPage = () => {  
  let page = document.querySelector("#page");
  page.innerHTML = loginPage;
  let registerBtn = document.getElementById('btnRegister');
  registerBtn.addEventListener("click", (e) => {
    e.preventDefault();
    RedirectUrl("/register");
  })
  let loginForm = document.querySelector("form");
  
  const user = getUserSessionData();
  if (user) {
    // re-render the navbar for the authenticated user
    Navbar();
    RedirectUrl("/");
  } 
  else {
    loginForm.addEventListener("submit", onLogin);
  }
};

const onLogin = async (e) => {
  e.preventDefault();
  let login = document.getElementById("pseudo");
  let password = document.getElementById("password");
  let rememberMeBtn = document.getElementById("rememberMeBtn");
  let remember = rememberMeBtn.checked;

  let user = {
    login: login.value,
    password: password.value,
  };

  try {
    const userLogged = await callAPI(
      API_BASE_URL + "login",
      "POST",
      undefined,
      user
    );
    if(userLogged.user.confirmedRegistration){
      onUserLogin(userLogged, remember);
    }
    else {
      let errorBox = document.getElementById("messageBoard");
      errorBox.innerText = "Votre compte n'a pas encore été confirmé par un admininistrateur";
      errorBox.classList.remove("d-none");
    }
  } catch (err) {
    console.error("LoginPage::onLogin", err);
    let errorBox = document.getElementById("messageBoard");
    errorBox.innerText = "Pseudo ou mot de passe incorrect";
    errorBox.classList.remove("d-none");
  }
};

const onUserLogin = (userData, remember) => {
  console.log("onUserLogin:", userData);
  const user = { ...userData, isAutenticated: true };
  setRememberData(remember);
  setUserSessionData(user);
  // re-render the navbar for the authenticated user
  Navbar();
  RedirectUrl("/");
};

export default LoginPage;
