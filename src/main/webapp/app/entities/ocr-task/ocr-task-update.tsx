import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IOcrTask } from 'app/shared/model/ocr-task.model';
import { getEntity, updateEntity, createEntity, reset } from './ocr-task.reducer';

export const OcrTaskUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const ocrTaskEntity = useAppSelector(state => state.ocrTask.entity);
  const loading = useAppSelector(state => state.ocrTask.loading);
  const updating = useAppSelector(state => state.ocrTask.updating);
  const updateSuccess = useAppSelector(state => state.ocrTask.updateSuccess);

  const handleClose = () => {
    navigate('/ocr-task');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.mediaId !== undefined && typeof values.mediaId !== 'number') {
      values.mediaId = Number(values.mediaId);
    }
    if (values.pageNumber !== undefined && typeof values.pageNumber !== 'number') {
      values.pageNumber = Number(values.pageNumber);
    }
    values.createTime = convertDateTimeToServer(values.createTime);
    values.startTime = convertDateTimeToServer(values.startTime);
    values.stopTime = convertDateTimeToServer(values.stopTime);

    const entity = {
      ...ocrTaskEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          createTime: displayDefaultDateTime(),
          startTime: displayDefaultDateTime(),
          stopTime: displayDefaultDateTime(),
        }
      : {
          ...ocrTaskEntity,
          createTime: convertDateTimeFromServer(ocrTaskEntity.createTime),
          startTime: convertDateTimeFromServer(ocrTaskEntity.startTime),
          stopTime: convertDateTimeFromServer(ocrTaskEntity.stopTime),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="chemista15App.ocrTask.home.createOrEditLabel" data-cy="OcrTaskCreateUpdateHeading">
            <Translate contentKey="chemista15App.ocrTask.home.createOrEditLabel">Create or edit a OcrTask</Translate>
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
                  id="ocr-task-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField label={translate('chemista15App.ocrTask.uuid')} id="ocr-task-uuid" name="uuid" data-cy="uuid" type="text" />
              <ValidatedField
                label={translate('chemista15App.ocrTask.mediaId')}
                id="ocr-task-mediaId"
                name="mediaId"
                data-cy="mediaId"
                type="text"
              />
              <ValidatedField
                label={translate('chemista15App.ocrTask.pageNumber')}
                id="ocr-task-pageNumber"
                name="pageNumber"
                data-cy="pageNumber"
                type="text"
              />
              <ValidatedField
                label={translate('chemista15App.ocrTask.jobStatus')}
                id="ocr-task-jobStatus"
                name="jobStatus"
                data-cy="jobStatus"
                type="text"
              />
              <ValidatedField
                label={translate('chemista15App.ocrTask.createTime')}
                id="ocr-task-createTime"
                name="createTime"
                data-cy="createTime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('chemista15App.ocrTask.startTime')}
                id="ocr-task-startTime"
                name="startTime"
                data-cy="startTime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('chemista15App.ocrTask.stopTime')}
                id="ocr-task-stopTime"
                name="stopTime"
                data-cy="stopTime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/ocr-task" replace color="info">
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

export default OcrTaskUpdate;
