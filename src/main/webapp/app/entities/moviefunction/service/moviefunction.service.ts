import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMoviefunction, getMoviefunctionIdentifier } from '../moviefunction.model';

export type EntityResponseType = HttpResponse<IMoviefunction>;
export type EntityArrayResponseType = HttpResponse<IMoviefunction[]>;

@Injectable({ providedIn: 'root' })
export class MoviefunctionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/moviefunctions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(moviefunction: IMoviefunction): Observable<EntityResponseType> {
    return this.http.post<IMoviefunction>(this.resourceUrl, moviefunction, { observe: 'response' });
  }

  update(moviefunction: IMoviefunction): Observable<EntityResponseType> {
    return this.http.put<IMoviefunction>(`${this.resourceUrl}/${getMoviefunctionIdentifier(moviefunction) as number}`, moviefunction, {
      observe: 'response',
    });
  }

  partialUpdate(moviefunction: IMoviefunction): Observable<EntityResponseType> {
    return this.http.patch<IMoviefunction>(`${this.resourceUrl}/${getMoviefunctionIdentifier(moviefunction) as number}`, moviefunction, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMoviefunction>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMoviefunction[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addMoviefunctionToCollectionIfMissing(
    moviefunctionCollection: IMoviefunction[],
    ...moviefunctionsToCheck: (IMoviefunction | null | undefined)[]
  ): IMoviefunction[] {
    const moviefunctions: IMoviefunction[] = moviefunctionsToCheck.filter(isPresent);
    if (moviefunctions.length > 0) {
      const moviefunctionCollectionIdentifiers = moviefunctionCollection.map(
        moviefunctionItem => getMoviefunctionIdentifier(moviefunctionItem)!
      );
      const moviefunctionsToAdd = moviefunctions.filter(moviefunctionItem => {
        const moviefunctionIdentifier = getMoviefunctionIdentifier(moviefunctionItem);
        if (moviefunctionIdentifier == null || moviefunctionCollectionIdentifiers.includes(moviefunctionIdentifier)) {
          return false;
        }
        moviefunctionCollectionIdentifiers.push(moviefunctionIdentifier);
        return true;
      });
      return [...moviefunctionsToAdd, ...moviefunctionCollection];
    }
    return moviefunctionCollection;
  }
  getByMovie(Id: number): Observable<IMoviefunction[]>{
    return this.http.get<IMoviefunction[]>(`${this.resourceUrl}/getbyMovie/${Id}`).pipe(tap(data=>console.log("Done")))
  }
}
