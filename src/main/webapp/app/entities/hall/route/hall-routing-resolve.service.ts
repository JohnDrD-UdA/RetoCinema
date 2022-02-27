import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IHall, Hall } from '../hall.model';
import { HallService } from '../service/hall.service';

@Injectable({ providedIn: 'root' })
export class HallRoutingResolveService implements Resolve<IHall> {
  constructor(protected service: HallService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IHall> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((hall: HttpResponse<Hall>) => {
          if (hall.body) {
            return of(hall.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Hall());
  }
}
