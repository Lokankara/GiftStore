import {Pipe, PipeTransform} from '@angular/core';
import {ICertificate} from "../model/entity/ICertificate";
import {ICriteria} from "../interfaces/ICriteria";

@Pipe({
  name: 'searchBy'
})
export class FilterPipe implements PipeTransform {

  public transform(certificates: ICertificate[], criteria: ICriteria): ICertificate[] {
    if (!criteria) {
      return certificates;
    } else {
      return certificates.filter((certificate: ICertificate) => {
        const descMatch = criteria.name
          ? certificate.description.toLowerCase()
          .includes(criteria.name.toLowerCase())
          : true;
        const nameMatch = criteria.name
          ? certificate.name.toLowerCase()
          .includes(criteria.name.toLowerCase())
          : true;
        const tagMatch = criteria.tag
          ? Array.from(certificate.tags)
          .some(tag => tag.name === criteria.tag)
          : true;
        return (descMatch || nameMatch) && tagMatch;
      });
    }
  }
}
