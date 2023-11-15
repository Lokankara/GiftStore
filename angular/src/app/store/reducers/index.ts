import {certificateReducer} from "./certificate.reducer";
import {ICertificate} from "../../model/entity/ICertificate";
import {cartReducer} from "./cart.reducer";
import {EntityState} from "@ngrx/entity";
import userReducer from "./user.reducer";

export interface IState {
  certificates: {
    items: ICertificate[],
    loading: boolean
  };
  cart: EntityState<ICertificate>
}

export const reducers = {
  certificates: certificateReducer,
  cart: cartReducer,
  user: userReducer
}
