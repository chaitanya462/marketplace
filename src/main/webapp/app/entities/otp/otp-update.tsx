import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity, updateEntity, createEntity, reset } from './otp.reducer';
import { IOtp } from 'app/shared/model/otp.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const OtpUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const otpEntity = useAppSelector(state => state.otp.entity);
  const loading = useAppSelector(state => state.otp.loading);
  const updating = useAppSelector(state => state.otp.updating);
  const updateSuccess = useAppSelector(state => state.otp.updateSuccess);

  const handleClose = () => {
    props.history.push('/otp');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(props.match.params.id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...otpEntity,
      ...values,
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
          ...otpEntity,
          type: 'Email',
          status: 'Pending',
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="simplifyMarketplaceApp.otp.home.createOrEditLabel" data-cy="OtpCreateUpdateHeading">
            <Translate contentKey="simplifyMarketplaceApp.otp.home.createOrEditLabel">Create or edit a Otp</Translate>
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
                  id="otp-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('simplifyMarketplaceApp.otp.contextId')}
                id="otp-contextId"
                name="contextId"
                data-cy="contextId"
                type="text"
              />
              <ValidatedField label={translate('simplifyMarketplaceApp.otp.otp')} id="otp-otp" name="otp" data-cy="otp" type="text" />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.otp.email')}
                id="otp-email"
                name="email"
                data-cy="email"
                type="text"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.otp.isActive')}
                id="otp-isActive"
                name="isActive"
                data-cy="isActive"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.otp.phone')}
                id="otp-phone"
                name="phone"
                data-cy="phone"
                type="text"
              />
              <ValidatedField label={translate('simplifyMarketplaceApp.otp.type')} id="otp-type" name="type" data-cy="type" type="select">
                <option value="Email">{translate('simplifyMarketplaceApp.OtpType.Email')}</option>
                <option value="Phone">{translate('simplifyMarketplaceApp.OtpType.Phone')}</option>
              </ValidatedField>
              <ValidatedField
                label={translate('simplifyMarketplaceApp.otp.expiryTime')}
                id="otp-expiryTime"
                name="expiryTime"
                data-cy="expiryTime"
                type="date"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.otp.status')}
                id="otp-status"
                name="status"
                data-cy="status"
                type="select"
              >
                <option value="Pending">{translate('simplifyMarketplaceApp.OtpStatus.Pending')}</option>
                <option value="Failed">{translate('simplifyMarketplaceApp.OtpStatus.Failed')}</option>
                <option value="Expired">{translate('simplifyMarketplaceApp.OtpStatus.Expired')}</option>
                <option value="Approved">{translate('simplifyMarketplaceApp.OtpStatus.Approved')}</option>
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/otp" replace color="info">
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

export default OtpUpdate;
