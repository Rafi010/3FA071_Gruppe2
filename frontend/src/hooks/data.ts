// hooks/useGetData.ts
import { useState, useEffect } from 'react';

export enum DataType {
  Customers = 'customers',
  Readings = 'readings',
}

interface Person {
  firstName: string;
  lastName: string;
  gender: string;
  id: string;
  birthDate: number[] | null;
}

interface Reading {
  customerID: string;
  id: string;
  kindOfMeter: string;
  dateOfReading: number[]; 
  meterCount: number;      
  comment: string;
}

export const useGetData = () => {
  const [customerData, setCustomers] = useState<Person[]>([]);
  const [readingsData, setReadings] = useState<Reading[]>([]);
  const [loading, setLoading] = useState<boolean>(true);

  useEffect(() => {
    setLoading(true);
    
    // Fetch both customer and reading data in parallel
    Promise.all([
      fetch('http://localhost:8080/customers').then((response) => response.json()),
      fetch('http://localhost:8080/readings').then((response) => response.json()),
    ])
    .then(([customersData, readingsData]) => {
      setCustomers(customersData); // Set customers data
      setReadings(readingsData); // Set readings data
      setLoading(false);
    })
    .catch((error) => {
      console.error('Error fetching data:', error);
      setLoading(false);
    });
  }, []);

  return { customerData, readingsData, loading };
};
