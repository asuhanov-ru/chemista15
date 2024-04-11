import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICollection } from 'app/shared/model/collection.model';
import { getEntities as getCollections } from 'app/entities/collection/collection.reducer';
import { IMedia } from 'app/shared/model/media.model';
import { getEntity, updateEntity, createEntity, reset } from './media.reducer';

export const MediaUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const collections = useAppSelector(state => state.collection.entities);
  const mediaEntity = useAppSelector(state => state.media.entity);
  const loading = useAppSelector(state => state.media.loading);
  const updating = useAppSelector(state => state.media.updating);
  const updateSuccess = useAppSelector(state => state.media.updateSuccess);

  const handleClose = () => {
    navigate('/media' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getCollections({}));
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

    const entity = {
      ...mediaEntity,
      ...values,
      collection: collections.find(it => it.id.toString() === values.collection?.toString()),
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
          ...mediaEntity,
          collection: mediaEntity?.collection?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="chemista15App.media.home.createOrEditLabel" data-cy="MediaCreateUpdateHeading">
            <Translate contentKey="chemista15App.media.home.createOrEditLabel">Create or edit a Media</Translate>
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
                  id="media-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField label={translate('chemista15App.media.uuid')} id="media-uuid" name="uuid" data-cy="uuid" type="text" />
              <ValidatedField
                label={translate('chemista15App.media.fileName')}
                id="media-fileName"
                name="fileName"
                data-cy="fileName"
                type="text"
              />
              <ValidatedField
                label={translate('chemista15App.media.fileType')}
                id="media-fileType"
                name="fileType"
                data-cy="fileType"
                type="text"
              />
              <ValidatedField
                label={translate('chemista15App.media.fileDesc')}
                id="media-fileDesc"
                name="fileDesc"
                data-cy="fileDesc"
                type="text"
              />
              <ValidatedField
                id="media-collection"
                name="collection"
                data-cy="collection"
                label={translate('chemista15App.media.collection')}
                type="select"
              >
                <option value="" key="0" />
                {collections
                  ? collections.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/media" replace color="info">
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

export default MediaUpdate;
