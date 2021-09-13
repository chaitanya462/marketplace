import { IWorker } from 'app/shared/model/worker.model';

export interface ISkillsMaster {
  id?: number;
  skillName?: string | null;
  workers?: IWorker[] | null;
}

export const defaultValue: Readonly<ISkillsMaster> = {};
