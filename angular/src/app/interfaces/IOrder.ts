import {ICertificate} from "../model/entity/ICertificate";

export interface IOrder {
  id: number;
  cost: number;
  orderDate: Date;
  certificateDtos: ICertificate[];
}
