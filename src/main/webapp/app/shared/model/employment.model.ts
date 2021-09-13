import dayjs from 'dayjs';
import { ILocation } from 'app/shared/model/location.model';
import { IClient } from 'app/shared/model/client.model';
import { IWorker } from 'app/shared/model/worker.model';

export interface IEmployment {
  id?: number;
  jobTitle?: string | null;
  companyName?: string | null;
  startDate?: string | null;
  endDate?: string | null;
  isCurrent?: boolean | null;
  lastSalary?: number | null;
  description?: string | null;
  locations?: ILocation[] | null;
  company?: IClient | null;
  worker?: IWorker | null;
}

export const defaultValue: Readonly<IEmployment> = {
  isCurrent: false,
};
