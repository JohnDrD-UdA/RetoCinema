import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Account } from 'app/core/auth/account.model';
import { AccountService } from 'app/core/auth/account.service';
import { BookingService } from 'app/entities/booking/service/booking.service';
import { IChair } from 'app/entities/chair/chair.model';
import { ChairService } from 'app/entities/chair/service/chair.service';
import { IMovie, Movie } from 'app/entities/movie/movie.model';
import { IMoviefunction, Moviefunction } from 'app/entities/moviefunction/moviefunction.model';
import { MoviefunctionService } from 'app/entities/moviefunction/service/moviefunction.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'jhi-chairselector',
  templateUrl: './chairselector.component.html',
  styleUrls: ['./chairselector.component.scss']
})
export class ChairselectorComponent implements OnInit {
  currentAccount: Account | null = null
  movie?:IMovie;
  funtions:IMoviefunction[]=[]
  chairs: IChair[][]=[]
  bookedChairs: IChair[]=[]
  cols=0
  ready=false

  chairsForm=this.fb.group({
    Funciones_:[null,Validators.required]
  })

  constructor(private movieFunctionSer: MoviefunctionService,
    private chairServ: ChairService ,
    protected activeModal:NgbActiveModal, 
    private fb:FormBuilder,
    private bookingserv: BookingService,
    private accountService: AccountService) { }


  ngOnInit(): void {
    this.accountService.identity().subscribe(account => (this.currentAccount = account));
    if(this.movie?.id!==undefined){this.movieFunctionSer.getByMovie(this.movie?.id).subscribe({next: funtions=>this.funtions=funtions})}
    }
    cancel(): void {
      this.activeModal.dismiss();
    }
  getCost(): number{
    return this.bookedChairs.length*8500
  }
  getBookings():void {
    console.log("entro")
    const fId=this.chairsForm.get(['Funciones_'])!.value
    console.log(fId)
    if(fId){
      this.chairServ.getAllchairsByFM(fId).subscribe({next: bookings=>this.chairs=bookings})
      console.log(this.chairs)
      if (this.funtions[0].hall?.cols_hall!==undefined){this.cols=this.funtions[0].hall?.cols_hall}

      console.log(this.cols)
      this.ready=true
    }
  }
selectChair(chair: IChair):void{  
  console.log(chair)
  if(chair.booking!=null && !this.bookedChairs.includes(chair)){return}
  if(!this.bookedChairs.includes(chair)){ this.bookedChairs.push(chair); console.log(this.bookedChairs)}
  else{this.bookedChairs.splice(this.bookedChairs.indexOf(chair),1); console.log(this.bookedChairs)}


}
book(): void{
  if(this.currentAccount?.login!==undefined){

this.bookingserv.bookAllChairs(this.bookedChairs,this.currentAccount?.login).subscribe(response=>
  {
    if(response.status===200){
      console.log("Exito")
      Swal.fire({
        icon:'success',
        title: 'Reserva Exitosa',
        text: 'Reserva hecha para: ' + this.movie?.name + 'en el horario:' +this.bookedChairs[0].moviefunction?.movie_date_time
      })
      this.cancel()
    }
    else{
      Swal.fire({
        icon:'error',
        title: 'Error en la reserva :/',
        text: 'Recarge la pagina e intente nueva mente'
      })
      this.cancel()
  }
  })
}
}

}
