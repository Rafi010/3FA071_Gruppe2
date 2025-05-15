import { Person } from "./Person";

export interface CustomerRef {
    id: string;
  }

export interface Reading {
    id: string;
    meterId: string;
    kindOfMeter: string;
    dateOfReading: number[]; 
    meterCount: number;      
    comment: string;
    substitute: boolean;
    isNew?: boolean;
    customer: CustomerRef
  }