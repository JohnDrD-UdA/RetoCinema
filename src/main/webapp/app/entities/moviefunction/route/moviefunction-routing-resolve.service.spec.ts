import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IMoviefunction, Moviefunction } from '../moviefunction.model';
import { MoviefunctionService } from '../service/moviefunction.service';

import { MoviefunctionRoutingResolveService } from './moviefunction-routing-resolve.service';

describe('Moviefunction routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: MoviefunctionRoutingResolveService;
  let service: MoviefunctionService;
  let resultMoviefunction: IMoviefunction | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(MoviefunctionRoutingResolveService);
    service = TestBed.inject(MoviefunctionService);
    resultMoviefunction = undefined;
  });

  describe('resolve', () => {
    it('should return IMoviefunction returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultMoviefunction = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultMoviefunction).toEqual({ id: 123 });
    });

    it('should return new IMoviefunction if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultMoviefunction = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultMoviefunction).toEqual(new Moviefunction());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Moviefunction })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultMoviefunction = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultMoviefunction).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
