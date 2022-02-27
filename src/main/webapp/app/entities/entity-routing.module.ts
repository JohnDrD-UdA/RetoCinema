import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'hall',
        data: { pageTitle: 'cinemasCidenetApp.hall.home.title' },
        loadChildren: () => import('./hall/hall.module').then(m => m.HallModule),
      },
      {
        path: 'movie',
        data: { pageTitle: 'cinemasCidenetApp.movie.home.title' },
        loadChildren: () => import('./movie/movie.module').then(m => m.MovieModule),
      },
      {
        path: 'moviefunction',
        data: { pageTitle: 'cinemasCidenetApp.moviefunction.home.title' },
        loadChildren: () => import('./moviefunction/moviefunction.module').then(m => m.MoviefunctionModule),
      },
      {
        path: 'booking',
        data: { pageTitle: 'cinemasCidenetApp.booking.home.title' },
        loadChildren: () => import('./booking/booking.module').then(m => m.BookingModule),
      },
      {
        path: 'chair',
        data: { pageTitle: 'cinemasCidenetApp.chair.home.title' },
        loadChildren: () => import('./chair/chair.module').then(m => m.ChairModule),
      },
      {
        path: 'billboard',
        data: { pageTitle: 'Billboard' },
        loadChildren: () => import('./billboard/billboard.module').then(m => m.BillboardModule),
      },
      {
        path: 'BookingList',
        data: { pageTitle: 'BookingList' },
        loadChildren: () => import('./booking-list/booking-list.module').then(m => m.BookingListModule),
      },

      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
