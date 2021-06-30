import { makeStyles } from '@material-ui/core/styles';

// style hook
// uses the theme provider https://material-ui.com/customization/theming/
const useStyles = makeStyles((theme) => ({
  app: {
    minHeight: "100vh",
    display: "flex",
    flexDirection: "column"
  },
  appbar: {
    //backgroundImage: `url(${process.env.PUBLIC_URL + "/pattern.png"})`
    backgroundColor: theme.palette.background.paper
  },
  appbarToolbarTop: {
    height: 100,
    //backgroundColor: "rgba(255, 255, 255, 0.6)",
    display: "flex",
    alignItems: "center"
  },
  appbarToolbarMenu: {
    backgroundColor: theme.palette.info.main
  },
  main: {
    backgroundImage: `url(${process.env.PUBLIC_URL + "/pattern.png"})`,
    display: "flex",
    flex: 1
  },
  footer: {
    height: 150
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