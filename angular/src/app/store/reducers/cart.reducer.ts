import {createEntityAdapter, EntityAdapter, EntityState} from "@ngrx/entity";
import {
  Action,
  createFeatureSelector,
  createReducer,
  createSelector,
  on
} from "@ngrx/store";
import {
  addProductToCart,
  decrementProductInCart,
  incrementProductInCart,
  removeFromProductToCart
} from "../actions/cart.action";
import {ActionReducer} from "@ngrx/store/src/models";
import {IUser} from "../../model/entity/IUser";
import {ICertificate} from "../../model/entity/ICertificate";

export const cartAdapter: EntityAdapter<ICertificate> = createEntityAdapter({
  selectId: (product: ICertificate) => product.id
})

const initialState: EntityState<ICertificate> = cartAdapter.getInitialState();

const reducer: ActionReducer<EntityState<ICertificate>> = createReducer(initialState,
  on(addProductToCart, (state: EntityState<ICertificate>, {product}) => {
    const entity: ICertificate = state.entities[product.id] as ICertificate;
    return cartAdapter.upsertOne({
      ...product,
      count: entity ? ++entity.count : 1
    }, state);
  }),
  on(removeFromProductToCart, (state: EntityState<ICertificate>, {id}) => {
    return cartAdapter.removeOne(id, state);
  }),
  on(incrementProductInCart, (state: EntityState<ICertificate>, {id}) => {
    const entity: ICertificate = state.entities[id] as ICertificate;
    return cartAdapter.updateOne({id, changes: {count: ++entity.count}}, state);
  }),
  on(decrementProductInCart, (state: EntityState<ICertificate>, {id}) => {
    const entity: ICertificate = state.entities[id] as ICertificate;
    return cartAdapter.updateOne({id, changes: {count: --entity.count}}, state);
  }),
)

export function cartReducer(
  state: EntityState<ICertificate> | undefined, action: Action):
  EntityState<ICertificate> {
  return reducer(state, action);
}

export const {selectAll} = cartAdapter.getSelectors();
export const selectCartState = createFeatureSelector<EntityState<ICertificate>>('cart');
export const selectUserState = createFeatureSelector<IUser>('user');
export const selectCartProducts = createSelector(selectCartState, selectAll);


export const cartProducts = createSelector(
  selectCartProducts,
  selectUserState,
  (products, _user) => {
    return products.map((product) => {
      return {...product, price: product.price * product.count};
    });
  }
);

export const totalProductsCount = createSelector(
  selectCartProducts,
  (products) => products.reduce((total, product) => total + product.count, 0)
);

export const cartProductsWithBonuses = createSelector(
  selectUserState,
  (user) => (productsState: EntityState<ICertificate>) => {
    const certificateIds = productsState.ids as string[];
    const certificates = certificateIds.map((id) => productsState.entities[id]);

    return certificates.map((certificate) => ({
      ...certificate, price: certificate!.price * user.bonuses
    }));
  }
);
