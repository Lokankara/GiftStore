import {Injectable} from '@angular/core';
import {BehaviorSubject} from "rxjs";
import {IUser} from "../model/entity/IUser";
import {LocalStorageService} from "./local-storage.service";
import {LoginState} from "../model/enum/LoginState";
import {ICertificate} from "../model/entity/ICertificate";

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private currentUser: BehaviorSubject<IUser | null>;
  loginState: BehaviorSubject<LoginState>;

  constructor(
    private readonly storage: LocalStorageService
  ) {
    this.loginState = new BehaviorSubject<LoginState>(LoginState.LOGGED_OUT);
    this.currentUser = new BehaviorSubject<IUser | null>(null);
    let user: IUser = this.getUser();
    if (user) {
      this.loginState.next(user.state);
    }
  }

  isLoggedIn() {
    return this.storage.getUser().state === LoginState.LOGGED_IN;
  }

  public login(user: IUser): void {
    user.state = LoginState.LOGGED_IN;
    this.loginState.next(user.state);
    this.currentUser.next(user);
    this.saveUser(user);
  }

  public logout(user: IUser): void {
    this.currentUser.next(null);
    user.state = LoginState.LOGGED_OUT;
    this.loginState.next(user.state);
    this.saveUser(user);
  }

  public getUsername(): string {
    return this.storage.getUsername();
  }

  public getUser(): IUser {
    return this.storage.getUser();
  }

  public saveUser(user: IUser): void {
    this.storage.saveUser(user);
  }

  public updateCertificates(certificates: ICertificate[]): void {
    certificates.forEach((cert: ICertificate) =>
      this.storage.updateCertificate(cert));
  }

  getCheckouts(): ICertificate[] {
    return this.storage.getCheckoutCertificates();
  }
}
