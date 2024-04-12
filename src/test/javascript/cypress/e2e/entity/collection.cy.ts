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

describe('Collection e2e test', () => {
  const collectionPageUrl = '/collection';
  const collectionPageUrlPattern = new RegExp('/collection(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const collectionSample = {};

  let collection;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/collections+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/collections').as('postEntityRequest');
    cy.intercept('DELETE', '/api/collections/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (collection) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/collections/${collection.id}`,
      }).then(() => {
        collection = undefined;
      });
    }
  });

  it('Collections menu should load Collections page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('collection');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Collection').should('exist');
    cy.url().should('match', collectionPageUrlPattern);
  });

  describe('Collection page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(collectionPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Collection page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/collection/new$'));
        cy.getEntityCreateUpdateHeading('Collection');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', collectionPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/collections',
          body: collectionSample,
        }).then(({ body }) => {
          collection = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/collections+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/collections?page=0&size=20>; rel="last",<http://localhost/api/collections?page=0&size=20>; rel="first"',
              },
              body: [collection],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(collectionPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Collection page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('collection');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', collectionPageUrlPattern);
      });

      it('edit button click should load edit Collection page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Collection');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', collectionPageUrlPattern);
      });

      it('edit button click should load edit Collection page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Collection');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', collectionPageUrlPattern);
      });

      it('last delete button click should delete instance of Collection', () => {
        cy.intercept('GET', '/api/collections/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('collection').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', collectionPageUrlPattern);

        collection = undefined;
      });
    });
  });

  describe('new Collection page', () => {
    beforeEach(() => {
      cy.visit(`${collectionPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Collection');
    });

    it('should create an instance of Collection', () => {
      cy.get(`[data-cy="uuid"]`).type('5ed69c56-2223-431c-8542-810dd18ec63a');
      cy.get(`[data-cy="uuid"]`).invoke('val').should('match', new RegExp('5ed69c56-2223-431c-8542-810dd18ec63a'));

      cy.get(`[data-cy="name"]`).type('ew');
      cy.get(`[data-cy="name"]`).should('have.value', 'ew');

      cy.get(`[data-cy="description"]`).type('furthermore glory unto');
      cy.get(`[data-cy="description"]`).should('have.value', 'furthermore glory unto');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        collection = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', collectionPageUrlPattern);
    });
  });
});
