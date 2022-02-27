export interface IMovie {
  id?: number;
  name?: string;
  gendre?: string;
  synopsis?: string;
  movie_format?: string;
  movie_length?: string;
  posterContentType?: string;
  poster?: string;
}

export class Movie implements IMovie {
  constructor(
    public id?: number,
    public name?: string,
    public gendre?: string,
    public synopsis?: string,
    public movie_format?: string,
    public movie_length?: string,
    public posterContentType?: string,
    public poster?: string
  ) {}
}

export function getMovieIdentifier(movie: IMovie): number | undefined {
  return movie.id;
}
