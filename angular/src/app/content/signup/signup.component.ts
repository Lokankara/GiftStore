import {Component, ViewEncapsulation} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {LoadService} from "../../services/load.service";
import {
  passwordMatch,
  required,
  userMatch
} from "../../directive/form-validator.directive";

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class SignupComponent {

  public signupForm!: FormGroup;

  constructor(
    private readonly load: LoadService,
    private formBuilder: FormBuilder
  ) {
    this.signupForm = this.formBuilder.group({
      username: ['', required, userMatch.bind(this)],
      firstName: ['', required, userMatch.bind(this)],
      password: this.formBuilder.group({
        password: ['', required],
        repeatPassword: ['', required],
      }, {validators: passwordMatch}),
      email: ['', [required, Validators.email]],
      address: ['', required]
    });
  }

  onCancel() {
    this.load.back();
  }

  public signup() {
    console.log(this.signupForm)
    const password = this.signupForm
    .get('password.password')?.value;
    if (!this.signupForm || !password) {
      this.load.modal.showByStatus(40001)
    } else {
      this.signupForm.value.password = password;
      this.load.signup(this.signupForm.value).subscribe({
        next: (response: any) => this.load.modal.showByStatus(response.status),
        // error: (error: any) => this.load.showByStatus(error.status)
      });
    }
  }
}
