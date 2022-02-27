import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { HallComponent } from '../list/hall.component';
import { HallDetailComponent } from '../detail/hall-detail.component';
import { HallUpdateComponent } from '../update/hall-update.component';
import { HallRoutingResolveService } from './hall-routing-resolve.service';

const hallRoute: Routes = [
  {
    path: '',
    component: HallComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: HallDetailComponent,
    resolve: {
      hall: HallRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: HallUpdateComponent,
    resolve: {
      hall: HallRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: HallUpdateComponent,
    resolve: {
      hall: HallRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(hallRoute)],
  exports: [RouterModule],
})
export class HallRoutingModule {}
