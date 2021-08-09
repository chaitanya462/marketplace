import dayjs from 'dayjs';
import { IWorker } from 'app/shared/model/worker.model';

export interface ICertificate {
  id?: number;
  certificateName?: string | null;
  issuer?: string | null;
  issueYear?: number | null;
  expiryYear?: number | null;
  description?: string | null;
  createdBy?: string | null;
  createdAt?: string | null;
  updatedBy?: string | null;
  updatedAt?: string | null;
  worker?: IWorker | null;
}

export const defaultValue: Readonly<ICertificate> = {};
