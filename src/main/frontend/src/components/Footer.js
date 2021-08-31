import React from "react";
import { Container, Typography, Link } from "@material-ui/core";
import useStyles from "../styles";

function Copyright() {
  const classes = useStyles();

  return (
    <Typography variant="h6" align="center" className={classes.footerText}>
      {"Copyright © "}
      <Link
        color="inherit"
        href={"https://www.linkedin.com/in/bernard-guiang"}
        target="_blank"
      >
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
