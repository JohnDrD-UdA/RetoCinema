import { IUser } from 'app/entities/user/user.model';

export interface IBooking {
  id?: number;
  user?: IUser;
}

export class Booking implements IBooking {
  constructor(public id?: number, public user?: IUser) {}
}

export function getBookingIdentifier(booking: IBooking): number | undefined {
  return booking.id;
}
