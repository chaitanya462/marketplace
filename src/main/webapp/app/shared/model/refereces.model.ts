import { IWorker } from 'app/shared/model/worker.model';
import { RelationType } from 'app/shared/model/enumerations/relation-type.model';

export interface IRefereces {
  id?: number;
  name?: string | null;
  email?: string | null;
  phone?: string | null;
  profileLink?: string | null;
  relationType?: RelationType | null;
  worker?: IWorker | null;
}

export const defaultValue: Readonly<IRefereces> = {};
