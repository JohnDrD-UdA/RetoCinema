import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IHall, Hall } from '../hall.model';
import { HallService } from '../service/hall.service';

@Component({
  selector: 'jhi-hall-update',
  templateUrl: './hall-update.component.html',
})
export class HallUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    cols_hall: [null, [Validators.required, Validators.min(1), Validators.max(30)]],
    rows_hall: [null, [Validators.required, Validators.min(1), Validators.max(28)]],
    name: [null, [Validators.required, Validators.minLength(1)]],
  });

  constructor(protected hallService: HallService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ hall }) => {
      this.updateForm(hall);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const hall = this.createFromForm();
    if (hall.id !== undefined) {
      this.subscribeToSaveResponse(this.hallService.update(hall));
    } else {
      this.subscribeToSaveResponse(this.hallService.create(hall));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IHall>>): void {
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

  protected updateForm(hall: IHall): void {
    this.editForm.patchValue({
      id: hall.id,
      cols_hall: hall.cols_hall,
      rows_hall: hall.rows_hall,
      name: hall.name,
    });
  }

  protected createFromForm(): IHall {
    return {
      ...new Hall(),
      id: this.editForm.get(['id'])!.value,
      cols_hall: this.editForm.get(['cols_hall'])!.value,
      rows_hall: this.editForm.get(['rows_hall'])!.value,
      name: this.editForm.get(['name'])!.value,
    };
  }
}
