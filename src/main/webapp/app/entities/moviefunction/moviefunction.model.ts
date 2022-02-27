import { IMovie } from 'app/entities/movie/movie.model';
import { IHall } from 'app/entities/hall/hall.model';

export interface IMoviefunction {
  id?: number;
  movie_date_time?: string;
  active_movie_function?: boolean;
  movie?: IMovie;
  hall?: IHall;
}

export class Moviefunction implements IMoviefunction {
  constructor(
    public id?: number,
    public movie_date_time?: string,
    public active_movie_function?: boolean,
    public movie?: IMovie,
    public hall?: IHall
  ) {
    this.active_movie_function = this.active_movie_function ?? false;
  }
}

export function getMoviefunctionIdentifier(moviefunction: IMoviefunction): number | undefined {
  return moviefunction.id;
}
