import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './user-email.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const UserEmailDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const userEmailEntity = useAppSelector(state => state.userEmail.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="userEmailDetailsHeading">
          <Translate contentKey="simplifyMarketplaceApp.userEmail.detail.title">UserEmail</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{userEmailEntity.id}</dd>
          <dt>
            <span id="email">
              <Translate contentKey="simplifyMarketplaceApp.userEmail.email">Email</Translate>
            </span>
          </dt>
          <dd>{userEmailEntity.email}</dd>
          <dt>
            <span id="isActive">
              <Translate contentKey="simplifyMarketplaceApp.userEmail.isActive">Is Active</Translate>
            </span>
          </dt>
          <dd>{userEmailEntity.isActive ? 'true' : 'false'}</dd>
          <dt>
            <span id="isPrimary">
              <Translate contentKey="simplifyMarketplaceApp.userEmail.isPrimary">Is Primary</Translate>
            </span>
          </dt>
          <dd>{userEmailEntity.isPrimary ? 'true' : 'false'}</dd>
          <dt>
            <span id="tag">
              <Translate contentKey="simplifyMarketplaceApp.userEmail.tag">Tag</Translate>
            </span>
          </dt>
          <dd>{userEmailEntity.tag}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="simplifyMarketplaceApp.userEmail.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{userEmailEntity.createdBy}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="simplifyMarketplaceApp.userEmail.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>
            {userEmailEntity.createdAt ? <TextFormat value={userEmailEntity.createdAt} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="updatedBy">
              <Translate contentKey="simplifyMarketplaceApp.userEmail.updatedBy">Updated By</Translate>
            </span>
          </dt>
          <dd>{userEmailEntity.updatedBy}</dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="simplifyMarketplaceApp.userEmail.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>
            {userEmailEntity.updatedAt ? <TextFormat value={userEmailEntity.updatedAt} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <Translate contentKey="simplifyMarketplaceApp.userEmail.customUser">Custom User</Translate>
          </dt>
          <dd>{userEmailEntity.customUser ? userEmailEntity.customUser.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/user-email" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/user-email/${userEmailEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default UserEmailDetail;
