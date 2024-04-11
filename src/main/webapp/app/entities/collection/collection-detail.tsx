import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './collection.reducer';

export const CollectionDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const collectionEntity = useAppSelector(state => state.collection.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="collectionDetailsHeading">
          <Translate contentKey="chemista15App.collection.detail.title">Collection</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{collectionEntity.id}</dd>
          <dt>
            <span id="uuid">
              <Translate contentKey="chemista15App.collection.uuid">Uuid</Translate>
            </span>
          </dt>
          <dd>{collectionEntity.uuid}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="chemista15App.collection.name">Name</Translate>
            </span>
          </dt>
          <dd>{collectionEntity.name}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="chemista15App.collection.description">Description</Translate>
            </span>
          </dt>
          <dd>{collectionEntity.description}</dd>
        </dl>
        <Button tag={Link} to="/collection" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/collection/${collectionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CollectionDetail;
