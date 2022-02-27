import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AccountService } from 'app/core/auth/account.service';
import Swal from 'sweetalert2';
import { IBooking } from '../booking/booking.model';
import { BookingService } from '../booking/service/booking.service';
import { IChair } from '../chair/chair.model';
import { ChairService } from '../chair/service/chair.service';

@Component({
  selector: 'jhi-booking-list',
  templateUrl: './booking-list.component.html',
  styleUrls: ['./booking-list.component.scss']
})
export class BookingListComponent implements OnInit {

  bookings: IChair[][]=[]
  chairsBK: IChair[][]=[]
  tofree: IChair[]=[]

  constructor(private bookingserv: BookingService,
    private chairServ: ChairService,
     private router: Router,
     private accountService: AccountService
    ) { }

  ngOnInit(): void {
    if(!this.accountService.isAuthenticated()){this.router.navigate(['login']); return}
    this.bookingserv.getBookingForCUser().subscribe({next:booking=>{
      this.bookings=booking; 
      }})
  }
  goLogIn():void{
    this.router.navigate(['billboard'])
  }

  deleteBooking(id: number | undefined):void{
    if(id!==undefined){
      this.bookingserv.delete(id).subscribe(
        response=>{
          if(response.status===204){
            Swal.fire({
              icon:'success',
              title:'Se ha borrado la reserva con exito'
            })
            window.location.reload()
          }
          else{
            Swal.fire({
              icon:'error',
              title:'Ha ocurrido un error intente mas tarde'
            })
            window.location.reload()
          }
        }
  
  
      )
    }
    }
  freeChairs(): void{
    if(this.tofree.length===0){return}
    else{
      this.chairServ.freeChairs(this.tofree).subscribe(
        response=>{
          console.log(response.status)
          if(response.status===200){
            Swal.fire({
              icon:'success',
              title: 'se han liberado las sillas con exito'
            })
            window.location.reload()
          }
          else{
            Swal.fire({
              icon:'error',
              title: 'Ha ocurrido un error intente mas tarde'
            })
            window.location.reload()
          }

        }
      )
    }
  }

  selectFreeChair(chari:IChair): void{
    if(!this.tofree.includes(chari)){this.tofree.push(chari); console.log(this.tofree)}
    else{this.tofree.splice(this.tofree.indexOf(chari),1); console.log(this.tofree)}
  }


}
