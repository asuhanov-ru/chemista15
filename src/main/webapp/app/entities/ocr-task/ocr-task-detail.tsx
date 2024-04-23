import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './ocr-task.reducer';

export const OcrTaskDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const ocrTaskEntity = useAppSelector(state => state.ocrTask.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="ocrTaskDetailsHeading">
          <Translate contentKey="chemista15App.ocrTask.detail.title">OcrTask</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{ocrTaskEntity.id}</dd>
          <dt>
            <span id="uuid">
              <Translate contentKey="chemista15App.ocrTask.uuid">Uuid</Translate>
            </span>
          </dt>
          <dd>{ocrTaskEntity.uuid}</dd>
          <dt>
            <span id="mediaId">
              <Translate contentKey="chemista15App.ocrTask.mediaId">Media Id</Translate>
            </span>
          </dt>
          <dd>{ocrTaskEntity.mediaId}</dd>
          <dt>
            <span id="pageNumber">
              <Translate contentKey="chemista15App.ocrTask.pageNumber">Page Number</Translate>
            </span>
          </dt>
          <dd>{ocrTaskEntity.pageNumber}</dd>
          <dt>
            <span id="jobStatus">
              <Translate contentKey="chemista15App.ocrTask.jobStatus">Job Status</Translate>
            </span>
          </dt>
          <dd>{ocrTaskEntity.jobStatus}</dd>
          <dt>
            <span id="createTime">
              <Translate contentKey="chemista15App.ocrTask.createTime">Create Time</Translate>
            </span>
          </dt>
          <dd>{ocrTaskEntity.createTime ? <TextFormat value={ocrTaskEntity.createTime} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="startTime">
              <Translate contentKey="chemista15App.ocrTask.startTime">Start Time</Translate>
            </span>
          </dt>
          <dd>{ocrTaskEntity.startTime ? <TextFormat value={ocrTaskEntity.startTime} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="stopTime">
              <Translate contentKey="chemista15App.ocrTask.stopTime">Stop Time</Translate>
            </span>
          </dt>
          <dd>{ocrTaskEntity.stopTime ? <TextFormat value={ocrTaskEntity.stopTime} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
        </dl>
        <Button tag={Link} to="/ocr-task" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/ocr-task/${ocrTaskEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default OcrTaskDetail;
