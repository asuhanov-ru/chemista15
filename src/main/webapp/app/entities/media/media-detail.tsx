import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './media.reducer';

export const MediaDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const mediaEntity = useAppSelector(state => state.media.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="mediaDetailsHeading">
          <Translate contentKey="chemista15App.media.detail.title">Media</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{mediaEntity.id}</dd>
          <dt>
            <span id="uuid">
              <Translate contentKey="chemista15App.media.uuid">Uuid</Translate>
            </span>
          </dt>
          <dd>{mediaEntity.uuid}</dd>
          <dt>
            <span id="fileName">
              <Translate contentKey="chemista15App.media.fileName">File Name</Translate>
            </span>
          </dt>
          <dd>{mediaEntity.fileName}</dd>
          <dt>
            <span id="fileType">
              <Translate contentKey="chemista15App.media.fileType">File Type</Translate>
            </span>
          </dt>
          <dd>{mediaEntity.fileType}</dd>
          <dt>
            <span id="fileDesc">
              <Translate contentKey="chemista15App.media.fileDesc">File Desc</Translate>
            </span>
          </dt>
          <dd>{mediaEntity.fileDesc}</dd>
          <dt>
            <Translate contentKey="chemista15App.media.collection">Collection</Translate>
          </dt>
          <dd>{mediaEntity.collection ? mediaEntity.collection.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/media" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/media/${mediaEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default MediaDetail;
