import { ICollection } from 'app/shared/model/collection.model';

export interface IMedia {
  id?: number;
  uuid?: string | null;
  fileName?: string | null;
  fileType?: string | null;
  fileDesc?: string | null;
  collection?: ICollection | null;
}

export const defaultValue: Readonly<IMedia> = {};
