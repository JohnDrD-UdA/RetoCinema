import { Component, OnInit } from '@angular/core';
import { AbstractFormGroupDirective } from '@angular/forms';
import { Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Account } from 'app/core/auth/account.model';
import { AccountService } from 'app/core/auth/account.service';
import { IMovie, Movie } from '../movie/movie.model';
import { MovieService } from '../movie/service/movie.service';
import { ChairselectorComponent } from './chairselector/chairselector.component';

@Component({
  selector: 'jhi-billboard',
  templateUrl: './billboard.component.html',
  styleUrls: ['./billboard.component.scss']
})
export class BillboardComponent implements OnInit {

  movies:IMovie[] | undefined =[]

  constructor(private movieServ:MovieService, private router:Router, protected modal: NgbModal) { }
  
  ngOnInit(): void {
    this.movieServ.getAllMovies().subscribe({next: movies=>this.movies=movies})
    console.log(this.movies);
  }

  book(movie:Movie):void{
    if(sessionStorage.length===0){
      console.log("IN")
      this.router.navigate(['login'])
    }
    else{
      const modalRef=this.modal.open(ChairselectorComponent,{ size: 'lg', backdrop: 'static' });
      modalRef.componentInstance.movie=movie
    }
  }

}
