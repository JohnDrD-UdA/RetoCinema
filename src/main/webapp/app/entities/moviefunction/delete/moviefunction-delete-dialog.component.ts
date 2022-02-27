import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMoviefunction } from '../moviefunction.model';
import { MoviefunctionService } from '../service/moviefunction.service';

@Component({
  templateUrl: './moviefunction-delete-dialog.component.html',
})
export class MoviefunctionDeleteDialogComponent {
  moviefunction?: IMoviefunction;

  constructor(protected moviefunctionService: MoviefunctionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.moviefunctionService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
