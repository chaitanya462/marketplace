import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IJobPreference } from 'app/shared/model/job-preference.model';
import { getEntities as getJobPreferences } from 'app/entities/job-preference/job-preference.reducer';
import { IField } from 'app/shared/model/field.model';
import { getEntities as getFields } from 'app/entities/field/field.reducer';
import { getEntity, updateEntity, createEntity, reset } from './field-value.reducer';
import { IFieldValue } from 'app/shared/model/field-value.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const FieldValueUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const jobPreferences = useAppSelector(state => state.jobPreference.entities);
  const fields = useAppSelector(state => state.field.entities);
  const fieldValueEntity = useAppSelector(state => state.fieldValue.entity);
  const loading = useAppSelector(state => state.fieldValue.loading);
  const updating = useAppSelector(state => state.fieldValue.updating);
  const updateSuccess = useAppSelector(state => state.fieldValue.updateSuccess);

  const handleClose = () => {
    props.history.push('/field-value');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getJobPreferences({}));
    dispatch(getFields({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...fieldValueEntity,
      ...values,
      jobpreference: jobPreferences.find(it => it.id.toString() === values.jobpreferenceId.toString()),
      field: fields.find(it => it.id.toString() === values.fieldId.toString()),
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
          ...fieldValueEntity,
          jobpreferenceId: fieldValueEntity?.jobpreference?.id,
          fieldId: fieldValueEntity?.field?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="simplifyMarketplaceApp.fieldValue.home.createOrEditLabel" data-cy="FieldValueCreateUpdateHeading">
            <Translate contentKey="simplifyMarketplaceApp.fieldValue.home.createOrEditLabel">Create or edit a FieldValue</Translate>
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
                  id="field-value-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('simplifyMarketplaceApp.fieldValue.value')}
                id="field-value-value"
                name="value"
                data-cy="value"
                type="text"
              />
              <ValidatedField
                id="field-value-jobpreference"
                name="jobpreferenceId"
                data-cy="jobpreference"
                label={translate('simplifyMarketplaceApp.fieldValue.jobpreference')}
                type="select"
              >
                <option value="" key="0" />
                {jobPreferences
                  ? jobPreferences.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="field-value-field"
                name="fieldId"
                data-cy="field"
                label={translate('simplifyMarketplaceApp.fieldValue.field')}
                type="select"
              >
                <option value="" key="0" />
                {fields
                  ? fields.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/field-value" replace color="info">
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

export default FieldValueUpdate;
