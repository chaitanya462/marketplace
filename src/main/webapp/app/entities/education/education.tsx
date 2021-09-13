import React, { useState, useEffect } from 'react';
import InfiniteScroll from 'react-infinite-scroller';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, TextFormat, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities, reset } from './education.reducer';
import { IEducation } from 'app/shared/model/education.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const Education = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(props.location, ITEMS_PER_PAGE, 'id'), props.location.search)
  );
  const [sorting, setSorting] = useState(false);

  const educationList = useAppSelector(state => state.education.entities);
  const loading = useAppSelector(state => state.education.loading);
  const totalItems = useAppSelector(state => state.education.totalItems);
  const links = useAppSelector(state => state.education.links);
  const entity = useAppSelector(state => state.education.entity);
  const updateSuccess = useAppSelector(state => state.education.updateSuccess);

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
      <h2 id="education-heading" data-cy="EducationHeading">
        <Translate contentKey="simplifyMarketplaceApp.education.home.title">Educations</Translate>
        <div className="d-flex justify-content-end">
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="simplifyMarketplaceApp.education.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="simplifyMarketplaceApp.education.home.createLabel">Create new Education</Translate>
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
          {educationList && educationList.length > 0 ? (
            <Table responsive>
              <thead>
                <tr>
                  <th className="hand" onClick={sort('id')}>
                    <Translate contentKey="simplifyMarketplaceApp.education.id">ID</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('degreeName')}>
                    <Translate contentKey="simplifyMarketplaceApp.education.degreeName">Degree Name</Translate>{' '}
                    <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('institute')}>
                    <Translate contentKey="simplifyMarketplaceApp.education.institute">Institute</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('yearOfPassing')}>
                    <Translate contentKey="simplifyMarketplaceApp.education.yearOfPassing">Year Of Passing</Translate>{' '}
                    <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('marks')}>
                    <Translate contentKey="simplifyMarketplaceApp.education.marks">Marks</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('marksType')}>
                    <Translate contentKey="simplifyMarketplaceApp.education.marksType">Marks Type</Translate>{' '}
                    <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('grade')}>
                    <Translate contentKey="simplifyMarketplaceApp.education.grade">Grade</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('startDate')}>
                    <Translate contentKey="simplifyMarketplaceApp.education.startDate">Start Date</Translate>{' '}
                    <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('endDate')}>
                    <Translate contentKey="simplifyMarketplaceApp.education.endDate">End Date</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('isComplete')}>
                    <Translate contentKey="simplifyMarketplaceApp.education.isComplete">Is Complete</Translate>{' '}
                    <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('degreeType')}>
                    <Translate contentKey="simplifyMarketplaceApp.education.degreeType">Degree Type</Translate>{' '}
                    <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('description')}>
                    <Translate contentKey="simplifyMarketplaceApp.education.description">Description</Translate>{' '}
                    <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>
                    <Translate contentKey="simplifyMarketplaceApp.education.majorSubject">Major Subject</Translate>{' '}
                    <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>
                    <Translate contentKey="simplifyMarketplaceApp.education.minorSubject">Minor Subject</Translate>{' '}
                    <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>
                    <Translate contentKey="simplifyMarketplaceApp.education.worker">Worker</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {educationList.map((education, i) => (
                  <tr key={`entity-${i}`} data-cy="entityTable">
                    <td>
                      <Button tag={Link} to={`${match.url}/${education.id}`} color="link" size="sm">
                        {education.id}
                      </Button>
                    </td>
                    <td>{education.degreeName}</td>
                    <td>{education.institute}</td>
                    <td>{education.yearOfPassing}</td>
                    <td>{education.marks}</td>
                    <td>
                      <Translate contentKey={`simplifyMarketplaceApp.MarksType.${education.marksType}`} />
                    </td>
                    <td>
                      <Translate contentKey={`simplifyMarketplaceApp.EducationGrade.${education.grade}`} />
                    </td>
                    <td>
                      {education.startDate ? <TextFormat type="date" value={education.startDate} format={APP_LOCAL_DATE_FORMAT} /> : null}
                    </td>
                    <td>
                      {education.endDate ? <TextFormat type="date" value={education.endDate} format={APP_LOCAL_DATE_FORMAT} /> : null}
                    </td>
                    <td>{education.isComplete ? 'true' : 'false'}</td>
                    <td>
                      <Translate contentKey={`simplifyMarketplaceApp.DegreeType.${education.degreeType}`} />
                    </td>
                    <td>{education.description}</td>
                    <td>
                      {education.majorSubject ? (
                        <Link to={`subject-master/${education.majorSubject.id}`}>{education.majorSubject.id}</Link>
                      ) : (
                        ''
                      )}
                    </td>
                    <td>
                      {education.minorSubject ? (
                        <Link to={`subject-master/${education.minorSubject.id}`}>{education.minorSubject.id}</Link>
                      ) : (
                        ''
                      )}
                    </td>
                    <td>{education.worker ? <Link to={`worker/${education.worker.id}`}>{education.worker.id}</Link> : ''}</td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`${match.url}/${education.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${education.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${education.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
                <Translate contentKey="simplifyMarketplaceApp.education.home.notFound">No Educations found</Translate>
              </div>
            )
          )}
        </InfiniteScroll>
      </div>
    </div>
  );
};

export default Education;
