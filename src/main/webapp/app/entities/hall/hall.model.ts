export interface IHall {
  id?: number;
  cols_hall?: number;
  rows_hall?: number;
  name?: string;
}

export class Hall implements IHall {
  constructor(public id?: number, public cols_hall?: number, public rows_hall?: number, public name?: string) {}
}

export function getHallIdentifier(hall: IHall): number | undefined {
  return hall.id;
}
