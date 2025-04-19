import { JwtPayload } from 'jwt-decode';

export interface LoginForm {
    email: string;
    password: string;
  }
  
  export interface RegisterForm {
    name: string;
    email: string;
    password: string;
  }
  
  export interface User {
    id: number;
    name: string;
    email: string;
  }
  
  export interface JwtPayload {
    exp: number;
    iat: number;
    sub: string;
    role?: string;
  }
  