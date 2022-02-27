import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMoviefunction } from '../moviefunction.model';

@Component({
  selector: 'jhi-moviefunction-detail',
  templateUrl: './moviefunction-detail.component.html',
})
export class MoviefunctionDetailComponent implements OnInit {
  moviefunction: IMoviefunction | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ moviefunction }) => {
      this.moviefunction = moviefunction;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
