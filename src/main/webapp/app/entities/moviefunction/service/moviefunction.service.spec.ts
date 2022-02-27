import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IMoviefunction, Moviefunction } from '../moviefunction.model';

import { MoviefunctionService } from './moviefunction.service';

describe('Moviefunction Service', () => {
  let service: MoviefunctionService;
  let httpMock: HttpTestingController;
  let elemDefault: IMoviefunction;
  let expectedResult: IMoviefunction | IMoviefunction[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MoviefunctionService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      movie_date_time: 'AAAAAAA',
      active_movie_function: false,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Moviefunction', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Moviefunction()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Moviefunction', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          movie_date_time: 'BBBBBB',
          active_movie_function: true,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Moviefunction', () => {
      const patchObject = Object.assign(
        {
          movie_date_time: 'BBBBBB',
        },
        new Moviefunction()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Moviefunction', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          movie_date_time: 'BBBBBB',
          active_movie_function: true,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Moviefunction', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addMoviefunctionToCollectionIfMissing', () => {
      it('should add a Moviefunction to an empty array', () => {
        const moviefunction: IMoviefunction = { id: 123 };
        expectedResult = service.addMoviefunctionToCollectionIfMissing([], moviefunction);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(moviefunction);
      });

      it('should not add a Moviefunction to an array that contains it', () => {
        const moviefunction: IMoviefunction = { id: 123 };
        const moviefunctionCollection: IMoviefunction[] = [
          {
            ...moviefunction,
          },
          { id: 456 },
        ];
        expectedResult = service.addMoviefunctionToCollectionIfMissing(moviefunctionCollection, moviefunction);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Moviefunction to an array that doesn't contain it", () => {
        const moviefunction: IMoviefunction = { id: 123 };
        const moviefunctionCollection: IMoviefunction[] = [{ id: 456 }];
        expectedResult = service.addMoviefunctionToCollectionIfMissing(moviefunctionCollection, moviefunction);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(moviefunction);
      });

      it('should add only unique Moviefunction to an array', () => {
        const moviefunctionArray: IMoviefunction[] = [{ id: 123 }, { id: 456 }, { id: 16135 }];
        const moviefunctionCollection: IMoviefunction[] = [{ id: 123 }];
        expectedResult = service.addMoviefunctionToCollectionIfMissing(moviefunctionCollection, ...moviefunctionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const moviefunction: IMoviefunction = { id: 123 };
        const moviefunction2: IMoviefunction = { id: 456 };
        expectedResult = service.addMoviefunctionToCollectionIfMissing([], moviefunction, moviefunction2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(moviefunction);
        expect(expectedResult).toContain(moviefunction2);
      });

      it('should accept null and undefined values', () => {
        const moviefunction: IMoviefunction = { id: 123 };
        expectedResult = service.addMoviefunctionToCollectionIfMissing([], null, moviefunction, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(moviefunction);
      });

      it('should return initial array if no Moviefunction is added', () => {
        const moviefunctionCollection: IMoviefunction[] = [{ id: 123 }];
        expectedResult = service.addMoviefunctionToCollectionIfMissing(moviefunctionCollection, undefined, null);
        expect(expectedResult).toEqual(moviefunctionCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
