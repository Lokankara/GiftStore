import {FilterPipe} from './filter.pipe';
import {ICertificate} from "../model/entity/ICertificate";

describe('FilterPipe', () => {
  let pipe: FilterPipe;
  let mockCertificates: ICertificate[];

  beforeEach(() => {
    pipe = new FilterPipe();
  });

  it('create an instance', () => {
    expect(pipe).toBeTruthy();
  });

  it('should filter certificates', () => {
    const mockCriteria = {name: 'test', tag: 'test'};

    const result = pipe.transform(mockCertificates, mockCriteria);

    expect(result).toEqual(mockCertificates);
  });

  it('should return all certificates if no criteria is provided', () => {

    const result = pipe.transform(mockCertificates, {name: '', tag: ''});

    expect(result).toEqual(mockCertificates);
  });
});
