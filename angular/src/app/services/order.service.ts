import {Inject, Injectable} from '@angular/core';
import {from, map, Observable} from "rxjs";
import {IOrder} from "../interfaces/IOrder";
import {IUser} from "../model/entity/IUser";
import {BASE_URL_TOKEN} from "../config";
import {LocalStorageService} from "./local-storage.service";

@Injectable()
export class OrderService {

  constructor(
    @Inject(BASE_URL_TOKEN) private baseUrl: string,
    private storage: LocalStorageService,
  ) {
  }

  getOrders(): Observable<IOrder[]> {
    const user: IUser = this.storage.getUser();
    const url: string = `${this.baseUrl}/orders/users/${user.id}`;
    const headers: Headers = new Headers({
      Authorization: `Bearer ${user.access_token}`,
      'Content-Type': 'application/json',
    });
    return from(
      fetch(url, {method: 'GET', headers: headers}).then(response => {
        if (!response.ok) {
          console.log(`Request failed fetch Orders`);
        }
        return response.json();
      })
    ).pipe(map(data => data._embedded.orderDtoList as IOrder[])
    );
  }
}
