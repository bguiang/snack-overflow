import "./App.css";
import { useState } from "react";
import "@fontsource/roboto";
import { BrowserRouter as Router, Switch, Route, Link } from "react-router-dom";
import Navbar from "./components/Navbar";
import Snacks from "./components/Snacks";
import Snack from "./components/Snack";
import Footer from "./components/Footer";
import Login from "./components/Login";
import SignUp from "./components/SignUp";
import { CssBaseline, Container } from "@material-ui/core";
import useStyles from "./styles";

function App() {
  const classes = useStyles();
  const [user, setUser] = useState({ user: {} });

  return (
    //<userContext.Provider value={user}>
    <Router>
      <div className={classes.app}>
        <CssBaseline />
        <Navbar />
        <main className={classes.main}>
          <Container className={classes.container} maxWidth="lg" flexGrow={1}>
            <Switch>
              <Route path="/snacks/:id">
                <Snack />
              </Route>
              <Route path="/about">
                <About />
              </Route>
              <Route path="/contact">
                <Contact />
              </Route>
              <Route path="/login">
                <Login />
              </Route>
              <Route path="/signup">
                <SignUp />
              </Route>
              <Route path="/">
                <Snacks />
              </Route>
            </Switch>
          </Container>
        </main>
        <footer className={classes.footer}>
          <Container>
            <Footer />
          </Container>
        </footer>
      </div>
    </Router>
    //</userContext.Provider>
  );
}

function About() {
  return <h2>About</h2>;
}

function Contact() {
  return <h2>Contact</h2>;
}

export default App;
