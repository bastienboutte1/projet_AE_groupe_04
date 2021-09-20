import HomePage from "./HomePage.js";
import LoginPage from "./LoginPage.js";
import RegisterPage from "./RegisterPage";
import LogoutComponent from "./LogoutComponent.js";
import AdminPage from "./AdminPage.js";
import ErrorPage from "./ErrorPage.js";
import FurnituresPage from "./FurnituresPage";
import ProfilePage from "./ProfilePage.js";
import UploadPage from "./UploadPage.js";

const routes = {
  "/": HomePage,
  "/login": LoginPage,
  "/register": RegisterPage,
  "/logout": LogoutComponent,
  "/admin": AdminPage,
  "/error": ErrorPage,
  "/meubles": FurnituresPage,
  "/compte": ProfilePage,
  "/upload": UploadPage,
};

let navBar = document.querySelector("#navBar");
let componentToRender;

const Router = () => {
  /* manage to route the right component when the page is loaded */
  window.addEventListener("load", (e) => {
    console.log("onload page:", [window.location.pathname]);
    componentToRender = routes[window.location.pathname];
    if (!componentToRender)
      return ErrorPage(
        new Error("The " + window.location.pathname + " ressource does not exist.")
      );
    componentToRender();
  });

  /* manage click on the navBar*/
  const onNavigate = (e) => {
    let uri;
    if (e.target.tagName === "A") {
      e.preventDefault();
      uri = e.target.dataset.uri;
    }
    if (uri) {
      window.history.pushState({}, uri, window.location.origin + uri);
      componentToRender = routes[uri];
      if (routes[uri]) {
        componentToRender();
      } else {
        ErrorPage(new Error("The " + uri + " ressource does not exist"));
      }
    }
  };

  navBar.addEventListener("click", onNavigate);

  window.addEventListener("popstate", () => {
    componentToRender = routes[window.location.pathname];
    componentToRender();
  });
};

const RedirectUrl = (uri, data) => {
  window.history.pushState({}, uri, window.location.origin + uri);
  componentToRender = routes[uri];
  if (routes[uri]) {
    if(!data)
      componentToRender();
    else
      componentToRender(data);
    
  } else {
    ErrorPage(new Error("The " + uri + " ressource does not exist"));
  }
};

export { Router, RedirectUrl };
