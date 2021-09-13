import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './address.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const AddressDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const addressEntity = useAppSelector(state => state.address.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="addressDetailsHeading">
          <Translate contentKey="simplifyMarketplaceApp.address.detail.title">Address</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{addressEntity.id}</dd>
          <dt>
            <span id="line1">
              <Translate contentKey="simplifyMarketplaceApp.address.line1">Line 1</Translate>
            </span>
          </dt>
          <dd>{addressEntity.line1}</dd>
          <dt>
            <span id="line2">
              <Translate contentKey="simplifyMarketplaceApp.address.line2">Line 2</Translate>
            </span>
          </dt>
          <dd>{addressEntity.line2}</dd>
          <dt>
            <span id="tag">
              <Translate contentKey="simplifyMarketplaceApp.address.tag">Tag</Translate>
            </span>
          </dt>
          <dd>{addressEntity.tag}</dd>
          <dt>
            <Translate contentKey="simplifyMarketplaceApp.address.location">Location</Translate>
          </dt>
          <dd>{addressEntity.location ? addressEntity.location.id : ''}</dd>
          <dt>
            <Translate contentKey="simplifyMarketplaceApp.address.user">User</Translate>
          </dt>
          <dd>{addressEntity.user ? addressEntity.user.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/address" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/address/${addressEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AddressDetail;
