import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { MoviefunctionComponent } from './list/moviefunction.component';
import { MoviefunctionDetailComponent } from './detail/moviefunction-detail.component';
import { MoviefunctionUpdateComponent } from './update/moviefunction-update.component';
import { MoviefunctionDeleteDialogComponent } from './delete/moviefunction-delete-dialog.component';
import { MoviefunctionRoutingModule } from './route/moviefunction-routing.module';

@NgModule({
  imports: [SharedModule, MoviefunctionRoutingModule],
  declarations: [MoviefunctionComponent, MoviefunctionDetailComponent, MoviefunctionUpdateComponent, MoviefunctionDeleteDialogComponent],
  entryComponents: [MoviefunctionDeleteDialogComponent],
})
export class MoviefunctionModule {}
