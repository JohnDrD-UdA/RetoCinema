import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChairselectorComponent } from './chairselector.component';

describe('ChairselectorComponent', () => {
  let component: ChairselectorComponent;
  let fixture: ComponentFixture<ChairselectorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ChairselectorComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ChairselectorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
