import { Person } from '../types/Person' // Or inline if you don't separate types

const BASE_URL = 'http://localhost:8080/customers';

export const fetchCustomers = async (): Promise<Person[]> => {
  const response = await fetch(BASE_URL);
  if (!response.ok) throw new Error('Fehler beim Laden der Kunden');
  const data = await response.json();
  return data.customers;
};

export const updateCustomer = async (customer: Person): Promise<Person> => {
    // Step 1: Wrap the customer data in the 'customer' key and update the customer data
    const updateResponse = await fetch(BASE_URL, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ customer }),  // Wrap the customer object inside a 'customer' key
    });
  
    //console.log(JSON.stringify({ customer }));  // Log the wrapped customer object
  
    if (updateResponse.status === 400) {
      const errorBody = await updateResponse.text(); // optional: oder .json() falls dein Backend JSON zurückgibt
      console.log(errorBody)
      throw new Error(`Ungültige Eingabe`);
    }
  
    if (!updateResponse.ok) {
      throw new Error('Fehler beim Aktualisieren des Kunden');
    }
  
    // Step 2: Fetch the updated customer data
    const fetchResponse = await fetch(`${BASE_URL}/${customer.id}`);
  
    if (!fetchResponse.ok) {
      throw new Error('Fehler beim Abrufen der aktualisierten Kundendaten');
    }
  
    // Step 3: Return the updated customer object from the response
    const responseData = await fetchResponse.json();
    const updatedCustomer = responseData.customer ? responseData.customer : responseData;
    return updatedCustomer;
};

export const createCustomer = async (customer: Person): Promise<Person> => {
    // 1. POST request senden (mit customer in { customer } gewrappt)
    const response = await fetch(BASE_URL, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ customer }),
    });
  
    if (!response.ok) throw new Error('Fehler beim Erstellen des Kunden');
  
    // 2. Den vollständigen Customer direkt mit der bekannten ID fetchen
    const fetchResponse = await fetch(`${BASE_URL}/${customer.id}`);
    if (!fetchResponse.ok) throw new Error('Fehler beim Abrufen des erstellten Kunden');
  
    const fullCustomerData = await fetchResponse.json();
    return fullCustomerData.customer ?? fullCustomerData;
  };

export const deleteCustomer = async (id: string): Promise<void> => {
  const response = await fetch(`${BASE_URL}/${id}`, {
    method: 'DELETE',
  });
  if (!response.ok) throw new Error('Fehler beim Löschen des Kunden');
};
