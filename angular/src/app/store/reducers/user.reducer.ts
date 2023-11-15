import {IUser} from "../../model/entity/IUser";
import {Action, createReducer} from "@ngrx/store";


const initialState: IUser = {} as IUser;

export const reducer = createReducer(
  initialState
);

export default function userReducer(state: any, action: Action): any {
  return reducer(state, action)
}
