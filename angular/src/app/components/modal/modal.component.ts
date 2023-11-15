import {
  Component, ComponentRef, HostListener,
  OnInit, ViewChild, ViewContainerRef,
  ViewEncapsulation
} from '@angular/core';
import {ModalService} from "./modal.service";
import {IModalData} from "../../interfaces/IModalData";

@Component({
  selector: 'app-modal',
  templateUrl: './modal.component.html',
  styleUrls: ['./modal.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class ModalComponent implements OnInit {

  @ViewChild('modalContent', {read: ViewContainerRef})
  private modalContent!: ViewContainerRef;
  public modalRef!: ComponentRef<any>
  public isOpen: boolean = false;
  public content!: any;

  constructor(
    private readonly modalService: ModalService
  ) {
  }

  ngOnInit(): void {
    this.modalService.modalSequence$.subscribe((data: IModalData | null) => {
      if (data) {
        this.isOpen = true;
        const {component, context} = data;
        this.modalRef = this.modalContent.createComponent(component);
        Object.keys(context).forEach((key: string) =>
          this.modalRef.instance[key] = context[key]);
      } else {
        this.close();
      }
    });
  }

  @HostListener('window:keyup', ['$event.keyCode'])
  close(code: number = 27): void {
    if (code !== 27) {
      return;
    }
    this.isOpen = false;
    if (this.modalRef) {
      this.modalRef.destroy();
    }
  }
}
