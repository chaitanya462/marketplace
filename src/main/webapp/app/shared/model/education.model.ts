import dayjs from 'dayjs';
import { ISubjectMaster } from 'app/shared/model/subject-master.model';
import { IWorker } from 'app/shared/model/worker.model';
import { MarksType } from 'app/shared/model/enumerations/marks-type.model';
import { EducationGrade } from 'app/shared/model/enumerations/education-grade.model';
import { DegreeType } from 'app/shared/model/enumerations/degree-type.model';

export interface IEducation {
  id?: number;
  degreeName?: string | null;
  institute?: string | null;
  yearOfPassing?: number | null;
  marks?: number | null;
  marksType?: MarksType | null;
  grade?: EducationGrade | null;
  startDate?: string | null;
  endDate?: string | null;
  isComplete?: boolean | null;
  degreeType?: DegreeType | null;
  description?: string | null;
  majorSubject?: ISubjectMaster | null;
  minorSubject?: ISubjectMaster | null;
  worker?: IWorker | null;
}

export const defaultValue: Readonly<IEducation> = {
  isComplete: false,
};
