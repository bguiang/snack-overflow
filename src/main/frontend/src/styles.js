import { makeStyles } from "@material-ui/core/styles";

// Theme colors
// theme.palette.primary.main - Blue: #00B1C6 or rgb(0,177,198)
// theme.palette.secondary.main - Yellow: #F8EB37 or rgb(248,235,55)
// Orange: #F3AB1C or rgb(243,171,28)

// style hook
// uses the theme provider https://material-ui.com/customization/theming/
const useStyles = makeStyles((theme) => ({
  container: {
    padding: 0,
  },
  content: {
    padding: 20,
  },
  appbar: {
    backgroundColor: theme.palette.secondary.main,
  },
  toolbarContainer: {
    paddingLeft: 0,
    paddingRight: 0,
  },
  toolbar: {
    // [theme.breakpoints.up("sm")]: {
    //   paddingLeft: 0,
    // },
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
  mobileIconButton: {
    paddingLeft: 0,
    paddingRight: 0,
  },
  menuButton: {
    color: "white",
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
    [theme.breakpoints.down("md")]: {
      display: "none",
    },
    [theme.breakpoints.up("md")]: {
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
    [theme.breakpoints.up("sm")]: {
      paddingLeft: 0,
      paddingRight: 0,
    },
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
    //justifyContent: "flex-start",
    justifyContent: "space-between",
    padding: "2px 4px",
    alignItems: "center",
  },
  toolbar2MenuItem: {
    textTransform: "capitalize",
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
    [theme.breakpoints.down("sm")]: {
      marginLeft: 0,
      flex: 1,
      marginRight: 36,
    },
    [theme.breakpoints.up("md")]: {
      marginLeft: "10%",
      width: 450,
      marginRight: 24,
    },
    [theme.breakpoints.up("lg")]: {
      marginLeft: "10%",
      width: 650,
      marginRight: 0,
    },
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
    height: 80,
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
  },
  footerText: {
    color: "#ffffff",
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
  snacksPaginationContainer: {
    display: "flex",
    justifyContent: "flex-end",
    paddingTop: 8,
  },
  snackCardContainer: {
    display: "flex",
  },
  snackCard: {
    flex: 1,
  },
  snackCardImage: {
    height: 140,
  },
  snackCardContent: {
    height: 200,
    overflow: "hidden",
  },
  snackCardActions: {
    marginTop: 4,
    display: "flex",
    justifyContent: "flex-end",
  },
  snackCardQuantity: {
    width: 80,
  },
  snackPageContainer: { display: "flex" },

  // Cart
  cartHeader: {
    display: "flex",
  },
  cartHeaderTitle: {
    flex: 1,
  },
  cartItemCard: {
    display: "flex",
    [theme.breakpoints.down("sm")]: {
      flexDirection: "column",
    },
    [theme.breakpoints.up("sm")]: {
      flexDirection: "row",
    },
    alignItems: "center",
  },
  cartItemCardActionArea: {
    display: "flex",
    flexDirection: "row",
    flex: 1,
    justifyContent: "flex-start",
    alignItems: "center",
  },
  cartItemCardImage: {
    height: 100,
    width: 100,
    margin: 16,
    [theme.breakpoints.down("xs")]: {
      height: 80,
      width: 80,
    },
  },
  cartItemName: {
    flex: 1,
  },
  cartItemPrice: { paddingLeft: 8, paddingRight: 8 },
  cartItemQuantity: {
    width: 100,
    textAlign: "center",
  },
  cartItemCardActions: {
    display: "flex",
    justifyContent: "flex-end",
    [theme.breakpoints.down("xs")]: {
      width: "100%",
    },
  },

  // Checkout
  checkoutHeader: {
    display: "flex",
    padding: 0,
  },
  checkoutHeaderTitle: {
    flex: 1,
  },
  checkoutForm: {
    flex: 1,
  },
  checkoutItem: {
    flex: 1,
    display: "flex",
    flexDirection: "row",
  },
  checkoutItemName: {
    flex: 1,
  },
  checkoutItemPrice: { paddingLeft: 8, paddingRight: 8 },
  checkoutItemQuantity: {
    width: 80,
    textAlign: "center",
  },
  checkoutTotal: {
    textAlign: "right",
    paddingTop: 8,
    borderTop: "1px",
    borderTopStyle: "solid",
    borderTopColor: theme.palette.primary.main,
    fontWeight: "bold",
  },
  checkoutOrderInfo: {
    flex: 1,
    padding: 16,
  },
  addressCard: {
    padding: 16,
    marginTop: 10,
  },

  // Orders
  orderListTitle: {
    fontWeight: "bold",
  },
  orderListTitleMobile: {
    [theme.breakpoints.down("md")]: {
      display: "flex",
    },
    [theme.breakpoints.up("md")]: {
      display: "none",
    },
  },
  orderCard: {
    padding: 16,
    [theme.breakpoints.down("md")]: {
      display: "none",
    },
    [theme.breakpoints.up("md")]: {
      display: "flex",
    },
  },
  orderCardMobile: {
    padding: 16,
    [theme.breakpoints.down("md")]: {
      display: "flex",
      flexDirection: "column",
    },
    [theme.breakpoints.up("md")]: {
      display: "none",
    },
  },
  orderCardActionArea: {
    flex: 1,
    display: "flex",
    flexDirection: "row",
  },
  orderCardActionAreaMobile: {
    flex: 1,
    display: "flex",
    flexDirection: "column",
    alignItems: "flex-start",
  },
  orderCardActionAreaItem: {
    flex: 1,
  },
  orderCardActionAreaItem2: {
    flex: 2,
  },
  orderItem: {
    flex: 1,
    display: "flex",
    alignItems: "center",
    padding: 16,
    [theme.breakpoints.down("sm")]: {
      flexDirection: "column",
    },
    [theme.breakpoints.up("sm")]: {
      flexDirection: "row",
    },
  },
  orderItemImageContainer: {
    width: 100,
  },
  orderItemImage: { height: 100 },
  orderItemName: {
    flex: 1,
    padding: 16,
  },
  orderItemPrice: {
    padding: 16,
  },
  orderItemQuantity: {
    padding: 16,
  },
  orderDetailsBillingAndShipping: {
    flex: 1,
    display: "flex",
    [theme.breakpoints.down("sm")]: {
      flexDirection: "column",
    },
    [theme.breakpoints.up("sm")]: {
      flexDirection: "row",
    },
  },
  orderBillingAndShippingCard: {
    flex: 1,
    [theme.breakpoints.down("sm")]: {
      margin: 8,
    },
    [theme.breakpoints.up("sm")]: {
      marginRight: 8,
    },
  },
  dashboard: {
    height: "100%",
    display: "flex",
    padding: 0,
  },
  dashboardMenu: {
    backgroundColor: "#242526",
    width: 160,
    [theme.breakpoints.down("sm")]: {
      display: "none",
    },
    [theme.breakpoints.up("sm")]: {
      display: "flex",
    },
    flexDirection: "column",
    boxShadow: "1px 0 10px #242526",
    WebkitBoxShadow: "1px 0 10px #242526",
    MozBoxShadow: "1px 0 10px #242526",
  },
  dashboardMenuMobile: {
    backgroundColor: "#242526",
    width: 40,
    [theme.breakpoints.down("sm")]: {
      display: "flex",
    },
    [theme.breakpoints.up("sm")]: {
      display: "none",
    },
    flexDirection: "column",
  },
  dashboardMenuItem: {
    color: theme.palette.secondary.main,
    justifyContent: "left",
  },
  dashboardContent: {
    flex: 1,
    padding: 20,
  },
  imageSection: {
    marginTop: 10,
    marginBottom: 10,
  },
  editImageContainer: {
    marginTop: 10,
    marginBottom: 10,
    padding: 10,
  },
  editImageContainerImageContainer: {
    display: "flex",
    justifyContent: "center",
  },
  editImageInputImage: {
    flex: 1,
    backgroundColor: "#bebebe",
    [theme.breakpoints.down("sm")]: {
      minHeight: 150,
      maxWidth: 320,
    },
    [theme.breakpoints.up("sm")]: {
      minHeight: 250,
      maxWidth: 320,
    },
  },
  editImageInputTextInput: {
    display: "flex",
  },
  deleteImageContainer: {
    display: "flex",
    justifyContent: "center",
  },
  productCardHorizontal: {
    [theme.breakpoints.down("md")]: {
      display: "none",
    },
    [theme.breakpoints.up("md")]: {
      display: "flex",
      flex: 1,
      padding: 10,
    },
  },
  productCardHorizontalTitle: {
    [theme.breakpoints.down("md")]: {
      display: "none",
    },
    [theme.breakpoints.up("md")]: {
      display: "flex",
      flex: 1,
      paddingLeft: 14,
      paddingRight: 14,
    },
  },
  productCardNewProductButton: {
    [theme.breakpoints.down("md")]: {
      display: "flex",
      flex: 1,
      paddingTop: 10,
      paddingBottom: 10,
    },
    [theme.breakpoints.up("md")]: {
      display: "none",
    },
  },
  productCardHorizontalMain: {
    flex: 6,
    display: "flex",
    flexDirection: "row",
    paddingLeft: 10,
    paddingRight: 10,
    alignItems: "center",
  },
  productCardHorizontalImage: {
    height: 100,
    width: 100,
    margin: 16,
  },
  productCardHorizontalName: {
    flex: 1,
    margin: 16,
  },
  productCardHorizontalID: {
    minWidth: 100,
    paddingLeft: 10,
    paddingRight: 10,
  },
  productCardHorizontalDate: {
    minWidth: 100,
    paddingLeft: 10,
    paddingRight: 10,
  },
  productCardHorizontalPrice: {
    minWidth: 100,
    paddingLeft: 10,
    paddingRight: 10,
  },
  productCardHorizontalUnitsSold: {
    width: 120,
    paddingLeft: 10,
    paddingRight: 10,
  },
  productCardHorizontalFiller: {
    width: 80,
  },
  verticalCard: {
    [theme.breakpoints.down("md")]: {
      display: "flex",
      flex: 1,
      flexDirection: "column",
      marginBottom: 10,
    },
    [theme.breakpoints.up("md")]: {
      display: "none",
    },
  },
  productCardVerticalImage: {
    height: 200,
    margin: 16,
  },
  selector: {
    minWidth: 120,
    marginLeft: 10,
  },
  success: {
    color: "#388e3c",
  },
  error: {
    color: "#d32f2f",
  },
  carouselImageContainer: {
    flex: 1,
    display: "flex",
    alignContent: "center",
    justifyContent: "center",
  },
  carouselImage: {
    [theme.breakpoints.down("sm")]: {
      height: 200,
      width: 200,
    },
    [theme.breakpoints.up("sm")]: {
      height: 300,
      width: 300,
    },
  },
  orderTitleFiller: {
    minWidth: 80,
  },
  adminSelector: {
    [theme.breakpoints.down("sm")]: {
      display: "none",
    },
    [theme.breakpoints.up("sm")]: {
      display: "inline-flex",
      border: 0,
      marginTop: 0,
      marginRight: 0,
      marginBottom: 0,
      marginLeft: 10,
      padding: 0,
      position: "relative",
      minWidth: 110,
      flexDirection: "column",
      verticalAlign: "top",
    },
  },
  adminSelectorMobileContainer: {
    [theme.breakpoints.down("sm")]: {
      display: "flex",
      flexDirection: "column",
      paddingTop: 10,
      paddingBottom: 20,
      paddingRight: 10,
    },
    [theme.breakpoints.up("sm")]: {
      display: "none",
    },
  },
  adminSelectorMobile: {
    [theme.breakpoints.down("sm")]: {
      display: "flex",
      marginBottom: 10,
    },
    [theme.breakpoints.up("sm")]: {
      display: "none",
    },
  },
  adminSearchContainer: {
    display: "flex",
    flexDirection: "column",
  },
  adminSearchBar: {
    flex: 1,
    [theme.breakpoints.down("sm")]: {
      marginLeft: 0,
      marginRight: 10,
    },
    [theme.breakpoints.up("sm")]: {
      marginLeft: 30,
      marginRight: 40,
    },

    display: "flex",
    alignItems: "center",
  },
  memberCard: {
    padding: 16,
    [theme.breakpoints.down("lg")]: {
      display: "none",
    },
    [theme.breakpoints.up("lg")]: {
      display: "flex",
    },
  },
  memberCardMobile: {
    padding: 16,
    [theme.breakpoints.down("lg")]: {
      display: "flex",
      flexDirection: "column",
    },
    [theme.breakpoints.up("lg")]: {
      display: "none",
    },
  },
  dashboardTile: {
    display: "flex",
    minHeight: 100,
  },
  dashboardTileItem: {
    flex: 1,
    padding: 5,
  },
  homeCarousel: {
    width: "100%",
  },
  snackCarouselItem: {
    flex: 1,
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    paddingRight: 10,
    paddingLeft: 10,
  },
  snackCarouselItemContent: {
    display: "flex",
    [theme.breakpoints.down("sm")]: {
      flexDirection: "column",
      flex: 1,
    },
    [theme.breakpoints.up("sm")]: {
      width: 600,
    },
    [theme.breakpoints.up("md")]: {
      width: 800,
    },
    [theme.breakpoints.up("lg")]: {
      width: 1000,
    },
  },
  snackItemCarouselImage: {
    [theme.breakpoints.down("sm")]: {
      height: 200,
      marginLeft: 10,
      marginRight: 10,
    },
    [theme.breakpoints.up("sm")]: {
      flex: 1,
      minHeight: 300,
    },
    [theme.breakpoints.up("md")]: {
      flex: 1,
      minHeight: 300,
    },
    [theme.breakpoints.up("lg")]: {
      flex: 1,
      minHeight: 400,
    },
  },
  snackItemCarouselDescription: {
    overflow: "hidden",
    [theme.breakpoints.down("sm")]: {
      height: 200,
      padding: 20,
    },
    [theme.breakpoints.up("sm")]: {
      flex: 2,
      height: 300,
      padding: 40,
    },
    [theme.breakpoints.up("md")]: {
      flex: 2,
      height: 300,
    },
    [theme.breakpoints.up("lg")]: {
      flex: 2,
      minHeight: 400,
      padding: 40,
    },
  },
}));

export default useStyles;
