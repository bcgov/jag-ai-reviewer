import React, { useState, useEffect } from "react";
import PropTypes from "prop-types";
import Keycloak from "keycloak-js";
import {URL, REALM, CLIENT_ID} from "EnvConfig";

const KEYCLOAK = {REALM, URL, CLIENT_ID};

export default function AuthenticationGuard({children}) {
  const [authedKeycloak, setAuthedKeycloak] = useState(null);

  async function keycloakInit() {
    const keycloak = Keycloak(KEYCLOAK);

    await keycloak
      .init({
        checkLoginIframe: false,
      })
      .success((authenticated) => {
        if (authenticated) {
          keycloak.loadUserInfo().success();

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

  return (
    <>
      {authedKeycloak ? children : null}
    </>
  )
}

AuthenticationGuard.propTypes = {
   children: PropTypes.element
};