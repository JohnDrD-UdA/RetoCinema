import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMoviefunction, Moviefunction } from '../moviefunction.model';
import { MoviefunctionService } from '../service/moviefunction.service';

@Injectable({ providedIn: 'root' })
export class MoviefunctionRoutingResolveService implements Resolve<IMoviefunction> {
  constructor(protected service: MoviefunctionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMoviefunction> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((moviefunction: HttpResponse<Moviefunction>) => {
          if (moviefunction.body) {
            return of(moviefunction.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Moviefunction());
  }
}
