import {IUser} from "./entity/IUser";
import {ICertificate} from "./entity/ICertificate";
import {LoginState} from "./enum/LoginState";
import {IInvoice} from "../interfaces/IInvoice";

export class User implements IUser {
  id: number = 0;
  access_token!: string;
  certificates: ICertificate[] = [];
  expired_at!: string;
  password!: string;
  refresh_token!: string;
  state: LoginState = LoginState.GUEST;
  username: string = 'user';
  invoices: IInvoice[] = [];
  bonuses: number = 0.8;
}
