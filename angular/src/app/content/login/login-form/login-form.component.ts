import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {LoadService} from "../../../services/load.service";
import {FormBuilder, FormGroup} from "@angular/forms";
import {IUser} from "../../../model/entity/IUser";
import {LoginState} from "../../../model/enum/LoginState";
import {
  minLength,
  userMatch
} from "../../../directive/form-validator.directive";

@Component({
  selector: 'app-login-form',
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class LoginFormComponent implements OnInit {

  public loginForm!: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private load: LoadService) {
  }

  login(): void {
    if (this.loginForm.invalid) {
      return this.load.modal.showByStatus(40001);
    }
    const user: IUser = {
      id: 0,
      username: this.loginForm.value.username,
      password: this.loginForm.value.password,
      access_token: '',
      refresh_token: '',
      expired_at: '',
      certificates: [],
      state: LoginState.LOGGED_OUT,
      invoices: [],
      bonuses: 1
    };

    this.load.loginUser(user).subscribe({
      next: (response: any) => {
        this.load.modal.showByText(20101, response.username);
      },
      error: (error: any) => {
        this.load.modal.showByStatus(error.statusCode);
      }
    });
  }

  ngOnInit() {
    this.load.loginState(LoginState.LOGGED_OUT);
    this.loginForm = this.formBuilder.group({
      username: ['', minLength, userMatch.bind(this)],
      password: ['', minLength]
    });
  }
}
