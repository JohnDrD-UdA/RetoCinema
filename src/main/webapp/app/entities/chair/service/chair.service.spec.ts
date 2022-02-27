import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IChair, Chair } from '../chair.model';

import { ChairService } from './chair.service';

describe('Chair Service', () => {
  let service: ChairService;
  let httpMock: HttpTestingController;
  let elemDefault: IChair;
  let expectedResult: IChair | IChair[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ChairService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      location: 'AAAAAAA',
      avaible_chair: false,
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

    it('should create a Chair', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Chair()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Chair', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          location: 'BBBBBB',
          avaible_chair: true,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Chair', () => {
      const patchObject = Object.assign(
        {
          avaible_chair: true,
        },
        new Chair()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Chair', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          location: 'BBBBBB',
          avaible_chair: true,
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

    it('should delete a Chair', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addChairToCollectionIfMissing', () => {
      it('should add a Chair to an empty array', () => {
        const chair: IChair = { id: 123 };
        expectedResult = service.addChairToCollectionIfMissing([], chair);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(chair);
      });

      it('should not add a Chair to an array that contains it', () => {
        const chair: IChair = { id: 123 };
        const chairCollection: IChair[] = [
          {
            ...chair,
          },
          { id: 456 },
        ];
        expectedResult = service.addChairToCollectionIfMissing(chairCollection, chair);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Chair to an array that doesn't contain it", () => {
        const chair: IChair = { id: 123 };
        const chairCollection: IChair[] = [{ id: 456 }];
        expectedResult = service.addChairToCollectionIfMissing(chairCollection, chair);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(chair);
      });

      it('should add only unique Chair to an array', () => {
        const chairArray: IChair[] = [{ id: 123 }, { id: 456 }, { id: 22771 }];
        const chairCollection: IChair[] = [{ id: 123 }];
        expectedResult = service.addChairToCollectionIfMissing(chairCollection, ...chairArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const chair: IChair = { id: 123 };
        const chair2: IChair = { id: 456 };
        expectedResult = service.addChairToCollectionIfMissing([], chair, chair2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(chair);
        expect(expectedResult).toContain(chair2);
      });

      it('should accept null and undefined values', () => {
        const chair: IChair = { id: 123 };
        expectedResult = service.addChairToCollectionIfMissing([], null, chair, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(chair);
      });

      it('should return initial array if no Chair is added', () => {
        const chairCollection: IChair[] = [{ id: 123 }];
        expectedResult = service.addChairToCollectionIfMissing(chairCollection, undefined, null);
        expect(expectedResult).toEqual(chairCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
