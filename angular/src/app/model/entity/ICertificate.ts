import { ITag } from './ITag';

export interface ICertificate {
  id: string;
  name: string;
  description: string;
  shortDescription: string;
  company: string;
  price: number;
  duration: number;
  createDate: Date;
  lastUpdate: Date;
  favorite: boolean;
  checkout: boolean;
  path: string;
  tags: Set<ITag>;
  count: number;
}
