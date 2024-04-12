import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IAuthor } from 'app/shared/model/author.model';
import { getEntities as getAuthors } from 'app/entities/author/author.reducer';
import { IMedia } from 'app/shared/model/media.model';
import { getEntities as getMedia } from 'app/entities/media/media.reducer';
import { IBook } from 'app/shared/model/book.model';
import { getEntity, updateEntity, createEntity, reset } from './book.reducer';

export const BookUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const authors = useAppSelector(state => state.author.entities);
  const media = useAppSelector(state => state.media.entities);
  const bookEntity = useAppSelector(state => state.book.entity);
  const loading = useAppSelector(state => state.book.loading);
  const updating = useAppSelector(state => state.book.updating);
  const updateSuccess = useAppSelector(state => state.book.updateSuccess);

  const handleClose = () => {
    navigate('/book' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getAuthors({}));
    dispatch(getMedia({}));
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
    if (values.mediaStartPage !== undefined && typeof values.mediaStartPage !== 'number') {
      values.mediaStartPage = Number(values.mediaStartPage);
    }
    if (values.mediaEndPage !== undefined && typeof values.mediaEndPage !== 'number') {
      values.mediaEndPage = Number(values.mediaEndPage);
    }

    const entity = {
      ...bookEntity,
      ...values,
      author: authors.find(it => it.id.toString() === values.author?.toString()),
      media: media.find(it => it.id.toString() === values.media?.toString()),
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
          ...bookEntity,
          author: bookEntity?.author?.id,
          media: bookEntity?.media?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="chemista15App.book.home.createOrEditLabel" data-cy="BookCreateUpdateHeading">
            <Translate contentKey="chemista15App.book.home.createOrEditLabel">Create or edit a Book</Translate>
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
                  id="book-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField label={translate('chemista15App.book.uuid')} id="book-uuid" name="uuid" data-cy="uuid" type="text" />
              <ValidatedField label={translate('chemista15App.book.name')} id="book-name" name="name" data-cy="name" type="text" />
              <ValidatedField
                label={translate('chemista15App.book.description')}
                id="book-description"
                name="description"
                data-cy="description"
                type="textarea"
              />
              <ValidatedField
                label={translate('chemista15App.book.mediaStartPage')}
                id="book-mediaStartPage"
                name="mediaStartPage"
                data-cy="mediaStartPage"
                type="text"
              />
              <ValidatedField
                label={translate('chemista15App.book.mediaEndPage')}
                id="book-mediaEndPage"
                name="mediaEndPage"
                data-cy="mediaEndPage"
                type="text"
              />
              <ValidatedField id="book-author" name="author" data-cy="author" label={translate('chemista15App.book.author')} type="select">
                <option value="" key="0" />
                {authors
                  ? authors.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="book-media" name="media" data-cy="media" label={translate('chemista15App.book.media')} type="select">
                <option value="" key="0" />
                {media
                  ? media.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.fileName}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/book" replace color="info">
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

export default BookUpdate;
