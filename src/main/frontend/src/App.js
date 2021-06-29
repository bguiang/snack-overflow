import './App.css';
import React from "react";
import {
  BrowserRouter as Router,
  Switch,
  Route,
  Link
} from "react-router-dom";
import Navbar from './components/Navbar'
import Snacks from './components/Snacks';
import Snack from './components/Snack';
import {Container} from '@material-ui/core'

function App() {
  return (
    <Router>
      <Navbar/>
      <main>
        <Container maxWidth="md">
          <Switch>
            <Route path="/snacks/:id">
              <Snack />
            </Route>
            <Route path="/snacks">
              <Snacks />
            </Route>
            <Route path="/about">
              <About />
            </Route>
            <Route path="/contact">
              <Contact />
            </Route>
            <Route path="/">
              <Home />
            </Route>
          </Switch>
        </Container>
      </main>
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
