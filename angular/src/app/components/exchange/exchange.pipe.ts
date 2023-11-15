import {Pipe, PipeTransform} from '@angular/core';

@Pipe({
  name: 'exchange'
})
export class ExchangePipe implements PipeTransform {

  transform(value: number, rate: number): number {
    return value * rate;
  }
}
