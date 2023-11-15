import {ICertificate} from "./entity/ICertificate";
import {ITag} from "./entity/ITag";

export class Certificate implements ICertificate {
  checkout!: boolean;
  company!: string;
  createDate!: Date;
  description!: string;
  duration!: number;
  favorite!: boolean;
  id!: string;
  lastUpdate!: Date;
  name!: string;
  path!: string;
  price!: number;
  shortDescription!: string;
  tags: Set<ITag> = new Set<ITag>();
  count: number = 1;
}
