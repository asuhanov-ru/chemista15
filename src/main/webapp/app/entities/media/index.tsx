import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Media from './media';
import MediaDetail from './media-detail';
import MediaUpdate from './media-update';
import MediaDeleteDialog from './media-delete-dialog';

const MediaRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Media />} />
    <Route path="new" element={<MediaUpdate />} />
    <Route path=":id">
      <Route index element={<MediaDetail />} />
      <Route path="edit" element={<MediaUpdate />} />
      <Route path="delete" element={<MediaDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default MediaRoutes;
