import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IHall } from '../hall.model';

@Component({
  selector: 'jhi-hall-detail',
  templateUrl: './hall-detail.component.html',
})
export class HallDetailComponent implements OnInit {
  hall: IHall | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ hall }) => {
      this.hall = hall;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
