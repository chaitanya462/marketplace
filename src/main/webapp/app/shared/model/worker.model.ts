import dayjs from 'dayjs';
import { ICustomUser } from 'app/shared/model/custom-user.model';
import { IFile } from 'app/shared/model/file.model';
import { IEducation } from 'app/shared/model/education.model';
import { ICertificate } from 'app/shared/model/certificate.model';
import { IEmployment } from 'app/shared/model/employment.model';
import { IPortfolio } from 'app/shared/model/portfolio.model';
import { IRefereces } from 'app/shared/model/refereces.model';
import { IJobPreference } from 'app/shared/model/job-preference.model';
import { ISkillsMaster } from 'app/shared/model/skills-master.model';

export interface IWorker {
  id?: number;
  firstName?: string;
  middleName?: string | null;
  lastName?: string;
  primaryPhone?: number;
  description?: string | null;
  dateOfBirth?: string | null;
  createdBy?: string | null;
  createdAt?: string | null;
  updatedBy?: string | null;
  updatedAt?: string | null;
  customUser?: ICustomUser | null;
  files?: IFile[] | null;
  educations?: IEducation[] | null;
  certificates?: ICertificate[] | null;
  employments?: IEmployment[] | null;
  portfolios?: IPortfolio[] | null;
  refereces?: IRefereces[] | null;
  jobPreferences?: IJobPreference[] | null;
  skills?: ISkillsMaster[] | null;
}

export const defaultValue: Readonly<IWorker> = {};
