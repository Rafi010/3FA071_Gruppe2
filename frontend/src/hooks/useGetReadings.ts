import { useQuery } from '@tanstack/react-query';

// Define FilterParams to ensure proper type checking for query parameters
interface FilterParams {
  kindOfMeter?: string;
  start?: string;
  end?: string;
  customer?: string;
}

// A generic fetch function for retrieving readings with dynamic query parameters
const fetchReadings = async ({ queryKey }: { queryKey: [string, FilterParams] }) => {
  const [, { kindOfMeter, start, end, customer }] = queryKey;

  let url = 'http://localhost:8080/readings';
  const params = new URLSearchParams();

  // Append parameters conditionally if they exist
  if (kindOfMeter) params.append('kindOfMeter', kindOfMeter);
  if (start) params.append('start', start);
  if (end) params.append('end', end);
  if (customer) params.append('customer', customer);

  const paramString = params.toString();
  if (paramString) {
  url += '?' + paramString;
  }

  const response = await fetch(url);
  if (!response.ok) throw new Error('Fehler beim Laden der Daten');
  const data = await response.json();
  return data.readings;
};

// Custom hook using React Query to fetch readings data
export const useGetReadings = (filterParams: FilterParams) => {
  const { data, error, isLoading } = useQuery({
    queryKey: ['readings', filterParams],
    queryFn: fetchReadings
  });

  return {
    readingsData: data || [],  
    loading: isLoading,       
    error,                    
  };
};
