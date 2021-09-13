import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { ISkillsMaster } from 'app/shared/model/skills-master.model';
import { getEntities as getSkillsMasters } from 'app/entities/skills-master/skills-master.reducer';
import { getEntity, updateEntity, createEntity, reset } from './worker.reducer';
import { IWorker } from 'app/shared/model/worker.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const WorkerUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const users = useAppSelector(state => state.userManagement.users);
  const skillsMasters = useAppSelector(state => state.skillsMaster.entities);
  const workerEntity = useAppSelector(state => state.worker.entity);
  const loading = useAppSelector(state => state.worker.loading);
  const updating = useAppSelector(state => state.worker.updating);
  const updateSuccess = useAppSelector(state => state.worker.updateSuccess);

  const handleClose = () => {
    props.history.push('/worker');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getUsers({}));
    dispatch(getSkillsMasters({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...workerEntity,
      ...values,
      skills: mapIdList(values.skills),
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
          ...workerEntity,
          userId: workerEntity?.user?.id,
          skills: workerEntity?.skills?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="simplifyMarketplaceApp.worker.home.createOrEditLabel" data-cy="WorkerCreateUpdateHeading">
            <Translate contentKey="simplifyMarketplaceApp.worker.home.createOrEditLabel">Create or edit a Worker</Translate>
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
                  id="worker-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('simplifyMarketplaceApp.worker.firstName')}
                id="worker-firstName"
                name="firstName"
                data-cy="firstName"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.worker.middleName')}
                id="worker-middleName"
                name="middleName"
                data-cy="middleName"
                type="text"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.worker.lastName')}
                id="worker-lastName"
                name="lastName"
                data-cy="lastName"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.worker.primaryPhone')}
                id="worker-primaryPhone"
                name="primaryPhone"
                data-cy="primaryPhone"
                type="text"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.worker.description')}
                id="worker-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.worker.dateOfBirth')}
                id="worker-dateOfBirth"
                name="dateOfBirth"
                data-cy="dateOfBirth"
                type="date"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.worker.isActive')}
                id="worker-isActive"
                name="isActive"
                data-cy="isActive"
                check
                type="checkbox"
              />
              <ValidatedField
                id="worker-user"
                name="userId"
                data-cy="user"
                label={translate('simplifyMarketplaceApp.worker.user')}
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
              <ValidatedField
                label={translate('simplifyMarketplaceApp.worker.skill')}
                id="worker-skill"
                data-cy="skill"
                type="select"
                multiple
                name="skills"
              >
                <option value="" key="0" />
                {skillsMasters
                  ? skillsMasters.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/worker" replace color="info">
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

export default WorkerUpdate;
