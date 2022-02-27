import { IBooking } from 'app/entities/booking/booking.model';
import { IMoviefunction } from 'app/entities/moviefunction/moviefunction.model';

export interface IChair {
  id?: number;
  location?: string;
  avaible_chair?: boolean;
  booking?: IBooking | null;
  moviefunction?: IMoviefunction;
}

export class Chair implements IChair {
  constructor(
    public id?: number,
    public location?: string,
    public avaible_chair?: boolean,
    public booking?: IBooking | null,
    public moviefunction?: IMoviefunction
  ) {
    this.avaible_chair = this.avaible_chair ?? false;
  }
}

export function getChairIdentifier(chair: IChair): number | undefined {
  return chair.id;
}
