import {
  AfterViewInit,
  Component,
  ElementRef,
  ViewChild,
  ViewEncapsulation
} from '@angular/core';
import {IMessage} from "../../../../interfaces/IMessage";

@Component({
  selector: 'app-menu-button',
  templateUrl: './menu-button.component.html',
  styleUrls: ['./menu-button.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class MenuButtonComponent implements AfterViewInit {

  @ViewChild('menuOpenCheckbox', {static: false})
  menuCheckbox!: ElementRef<HTMLInputElement>;
  @ViewChild('logoLabel')
  logoLabel!: ElementRef<HTMLInputElement>;
  @ViewChild('logoName')
  logoName!: ElementRef<HTMLInputElement>;

  public checkbox!: HTMLInputElement;
  public logo!: HTMLElement;
  public name!: HTMLElement;

  public menuItems: IMessage[] = [
    {name: "home", href: "/", color: ''},
    {name: "playlist_add_check", href: "details", color: ''},
    {name: "post_add", href: "coupon", color: ''},
    {name: "shopping_cart_checkout", href: "checkout", color: ''},
    {name: "person_add", href: "signup", color: ''},
    {name: "login", href: "login", color: ''},
  ];

  ngAfterViewInit() {
    if (this.menuCheckbox) {
      this.menuCheckbox.nativeElement.checked = false;
    }
  }

  public toggleCheckbox(): void {
    this.checkbox = this.menuCheckbox.nativeElement;
    this.logo = this.logoLabel.nativeElement;
    this.name = this.logoName.nativeElement;

    if (this.checkbox.checked) {
      this.name.style.opacity = '1';
      this.name.style.zIndex = '1';
      this.logo.style.transform = 'none';
    } else {
      this.name.style.opacity = '0';
      this.name.style.zIndex = '-5';
      this.logo.style.transform = 'rotate(42deg)';
    }
    this.checkbox.checked = !this.checkbox.checked;
  }
}
