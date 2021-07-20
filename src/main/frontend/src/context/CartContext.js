import React, { useContext, createContext, useEffect, useReducer } from "react";
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
  let i;
  for (i = 0; i < cart.length; i++) {
    if (cart[i].productId === productId) {
      return i;
    }
  }
  return null;
};

const cartReducer = (state, action) => {
  switch (action.type) {
    case ACTIONS.ADD_ITEM: {
      let cartItemMatchedIndex = findCartItemIndexByProductId(
        state,
        action.productId
      );
      if (cartItemMatchedIndex !== null) {
        state[cartItemMatchedIndex].quantity += action.quantity;
      } else {
        let newItem = {
          productName: action.productName,
          productId: action.productId,
          quantity: action.quantity,
        };
        state.push(newItem);
      }
      return Array.from(state);
    }
    case ACTIONS.REMOVE_ITEM: {
      let cartItemMatchedIndex = findCartItemIndexByProductId(
        state,
        action.productId
      );
      if (cartItemMatchedIndex !== null) {
        state.splice(cartItemMatchedIndex, 1);
      }
      return Array.from(state);
    }
    case ACTIONS.UPDATE_ITEM_QUANTITY: {
      let cartItemMatchedIndex = findCartItemIndexByProductId(
        state,
        action.productId
      );
      if (cartItemMatchedIndex !== null) {
        state[cartItemMatchedIndex].quantity = action.quantity;
      } else {
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
    loadCart();
  }, []);

  // Save Cart to Local Storage whenever Cart is Updated
  useEffect(() => {
    localStorage.setItem(SNACK_OVERFLOW_CART, JSON.stringify(cart));
  }, [cart]);

  const addItem = ({ quantity, productId, productName }) => {
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

  const cartContextValue = {
    cart,
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
