const STORE_NAME = "user";
const REMEMBER = "remember";

/*
  General Access (get, set, remove)
*/
const getUserSessionData = () => {
  const rememberMe = getRememberData();
  if (rememberMe == null) return;
  
  if(rememberMe){
    return getStorageData();
  } else {
    return getSessionData();
  }
};

const setUserSessionData = (user) => {
  const rememberMe = getRememberData();

  if(rememberMe == null){
    console.error("setUserSessionData() => soucis avec le remember");
    return;
  }

  if(rememberMe){
    return setStorageData(user);
  } else {
    return setSessionData(user);
  }
};

const removeUserSessionData = () => {
  removeStorageData();
  removeSessiontData();
  removeRememberData();
};


/* 
  LocalStorage (get, set, remove)
*/
const getStorageData = () => {
  const retrievedUser = localStorage.getItem(STORE_NAME);
  if (!retrievedUser) return;
  return JSON.parse(retrievedUser);
};

const setStorageData = (user) => {
  const storageValue = JSON.stringify(user);
  localStorage.setItem(STORE_NAME, storageValue);
};

const removeStorageData = () => {
  localStorage.removeItem(STORE_NAME);
};


/*
  SessionStorage (get, set, remove)
*/
const getSessionData = () => {
  const retrievedUser = sessionStorage.getItem(STORE_NAME);
  if(!retrievedUser) return;
  return JSON.parse(retrievedUser);
};

const setSessionData = (user) => {
  const storageValue = JSON.stringify(user);
  sessionStorage.setItem(STORE_NAME, storageValue);
};

const removeSessiontData = () => {
  sessionStorage.removeItem(STORE_NAME);
};


/*
  RememberData (get, set, remove)
*/
const getRememberData = () => {
  const rememberMe = localStorage.getItem(REMEMBER); 
  if(!rememberMe) return;
  return JSON.parse(rememberMe);
};

const setRememberData = (bool) => {
  localStorage.setItem(REMEMBER, bool);
}

const removeRememberData = () => {
  localStorage.removeItem(REMEMBER);
};

export {
  getUserSessionData,
  setUserSessionData,
  removeUserSessionData,
  setRememberData,
};
