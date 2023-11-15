import {ICertificate} from "./ICertificate";
import {LoginState} from "../enum/LoginState";
import {IInvoice} from "../../interfaces/IInvoice";

export interface IUser {
  id: number;
  username: string;
  password: string;
  access_token: string;
  refresh_token: string;
  expired_at: string;
  certificates: ICertificate[];
  state: LoginState;
  invoices: IInvoice[];
  bonuses: number
}
