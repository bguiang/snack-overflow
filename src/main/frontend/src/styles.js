import { makeStyles } from "@material-ui/core/styles";

// Theme colors
// Blue: #00B1C6 or rgb(0,177,198)
// Yellow: #F8EB37 or rgb(248,235,55)

// style hook
// uses the theme provider https://material-ui.com/customization/theming/
const useStyles = makeStyles((theme) => ({
  app: {
    minHeight: "100vh",
    display: "flex",
    flexDirection: "column",
  },
  appbar: {
    backgroundColor: "#F8EB37",
  },
  toolbarContainer: {
    paddingLeft: 0,
    paddingRight: 0,
  },
  toolbar: {
    [theme.breakpoints.up("sm")]: {
      paddingLeft: 0,
    },
    paddingRight: 0,
    flex: 1,
    display: "flex",
    alignItems: "center",
  },
  toolbarMenu: {
    [theme.breakpoints.down("sm")]: {
      display: "none",
    },
    [theme.breakpoints.up("sm")]: {
      display: "flex",
    },
    flex: 1,
    justifyContent: "flex-end",
    padding: "2px 4px",
    alignItems: "center",
  },
  toolbarMenuMobile: {
    [theme.breakpoints.down("sm")]: {
      display: "flex",
    },
    [theme.breakpoints.up("sm")]: {
      display: "none",
    },
    flex: 1,
    justifyContent: "flex-end",
    padding: "2px 4px",
    alignItems: "center",
  },
  socialsMenu: {
    [theme.breakpoints.down("sm")]: {
      display: "none",
    },
    [theme.breakpoints.up("sm")]: {
      display: "flex",
    },
    flex: 1,
    padding: "2px 4px",
    alignItems: "center",
  },
  logo: {
    height: "110px",
    paddingTop: 10,
    paddingBottom: 10,
  },
  logoContainer: {
    padding: "2px 4px",
    display: "flex",
  },
  shoppingCartButton: {
    [theme.breakpoints.down("sm")]: {
      paddingRight: 0,
    },
  },
  bottomToolbar: {
    backgroundColor: "#00B1C6",
  },
  toolbar2: {
    paddingTop: 10,
    paddingBottom: 10,
    flex: 1,
    display: "flex",
    alignItems: "center",
  },
  toolbar2Menu: {
    [theme.breakpoints.down("md")]: {
      display: "none",
    },
    [theme.breakpoints.up("md")]: {
      display: "flex",
    },
    flex: 1,
    justifyContent: "flex-start",
    padding: "2px 4px",
    alignItems: "center",
  },
  toolbar2MenuMobile: {
    [theme.breakpoints.down("md")]: {
      display: "flex",
    },
    [theme.breakpoints.up("md")]: {
      display: "none",
    },
    justifyContent: "flex-end",
    padding: "2px 4px",
    alignItems: "center",
  },
  toolbar2SearchContainer: {
    [theme.breakpoints.down("md")]: {
      flex: 1,
      marginRight: 36,
    },
    width: 400,
    padding: "2px 4px",
    display: "flex",
    alignItems: "center",
  },
  search: {
    marginLeft: theme.spacing(1),
    flex: 1,
  },
  main: {
    paddingTop: 186,
    backgroundImage: `url(${process.env.PUBLIC_URL + "/pattern.png"})`,
    display: "flex",
    flex: 1,
  },
  footer: {
    backgroundColor: "#00B1C6",
    height: 50,
  },
  shop: {
    flexGrow: 1,
  },
  shopItem: {
    height: 200,
    width: 150,
  },
  control: {
    padding: theme.spacing(2),
  },
  container: {
    backgroundColor: theme.palette.background.paper,
    padding: 20,
  },
}));

export default useStyles;
