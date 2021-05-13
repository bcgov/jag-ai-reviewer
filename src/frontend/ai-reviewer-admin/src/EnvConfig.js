export const REACT_APP_AI_REVIEWER_API_URL = window.env
  ? window.env.REACT_APP_AI_REVIEWER_API_URL
  : process.env.REACT_APP_AI_REVIEWER_API_URL;

export const URL = window.env
  ? window.env.REACT_APP_KEYCLOAK_URL
  : process.env.REACT_APP_KEYCLOAK_URL;

export const REALM = window.env
  ? window.env.REACT_APP_KEYCLOAK_REALM
  : process.env.REACT_APP_KEYCLOAK_REALM;
  
export const CLIENT_ID = window.env
  ? window.env.REACT_APP_KEYCLOAK_CLIENT_ID
  : process.env.REACT_APP_KEYCLOAK_CLIENT_ID;
