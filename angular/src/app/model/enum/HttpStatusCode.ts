import {IMessage} from "../../interfaces/IMessage";

export class StatusCode {
  static Ok = 201;
  static CreatedUser = 20101;
  static CreatedOrder = 20103;
  static BadRequestUser = 40001;
  static BadRequestCoupon = 40002;
  static BadRequestOrder = 400;
  static Unauthorized = 401;
  static NotAllowed = 405;
  static InternalServerError = 500;

  static getMessageForStatus(code: number, text: string): IMessage {
    switch (code) {
      case StatusCode.Ok:
        return {
          name: `The ${text} has been created successfully`,
          href: '/details',
          color: 'green',
        };
      case StatusCode.CreatedUser:
        return {
          name: `The user ${text} has been registered`,
          href: '/',
          color: 'green',
        };
      case StatusCode.CreatedOrder:
        return {
          name: `Order sent by name ${text} has been registered`,
          href: '/invoice',
          color: 'green',
        };
      case StatusCode.BadRequestUser:
        return {
          name: 'Invalid form data. Please check your inputs',
          href: '',
          color: 'red',
        };
      case StatusCode.BadRequestCoupon:
        return {
          name: 'Invalid form data coupon. Please check your inputs',
          href: '',
          color: 'red',
        };
        case StatusCode.BadRequestOrder:
        return {
          name: 'Invalid data invoice. Please check your inputs',
          href: '',
          color: 'red',
        };
      case StatusCode.Unauthorized:
        return {
          name: `User ${text} is not authorized`,
          href: '/login',
          color: 'red',
        };
      case StatusCode.NotAllowed:
        return {
          name: `User ${text} not found. Please check name or password`,
          href: '',
          color: 'red',
        };
      case StatusCode.InternalServerError:
        return {
          name: 'An internal server error occurred',
          href: '/',
          color: 'red',
        };
      default:
        return {
          name: ``,
          href: '',
          color: '',
        };
    }
  }
}
