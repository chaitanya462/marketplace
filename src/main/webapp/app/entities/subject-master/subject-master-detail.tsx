import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './subject-master.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const SubjectMasterDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const subjectMasterEntity = useAppSelector(state => state.subjectMaster.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="subjectMasterDetailsHeading">
          <Translate contentKey="simplifyMarketplaceApp.subjectMaster.detail.title">SubjectMaster</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{subjectMasterEntity.id}</dd>
          <dt>
            <span id="subjectName">
              <Translate contentKey="simplifyMarketplaceApp.subjectMaster.subjectName">Subject Name</Translate>
            </span>
          </dt>
          <dd>{subjectMasterEntity.subjectName}</dd>
        </dl>
        <Button tag={Link} to="/subject-master" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/subject-master/${subjectMasterEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default SubjectMasterDetail;
