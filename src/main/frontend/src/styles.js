import { makeStyles } from "@material-ui/core/styles";

// Theme colors
// theme.palette.primary.main - Blue: #00B1C6 or rgb(0,177,198)
// theme.palette.secondary.main - Yellow: #F8EB37 or rgb(248,235,55)
// Orange: #F3AB1C or rgb(243,171,28)

// style hook
// uses the theme provider https://material-ui.com/customization/theming/
const useStyles = makeStyles((theme) => ({
  appbar: {
    backgroundColor: theme.palette.secondary.main,
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
    backgroundColor: theme.palette.primary.main,
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
  toolbar2MenuItem: {
    fontWeight: "bold",
    color: "white",
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
    [theme.breakpoints.up("md")]: {
      flex: 1,
      marginRight: 32,
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
  footer: {
    backgroundColor: theme.palette.primary.main,
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
  loginSignUp: {
    display: "flex",
    [theme.breakpoints.down("sm")]: {
      flexDirection: "column",
    },
    [theme.breakpoints.up("sm")]: {
      flexDirection: "row",
    },
  },
  login: {
    flex: 1,
    display: "flex",
  },
  flexLineBetween: {
    [theme.breakpoints.down("sm")]: {
      marginTop: 16,
      marginBottom: 16,
      borderTop: "1px",
      borderTopStyle: "solid",
      borderTopColor: theme.palette.primary.main,
    },
    [theme.breakpoints.up("sm")]: {
      borderLeft: "1px",
      borderLeftStyle: "solid",
      borderLeftColor: theme.palette.primary.main,
    },
  },
  signUp: {
    flex: 1,
    display: "flex",
  },
}));

export default useStyles;
