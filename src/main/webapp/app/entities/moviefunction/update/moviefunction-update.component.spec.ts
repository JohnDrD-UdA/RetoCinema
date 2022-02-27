import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { MoviefunctionService } from '../service/moviefunction.service';
import { IMoviefunction, Moviefunction } from '../moviefunction.model';
import { IMovie } from 'app/entities/movie/movie.model';
import { MovieService } from 'app/entities/movie/service/movie.service';
import { IHall } from 'app/entities/hall/hall.model';
import { HallService } from 'app/entities/hall/service/hall.service';

import { MoviefunctionUpdateComponent } from './moviefunction-update.component';

describe('Moviefunction Management Update Component', () => {
  let comp: MoviefunctionUpdateComponent;
  let fixture: ComponentFixture<MoviefunctionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let moviefunctionService: MoviefunctionService;
  let movieService: MovieService;
  let hallService: HallService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [MoviefunctionUpdateComponent],
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
      .overrideTemplate(MoviefunctionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MoviefunctionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    moviefunctionService = TestBed.inject(MoviefunctionService);
    movieService = TestBed.inject(MovieService);
    hallService = TestBed.inject(HallService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Movie query and add missing value', () => {
      const moviefunction: IMoviefunction = { id: 456 };
      const movie: IMovie = { id: 30370 };
      moviefunction.movie = movie;

      const movieCollection: IMovie[] = [{ id: 28831 }];
      jest.spyOn(movieService, 'query').mockReturnValue(of(new HttpResponse({ body: movieCollection })));
      const additionalMovies = [movie];
      const expectedCollection: IMovie[] = [...additionalMovies, ...movieCollection];
      jest.spyOn(movieService, 'addMovieToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ moviefunction });
      comp.ngOnInit();

      expect(movieService.query).toHaveBeenCalled();
      expect(movieService.addMovieToCollectionIfMissing).toHaveBeenCalledWith(movieCollection, ...additionalMovies);
      expect(comp.moviesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Hall query and add missing value', () => {
      const moviefunction: IMoviefunction = { id: 456 };
      const hall: IHall = { id: 85138 };
      moviefunction.hall = hall;

      const hallCollection: IHall[] = [{ id: 81215 }];
      jest.spyOn(hallService, 'query').mockReturnValue(of(new HttpResponse({ body: hallCollection })));
      const additionalHalls = [hall];
      const expectedCollection: IHall[] = [...additionalHalls, ...hallCollection];
      jest.spyOn(hallService, 'addHallToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ moviefunction });
      comp.ngOnInit();

      expect(hallService.query).toHaveBeenCalled();
      expect(hallService.addHallToCollectionIfMissing).toHaveBeenCalledWith(hallCollection, ...additionalHalls);
      expect(comp.hallsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const moviefunction: IMoviefunction = { id: 456 };
      const movie: IMovie = { id: 42879 };
      moviefunction.movie = movie;
      const hall: IHall = { id: 19025 };
      moviefunction.hall = hall;

      activatedRoute.data = of({ moviefunction });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(moviefunction));
      expect(comp.moviesSharedCollection).toContain(movie);
      expect(comp.hallsSharedCollection).toContain(hall);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Moviefunction>>();
      const moviefunction = { id: 123 };
      jest.spyOn(moviefunctionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ moviefunction });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: moviefunction }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(moviefunctionService.update).toHaveBeenCalledWith(moviefunction);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Moviefunction>>();
      const moviefunction = new Moviefunction();
      jest.spyOn(moviefunctionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ moviefunction });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: moviefunction }));
      saveSubject.complete();

      // THEN
      expect(moviefunctionService.create).toHaveBeenCalledWith(moviefunction);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Moviefunction>>();
      const moviefunction = { id: 123 };
      jest.spyOn(moviefunctionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ moviefunction });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(moviefunctionService.update).toHaveBeenCalledWith(moviefunction);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackMovieById', () => {
      it('Should return tracked Movie primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackMovieById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackHallById', () => {
      it('Should return tracked Hall primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackHallById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
