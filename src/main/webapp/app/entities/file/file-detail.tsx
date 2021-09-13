import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './file.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const FileDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const fileEntity = useAppSelector(state => state.file.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="fileDetailsHeading">
          <Translate contentKey="simplifyMarketplaceApp.file.detail.title">File</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{fileEntity.id}</dd>
          <dt>
            <span id="path">
              <Translate contentKey="simplifyMarketplaceApp.file.path">Path</Translate>
            </span>
          </dt>
          <dd>{fileEntity.path}</dd>
          <dt>
            <span id="fileformat">
              <Translate contentKey="simplifyMarketplaceApp.file.fileformat">Fileformat</Translate>
            </span>
          </dt>
          <dd>{fileEntity.fileformat}</dd>
          <dt>
            <span id="filetype">
              <Translate contentKey="simplifyMarketplaceApp.file.filetype">Filetype</Translate>
            </span>
          </dt>
          <dd>{fileEntity.filetype}</dd>
          <dt>
            <span id="tag">
              <Translate contentKey="simplifyMarketplaceApp.file.tag">Tag</Translate>
            </span>
          </dt>
          <dd>{fileEntity.tag}</dd>
          <dt>
            <span id="isDefault">
              <Translate contentKey="simplifyMarketplaceApp.file.isDefault">Is Default</Translate>
            </span>
          </dt>
          <dd>{fileEntity.isDefault ? 'true' : 'false'}</dd>
          <dt>
            <span id="isResume">
              <Translate contentKey="simplifyMarketplaceApp.file.isResume">Is Resume</Translate>
            </span>
          </dt>
          <dd>{fileEntity.isResume ? 'true' : 'false'}</dd>
          <dt>
            <span id="isProfilePic">
              <Translate contentKey="simplifyMarketplaceApp.file.isProfilePic">Is Profile Pic</Translate>
            </span>
          </dt>
          <dd>{fileEntity.isProfilePic ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="simplifyMarketplaceApp.file.worker">Worker</Translate>
          </dt>
          <dd>{fileEntity.worker ? fileEntity.worker.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/file" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/file/${fileEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default FileDetail;
