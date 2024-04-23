import dayjs from 'dayjs';

export interface IOcrTask {
  id?: number;
  uuid?: string | null;
  mediaId?: number | null;
  pageNumber?: number | null;
  jobStatus?: string | null;
  createTime?: dayjs.Dayjs | null;
  startTime?: dayjs.Dayjs | null;
  stopTime?: dayjs.Dayjs | null;
}

export const defaultValue: Readonly<IOcrTask> = {};
