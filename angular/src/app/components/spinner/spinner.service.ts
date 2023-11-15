import {Injectable} from '@angular/core';
import {BehaviorSubject} from "rxjs";

@Injectable()
export class SpinnerService {

  static visibility = new BehaviorSubject<boolean>(false);

  getVisibility() {
    return SpinnerService.visibility.asObservable();
  }

  static toggle() {
    SpinnerService.visibility.next(
      !SpinnerService.visibility.value);
  }
}
