import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IJobPreference } from 'app/shared/model/job-preference.model';
import { getEntities as getJobPreferences } from 'app/entities/job-preference/job-preference.reducer';
import { ILocation } from 'app/shared/model/location.model';
import { getEntities as getLocations } from 'app/entities/location/location.reducer';
import { getEntity, updateEntity, createEntity, reset } from './location-prefrence.reducer';
import { ILocationPrefrence } from 'app/shared/model/location-prefrence.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const LocationPrefrenceUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const jobPreferences = useAppSelector(state => state.jobPreference.entities);
  const locations = useAppSelector(state => state.location.entities);
  const locationPrefrenceEntity = useAppSelector(state => state.locationPrefrence.entity);
  const loading = useAppSelector(state => state.locationPrefrence.loading);
  const updating = useAppSelector(state => state.locationPrefrence.updating);
  const updateSuccess = useAppSelector(state => state.locationPrefrence.updateSuccess);

  const handleClose = () => {
    props.history.push('/location-prefrence');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getJobPreferences({}));
    dispatch(getLocations({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...locationPrefrenceEntity,
      ...values,
      worker: jobPreferences.find(it => it.id.toString() === values.workerId.toString()),
      location: locations.find(it => it.id.toString() === values.locationId.toString()),
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
          ...locationPrefrenceEntity,
          workerId: locationPrefrenceEntity?.worker?.id,
          locationId: locationPrefrenceEntity?.location?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="simplifyMarketplaceApp.locationPrefrence.home.createOrEditLabel" data-cy="LocationPrefrenceCreateUpdateHeading">
            <Translate contentKey="simplifyMarketplaceApp.locationPrefrence.home.createOrEditLabel">
              Create or edit a LocationPrefrence
            </Translate>
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
                  id="location-prefrence-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('simplifyMarketplaceApp.locationPrefrence.prefrenceOrder')}
                id="location-prefrence-prefrenceOrder"
                name="prefrenceOrder"
                data-cy="prefrenceOrder"
                type="text"
              />
              <ValidatedField
                id="location-prefrence-worker"
                name="workerId"
                data-cy="worker"
                label={translate('simplifyMarketplaceApp.locationPrefrence.worker')}
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
                id="location-prefrence-location"
                name="locationId"
                data-cy="location"
                label={translate('simplifyMarketplaceApp.locationPrefrence.location')}
                type="select"
              >
                <option value="" key="0" />
                {locations
                  ? locations.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/location-prefrence" replace color="info">
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

export default LocationPrefrenceUpdate;
