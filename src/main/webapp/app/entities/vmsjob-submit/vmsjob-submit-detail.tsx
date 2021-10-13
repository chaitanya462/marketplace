import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './vmsjob-submit.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const VmsjobSubmitDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const vmsjobSubmitEntity = useAppSelector(state => state.vmsjobSubmit.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="vmsjobSubmitDetailsHeading">
          <Translate contentKey="simplifyMarketplaceApp.vmsjobSubmit.detail.title">VmsjobSubmit</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{vmsjobSubmitEntity.id}</dd>
          <dt>
            <span id="vmsjobsubmitName">
              <Translate contentKey="simplifyMarketplaceApp.vmsjobSubmit.vmsjobsubmitName">Vmsjobsubmit Name</Translate>
            </span>
          </dt>
          <dd>{vmsjobSubmitEntity.vmsjobsubmitName}</dd>
        </dl>
        <Button tag={Link} to="/vmsjob-submit" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/vmsjob-submit/${vmsjobSubmitEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default VmsjobSubmitDetail;
