import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IChair } from '../chair.model';

@Component({
  selector: 'jhi-chair-detail',
  templateUrl: './chair-detail.component.html',
})
export class ChairDetailComponent implements OnInit {
  chair: IChair | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ chair }) => {
      this.chair = chair;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
