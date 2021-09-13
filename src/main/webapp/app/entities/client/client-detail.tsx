import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './client.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const ClientDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const clientEntity = useAppSelector(state => state.client.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="clientDetailsHeading">
          <Translate contentKey="simplifyMarketplaceApp.client.detail.title">Client</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{clientEntity.id}</dd>
          <dt>
            <span id="companyName">
              <Translate contentKey="simplifyMarketplaceApp.client.companyName">Company Name</Translate>
            </span>
          </dt>
          <dd>{clientEntity.companyName}</dd>
          <dt>
            <span id="companyWebsite">
              <Translate contentKey="simplifyMarketplaceApp.client.companyWebsite">Company Website</Translate>
            </span>
          </dt>
          <dd>{clientEntity.companyWebsite}</dd>
          <dt>
            <span id="companyType">
              <Translate contentKey="simplifyMarketplaceApp.client.companyType">Company Type</Translate>
            </span>
          </dt>
          <dd>{clientEntity.companyType}</dd>
          <dt>
            <span id="primaryPhone">
              <Translate contentKey="simplifyMarketplaceApp.client.primaryPhone">Primary Phone</Translate>
            </span>
          </dt>
          <dd>{clientEntity.primaryPhone}</dd>
          <dt>
            <span id="isActive">
              <Translate contentKey="simplifyMarketplaceApp.client.isActive">Is Active</Translate>
            </span>
          </dt>
          <dd>{clientEntity.isActive ? 'true' : 'false'}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="simplifyMarketplaceApp.client.description">Description</Translate>
            </span>
          </dt>
          <dd>{clientEntity.description}</dd>
          <dt>
            <span id="startDate">
              <Translate contentKey="simplifyMarketplaceApp.client.startDate">Start Date</Translate>
            </span>
          </dt>
          <dd>
            {clientEntity.startDate ? <TextFormat value={clientEntity.startDate} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <Translate contentKey="simplifyMarketplaceApp.client.user">User</Translate>
          </dt>
          <dd>{clientEntity.user ? clientEntity.user.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/client" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/client/${clientEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ClientDetail;
