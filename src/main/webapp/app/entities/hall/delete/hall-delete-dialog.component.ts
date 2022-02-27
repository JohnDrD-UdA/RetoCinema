import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IHall } from '../hall.model';
import { HallService } from '../service/hall.service';

@Component({
  templateUrl: './hall-delete-dialog.component.html',
})
export class HallDeleteDialogComponent {
  hall?: IHall;

  constructor(protected hallService: HallService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.hallService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
