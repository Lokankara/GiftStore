import {Injectable} from '@angular/core';
import {Location} from "@angular/common";
import {Router} from "@angular/router";

@Injectable({
  providedIn: 'root'
})
export class NavigateService {

  constructor(
    private location: Location,
    private router: Router,
    ) {
  }

  back() {
    this.location.back();
  }

  redirect(path: string) {
    this.router.navigate([path]).then((success): void => {
      let message: string = success
        ? `Navigation to ${path} was successful`
        : `Navigation to ${path} failed`;
      console.log(message);
      location.reload(); //TODO
    }).catch((error) => {
      console.error('Error during navigation:', error);
    });
  }
}
