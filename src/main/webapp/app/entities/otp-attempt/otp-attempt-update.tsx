import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity, updateEntity, createEntity, reset } from './otp-attempt.reducer';
import { IOtpAttempt } from 'app/shared/model/otp-attempt.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const OtpAttemptUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const otpAttemptEntity = useAppSelector(state => state.otpAttempt.entity);
  const loading = useAppSelector(state => state.otpAttempt.loading);
  const updating = useAppSelector(state => state.otpAttempt.updating);
  const updateSuccess = useAppSelector(state => state.otpAttempt.updateSuccess);

  const handleClose = () => {
    props.history.push('/otp-attempt');
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
      ...otpAttemptEntity,
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
          ...otpAttemptEntity,
          status: 'Pending',
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="simplifyMarketplaceApp.otpAttempt.home.createOrEditLabel" data-cy="OtpAttemptCreateUpdateHeading">
            <Translate contentKey="simplifyMarketplaceApp.otpAttempt.home.createOrEditLabel">Create or edit a OtpAttempt</Translate>
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
                  id="otp-attempt-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('simplifyMarketplaceApp.otpAttempt.contextId')}
                id="otp-attempt-contextId"
                name="contextId"
                data-cy="contextId"
                type="text"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.otpAttempt.otp')}
                id="otp-attempt-otp"
                name="otp"
                data-cy="otp"
                type="text"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.otpAttempt.isActive')}
                id="otp-attempt-isActive"
                name="isActive"
                data-cy="isActive"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.otpAttempt.status')}
                id="otp-attempt-status"
                name="status"
                data-cy="status"
                type="select"
              >
                <option value="Pending">{translate('simplifyMarketplaceApp.OtpStatus.Pending')}</option>
                <option value="Failed">{translate('simplifyMarketplaceApp.OtpStatus.Failed')}</option>
                <option value="Expired">{translate('simplifyMarketplaceApp.OtpStatus.Expired')}</option>
                <option value="Approved">{translate('simplifyMarketplaceApp.OtpStatus.Approved')}</option>
              </ValidatedField>
              <ValidatedField
                label={translate('simplifyMarketplaceApp.otpAttempt.ip')}
                id="otp-attempt-ip"
                name="ip"
                data-cy="ip"
                type="text"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.otpAttempt.coookie')}
                id="otp-attempt-coookie"
                name="coookie"
                data-cy="coookie"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/otp-attempt" replace color="info">
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

export default OtpAttemptUpdate;
