import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './otp-attempt.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const OtpAttemptDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const otpAttemptEntity = useAppSelector(state => state.otpAttempt.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="otpAttemptDetailsHeading">
          <Translate contentKey="simplifyMarketplaceApp.otpAttempt.detail.title">OtpAttempt</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{otpAttemptEntity.id}</dd>
          <dt>
            <span id="contextId">
              <Translate contentKey="simplifyMarketplaceApp.otpAttempt.contextId">Context Id</Translate>
            </span>
          </dt>
          <dd>{otpAttemptEntity.contextId}</dd>
          <dt>
            <span id="otp">
              <Translate contentKey="simplifyMarketplaceApp.otpAttempt.otp">Otp</Translate>
            </span>
          </dt>
          <dd>{otpAttemptEntity.otp}</dd>
          <dt>
            <span id="isActive">
              <Translate contentKey="simplifyMarketplaceApp.otpAttempt.isActive">Is Active</Translate>
            </span>
          </dt>
          <dd>{otpAttemptEntity.isActive ? 'true' : 'false'}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="simplifyMarketplaceApp.otpAttempt.status">Status</Translate>
            </span>
          </dt>
          <dd>{otpAttemptEntity.status}</dd>
          <dt>
            <span id="ip">
              <Translate contentKey="simplifyMarketplaceApp.otpAttempt.ip">Ip</Translate>
            </span>
          </dt>
          <dd>{otpAttemptEntity.ip}</dd>
          <dt>
            <span id="coookie">
              <Translate contentKey="simplifyMarketplaceApp.otpAttempt.coookie">Coookie</Translate>
            </span>
          </dt>
          <dd>{otpAttemptEntity.coookie}</dd>
        </dl>
        <Button tag={Link} to="/otp-attempt" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/otp-attempt/${otpAttemptEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default OtpAttemptDetail;
