import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './otp.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const OtpDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const otpEntity = useAppSelector(state => state.otp.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="otpDetailsHeading">
          <Translate contentKey="simplifyMarketplaceApp.otp.detail.title">Otp</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{otpEntity.id}</dd>
          <dt>
            <span id="contextId">
              <Translate contentKey="simplifyMarketplaceApp.otp.contextId">Context Id</Translate>
            </span>
          </dt>
          <dd>{otpEntity.contextId}</dd>
          <dt>
            <span id="otp">
              <Translate contentKey="simplifyMarketplaceApp.otp.otp">Otp</Translate>
            </span>
          </dt>
          <dd>{otpEntity.otp}</dd>
          <dt>
            <span id="email">
              <Translate contentKey="simplifyMarketplaceApp.otp.email">Email</Translate>
            </span>
          </dt>
          <dd>{otpEntity.email}</dd>
          <dt>
            <span id="isActive">
              <Translate contentKey="simplifyMarketplaceApp.otp.isActive">Is Active</Translate>
            </span>
          </dt>
          <dd>{otpEntity.isActive ? 'true' : 'false'}</dd>
          <dt>
            <span id="phone">
              <Translate contentKey="simplifyMarketplaceApp.otp.phone">Phone</Translate>
            </span>
          </dt>
          <dd>{otpEntity.phone}</dd>
          <dt>
            <span id="type">
              <Translate contentKey="simplifyMarketplaceApp.otp.type">Type</Translate>
            </span>
          </dt>
          <dd>{otpEntity.type}</dd>
          <dt>
            <span id="expiryTime">
              <Translate contentKey="simplifyMarketplaceApp.otp.expiryTime">Expiry Time</Translate>
            </span>
          </dt>
          <dd>{otpEntity.expiryTime ? <TextFormat value={otpEntity.expiryTime} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="simplifyMarketplaceApp.otp.status">Status</Translate>
            </span>
          </dt>
          <dd>{otpEntity.status}</dd>
        </dl>
        <Button tag={Link} to="/otp" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/otp/${otpEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default OtpDetail;
