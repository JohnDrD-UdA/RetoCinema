import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IHall, Hall } from '../hall.model';

import { HallService } from './hall.service';

describe('Hall Service', () => {
  let service: HallService;
  let httpMock: HttpTestingController;
  let elemDefault: IHall;
  let expectedResult: IHall | IHall[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(HallService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      cols_hall: 0,
      rows_hall: 0,
      name: 'AAAAAAA',
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

    it('should create a Hall', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Hall()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Hall', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          cols_hall: 1,
          rows_hall: 1,
          name: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Hall', () => {
      const patchObject = Object.assign(
        {
          cols_hall: 1,
          rows_hall: 1,
          name: 'BBBBBB',
        },
        new Hall()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Hall', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          cols_hall: 1,
          rows_hall: 1,
          name: 'BBBBBB',
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

    it('should delete a Hall', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addHallToCollectionIfMissing', () => {
      it('should add a Hall to an empty array', () => {
        const hall: IHall = { id: 123 };
        expectedResult = service.addHallToCollectionIfMissing([], hall);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(hall);
      });

      it('should not add a Hall to an array that contains it', () => {
        const hall: IHall = { id: 123 };
        const hallCollection: IHall[] = [
          {
            ...hall,
          },
          { id: 456 },
        ];
        expectedResult = service.addHallToCollectionIfMissing(hallCollection, hall);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Hall to an array that doesn't contain it", () => {
        const hall: IHall = { id: 123 };
        const hallCollection: IHall[] = [{ id: 456 }];
        expectedResult = service.addHallToCollectionIfMissing(hallCollection, hall);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(hall);
      });

      it('should add only unique Hall to an array', () => {
        const hallArray: IHall[] = [{ id: 123 }, { id: 456 }, { id: 25421 }];
        const hallCollection: IHall[] = [{ id: 123 }];
        expectedResult = service.addHallToCollectionIfMissing(hallCollection, ...hallArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const hall: IHall = { id: 123 };
        const hall2: IHall = { id: 456 };
        expectedResult = service.addHallToCollectionIfMissing([], hall, hall2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(hall);
        expect(expectedResult).toContain(hall2);
      });

      it('should accept null and undefined values', () => {
        const hall: IHall = { id: 123 };
        expectedResult = service.addHallToCollectionIfMissing([], null, hall, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(hall);
      });

      it('should return initial array if no Hall is added', () => {
        const hallCollection: IHall[] = [{ id: 123 }];
        expectedResult = service.addHallToCollectionIfMissing(hallCollection, undefined, null);
        expect(expectedResult).toEqual(hallCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
