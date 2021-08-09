import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './refereces.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const ReferecesDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const referecesEntity = useAppSelector(state => state.refereces.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="referecesDetailsHeading">
          <Translate contentKey="simplifyMarketplaceApp.refereces.detail.title">Refereces</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{referecesEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="simplifyMarketplaceApp.refereces.name">Name</Translate>
            </span>
          </dt>
          <dd>{referecesEntity.name}</dd>
          <dt>
            <span id="email">
              <Translate contentKey="simplifyMarketplaceApp.refereces.email">Email</Translate>
            </span>
          </dt>
          <dd>{referecesEntity.email}</dd>
          <dt>
            <span id="phone">
              <Translate contentKey="simplifyMarketplaceApp.refereces.phone">Phone</Translate>
            </span>
          </dt>
          <dd>{referecesEntity.phone}</dd>
          <dt>
            <span id="profileLink">
              <Translate contentKey="simplifyMarketplaceApp.refereces.profileLink">Profile Link</Translate>
            </span>
          </dt>
          <dd>{referecesEntity.profileLink}</dd>
          <dt>
            <span id="relationType">
              <Translate contentKey="simplifyMarketplaceApp.refereces.relationType">Relation Type</Translate>
            </span>
          </dt>
          <dd>{referecesEntity.relationType}</dd>
          <dt>
            <Translate contentKey="simplifyMarketplaceApp.refereces.worker">Worker</Translate>
          </dt>
          <dd>{referecesEntity.worker ? referecesEntity.worker.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/refereces" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/refereces/${referecesEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ReferecesDetail;
