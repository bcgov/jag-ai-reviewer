import React, { useState, useEffect } from "react";
import PropTypes from "prop-types";
import Keycloak from "keycloak-js";
import {url, realm, clientId} from "EnvConfig";

const KEYCLOAK = {realm, url, clientId};

export default function AuthenticationGuard({children}) {
  const [authedKeycloak, setAuthedKeycloak] = useState(null);

  async function keycloakInit() {
    const keycloak = Keycloak(KEYCLOAK);

    await keycloak
      .init({
        checkLoginIframe: false,
      })
      .then((authenticated) => {
        if (authenticated) {
          keycloak.loadUserInfo();
          localStorage.setItem("jwt", keycloak.token);
          setAuthedKeycloak(keycloak);
        } else {
          keycloak.login();
        }
      });
  }

  useEffect(() => {
    keycloakInit();
  }, []);

  setInterval(() => {
    keycloak.updateToken(70).then(refreshed => {
      if (refreshed) {
        console.log("refreshed")
      }else{
        console.log("not refreshed")
      }
    })
    .catch(err => console.log(err))
  }, 6000)

  return (
    <>
      {authedKeycloak && children}
      {!authedKeycloak && null}
    </>
  )
}

AuthenticationGuard.propTypes = {
   children: PropTypes.element
};