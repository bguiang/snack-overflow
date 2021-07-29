import React from "react";
import { Container, Typography, Link } from "@material-ui/core";
import useStyles from "../styles";

function Copyright() {
  return (
    <Typography variant="body2" color="textSecondary" align="center">
      {"Copyright © "}
      <Link color="inherit" href="http://bernardguiang.com/">
        Bernard Guiang
      </Link>{" "}
      {new Date().getFullYear()}
      {"."}
    </Typography>
  );
}

const Footer = () => {
  const classes = useStyles();
  return (
    <footer className={classes.footer}>
      <Container>
        <div>
          <Copyright />
        </div>
      </Container>
    </footer>
  );
};

export default Footer;
