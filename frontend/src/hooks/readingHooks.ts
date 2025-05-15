
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { createReading, fetchReadings } from '../api/readings';


// Custom hook using React Query to fetch readings data
export const useGetReadings = () => {
  return useQuery({
    queryKey: ['readings'],  // just a simple key without filterParams
    queryFn: fetchReadings,
  });
};


export const useCreateReading = () => {
    const queryClient = useQueryClient();
  
    return useMutation({
      mutationFn: createReading,
      onSuccess: () => {
        // Option 1: refetch readings list
        queryClient.invalidateQueries({ queryKey: ['readings'] });
        
        // Option 2: OR update the cache manually if desired
      },
      onError: (error) => {
        console.error('Fehler beim Erstellen des Messwerts:', error);
      },
    });
  };