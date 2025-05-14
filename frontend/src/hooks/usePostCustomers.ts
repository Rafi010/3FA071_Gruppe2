// src/hooks/usePostCustomer.ts
import { useMutation } from '@tanstack/react-query';

interface Customer {
  id?: string;
  firstName: string;
  lastName: string;
  gender: 'M' | 'W' | 'D' | 'U';
  birthDate?: string | null;
}

const postCustomer = async (customer: Customer) => {
  const response = await fetch('http://localhost:8080/customers', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(customer),
  });

  if (!response.ok) {
    const errorText = await response.text();
    throw new Error(`Import fehlgeschlagen: ${response.status} ${errorText}`);
  }

  return await response.json(); // oder response.text(), falls dein Backend nur Strings zurückgibt
};

export const usePostCustomer = () => {
  return useMutation({
    mutationFn: postCustomer,
  });
};
