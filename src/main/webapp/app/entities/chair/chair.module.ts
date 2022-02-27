import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ChairComponent } from './list/chair.component';
import { ChairDetailComponent } from './detail/chair-detail.component';
import { ChairUpdateComponent } from './update/chair-update.component';
import { ChairDeleteDialogComponent } from './delete/chair-delete-dialog.component';
import { ChairRoutingModule } from './route/chair-routing.module';

@NgModule({
  imports: [SharedModule, ChairRoutingModule],
  declarations: [ChairComponent, ChairDetailComponent, ChairUpdateComponent, ChairDeleteDialogComponent],
  entryComponents: [ChairDeleteDialogComponent],
})
export class ChairModule {}
