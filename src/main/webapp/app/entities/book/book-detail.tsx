import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './book.reducer';

export const BookDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const bookEntity = useAppSelector(state => state.book.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="bookDetailsHeading">
          <Translate contentKey="chemista15App.book.detail.title">Book</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{bookEntity.id}</dd>
          <dt>
            <span id="uuid">
              <Translate contentKey="chemista15App.book.uuid">Uuid</Translate>
            </span>
          </dt>
          <dd>{bookEntity.uuid}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="chemista15App.book.name">Name</Translate>
            </span>
          </dt>
          <dd>{bookEntity.name}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="chemista15App.book.description">Description</Translate>
            </span>
          </dt>
          <dd>{bookEntity.description}</dd>
          <dt>
            <span id="mediaStartPage">
              <Translate contentKey="chemista15App.book.mediaStartPage">Media Start Page</Translate>
            </span>
          </dt>
          <dd>{bookEntity.mediaStartPage}</dd>
          <dt>
            <span id="mediaEndPage">
              <Translate contentKey="chemista15App.book.mediaEndPage">Media End Page</Translate>
            </span>
          </dt>
          <dd>{bookEntity.mediaEndPage}</dd>
          <dt>
            <Translate contentKey="chemista15App.book.author">Author</Translate>
          </dt>
          <dd>{bookEntity.author ? bookEntity.author.name : ''}</dd>
          <dt>
            <Translate contentKey="chemista15App.book.media">Media</Translate>
          </dt>
          <dd>{bookEntity.media ? bookEntity.media.fileName : ''}</dd>
        </dl>
        <Button tag={Link} to="/book" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/book/${bookEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default BookDetail;
