import {Component, NgModule, ViewEncapsulation} from '@angular/core';
import {CommonModule} from "@angular/common";
import {MatCardModule} from "@angular/material/card";
import {MatButtonModule} from "@angular/material/button";
import {FlexLayoutModule} from "@angular/flex-layout";
import {ImageModule} from "../image/image.module";
import {IMessage} from "../../interfaces/IMessage";

@Component({
  selector: 'app-item',
  templateUrl: './item.component.html',
  styleUrls: ['./item.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class ItemComponent {

  public message!: IMessage;
  public save!: () => void;
  public close!: () => void;
}

@NgModule({
  declarations: [ItemComponent],
  exports: [
    ItemComponent
  ],
  imports: [CommonModule, MatCardModule, MatButtonModule, FlexLayoutModule, ImageModule]
})
export class ItemModule {
}
