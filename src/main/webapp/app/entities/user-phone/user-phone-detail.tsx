import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './user-phone.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const UserPhoneDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const userPhoneEntity = useAppSelector(state => state.userPhone.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="userPhoneDetailsHeading">
          <Translate contentKey="simplifyMarketplaceApp.userPhone.detail.title">UserPhone</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{userPhoneEntity.id}</dd>
          <dt>
            <span id="phone">
              <Translate contentKey="simplifyMarketplaceApp.userPhone.phone">Phone</Translate>
            </span>
          </dt>
          <dd>{userPhoneEntity.phone}</dd>
          <dt>
            <span id="isActive">
              <Translate contentKey="simplifyMarketplaceApp.userPhone.isActive">Is Active</Translate>
            </span>
          </dt>
          <dd>{userPhoneEntity.isActive ? 'true' : 'false'}</dd>
          <dt>
            <span id="isPrimary">
              <Translate contentKey="simplifyMarketplaceApp.userPhone.isPrimary">Is Primary</Translate>
            </span>
          </dt>
          <dd>{userPhoneEntity.isPrimary ? 'true' : 'false'}</dd>
          <dt>
            <span id="tag">
              <Translate contentKey="simplifyMarketplaceApp.userPhone.tag">Tag</Translate>
            </span>
          </dt>
          <dd>{userPhoneEntity.tag}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="simplifyMarketplaceApp.userPhone.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{userPhoneEntity.createdBy}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="simplifyMarketplaceApp.userPhone.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>
            {userPhoneEntity.createdAt ? <TextFormat value={userPhoneEntity.createdAt} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="updatedBy">
              <Translate contentKey="simplifyMarketplaceApp.userPhone.updatedBy">Updated By</Translate>
            </span>
          </dt>
          <dd>{userPhoneEntity.updatedBy}</dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="simplifyMarketplaceApp.userPhone.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>
            {userPhoneEntity.updatedAt ? <TextFormat value={userPhoneEntity.updatedAt} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <Translate contentKey="simplifyMarketplaceApp.userPhone.customUser">Custom User</Translate>
          </dt>
          <dd>{userPhoneEntity.customUser ? userPhoneEntity.customUser.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/user-phone" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/user-phone/${userPhoneEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default UserPhoneDetail;
