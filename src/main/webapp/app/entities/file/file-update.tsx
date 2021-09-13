import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IWorker } from 'app/shared/model/worker.model';
import { getEntities as getWorkers } from 'app/entities/worker/worker.reducer';
import { getEntity, updateEntity, createEntity, reset } from './file.reducer';
import { IFile } from 'app/shared/model/file.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const FileUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const workers = useAppSelector(state => state.worker.entities);
  const fileEntity = useAppSelector(state => state.file.entity);
  const loading = useAppSelector(state => state.file.loading);
  const updating = useAppSelector(state => state.file.updating);
  const updateSuccess = useAppSelector(state => state.file.updateSuccess);

  const handleClose = () => {
    props.history.push('/file');
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
      ...fileEntity,
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
          ...fileEntity,
          fileformat: 'PDF',
          filetype: 'Resume',
          workerId: fileEntity?.worker?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="simplifyMarketplaceApp.file.home.createOrEditLabel" data-cy="FileCreateUpdateHeading">
            <Translate contentKey="simplifyMarketplaceApp.file.home.createOrEditLabel">Create or edit a File</Translate>
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
                  id="file-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField label={translate('simplifyMarketplaceApp.file.path')} id="file-path" name="path" data-cy="path" type="text" />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.file.fileformat')}
                id="file-fileformat"
                name="fileformat"
                data-cy="fileformat"
                type="select"
              >
                <option value="PDF">{translate('simplifyMarketplaceApp.FileFormat.PDF')}</option>
                <option value="DOC">{translate('simplifyMarketplaceApp.FileFormat.DOC')}</option>
                <option value="PPT">{translate('simplifyMarketplaceApp.FileFormat.PPT')}</option>
              </ValidatedField>
              <ValidatedField
                label={translate('simplifyMarketplaceApp.file.filetype')}
                id="file-filetype"
                name="filetype"
                data-cy="filetype"
                type="select"
              >
                <option value="Resume">{translate('simplifyMarketplaceApp.FileType.Resume')}</option>
                <option value="Profile_Pic">{translate('simplifyMarketplaceApp.FileType.Profile_Pic')}</option>
                <option value="Other_Pic">{translate('simplifyMarketplaceApp.FileType.Other_Pic')}</option>
                <option value="Cover_Letter">{translate('simplifyMarketplaceApp.FileType.Cover_Letter')}</option>
                <option value="Other">{translate('simplifyMarketplaceApp.FileType.Other')}</option>
              </ValidatedField>
              <ValidatedField label={translate('simplifyMarketplaceApp.file.tag')} id="file-tag" name="tag" data-cy="tag" type="text" />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.file.isDefault')}
                id="file-isDefault"
                name="isDefault"
                data-cy="isDefault"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.file.isResume')}
                id="file-isResume"
                name="isResume"
                data-cy="isResume"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.file.isProfilePic')}
                id="file-isProfilePic"
                name="isProfilePic"
                data-cy="isProfilePic"
                check
                type="checkbox"
              />
              <ValidatedField
                id="file-worker"
                name="workerId"
                data-cy="worker"
                label={translate('simplifyMarketplaceApp.file.worker')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/file" replace color="info">
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

export default FileUpdate;
