import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IChair, getChairIdentifier } from '../chair.model';
import { IBooking } from 'app/entities/booking/booking.model';

export type EntityResponseType = HttpResponse<IChair>;
export type EntityArrayResponseType = HttpResponse<IChair[]>;

@Injectable({ providedIn: 'root' })
export class ChairService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/chairs');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(chair: IChair): Observable<EntityResponseType> {
    return this.http.post<IChair>(this.resourceUrl, chair, { observe: 'response' });
  }

  update(chair: IChair): Observable<EntityResponseType> {
    return this.http.put<IChair>(`${this.resourceUrl}/${getChairIdentifier(chair) as number}`, chair, { observe: 'response' });
  }

  partialUpdate(chair: IChair): Observable<EntityResponseType> {
    return this.http.patch<IChair>(`${this.resourceUrl}/${getChairIdentifier(chair) as number}`, chair, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IChair>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IChair[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addChairToCollectionIfMissing(chairCollection: IChair[], ...chairsToCheck: (IChair | null | undefined)[]): IChair[] {
    const chairs: IChair[] = chairsToCheck.filter(isPresent);
    if (chairs.length > 0) {
      const chairCollectionIdentifiers = chairCollection.map(chairItem => getChairIdentifier(chairItem)!);
      const chairsToAdd = chairs.filter(chairItem => {
        const chairIdentifier = getChairIdentifier(chairItem);
        if (chairIdentifier == null || chairCollectionIdentifiers.includes(chairIdentifier)) {
          return false;
        }
        chairCollectionIdentifiers.push(chairIdentifier);
        return true;
      });
      return [...chairsToAdd, ...chairCollection];
    }
    return chairCollection;
  }
  
  getAllchairsByFM(id: number): Observable<IChair[][]>{
    return this.http.get<IChair[][]>(`${this.resourceUrl}/getByFunctionM/${id}`).pipe(tap(data=>console.log("Done")))
  }
  getAllChairsByBk(id:number):Observable<EntityArrayResponseType>{
    return this.http.get<IChair[]>(`${this.resourceUrl}/getByBooking/${id}`,{observe: 'response' }).pipe(tap(data=>console.log(JSON.stringify(data))))
  }
  freeChairs(chairs: IChair[]): Observable<EntityResponseType> {
    return this.http.post<IChair>(`${this.resourceUrl}/freeChairs`, chairs, { observe: 'response' });
  }
  
}
