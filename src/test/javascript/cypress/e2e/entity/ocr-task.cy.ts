import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('OcrTask e2e test', () => {
  const ocrTaskPageUrl = '/ocr-task';
  const ocrTaskPageUrlPattern = new RegExp('/ocr-task(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const ocrTaskSample = {};

  let ocrTask;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/ocr-tasks+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/ocr-tasks').as('postEntityRequest');
    cy.intercept('DELETE', '/api/ocr-tasks/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (ocrTask) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/ocr-tasks/${ocrTask.id}`,
      }).then(() => {
        ocrTask = undefined;
      });
    }
  });

  it('OcrTasks menu should load OcrTasks page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('ocr-task');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('OcrTask').should('exist');
    cy.url().should('match', ocrTaskPageUrlPattern);
  });

  describe('OcrTask page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(ocrTaskPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create OcrTask page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/ocr-task/new$'));
        cy.getEntityCreateUpdateHeading('OcrTask');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', ocrTaskPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/ocr-tasks',
          body: ocrTaskSample,
        }).then(({ body }) => {
          ocrTask = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/ocr-tasks+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/ocr-tasks?page=0&size=20>; rel="last",<http://localhost/api/ocr-tasks?page=0&size=20>; rel="first"',
              },
              body: [ocrTask],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(ocrTaskPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details OcrTask page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('ocrTask');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', ocrTaskPageUrlPattern);
      });

      it('edit button click should load edit OcrTask page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('OcrTask');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', ocrTaskPageUrlPattern);
      });

      it('edit button click should load edit OcrTask page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('OcrTask');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', ocrTaskPageUrlPattern);
      });

      it('last delete button click should delete instance of OcrTask', () => {
        cy.intercept('GET', '/api/ocr-tasks/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('ocrTask').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', ocrTaskPageUrlPattern);

        ocrTask = undefined;
      });
    });
  });

  describe('new OcrTask page', () => {
    beforeEach(() => {
      cy.visit(`${ocrTaskPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('OcrTask');
    });

    it('should create an instance of OcrTask', () => {
      cy.get(`[data-cy="uuid"]`).type('24e7d494-790d-457a-a305-2669806816ee');
      cy.get(`[data-cy="uuid"]`).invoke('val').should('match', new RegExp('24e7d494-790d-457a-a305-2669806816ee'));

      cy.get(`[data-cy="mediaId"]`).type('28510');
      cy.get(`[data-cy="mediaId"]`).should('have.value', '28510');

      cy.get(`[data-cy="pageNumber"]`).type('18776');
      cy.get(`[data-cy="pageNumber"]`).should('have.value', '18776');

      cy.get(`[data-cy="jobStatus"]`).type('disgusting');
      cy.get(`[data-cy="jobStatus"]`).should('have.value', 'disgusting');

      cy.get(`[data-cy="createTime"]`).type('2024-04-15T04:41');
      cy.get(`[data-cy="createTime"]`).blur();
      cy.get(`[data-cy="createTime"]`).should('have.value', '2024-04-15T04:41');

      cy.get(`[data-cy="startTime"]`).type('2024-04-14T18:29');
      cy.get(`[data-cy="startTime"]`).blur();
      cy.get(`[data-cy="startTime"]`).should('have.value', '2024-04-14T18:29');

      cy.get(`[data-cy="stopTime"]`).type('2024-04-15T12:49');
      cy.get(`[data-cy="stopTime"]`).blur();
      cy.get(`[data-cy="stopTime"]`).should('have.value', '2024-04-15T12:49');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        ocrTask = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', ocrTaskPageUrlPattern);
    });
  });
});
