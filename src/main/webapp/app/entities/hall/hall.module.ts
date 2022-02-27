import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { HallComponent } from './list/hall.component';
import { HallDetailComponent } from './detail/hall-detail.component';
import { HallUpdateComponent } from './update/hall-update.component';
import { HallDeleteDialogComponent } from './delete/hall-delete-dialog.component';
import { HallRoutingModule } from './route/hall-routing.module';

@NgModule({
  imports: [SharedModule, HallRoutingModule],
  declarations: [HallComponent, HallDetailComponent, HallUpdateComponent, HallDeleteDialogComponent],
  entryComponents: [HallDeleteDialogComponent],
})
export class HallModule {}
