import {Inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {
  catchError,
  from,
  map,
  mergeMap,
  Observable,
  tap,
  throwError
} from 'rxjs';
import {ICertificate} from '../model/entity/ICertificate';
import {ITag} from "../model/entity/ITag";
import {ICategory} from "../interfaces/ICategory";
import {BASE_URL_TOKEN, SRC_URL_TOKEN} from "../config";
import {UserService} from "./user.service";
import {IUser} from "../model/entity/IUser";
import {FormArray, FormGroup} from "@angular/forms";
import {SpinnerService} from "../components/spinner/spinner.service";
import {LoginState} from "../model/enum/LoginState";
import {ModalService} from "../components/modal/modal.service";
import {NavigateService} from "./navigate.service";
import {IInvoice} from "../interfaces/IInvoice";

@Injectable({
  providedIn: 'root',
})
export class LoadService {

  constructor(
    @Inject(SRC_URL_TOKEN) private srcUrl: string,
    @Inject(BASE_URL_TOKEN) private baseUrl: string,
    private readonly navigator: NavigateService,
    public readonly modal: ModalService,
    private readonly userService: UserService,
    private readonly http: HttpClient
  ) {
  }

  back(): void {
    this.navigator.back();
  }

  loginState(state: LoginState) {
    this.userService.loginState.next(state);
  }

  async saveForm(form: FormGroup): Promise<Response> {
    const body: string = JSON.stringify(this.createRequestBody(form));
    const url: string = `${this.baseUrl}/certificates`;
    const response: Response = await this.fetchResponse(url, body);
    if (response.ok) {
      localStorage.setItem('product',
        JSON.stringify(await response.json()));
    }
    return response;
  }

  async saveImage(fileControl: any) {
    if (fileControl.value) {
      const formData = new FormData();
      formData.append('file', fileControl.value);

      const response = await fetch(`${this.baseUrl}/upload`, {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${this.userService.getUser().access_token}`
        },
        body: formData
      });

      if (!response.ok) {
        await this.modal.showResponse(response);
      }
      return response.json();
    }
  }

  private async fetchResponse(url: string, body: any): Promise<Response> {
    return await fetch(url, {
      method: 'POST',
      headers: {
        'Authorization': `Bearer ${this.userService.getUser().access_token}`,
        'Content-Type': 'application/json'
      },
      body: body
    });
  }

  private createRequestBody(form: FormGroup) {
    const duration: number = Math.ceil(Math.abs(
      new Date(form.get('expired')?.value).getTime()
      - new Date().getTime()) / (1000 * 60 * 60 * 24));

    const selectedTags = [];
    const tagsControl = <FormArray>form.get('tags');
    for (let tagControl of tagsControl.controls) {
      if (tagControl.value.selected) {
        selectedTags.push({name: tagControl.value.name});
      }
    }
    let path;
    const fileControl = form.get('file');
    if (fileControl && fileControl.value) { //TODO
      const imageName: string = (fileControl.value as File).name;
      path = imageName ? `${this.baseUrl}/upload/${imageName}` : '';
      console.log(path);
    }

    return {
      "name": form.value.name,
      "description": form.value.description,
      "price": form.value.price,
      "duration": duration,
      "tags": selectedTags,
      "path": path
    };
  }

  getHttp(url: string): Observable<ICertificate[]> {
    SpinnerService.toggle();
    return this.http
    .get<any>(url)
    .pipe(map((data: any) =>
      data._embedded.certificateDtoList.map(this.mapper))
    );
  }

  getCertificates(page: number): Observable<ICertificate[]> {
    return this.getHttp(`/certificates?page=${page.toFixed()}`);
  }

  getCertificatesByTags(size: number, name: string): Observable<ICertificate[]> {
    return this.getHttp(`/certificates/search?tagNames=${name}&size=${size}`);
  }

  getTags(): Observable<string[]> {
    return this.http.get<any>('/tags?size=25')
    .pipe(map((data: any) => data._embedded.tagDtoList
    .map(this.tagMapper)));
  }

  loginUser(user: IUser): Observable<any> {
    return this.http.post(`/login`, user).pipe(
      tap((response: any): void =>
        this.updateUser(user, response)),
      catchError(error => {
        return throwError(() => {
          this.modal.showByStatus(error.status);
        });
      })
    );
  }

  signup(formData: any): Observable<any> {
    console.log(formData.toString())
    return from(import('../components/item/item.component')).pipe(
      mergeMap(() => {
        return this.http.post(`/signup`, formData).pipe(
          tap((response: any) =>
            this.modal.showByText(20101, `${response.username} ${formData.firstName}`)
          )
        );
      })
    );
  }

  public saveOrders(total: number, user: IUser, callback: (statusCode: number) => void): void {
    const ids: string[] = user.certificates.map((certificate) => certificate.id);
    const counters: number[] = user.certificates.map((certificate) => certificate.count);
    const url = `${this.baseUrl}/orders/${user.username}?certificateIds=${ids.join(",")}&counters=${counters.join(",")}`;
    const invoice: IInvoice = {counters: counters, ids: ids, totalPrice: total};

    const requestBody = {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${user.access_token}`,
      }
    };
    fetch(url, requestBody)
    .then((response) => {
      callback(response.status);
      let certificates = user.certificates;
      certificates.forEach(cert => {
        cert.count = 1;
        cert.checkout = false
      });
      this.userService.updateCertificates(certificates)
      user.invoices.push(invoice);
      this.userService.saveUser(user);
    })
    .catch((error) => {
      callback(error.status);
    });
  }

  private mapper(data: any): ICertificate {
    return {
      id: data.id,
      name: data.name,
      description: data.description,
      shortDescription: data.shortDescription,
      company: data.company,
      price: data.price,
      duration: data.duration,
      createDate: new Date(data.createDate),
      lastUpdate: new Date(data.lastUpdateDate),
      favorite: false,
      checkout: false,
      path: data.path,
      tags: data.tags.map((tag: any): ITag => ({id: tag.id, name: tag.name})),
      count: 1
    };
  }

  private tagMapper = (data: any): ICategory => {
    return {
      name: data.name,
      tag: data.name,
      url: `${this.srcUrl}/200x150/?${data.name}`,
    };
  }

  private updateUser(user: IUser, response: any) {
    user.id = response.id;
    user.access_token = response.access_token;
    user.refresh_token = response.refresh_token;
    user.expired_at = response.expired_at;
    user.certificates = [];
    this.userService.login(user);
  }

  sendOrders(total: number) {
    const user: IUser = this.userService.getUser();
    user.certificates = this.userService.getCheckouts();
    SpinnerService.toggle();
    this.saveOrders(total, user, (statusCode: number) => {
      if (statusCode === 201) {
        this.modal.showByText(20103, user.username);
      } else {
        this.modal.showByText(statusCode, `Failed to send orders by ${user.username}`);
      }
    });
  }
}
