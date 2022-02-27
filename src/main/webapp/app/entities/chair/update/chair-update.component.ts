import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IChair, Chair } from '../chair.model';
import { ChairService } from '../service/chair.service';
import { IBooking } from 'app/entities/booking/booking.model';
import { BookingService } from 'app/entities/booking/service/booking.service';
import { IMoviefunction } from 'app/entities/moviefunction/moviefunction.model';
import { MoviefunctionService } from 'app/entities/moviefunction/service/moviefunction.service';

@Component({
  selector: 'jhi-chair-update',
  templateUrl: './chair-update.component.html',
})
export class ChairUpdateComponent implements OnInit {
  isSaving = false;

  bookingsSharedCollection: IBooking[] = [];
  moviefunctionsSharedCollection: IMoviefunction[] = [];

  editForm = this.fb.group({
    id: [],
    location: [null, [Validators.required, Validators.minLength(1), Validators.maxLength(6)]],
    avaible_chair: [null, [Validators.required]],
    booking: [],
    moviefunction: [null, Validators.required],
  });

  constructor(
    protected chairService: ChairService,
    protected bookingService: BookingService,
    protected moviefunctionService: MoviefunctionService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ chair }) => {
      this.updateForm(chair);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const chair = this.createFromForm();
    if (chair.id !== undefined) {
      this.subscribeToSaveResponse(this.chairService.update(chair));
    } else {
      this.subscribeToSaveResponse(this.chairService.create(chair));
    }
  }

  trackBookingById(index: number, item: IBooking): number {
    return item.id!;
  }

  trackMoviefunctionById(index: number, item: IMoviefunction): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IChair>>): void {
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

  protected updateForm(chair: IChair): void {
    this.editForm.patchValue({
      id: chair.id,
      location: chair.location,
      avaible_chair: chair.avaible_chair,
      booking: chair.booking,
      moviefunction: chair.moviefunction,
    });

    this.bookingsSharedCollection = this.bookingService.addBookingToCollectionIfMissing(this.bookingsSharedCollection, chair.booking);
    this.moviefunctionsSharedCollection = this.moviefunctionService.addMoviefunctionToCollectionIfMissing(
      this.moviefunctionsSharedCollection,
      chair.moviefunction
    );
  }

  protected loadRelationshipsOptions(): void {
    this.bookingService
      .query()
      .pipe(map((res: HttpResponse<IBooking[]>) => res.body ?? []))
      .pipe(
        map((bookings: IBooking[]) => this.bookingService.addBookingToCollectionIfMissing(bookings, this.editForm.get('booking')!.value))
      )
      .subscribe((bookings: IBooking[]) => (this.bookingsSharedCollection = bookings));

    this.moviefunctionService
      .query()
      .pipe(map((res: HttpResponse<IMoviefunction[]>) => res.body ?? []))
      .pipe(
        map((moviefunctions: IMoviefunction[]) =>
          this.moviefunctionService.addMoviefunctionToCollectionIfMissing(moviefunctions, this.editForm.get('moviefunction')!.value)
        )
      )
      .subscribe((moviefunctions: IMoviefunction[]) => (this.moviefunctionsSharedCollection = moviefunctions));
  }

  protected createFromForm(): IChair {
    return {
      ...new Chair(),
      id: this.editForm.get(['id'])!.value,
      location: this.editForm.get(['location'])!.value,
      avaible_chair: this.editForm.get(['avaible_chair'])!.value,
      booking: this.editForm.get(['booking'])!.value,
      moviefunction: this.editForm.get(['moviefunction'])!.value,
    };
  }
}
