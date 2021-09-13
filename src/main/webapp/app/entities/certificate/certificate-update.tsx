import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IWorker } from 'app/shared/model/worker.model';
import { getEntities as getWorkers } from 'app/entities/worker/worker.reducer';
import { getEntity, updateEntity, createEntity, reset } from './certificate.reducer';
import { ICertificate } from 'app/shared/model/certificate.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const CertificateUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const workers = useAppSelector(state => state.worker.entities);
  const certificateEntity = useAppSelector(state => state.certificate.entity);
  const loading = useAppSelector(state => state.certificate.loading);
  const updating = useAppSelector(state => state.certificate.updating);
  const updateSuccess = useAppSelector(state => state.certificate.updateSuccess);

  const handleClose = () => {
    props.history.push('/certificate');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getWorkers({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...certificateEntity,
      ...values,
      worker: workers.find(it => it.id.toString() === values.workerId.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...certificateEntity,
          workerId: certificateEntity?.worker?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="simplifyMarketplaceApp.certificate.home.createOrEditLabel" data-cy="CertificateCreateUpdateHeading">
            <Translate contentKey="simplifyMarketplaceApp.certificate.home.createOrEditLabel">Create or edit a Certificate</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="certificate-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('simplifyMarketplaceApp.certificate.certificateName')}
                id="certificate-certificateName"
                name="certificateName"
                data-cy="certificateName"
                type="text"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.certificate.issuer')}
                id="certificate-issuer"
                name="issuer"
                data-cy="issuer"
                type="text"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.certificate.issueYear')}
                id="certificate-issueYear"
                name="issueYear"
                data-cy="issueYear"
                type="text"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.certificate.expiryYear')}
                id="certificate-expiryYear"
                name="expiryYear"
                data-cy="expiryYear"
                type="text"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.certificate.description')}
                id="certificate-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField
                id="certificate-worker"
                name="workerId"
                data-cy="worker"
                label={translate('simplifyMarketplaceApp.certificate.worker')}
                type="select"
              >
                <option value="" key="0" />
                {workers
                  ? workers.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/certificate" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default CertificateUpdate;
