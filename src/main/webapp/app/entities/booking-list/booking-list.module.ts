import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BookingListRoutingModule } from './booking-list-routing.module';
import { BookingListComponent } from './booking-list.component';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';



@NgModule({
  declarations: [BookingListComponent],
  imports: [
    CommonModule,
    BookingListRoutingModule,
    FontAwesomeModule

  ]
})
export class BookingListModule {
 }
