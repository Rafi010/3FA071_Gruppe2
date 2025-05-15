export interface Person {
    firstName: string;
    lastName: string;
    gender: string;
    id: string;
    birthDate: number[] | null;
    isNew?: boolean;
  }