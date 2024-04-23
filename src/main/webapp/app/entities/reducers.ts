import collection from 'app/entities/collection/collection.reducer';
import media from 'app/entities/media/media.reducer';
import author from 'app/entities/author/author.reducer';
import book from 'app/entities/book/book.reducer';
import ocrTask from 'app/entities/ocr-task/ocr-task.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  collection,
  media,
  author,
  book,
  ocrTask,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
