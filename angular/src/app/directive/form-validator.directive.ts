import {Directive} from '@angular/core';
import {
  AbstractControl, NG_VALIDATORS,
  ValidationErrors, Validators
}
  from '@angular/forms';
import {Observable, of} from "rxjs";

@Directive({
  selector: '[appUserValidator]',
  providers: [{
    provide: NG_VALIDATORS,
    useValue: userMatch,
    // useExisting: FormValidatorDirective,
    multi: true
  }]
})
export class FormValidatorDirective {
}

export function userMatch(control: AbstractControl):
  Observable<ValidationErrors | null> {
  return /^[a-zA-Z]+$/.test(control.value)
    ? of(null)
    : of({userMatch: 'allowed only letters'});
}

export function passwordMatch(
  control: AbstractControl): ValidationErrors | null {
  const [password, repeat] = Object.values(control.value)
  return password === repeat ? null
    : {passwordMismatch: 'Passwords are not equals'};
}

export const required = Validators.required;
export const minLength = [Validators.required, Validators.minLength(3)];
