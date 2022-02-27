import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IChair, Chair } from '../chair.model';
import { ChairService } from '../service/chair.service';

@Injectable({ providedIn: 'root' })
export class ChairRoutingResolveService implements Resolve<IChair> {
  constructor(protected service: ChairService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IChair> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((chair: HttpResponse<Chair>) => {
          if (chair.body) {
            return of(chair.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Chair());
  }
}
