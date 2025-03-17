// hooks/useGetData.ts
import { useState, useEffect } from 'react';

interface Person {
  firstName: string;
  lastName: string;
  gender: string;
  id: string;
  birthDate: number[] | null;
}

export enum DataType {
  Customers = 'customers',
  Readings = 'readings',
}

export const useGetData = (dataType: DataType) => {
  const [data, setData] = useState<Person[]>([]);
  const [loading, setLoading] = useState<boolean>(true);

  useEffect(() => {
    setLoading(true);
    fetch(`http://localhost:8080/${dataType}`)
      .then((response) => response.json())
      .then((json) => {
        setData(json);
        setLoading(false);
      })
      .catch((error) => {
        console.error("Error fetching data:", error);
        setLoading(false);
      });
  }, 
  [dataType]);

  return { data, loading };
};
