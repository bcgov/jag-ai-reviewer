import React, { useState, useEffect } from "react";
import PropTypes from "prop-types";
import Keycloak from "keycloak-js";
import { url, realm, clientId } from "EnvConfig";

const KEYCLOAK = { realm, url, clientId };

export default function AuthenticationGuard({ children }) {
  const [authedKeycloak, setAuthedKeycloak] = useState(null);
  const keycloak = Keycloak(KEYCLOAK);

  async function keycloakInit() {
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

  return (
    <>
      {authedKeycloak && children}
      {!authedKeycloak && null}
    </>
  );
}

AuthenticationGuard.propTypes = {
  children: PropTypes.element,
};
