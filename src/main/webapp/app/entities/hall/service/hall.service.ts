import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IHall, getHallIdentifier } from '../hall.model';

export type EntityResponseType = HttpResponse<IHall>;
export type EntityArrayResponseType = HttpResponse<IHall[]>;

@Injectable({ providedIn: 'root' })
export class HallService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/halls');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(hall: IHall): Observable<EntityResponseType> {
    return this.http.post<IHall>(this.resourceUrl, hall, { observe: 'response' });
  }

  update(hall: IHall): Observable<EntityResponseType> {
    return this.http.put<IHall>(`${this.resourceUrl}/${getHallIdentifier(hall) as number}`, hall, { observe: 'response' });
  }

  partialUpdate(hall: IHall): Observable<EntityResponseType> {
    return this.http.patch<IHall>(`${this.resourceUrl}/${getHallIdentifier(hall) as number}`, hall, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IHall>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IHall[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addHallToCollectionIfMissing(hallCollection: IHall[], ...hallsToCheck: (IHall | null | undefined)[]): IHall[] {
    const halls: IHall[] = hallsToCheck.filter(isPresent);
    if (halls.length > 0) {
      const hallCollectionIdentifiers = hallCollection.map(hallItem => getHallIdentifier(hallItem)!);
      const hallsToAdd = halls.filter(hallItem => {
        const hallIdentifier = getHallIdentifier(hallItem);
        if (hallIdentifier == null || hallCollectionIdentifiers.includes(hallIdentifier)) {
          return false;
        }
        hallCollectionIdentifiers.push(hallIdentifier);
        return true;
      });
      return [...hallsToAdd, ...hallCollection];
    }
    return hallCollection;
  }
}
