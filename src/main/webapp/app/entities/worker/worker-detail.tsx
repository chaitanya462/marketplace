import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './worker.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const WorkerDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const workerEntity = useAppSelector(state => state.worker.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="workerDetailsHeading">
          <Translate contentKey="simplifyMarketplaceApp.worker.detail.title">Worker</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{workerEntity.id}</dd>
          <dt>
            <span id="firstName">
              <Translate contentKey="simplifyMarketplaceApp.worker.firstName">First Name</Translate>
            </span>
          </dt>
          <dd>{workerEntity.firstName}</dd>
          <dt>
            <span id="middleName">
              <Translate contentKey="simplifyMarketplaceApp.worker.middleName">Middle Name</Translate>
            </span>
          </dt>
          <dd>{workerEntity.middleName}</dd>
          <dt>
            <span id="lastName">
              <Translate contentKey="simplifyMarketplaceApp.worker.lastName">Last Name</Translate>
            </span>
          </dt>
          <dd>{workerEntity.lastName}</dd>
          <dt>
            <span id="primaryPhone">
              <Translate contentKey="simplifyMarketplaceApp.worker.primaryPhone">Primary Phone</Translate>
            </span>
          </dt>
          <dd>{workerEntity.primaryPhone}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="simplifyMarketplaceApp.worker.description">Description</Translate>
            </span>
          </dt>
          <dd>{workerEntity.description}</dd>
          <dt>
            <span id="dateOfBirth">
              <Translate contentKey="simplifyMarketplaceApp.worker.dateOfBirth">Date Of Birth</Translate>
            </span>
          </dt>
          <dd>
            {workerEntity.dateOfBirth ? <TextFormat value={workerEntity.dateOfBirth} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="isActive">
              <Translate contentKey="simplifyMarketplaceApp.worker.isActive">Is Active</Translate>
            </span>
          </dt>
          <dd>{workerEntity.isActive ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="simplifyMarketplaceApp.worker.user">User</Translate>
          </dt>
          <dd>{workerEntity.user ? workerEntity.user.id : ''}</dd>
          <dt>
            <Translate contentKey="simplifyMarketplaceApp.worker.skill">Skill</Translate>
          </dt>
          <dd>
            {workerEntity.skills
              ? workerEntity.skills.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {workerEntity.skills && i === workerEntity.skills.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/worker" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/worker/${workerEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default WorkerDetail;
