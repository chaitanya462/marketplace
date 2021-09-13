import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IEmployment } from 'app/shared/model/employment.model';
import { getEntities as getEmployments } from 'app/entities/employment/employment.reducer';
import { getEntity, updateEntity, createEntity, reset } from './location.reducer';
import { ILocation } from 'app/shared/model/location.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const LocationUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const employments = useAppSelector(state => state.employment.entities);
  const locationEntity = useAppSelector(state => state.location.entity);
  const loading = useAppSelector(state => state.location.loading);
  const updating = useAppSelector(state => state.location.updating);
  const updateSuccess = useAppSelector(state => state.location.updateSuccess);

  const handleClose = () => {
    props.history.push('/location');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getEmployments({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...locationEntity,
      ...values,
      employment: employments.find(it => it.id.toString() === values.employmentId.toString()),
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
          ...locationEntity,
          employmentId: locationEntity?.employment?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="simplifyMarketplaceApp.location.home.createOrEditLabel" data-cy="LocationCreateUpdateHeading">
            <Translate contentKey="simplifyMarketplaceApp.location.home.createOrEditLabel">Create or edit a Location</Translate>
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
                  id="location-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('simplifyMarketplaceApp.location.pincode')}
                id="location-pincode"
                name="pincode"
                data-cy="pincode"
                type="text"
                validate={{
                  min: { value: 10000, message: translate('entity.validation.min', { min: 10000 }) },
                  max: { value: 99999, message: translate('entity.validation.max', { max: 99999 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.location.country')}
                id="location-country"
                name="country"
                data-cy="country"
                type="text"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.location.state')}
                id="location-state"
                name="state"
                data-cy="state"
                type="text"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.location.city')}
                id="location-city"
                name="city"
                data-cy="city"
                type="text"
              />
              <ValidatedField
                id="location-employment"
                name="employmentId"
                data-cy="employment"
                label={translate('simplifyMarketplaceApp.location.employment')}
                type="select"
              >
                <option value="" key="0" />
                {employments
                  ? employments.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/location" replace color="info">
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

export default LocationUpdate;
