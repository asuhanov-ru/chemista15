export interface ICollection {
  id?: number;
  uuid?: string | null;
  name?: string | null;
  description?: string | null;
}

export const defaultValue: Readonly<ICollection> = {};
