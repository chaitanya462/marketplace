import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './certificate.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const CertificateDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const certificateEntity = useAppSelector(state => state.certificate.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="certificateDetailsHeading">
          <Translate contentKey="simplifyMarketplaceApp.certificate.detail.title">Certificate</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{certificateEntity.id}</dd>
          <dt>
            <span id="certificateName">
              <Translate contentKey="simplifyMarketplaceApp.certificate.certificateName">Certificate Name</Translate>
            </span>
          </dt>
          <dd>{certificateEntity.certificateName}</dd>
          <dt>
            <span id="issuer">
              <Translate contentKey="simplifyMarketplaceApp.certificate.issuer">Issuer</Translate>
            </span>
          </dt>
          <dd>{certificateEntity.issuer}</dd>
          <dt>
            <span id="issueYear">
              <Translate contentKey="simplifyMarketplaceApp.certificate.issueYear">Issue Year</Translate>
            </span>
          </dt>
          <dd>{certificateEntity.issueYear}</dd>
          <dt>
            <span id="expiryYear">
              <Translate contentKey="simplifyMarketplaceApp.certificate.expiryYear">Expiry Year</Translate>
            </span>
          </dt>
          <dd>{certificateEntity.expiryYear}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="simplifyMarketplaceApp.certificate.description">Description</Translate>
            </span>
          </dt>
          <dd>{certificateEntity.description}</dd>
          <dt>
            <Translate contentKey="simplifyMarketplaceApp.certificate.worker">Worker</Translate>
          </dt>
          <dd>{certificateEntity.worker ? certificateEntity.worker.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/certificate" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/certificate/${certificateEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CertificateDetail;
