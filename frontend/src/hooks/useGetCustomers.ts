import { useQuery } from '@tanstack/react-query';
import { Person } from '../types/Person';

const fetchCustomers = async () => {
  const response = await fetch('http://localhost:8080/customers');
  if (!response.ok) throw new Error('Fehler beim Laden der Kunden');
  const data = await response.json();
  return data.customers;
};

export const useGetCustomers = () => {
  const { data, error, isLoading } = useQuery<Person[], Error>({
    queryKey: ['customers'], 
    queryFn: fetchCustomers,
  });

  return {
    customerData: data || [],  
    loading: isLoading,
    error,                   
  };
}