import React, { useState, useEffect } from 'react';
import InfiniteScroll from 'react-infinite-scroller';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, TextFormat, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities, reset } from './job-preference.reducer';
import { IJobPreference } from 'app/shared/model/job-preference.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const JobPreference = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(props.location, ITEMS_PER_PAGE, 'id'), props.location.search)
  );
  const [sorting, setSorting] = useState(false);

  const jobPreferenceList = useAppSelector(state => state.jobPreference.entities);
  const loading = useAppSelector(state => state.jobPreference.loading);
  const totalItems = useAppSelector(state => state.jobPreference.totalItems);
  const links = useAppSelector(state => state.jobPreference.links);
  const entity = useAppSelector(state => state.jobPreference.entity);
  const updateSuccess = useAppSelector(state => state.jobPreference.updateSuccess);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      })
    );
  };

  const resetAll = () => {
    dispatch(reset());
    setPaginationState({
      ...paginationState,
      activePage: 1,
    });
    dispatch(getEntities({}));
  };

  useEffect(() => {
    resetAll();
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      resetAll();
    }
  }, [updateSuccess]);

  useEffect(() => {
    getAllEntities();
  }, [paginationState.activePage]);

  const handleLoadMore = () => {
    if ((window as any).pageYOffset > 0) {
      setPaginationState({
        ...paginationState,
        activePage: paginationState.activePage + 1,
      });
    }
  };

  useEffect(() => {
    if (sorting) {
      getAllEntities();
      setSorting(false);
    }
  }, [sorting]);

  const sort = p => () => {
    dispatch(reset());
    setPaginationState({
      ...paginationState,
      activePage: 1,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
    setSorting(true);
  };

  const handleSyncList = () => {
    resetAll();
  };

  const { match } = props;

  return (
    <div>
      <h2 id="job-preference-heading" data-cy="JobPreferenceHeading">
        <Translate contentKey="simplifyMarketplaceApp.jobPreference.home.title">Job Preferences</Translate>
        <div className="d-flex justify-content-end">
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="simplifyMarketplaceApp.jobPreference.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="simplifyMarketplaceApp.jobPreference.home.createLabel">Create new Job Preference</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        <InfiniteScroll
          pageStart={paginationState.activePage}
          loadMore={handleLoadMore}
          hasMore={paginationState.activePage - 1 < links.next}
          loader={<div className="loader">Loading ...</div>}
          threshold={0}
          initialLoad={false}
        >
          {jobPreferenceList && jobPreferenceList.length > 0 ? (
            <Table responsive>
              <thead>
                <tr>
                  <th className="hand" onClick={sort('id')}>
                    <Translate contentKey="simplifyMarketplaceApp.jobPreference.id">ID</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('hourlyRate')}>
                    <Translate contentKey="simplifyMarketplaceApp.jobPreference.hourlyRate">Hourly Rate</Translate>{' '}
                    <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('dailyRate')}>
                    <Translate contentKey="simplifyMarketplaceApp.jobPreference.dailyRate">Daily Rate</Translate>{' '}
                    <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('monthlyRate')}>
                    <Translate contentKey="simplifyMarketplaceApp.jobPreference.monthlyRate">Monthly Rate</Translate>{' '}
                    <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('hourPerDay')}>
                    <Translate contentKey="simplifyMarketplaceApp.jobPreference.hourPerDay">Hour Per Day</Translate>{' '}
                    <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('hourPerWeek')}>
                    <Translate contentKey="simplifyMarketplaceApp.jobPreference.hourPerWeek">Hour Per Week</Translate>{' '}
                    <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('engagementType')}>
                    <Translate contentKey="simplifyMarketplaceApp.jobPreference.engagementType">Engagement Type</Translate>{' '}
                    <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('employmentType')}>
                    <Translate contentKey="simplifyMarketplaceApp.jobPreference.employmentType">Employment Type</Translate>{' '}
                    <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('locationType')}>
                    <Translate contentKey="simplifyMarketplaceApp.jobPreference.locationType">Location Type</Translate>{' '}
                    <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('availableFrom')}>
                    <Translate contentKey="simplifyMarketplaceApp.jobPreference.availableFrom">Available From</Translate>{' '}
                    <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('availableTo')}>
                    <Translate contentKey="simplifyMarketplaceApp.jobPreference.availableTo">Available To</Translate>{' '}
                    <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('isActive')}>
                    <Translate contentKey="simplifyMarketplaceApp.jobPreference.isActive">Is Active</Translate>{' '}
                    <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>
                    <Translate contentKey="simplifyMarketplaceApp.jobPreference.subCategory">Sub Category</Translate>{' '}
                    <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>
                    <Translate contentKey="simplifyMarketplaceApp.jobPreference.worker">Worker</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {jobPreferenceList.map((jobPreference, i) => (
                  <tr key={`entity-${i}`} data-cy="entityTable">
                    <td>
                      <Button tag={Link} to={`${match.url}/${jobPreference.id}`} color="link" size="sm">
                        {jobPreference.id}
                      </Button>
                    </td>
                    <td>{jobPreference.hourlyRate}</td>
                    <td>{jobPreference.dailyRate}</td>
                    <td>{jobPreference.monthlyRate}</td>
                    <td>{jobPreference.hourPerDay}</td>
                    <td>{jobPreference.hourPerWeek}</td>
                    <td>
                      <Translate contentKey={`simplifyMarketplaceApp.EngagementType.${jobPreference.engagementType}`} />
                    </td>
                    <td>
                      <Translate contentKey={`simplifyMarketplaceApp.EmploymentType.${jobPreference.employmentType}`} />
                    </td>
                    <td>
                      <Translate contentKey={`simplifyMarketplaceApp.LocationType.${jobPreference.locationType}`} />
                    </td>
                    <td>
                      {jobPreference.availableFrom ? (
                        <TextFormat type="date" value={jobPreference.availableFrom} format={APP_LOCAL_DATE_FORMAT} />
                      ) : null}
                    </td>
                    <td>
                      {jobPreference.availableTo ? (
                        <TextFormat type="date" value={jobPreference.availableTo} format={APP_LOCAL_DATE_FORMAT} />
                      ) : null}
                    </td>
                    <td>{jobPreference.isActive ? 'true' : 'false'}</td>
                    <td>
                      {jobPreference.subCategory ? (
                        <Link to={`category/${jobPreference.subCategory.id}`}>{jobPreference.subCategory.id}</Link>
                      ) : (
                        ''
                      )}
                    </td>
                    <td>{jobPreference.worker ? <Link to={`worker/${jobPreference.worker.id}`}>{jobPreference.worker.id}</Link> : ''}</td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`${match.url}/${jobPreference.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button
                          tag={Link}
                          to={`${match.url}/${jobPreference.id}/edit`}
                          color="primary"
                          size="sm"
                          data-cy="entityEditButton"
                        >
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                        <Button
                          tag={Link}
                          to={`${match.url}/${jobPreference.id}/delete`}
                          color="danger"
                          size="sm"
                          data-cy="entityDeleteButton"
                        >
                          <FontAwesomeIcon icon="trash" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.delete">Delete</Translate>
                          </span>
                        </Button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
          ) : (
            !loading && (
              <div className="alert alert-warning">
                <Translate contentKey="simplifyMarketplaceApp.jobPreference.home.notFound">No Job Preferences found</Translate>
              </div>
            )
          )}
        </InfiniteScroll>
      </div>
    </div>
  );
};

export default JobPreference;
