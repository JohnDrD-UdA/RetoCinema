import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IChair } from '../chair.model';
import { ChairService } from '../service/chair.service';

@Component({
  templateUrl: './chair-delete-dialog.component.html',
})
export class ChairDeleteDialogComponent {
  chair?: IChair;

  constructor(protected chairService: ChairService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.chairService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
