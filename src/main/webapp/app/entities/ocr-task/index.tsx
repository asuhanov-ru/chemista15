import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import OcrTask from './ocr-task';
import OcrTaskDetail from './ocr-task-detail';
import OcrTaskUpdate from './ocr-task-update';
import OcrTaskDeleteDialog from './ocr-task-delete-dialog';

const OcrTaskRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<OcrTask />} />
    <Route path="new" element={<OcrTaskUpdate />} />
    <Route path=":id">
      <Route index element={<OcrTaskDetail />} />
      <Route path="edit" element={<OcrTaskUpdate />} />
      <Route path="delete" element={<OcrTaskDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default OcrTaskRoutes;
