import { RedirectUrl } from "./Router.js";
import Navbar from "./Navbar.js";
import {removeUserSessionData} from "../utils/session.js";

const Logout = () => {
  removeUserSessionData();
  // re-render the navbar for a non-authenticated user
  Navbar();
  RedirectUrl("/login"); 
};

export default Logout;