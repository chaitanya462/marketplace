import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { ICustomUser } from 'app/shared/model/custom-user.model';
import { getEntities as getCustomUsers } from 'app/entities/custom-user/custom-user.reducer';
import { getEntity, updateEntity, createEntity, reset } from './user-phone.reducer';
import { IUserPhone } from 'app/shared/model/user-phone.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const UserPhoneUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const customUsers = useAppSelector(state => state.customUser.entities);
  const userPhoneEntity = useAppSelector(state => state.userPhone.entity);
  const loading = useAppSelector(state => state.userPhone.loading);
  const updating = useAppSelector(state => state.userPhone.updating);
  const updateSuccess = useAppSelector(state => state.userPhone.updateSuccess);

  const handleClose = () => {
    props.history.push('/user-phone');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getCustomUsers({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...userPhoneEntity,
      ...values,
      customUser: customUsers.find(it => it.id.toString() === values.customUserId.toString()),
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
          ...userPhoneEntity,
          customUserId: userPhoneEntity?.customUser?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="simplifyMarketplaceApp.userPhone.home.createOrEditLabel" data-cy="UserPhoneCreateUpdateHeading">
            <Translate contentKey="simplifyMarketplaceApp.userPhone.home.createOrEditLabel">Create or edit a UserPhone</Translate>
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
                  id="user-phone-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('simplifyMarketplaceApp.userPhone.phone')}
                id="user-phone-phone"
                name="phone"
                data-cy="phone"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  min: { value: 1000000000, message: translate('entity.validation.min', { min: 1000000000 }) },
                  max: { value: 9999999999, message: translate('entity.validation.max', { max: 9999999999 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.userPhone.isActive')}
                id="user-phone-isActive"
                name="isActive"
                data-cy="isActive"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.userPhone.isPrimary')}
                id="user-phone-isPrimary"
                name="isPrimary"
                data-cy="isPrimary"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.userPhone.tag')}
                id="user-phone-tag"
                name="tag"
                data-cy="tag"
                type="text"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.userPhone.createdBy')}
                id="user-phone-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.userPhone.createdAt')}
                id="user-phone-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="date"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.userPhone.updatedBy')}
                id="user-phone-updatedBy"
                name="updatedBy"
                data-cy="updatedBy"
                type="text"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.userPhone.updatedAt')}
                id="user-phone-updatedAt"
                name="updatedAt"
                data-cy="updatedAt"
                type="date"
              />
              <ValidatedField
                id="user-phone-customUser"
                name="customUserId"
                data-cy="customUser"
                label={translate('simplifyMarketplaceApp.userPhone.customUser')}
                type="select"
              >
                <option value="" key="0" />
                {customUsers
                  ? customUsers.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/user-phone" replace color="info">
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

export default UserPhoneUpdate;
