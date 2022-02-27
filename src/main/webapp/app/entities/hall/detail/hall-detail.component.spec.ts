import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { HallDetailComponent } from './hall-detail.component';

describe('Hall Management Detail Component', () => {
  let comp: HallDetailComponent;
  let fixture: ComponentFixture<HallDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [HallDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ hall: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(HallDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(HallDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load hall on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.hall).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
