import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MoviefunctionDetailComponent } from './moviefunction-detail.component';

describe('Moviefunction Management Detail Component', () => {
  let comp: MoviefunctionDetailComponent;
  let fixture: ComponentFixture<MoviefunctionDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MoviefunctionDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ moviefunction: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(MoviefunctionDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(MoviefunctionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load moviefunction on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.moviefunction).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
