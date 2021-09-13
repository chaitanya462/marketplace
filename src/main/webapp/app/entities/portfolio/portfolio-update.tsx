import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IWorker } from 'app/shared/model/worker.model';
import { getEntities as getWorkers } from 'app/entities/worker/worker.reducer';
import { getEntity, updateEntity, createEntity, reset } from './portfolio.reducer';
import { IPortfolio } from 'app/shared/model/portfolio.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const PortfolioUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const workers = useAppSelector(state => state.worker.entities);
  const portfolioEntity = useAppSelector(state => state.portfolio.entity);
  const loading = useAppSelector(state => state.portfolio.loading);
  const updating = useAppSelector(state => state.portfolio.updating);
  const updateSuccess = useAppSelector(state => state.portfolio.updateSuccess);

  const handleClose = () => {
    props.history.push('/portfolio');
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
      ...portfolioEntity,
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
          ...portfolioEntity,
          type: 'GIT',
          workerId: portfolioEntity?.worker?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="simplifyMarketplaceApp.portfolio.home.createOrEditLabel" data-cy="PortfolioCreateUpdateHeading">
            <Translate contentKey="simplifyMarketplaceApp.portfolio.home.createOrEditLabel">Create or edit a Portfolio</Translate>
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
                  id="portfolio-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('simplifyMarketplaceApp.portfolio.portfolioURL')}
                id="portfolio-portfolioURL"
                name="portfolioURL"
                data-cy="portfolioURL"
                type="text"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.portfolio.type')}
                id="portfolio-type"
                name="type"
                data-cy="type"
                type="select"
              >
                <option value="GIT">{translate('simplifyMarketplaceApp.PortfolioType.GIT')}</option>
                <option value="Linkedin">{translate('simplifyMarketplaceApp.PortfolioType.Linkedin')}</option>
                <option value="Twitter">{translate('simplifyMarketplaceApp.PortfolioType.Twitter')}</option>
                <option value="Kaggle">{translate('simplifyMarketplaceApp.PortfolioType.Kaggle')}</option>
                <option value="Medium">{translate('simplifyMarketplaceApp.PortfolioType.Medium')}</option>
                <option value="SOF">{translate('simplifyMarketplaceApp.PortfolioType.SOF')}</option>
                <option value="Other">{translate('simplifyMarketplaceApp.PortfolioType.Other')}</option>
              </ValidatedField>
              <ValidatedField
                id="portfolio-worker"
                name="workerId"
                data-cy="worker"
                label={translate('simplifyMarketplaceApp.portfolio.worker')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/portfolio" replace color="info">
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

export default PortfolioUpdate;
