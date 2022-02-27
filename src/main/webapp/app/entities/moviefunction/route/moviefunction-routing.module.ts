import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MoviefunctionComponent } from '../list/moviefunction.component';
import { MoviefunctionDetailComponent } from '../detail/moviefunction-detail.component';
import { MoviefunctionUpdateComponent } from '../update/moviefunction-update.component';
import { MoviefunctionRoutingResolveService } from './moviefunction-routing-resolve.service';

const moviefunctionRoute: Routes = [
  {
    path: '',
    component: MoviefunctionComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MoviefunctionDetailComponent,
    resolve: {
      moviefunction: MoviefunctionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MoviefunctionUpdateComponent,
    resolve: {
      moviefunction: MoviefunctionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MoviefunctionUpdateComponent,
    resolve: {
      moviefunction: MoviefunctionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(moviefunctionRoute)],
  exports: [RouterModule],
})
export class MoviefunctionRoutingModule {}
