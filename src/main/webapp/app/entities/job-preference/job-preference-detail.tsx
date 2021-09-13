import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './job-preference.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const JobPreferenceDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const jobPreferenceEntity = useAppSelector(state => state.jobPreference.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="jobPreferenceDetailsHeading">
          <Translate contentKey="simplifyMarketplaceApp.jobPreference.detail.title">JobPreference</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{jobPreferenceEntity.id}</dd>
          <dt>
            <span id="hourlyRate">
              <Translate contentKey="simplifyMarketplaceApp.jobPreference.hourlyRate">Hourly Rate</Translate>
            </span>
          </dt>
          <dd>{jobPreferenceEntity.hourlyRate}</dd>
          <dt>
            <span id="dailyRate">
              <Translate contentKey="simplifyMarketplaceApp.jobPreference.dailyRate">Daily Rate</Translate>
            </span>
          </dt>
          <dd>{jobPreferenceEntity.dailyRate}</dd>
          <dt>
            <span id="monthlyRate">
              <Translate contentKey="simplifyMarketplaceApp.jobPreference.monthlyRate">Monthly Rate</Translate>
            </span>
          </dt>
          <dd>{jobPreferenceEntity.monthlyRate}</dd>
          <dt>
            <span id="hourPerDay">
              <Translate contentKey="simplifyMarketplaceApp.jobPreference.hourPerDay">Hour Per Day</Translate>
            </span>
          </dt>
          <dd>{jobPreferenceEntity.hourPerDay}</dd>
          <dt>
            <span id="hourPerWeek">
              <Translate contentKey="simplifyMarketplaceApp.jobPreference.hourPerWeek">Hour Per Week</Translate>
            </span>
          </dt>
          <dd>{jobPreferenceEntity.hourPerWeek}</dd>
          <dt>
            <span id="engagementType">
              <Translate contentKey="simplifyMarketplaceApp.jobPreference.engagementType">Engagement Type</Translate>
            </span>
          </dt>
          <dd>{jobPreferenceEntity.engagementType}</dd>
          <dt>
            <span id="employmentType">
              <Translate contentKey="simplifyMarketplaceApp.jobPreference.employmentType">Employment Type</Translate>
            </span>
          </dt>
          <dd>{jobPreferenceEntity.employmentType}</dd>
          <dt>
            <span id="locationType">
              <Translate contentKey="simplifyMarketplaceApp.jobPreference.locationType">Location Type</Translate>
            </span>
          </dt>
          <dd>{jobPreferenceEntity.locationType}</dd>
          <dt>
            <span id="availableFrom">
              <Translate contentKey="simplifyMarketplaceApp.jobPreference.availableFrom">Available From</Translate>
            </span>
          </dt>
          <dd>
            {jobPreferenceEntity.availableFrom ? (
              <TextFormat value={jobPreferenceEntity.availableFrom} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="availableTo">
              <Translate contentKey="simplifyMarketplaceApp.jobPreference.availableTo">Available To</Translate>
            </span>
          </dt>
          <dd>
            {jobPreferenceEntity.availableTo ? (
              <TextFormat value={jobPreferenceEntity.availableTo} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="isActive">
              <Translate contentKey="simplifyMarketplaceApp.jobPreference.isActive">Is Active</Translate>
            </span>
          </dt>
          <dd>{jobPreferenceEntity.isActive ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="simplifyMarketplaceApp.jobPreference.subCategory">Sub Category</Translate>
          </dt>
          <dd>{jobPreferenceEntity.subCategory ? jobPreferenceEntity.subCategory.id : ''}</dd>
          <dt>
            <Translate contentKey="simplifyMarketplaceApp.jobPreference.worker">Worker</Translate>
          </dt>
          <dd>{jobPreferenceEntity.worker ? jobPreferenceEntity.worker.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/job-preference" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/job-preference/${jobPreferenceEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default JobPreferenceDetail;
