import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ChairDetailComponent } from './chair-detail.component';

describe('Chair Management Detail Component', () => {
  let comp: ChairDetailComponent;
  let fixture: ComponentFixture<ChairDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ChairDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ chair: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ChairDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ChairDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load chair on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.chair).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
