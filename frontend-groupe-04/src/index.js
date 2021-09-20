import {RedirectUrl, Router} from "./Components/Router.js";
import Navbar from "./Components/Navbar.js";
import "./stylesheets/style.css";
import 'bootstrap/dist/css/bootstrap.css';
import 'bootstrap';
import { getUserSessionData, setUserSessionData } from "./utils/session.js";
import callAPI from "./utils/api.js";

let init = async () => {
    let userSession = getUserSessionData();
    if(userSession){
        try{
            let newUser = await callAPI(
                "/api/auths/me",
                "GET",
                userSession.token,
                undefined);
            setUserSessionData(newUser);
        }
        catch(err) {
            RedirectUrl("/logout");
        }
    }
}

init();
Navbar();
Router();

