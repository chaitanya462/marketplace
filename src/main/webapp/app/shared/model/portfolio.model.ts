import { IWorker } from 'app/shared/model/worker.model';
import { PortfolioType } from 'app/shared/model/enumerations/portfolio-type.model';

export interface IPortfolio {
  id?: number;
  portfolioURL?: string | null;
  type?: PortfolioType | null;
  worker?: IWorker | null;
}

export const defaultValue: Readonly<IPortfolio> = {};
