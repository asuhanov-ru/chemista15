import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Collection from './collection';
import Media from './media';
import Author from './author';
import Book from './book';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="collection/*" element={<Collection />} />
        <Route path="media/*" element={<Media />} />
        <Route path="author/*" element={<Author />} />
        <Route path="book/*" element={<Book />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
