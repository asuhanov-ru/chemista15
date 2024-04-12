export interface IAuthor {
  id?: number;
  uuid?: string | null;
  name?: string | null;
  callsign?: string | null;
  imageContentType?: string | null;
  image?: string | null;
  description?: string | null;
}

export const defaultValue: Readonly<IAuthor> = {};
