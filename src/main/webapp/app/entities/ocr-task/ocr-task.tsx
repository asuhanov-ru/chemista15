import React, { useState, useEffect } from 'react';
import InfiniteScroll from 'react-infinite-scroll-component';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities, reset } from './ocr-task.reducer';

export const OcrTask = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );
  const [sorting, setSorting] = useState(false);

  const ocrTaskList = useAppSelector(state => state.ocrTask.entities);
  const loading = useAppSelector(state => state.ocrTask.loading);
  const links = useAppSelector(state => state.ocrTask.links);
  const updateSuccess = useAppSelector(state => state.ocrTask.updateSuccess);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  const resetAll = () => {
    dispatch(reset());
    setPaginationState({
      ...paginationState,
      activePage: 1,
    });
    dispatch(getEntities({}));
  };

  useEffect(() => {
    resetAll();
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      resetAll();
    }
  }, [updateSuccess]);

  useEffect(() => {
    getAllEntities();
  }, [paginationState.activePage]);

  const handleLoadMore = () => {
    if ((window as any).pageYOffset > 0) {
      setPaginationState({
        ...paginationState,
        activePage: paginationState.activePage + 1,
      });
    }
  };

  useEffect(() => {
    if (sorting) {
      getAllEntities();
      setSorting(false);
    }
  }, [sorting]);

  const sort = p => () => {
    dispatch(reset());
    setPaginationState({
      ...paginationState,
      activePage: 1,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
    setSorting(true);
  };

  const handleSyncList = () => {
    resetAll();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  return (
    <div>
      <h2 id="ocr-task-heading" data-cy="OcrTaskHeading">
        <Translate contentKey="chemista15App.ocrTask.home.title">Ocr Tasks</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="chemista15App.ocrTask.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/ocr-task/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="chemista15App.ocrTask.home.createLabel">Create new Ocr Task</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        <InfiniteScroll
          dataLength={ocrTaskList ? ocrTaskList.length : 0}
          next={handleLoadMore}
          hasMore={paginationState.activePage - 1 < links.next}
          loader={<div className="loader">Loading ...</div>}
        >
          {ocrTaskList && ocrTaskList.length > 0 ? (
            <Table responsive>
              <thead>
                <tr>
                  <th className="hand" onClick={sort('id')}>
                    <Translate contentKey="chemista15App.ocrTask.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                  </th>
                  <th className="hand" onClick={sort('uuid')}>
                    <Translate contentKey="chemista15App.ocrTask.uuid">Uuid</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('uuid')} />
                  </th>
                  <th className="hand" onClick={sort('mediaId')}>
                    <Translate contentKey="chemista15App.ocrTask.mediaId">Media Id</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('mediaId')} />
                  </th>
                  <th className="hand" onClick={sort('pageNumber')}>
                    <Translate contentKey="chemista15App.ocrTask.pageNumber">Page Number</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('pageNumber')} />
                  </th>
                  <th className="hand" onClick={sort('jobStatus')}>
                    <Translate contentKey="chemista15App.ocrTask.jobStatus">Job Status</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('jobStatus')} />
                  </th>
                  <th className="hand" onClick={sort('createTime')}>
                    <Translate contentKey="chemista15App.ocrTask.createTime">Create Time</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('createTime')} />
                  </th>
                  <th className="hand" onClick={sort('startTime')}>
                    <Translate contentKey="chemista15App.ocrTask.startTime">Start Time</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('startTime')} />
                  </th>
                  <th className="hand" onClick={sort('stopTime')}>
                    <Translate contentKey="chemista15App.ocrTask.stopTime">Stop Time</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('stopTime')} />
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {ocrTaskList.map((ocrTask, i) => (
                  <tr key={`entity-${i}`} data-cy="entityTable">
                    <td>
                      <Button tag={Link} to={`/ocr-task/${ocrTask.id}`} color="link" size="sm">
                        {ocrTask.id}
                      </Button>
                    </td>
                    <td>{ocrTask.uuid}</td>
                    <td>{ocrTask.mediaId}</td>
                    <td>{ocrTask.pageNumber}</td>
                    <td>{ocrTask.jobStatus}</td>
                    <td>{ocrTask.createTime ? <TextFormat type="date" value={ocrTask.createTime} format={APP_DATE_FORMAT} /> : null}</td>
                    <td>{ocrTask.startTime ? <TextFormat type="date" value={ocrTask.startTime} format={APP_DATE_FORMAT} /> : null}</td>
                    <td>{ocrTask.stopTime ? <TextFormat type="date" value={ocrTask.stopTime} format={APP_DATE_FORMAT} /> : null}</td>
                    <td className="text-end">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`/ocr-task/${ocrTask.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`/ocr-task/${ocrTask.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                        <Button
                          onClick={() => (window.location.href = `/ocr-task/${ocrTask.id}/delete`)}
                          color="danger"
                          size="sm"
                          data-cy="entityDeleteButton"
                        >
                          <FontAwesomeIcon icon="trash" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.delete">Delete</Translate>
                          </span>
                        </Button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
          ) : (
            !loading && (
              <div className="alert alert-warning">
                <Translate contentKey="chemista15App.ocrTask.home.notFound">No Ocr Tasks found</Translate>
              </div>
            )
          )}
        </InfiniteScroll>
      </div>
    </div>
  );
};

export default OcrTask;
