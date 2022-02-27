import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { HallService } from '../service/hall.service';
import { IHall, Hall } from '../hall.model';

import { HallUpdateComponent } from './hall-update.component';

describe('Hall Management Update Component', () => {
  let comp: HallUpdateComponent;
  let fixture: ComponentFixture<HallUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let hallService: HallService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [HallUpdateComponent],
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
      .overrideTemplate(HallUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(HallUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    hallService = TestBed.inject(HallService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const hall: IHall = { id: 456 };

      activatedRoute.data = of({ hall });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(hall));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Hall>>();
      const hall = { id: 123 };
      jest.spyOn(hallService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ hall });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: hall }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(hallService.update).toHaveBeenCalledWith(hall);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Hall>>();
      const hall = new Hall();
      jest.spyOn(hallService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ hall });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: hall }));
      saveSubject.complete();

      // THEN
      expect(hallService.create).toHaveBeenCalledWith(hall);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Hall>>();
      const hall = { id: 123 };
      jest.spyOn(hallService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ hall });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(hallService.update).toHaveBeenCalledWith(hall);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
