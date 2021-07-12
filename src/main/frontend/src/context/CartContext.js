import { AcUnitTwoTone } from "@material-ui/icons";
import React, {
  useContext,
  useState,
  createContext,
  useEffect,
  useReducer,
} from "react";
import SnackOverflow from "../api/SnackOverflow";

const SNACK_OVERFLOW_CART = "snackoverflow-cart";

const ACTIONS = {
  ADD_ITEM: "add item",
  REMOVE_ITEM: "remove item",
  UPDATE_ITEM_QUANTITY: "update item quantity",
  CLEAR: "clear",
  LOAD_CART: "load cart",
};

// Create context
const CartContext = createContext();

const findCartItemIndexByProductId = (cart, productId) => {
  console.log("findCartItemIndexByProductId()");
  let i;
  for (i = 0; i < cart.length; i++) {
    console.log("INDEX: " + i + ", Item: " + JSON.stringify(cart[i]));
    if (cart[i].productId === productId) {
      console.log("MATCHED!!!!!!!!!!!!!!!!!");
      return i;
    }
    console.log("NOT MATCHED");
  }
  return null;
};

const cartReducer = (state, action) => {
  switch (action.type) {
    case ACTIONS.ADD_ITEM: {
      console.log("ADD ITEM WITH ID: " + action.productId);
      let cartItemMatchedIndex = findCartItemIndexByProductId(
        state,
        action.productId
      );
      if (cartItemMatchedIndex !== null) {
        console.log("CART ITEM MATCHED, ADD TO QUANTITY");
        state[cartItemMatchedIndex].quantity += action.quantity;
      } else {
        console.log("CART ITEM NOT MATCHED, ADD NEW ITEM");
        let newItem = {
          productName: action.productName,
          productId: action.productId,
          quantity: action.quantity,
        };
        state.push(newItem);
      }
      console.log(state);
      return Array.from(state);
    }
    case ACTIONS.ACTIONSREMOVE_ITEM: {
      let cartItemMatchedIndex = findCartItemIndexByProductId(
        state,
        action.productId
      );
      if (cartItemMatchedIndex) {
        state.splice(cartItemMatchedIndex, 1);
      }
      return Array.from(state);
    }
    case ACTIONS.UPDATE_ITEM_QUANTITY: {
      let cartItemMatchedIndex = findCartItemIndexByProductId(
        state,
        action.productId
      );
      if (cartItemMatchedIndex) {
        state[cartItemMatchedIndex].quantity = action.quantity;
      }
      return Array.from(state);
    }
    case ACTIONS.CLEAR: {
      state = [];
      return Array.from(state);
    }
    case ACTIONS.LOAD_CART: {
      try {
        let localCartData = localStorage.getItem(SNACK_OVERFLOW_CART);
        if (localCartData !== null) state = JSON.parse(localCartData);
        else state = [];
      } catch (err) {
        console.log(err);
        state = [];
      }
      return state;
    }
    default:
      return state;
  }
};

// Custom context provider
export function CartProvider({ children }) {
  const [cart, dispatch] = useReducer(cartReducer, []);

  // Load Cart Data from Local Storage on mount
  useEffect(() => {
    console.log("App Reloaded, Load Cart From LocalStorage");
    loadCart();
  }, []);

  // Save Cart to Local Storage whenever Cart is Updated
  useEffect(() => {
    console.log("Cart Updated, Save Cart to LocalStorage");
    localStorage.setItem(SNACK_OVERFLOW_CART, JSON.stringify(cart));
  }, [cart]);

  const addItem = ({ quantity, productId, productName }) => {
    console.log("addItem: " + productId + ", " + "x" + quantity);
    let action = { type: ACTIONS.ADD_ITEM, quantity, productId, productName };
    dispatch(action);
  };

  const removeItem = ({ productId }) => {
    let action = { type: ACTIONS.REMOVE_ITEM, productId };
    dispatch(action);
  };

  const updateItemQuantity = ({ productId, quantity }) => {
    let action = { type: ACTIONS.UPDATE_ITEM_QUANTITY, productId, quantity };
    dispatch(action);
  };

  const clearItems = () => {
    let action = { type: ACTIONS.CLEAR };
    dispatch(action);
  };

  const loadCart = () => {
    let action = { type: ACTIONS.LOAD_CART };
    dispatch(action);
  };
  const getItemCount = () => {
    let i;
    let cartItemCount = 0;
    for (i = 0; i < cart.length; i++) {
      let item = cart[i];
      cartItemCount += item.quantity;
    }
    return cartItemCount;
  };

  const getCartTotal = (cartItems) => {
    console.log("Calculating Cart Total");
    let i;
    let cartTotal = 0;
    for (i = 0; i < cartItems.length; i++) {
      let item = cartItems[i];
      console.log("Item: " + JSON.stringify(item));
      cartTotal += item.quantity * item.product.price;
    }
    return cartTotal.toFixed(2);
  };

  const getSnacksById = async (cartItemIds) => {
    try {
      let response = await SnackOverflow.get(
        `/products?productIds=${cartItemIds.toString()}`
      );
      console.log("Returning getSnacks reponse...");
      return response.data;
    } catch (error) {
      console.log(error);
    }
  };

  const getCartInfo = async () => {
    let cartItemIds = [];
    let i;
    for (i = 0; i < cart.length; i++) {
      cartItemIds.push(cart[i].productId);
    }

    let cartItemInfo = await getSnacksById(cartItemIds);
    console.log("INFO ON CART PRODUCTS");
    console.log(cartItemInfo);

    // Build Cart Info
    let updatedCart = {};

    let cartItems = [];

    // Set Items
    for (i = 0; i < cart.length; i++) {
      let cartItem = {};
      // Quantity
      cartItem.quantity = cart[i].quantity;
      // Product Info
      cartItem.product = cartItemInfo[cart[i].productId];

      cartItems.push(cartItem);
    }
    updatedCart.items = cartItems;

    // Total
    let cartTotal = getCartTotal(cartItems);
    updatedCart.total = cartTotal;

    return updatedCart;
  };

  const cartContextValue = {
    getCartInfo,
    addItem,
    removeItem,
    updateItemQuantity,
    clearItems,
    getItemCount,
  };

  return (
    <CartContext.Provider value={cartContextValue}>
      {children}
    </CartContext.Provider>
  );
}

// expose context
export function useCart() {
  return useContext(CartContext);
}
