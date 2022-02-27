import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ChairComponent } from '../list/chair.component';
import { ChairDetailComponent } from '../detail/chair-detail.component';
import { ChairUpdateComponent } from '../update/chair-update.component';
import { ChairRoutingResolveService } from './chair-routing-resolve.service';

const chairRoute: Routes = [
  {
    path: '',
    component: ChairComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ChairDetailComponent,
    resolve: {
      chair: ChairRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ChairUpdateComponent,
    resolve: {
      chair: ChairRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ChairUpdateComponent,
    resolve: {
      chair: ChairRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(chairRoute)],
  exports: [RouterModule],
})
export class ChairRoutingModule {}
