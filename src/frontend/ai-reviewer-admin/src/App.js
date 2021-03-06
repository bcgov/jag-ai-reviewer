/* eslint-disable react/jsx-one-expression-per-line */
import React from "react";
import {
  useHistory,
  Switch,
  Route,
  BrowserRouter as Router,
} from "react-router-dom";
import { Header, Footer } from "shared-components";
import AuthenticationGuard from "domain/authentication/AuthenticationGuard"
import Container from "@material-ui/core/Container";
import NavDrawer from "components/nav-drawer/NavDrawer";
import DocumentTypeEditor from "domain/documents/DocumentTypeEditor";
import SimulateTransaction from "domain/simulate-transaction/SimulateTransaction"
import "./App.scss";

function App() {
  const header = {
    name: "AI Reviewer Admin Client",
    history: useHistory() || {},
  };

  return (
    <>
      <Header header={header} />
      <AuthenticationGuard>
        <Router>
          <Container className="content">
            <NavDrawer variant="permanent" />
            <NavDrawer variant="temporary" />

            <Switch>
              <Route exact path="/">
                <DocumentTypeEditor />
              </Route>
              <Route exact path="/simulateTransaction/">
                <SimulateTransaction />
              </Route>
            </Switch>
          </Container>
        </Router>
      </AuthenticationGuard>
      <Footer />
    </>
  );
}

export default App;
