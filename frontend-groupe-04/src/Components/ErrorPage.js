import PrintError from "./PrintError.js";

const ErrorPage = (err) => {
  // deal with page title
  let page = document.querySelector("#page");
  page.innerHTML="";
  let title = document.createElement("h4");
  title.id = "pageTitle";
  title.innerText = "Error";
  page.appendChild(title);
  
  PrintError(err);
};

export default ErrorPage;
