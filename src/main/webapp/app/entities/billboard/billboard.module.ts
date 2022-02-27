import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BillboardComponent } from './billboard.component';
import { BillboardRoutingModule } from './billboard-routing.module';
import { ChairselectorComponent } from './chairselector/chairselector.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';



@NgModule({
  declarations: [
    BillboardComponent,
    ChairselectorComponent
  ],
  imports: [
    CommonModule,
    BillboardRoutingModule,
    FormsModule, ReactiveFormsModule,
    FontAwesomeModule
  ]
})
export class BillboardModule { }
