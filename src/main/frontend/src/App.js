import './App.css';
import React from "react";
import '@fontsource/roboto';
import {
  BrowserRouter as Router,
  Switch,
  Route,
  Link
} from "react-router-dom";
import Navbar from './components/Navbar'
import Snacks from './components/Snacks';
import Snack from './components/Snack';
import {CssBaseline, Container} from '@material-ui/core'
import useStyles from './styles'

function App() {

  const classes = useStyles();


  return (
    <Router>
        <div className={classes.app}>
          <CssBaseline />
          <Navbar/>
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
                <Route path="/">
                  <Snacks />
                </Route>
              </Switch>
            </Container>
          </main>
          <footer className={classes.footer}>
            <Container>

            </Container>
          </footer>
        </div>
    </Router>
  );
}

function Home() {
  return <h2>Home</h2>;
}

function About() {
  return <h2>About</h2>;
}

function Contact() {
  return <h2>Contact</h2>;
}

export default App;
