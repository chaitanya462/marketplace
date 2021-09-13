import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { getEntity, updateEntity, createEntity, reset } from './client.reducer';
import { IClient } from 'app/shared/model/client.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const ClientUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const users = useAppSelector(state => state.userManagement.users);
  const clientEntity = useAppSelector(state => state.client.entity);
  const loading = useAppSelector(state => state.client.loading);
  const updating = useAppSelector(state => state.client.updating);
  const updateSuccess = useAppSelector(state => state.client.updateSuccess);

  const handleClose = () => {
    props.history.push('/client');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getUsers({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...clientEntity,
      ...values,
      user: users.find(it => it.id.toString() === values.userId.toString()),
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
          ...clientEntity,
          companyType: 'IT',
          userId: clientEntity?.user?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="simplifyMarketplaceApp.client.home.createOrEditLabel" data-cy="ClientCreateUpdateHeading">
            <Translate contentKey="simplifyMarketplaceApp.client.home.createOrEditLabel">Create or edit a Client</Translate>
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
                  id="client-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('simplifyMarketplaceApp.client.companyName')}
                id="client-companyName"
                name="companyName"
                data-cy="companyName"
                type="text"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.client.companyWebsite')}
                id="client-companyWebsite"
                name="companyWebsite"
                data-cy="companyWebsite"
                type="text"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.client.companyType')}
                id="client-companyType"
                name="companyType"
                data-cy="companyType"
                type="select"
              >
                <option value="IT">{translate('simplifyMarketplaceApp.CompanyType.IT')}</option>
                <option value="Consultant">{translate('simplifyMarketplaceApp.CompanyType.Consultant')}</option>
                <option value="Hospital">{translate('simplifyMarketplaceApp.CompanyType.Hospital')}</option>
                <option value="MediaHouse">{translate('simplifyMarketplaceApp.CompanyType.MediaHouse')}</option>
              </ValidatedField>
              <ValidatedField
                label={translate('simplifyMarketplaceApp.client.primaryPhone')}
                id="client-primaryPhone"
                name="primaryPhone"
                data-cy="primaryPhone"
                type="text"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.client.isActive')}
                id="client-isActive"
                name="isActive"
                data-cy="isActive"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.client.description')}
                id="client-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.client.startDate')}
                id="client-startDate"
                name="startDate"
                data-cy="startDate"
                type="date"
              />
              <ValidatedField
                id="client-user"
                name="userId"
                data-cy="user"
                label={translate('simplifyMarketplaceApp.client.user')}
                type="select"
              >
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/client" replace color="info">
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

export default ClientUpdate;
