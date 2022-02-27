import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ChairService } from '../service/chair.service';
import { IChair, Chair } from '../chair.model';
import { IBooking } from 'app/entities/booking/booking.model';
import { BookingService } from 'app/entities/booking/service/booking.service';
import { IMoviefunction } from 'app/entities/moviefunction/moviefunction.model';
import { MoviefunctionService } from 'app/entities/moviefunction/service/moviefunction.service';

import { ChairUpdateComponent } from './chair-update.component';

describe('Chair Management Update Component', () => {
  let comp: ChairUpdateComponent;
  let fixture: ComponentFixture<ChairUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let chairService: ChairService;
  let bookingService: BookingService;
  let moviefunctionService: MoviefunctionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ChairUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ChairUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ChairUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    chairService = TestBed.inject(ChairService);
    bookingService = TestBed.inject(BookingService);
    moviefunctionService = TestBed.inject(MoviefunctionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Booking query and add missing value', () => {
      const chair: IChair = { id: 456 };
      const booking: IBooking = { id: 231 };
      chair.booking = booking;

      const bookingCollection: IBooking[] = [{ id: 15688 }];
      jest.spyOn(bookingService, 'query').mockReturnValue(of(new HttpResponse({ body: bookingCollection })));
      const additionalBookings = [booking];
      const expectedCollection: IBooking[] = [...additionalBookings, ...bookingCollection];
      jest.spyOn(bookingService, 'addBookingToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ chair });
      comp.ngOnInit();

      expect(bookingService.query).toHaveBeenCalled();
      expect(bookingService.addBookingToCollectionIfMissing).toHaveBeenCalledWith(bookingCollection, ...additionalBookings);
      expect(comp.bookingsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Moviefunction query and add missing value', () => {
      const chair: IChair = { id: 456 };
      const moviefunction: IMoviefunction = { id: 875 };
      chair.moviefunction = moviefunction;

      const moviefunctionCollection: IMoviefunction[] = [{ id: 63671 }];
      jest.spyOn(moviefunctionService, 'query').mockReturnValue(of(new HttpResponse({ body: moviefunctionCollection })));
      const additionalMoviefunctions = [moviefunction];
      const expectedCollection: IMoviefunction[] = [...additionalMoviefunctions, ...moviefunctionCollection];
      jest.spyOn(moviefunctionService, 'addMoviefunctionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ chair });
      comp.ngOnInit();

      expect(moviefunctionService.query).toHaveBeenCalled();
      expect(moviefunctionService.addMoviefunctionToCollectionIfMissing).toHaveBeenCalledWith(
        moviefunctionCollection,
        ...additionalMoviefunctions
      );
      expect(comp.moviefunctionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const chair: IChair = { id: 456 };
      const booking: IBooking = { id: 18024 };
      chair.booking = booking;
      const moviefunction: IMoviefunction = { id: 33941 };
      chair.moviefunction = moviefunction;

      activatedRoute.data = of({ chair });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(chair));
      expect(comp.bookingsSharedCollection).toContain(booking);
      expect(comp.moviefunctionsSharedCollection).toContain(moviefunction);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Chair>>();
      const chair = { id: 123 };
      jest.spyOn(chairService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ chair });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: chair }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(chairService.update).toHaveBeenCalledWith(chair);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Chair>>();
      const chair = new Chair();
      jest.spyOn(chairService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ chair });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: chair }));
      saveSubject.complete();

      // THEN
      expect(chairService.create).toHaveBeenCalledWith(chair);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Chair>>();
      const chair = { id: 123 };
      jest.spyOn(chairService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ chair });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(chairService.update).toHaveBeenCalledWith(chair);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackBookingById', () => {
      it('Should return tracked Booking primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackBookingById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackMoviefunctionById', () => {
      it('Should return tracked Moviefunction primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackMoviefunctionById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
