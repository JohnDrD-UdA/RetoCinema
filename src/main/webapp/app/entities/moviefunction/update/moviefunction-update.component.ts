import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IMoviefunction, Moviefunction } from '../moviefunction.model';
import { MoviefunctionService } from '../service/moviefunction.service';
import { IMovie } from 'app/entities/movie/movie.model';
import { MovieService } from 'app/entities/movie/service/movie.service';
import { IHall } from 'app/entities/hall/hall.model';
import { HallService } from 'app/entities/hall/service/hall.service';

@Component({
  selector: 'jhi-moviefunction-update',
  templateUrl: './moviefunction-update.component.html',
})
export class MoviefunctionUpdateComponent implements OnInit {
  isSaving = false;

  moviesSharedCollection: IMovie[] = [];
  hallsSharedCollection: IHall[] = [];

  editForm = this.fb.group({
    id: [],
    movie_date_time: [null, [Validators.required]],
    active_movie_function: [null, [Validators.required]],
    movie: [null, Validators.required],
    hall: [null, Validators.required],
  });

  constructor(
    protected moviefunctionService: MoviefunctionService,
    protected movieService: MovieService,
    protected hallService: HallService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ moviefunction }) => {
      this.updateForm(moviefunction);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const moviefunction = this.createFromForm();
    if (moviefunction.id !== undefined) {
      this.subscribeToSaveResponse(this.moviefunctionService.update(moviefunction));
    } else {
      this.subscribeToSaveResponse(this.moviefunctionService.create(moviefunction));
    }
  }

  trackMovieById(index: number, item: IMovie): number {
    return item.id!;
  }

  trackHallById(index: number, item: IHall): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMoviefunction>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(moviefunction: IMoviefunction): void {
    this.editForm.patchValue({
      id: moviefunction.id,
      movie_date_time: moviefunction.movie_date_time,
      active_movie_function: moviefunction.active_movie_function,
      movie: moviefunction.movie,
      hall: moviefunction.hall,
    });

    this.moviesSharedCollection = this.movieService.addMovieToCollectionIfMissing(this.moviesSharedCollection, moviefunction.movie);
    this.hallsSharedCollection = this.hallService.addHallToCollectionIfMissing(this.hallsSharedCollection, moviefunction.hall);
  }

  protected loadRelationshipsOptions(): void {
    this.movieService
      .query()
      .pipe(map((res: HttpResponse<IMovie[]>) => res.body ?? []))
      .pipe(map((movies: IMovie[]) => this.movieService.addMovieToCollectionIfMissing(movies, this.editForm.get('movie')!.value)))
      .subscribe((movies: IMovie[]) => (this.moviesSharedCollection = movies));

    this.hallService
      .query()
      .pipe(map((res: HttpResponse<IHall[]>) => res.body ?? []))
      .pipe(map((halls: IHall[]) => this.hallService.addHallToCollectionIfMissing(halls, this.editForm.get('hall')!.value)))
      .subscribe((halls: IHall[]) => (this.hallsSharedCollection = halls));
  }

  protected createFromForm(): IMoviefunction {
    return {
      ...new Moviefunction(),
      id: this.editForm.get(['id'])!.value,
      movie_date_time: this.editForm.get(['movie_date_time'])!.value,
      active_movie_function: this.editForm.get(['active_movie_function'])!.value,
      movie: this.editForm.get(['movie'])!.value,
      hall: this.editForm.get(['hall'])!.value,
    };
  }
}
