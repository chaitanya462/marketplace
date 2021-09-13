import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IWorker } from 'app/shared/model/worker.model';
import { getEntities as getWorkers } from 'app/entities/worker/worker.reducer';
import { getEntity, updateEntity, createEntity, reset } from './refereces.reducer';
import { IRefereces } from 'app/shared/model/refereces.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const ReferecesUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const workers = useAppSelector(state => state.worker.entities);
  const referecesEntity = useAppSelector(state => state.refereces.entity);
  const loading = useAppSelector(state => state.refereces.loading);
  const updating = useAppSelector(state => state.refereces.updating);
  const updateSuccess = useAppSelector(state => state.refereces.updateSuccess);

  const handleClose = () => {
    props.history.push('/refereces');
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
      ...referecesEntity,
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
          ...referecesEntity,
          relationType: 'Supervisor',
          workerId: referecesEntity?.worker?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="simplifyMarketplaceApp.refereces.home.createOrEditLabel" data-cy="ReferecesCreateUpdateHeading">
            <Translate contentKey="simplifyMarketplaceApp.refereces.home.createOrEditLabel">Create or edit a Refereces</Translate>
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
                  id="refereces-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('simplifyMarketplaceApp.refereces.name')}
                id="refereces-name"
                name="name"
                data-cy="name"
                type="text"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.refereces.email')}
                id="refereces-email"
                name="email"
                data-cy="email"
                type="text"
                validate={{
                  pattern: {
                    value: /^[^@\s]+@[^@\s]+\.[^@\s]+$/,
                    message: translate('entity.validation.pattern', { pattern: '^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$' }),
                  },
                }}
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.refereces.phone')}
                id="refereces-phone"
                name="phone"
                data-cy="phone"
                type="text"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.refereces.profileLink')}
                id="refereces-profileLink"
                name="profileLink"
                data-cy="profileLink"
                type="text"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.refereces.relationType')}
                id="refereces-relationType"
                name="relationType"
                data-cy="relationType"
                type="select"
              >
                <option value="Supervisor">{translate('simplifyMarketplaceApp.RelationType.Supervisor')}</option>
                <option value="Peer">{translate('simplifyMarketplaceApp.RelationType.Peer')}</option>
                <option value="Other">{translate('simplifyMarketplaceApp.RelationType.Other')}</option>
              </ValidatedField>
              <ValidatedField
                id="refereces-worker"
                name="workerId"
                data-cy="worker"
                label={translate('simplifyMarketplaceApp.refereces.worker')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/refereces" replace color="info">
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

export default ReferecesUpdate;
