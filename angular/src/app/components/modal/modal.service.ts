import {Injectable} from '@angular/core';
import {Observable, Subject} from "rxjs";
import {IModalData} from "../../interfaces/IModalData";
import {IMessage} from "../../interfaces/IMessage";
import {SpinnerService} from "../spinner/spinner.service";
import {StatusCode} from "../../model/enum/HttpStatusCode";

@Injectable()
export class ModalService {

  #control$: Subject<IModalData | null> = new Subject();

  public open(data: IModalData | null): void {
    this.#control$.next(data);
    SpinnerService.visibility.next(false);
  }

  public close(): void {
    this.#control$.next(null);
  }

  public get modalSequence$(): Observable<any> {
    return this.#control$.asObservable();
  }

  showByText(code: number, text: string): void {
    this.showMessage(StatusCode.getMessageForStatus(code, text));
  }

  showByStatus(code: number): void {
    this.showMessage(StatusCode.getMessageForStatus(code, ''));
  }

  async showResponse(response: Response): Promise<void> {
    this.showMessage(StatusCode.getMessageForStatus(
      response.status, await response.text()));
  }

  showMessage(message: IMessage) {
    this.show(message);
    const delay: number = 1500;
    setTimeout(() => {
      // this.navigator.redirect(message.href);  //TODO
    }, delay);
  }

  show(message: IMessage) {
    (async (): Promise<void> => {
      const {ItemComponent} = await import('../../components/item/item.component');
      this.open({
        component: ItemComponent,
        context: {
          message: message,
          save: () => this.close(),
          close: () => this.close(),
        },
      });
    })();
  }
}
