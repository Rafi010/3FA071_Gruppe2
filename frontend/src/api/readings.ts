import { Reading } from '../types/Reading'; // Adjust the path as needed

const BASE_URL_READINGS = 'http://localhost:8080/readings';

export const fetchReadings = async (): Promise<Reading[]> => {
  const response = await fetch(BASE_URL_READINGS);
  if (!response.ok) throw new Error('Fehler beim Laden der Messwerte');
  const data = await response.json();
  return data.readings;  // Adjust if your API response shape differs
};

export const updateReading = async (reading: Reading): Promise<Reading> => {
  const updateResponse = await fetch(BASE_URL_READINGS, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({reading}),  // Wrap in reading key
  });

console.log(JSON.stringify({reading}))

  if (updateResponse.status === 400) {
    const errorBody = await updateResponse.text();
    console.log(errorBody);
    throw new Error('Ungültige Eingabe');
  }

  if (!updateResponse.ok) {
    throw new Error('Fehler beim Aktualisieren des Messwerts');
  }

  // Fetch updated reading by ID
  const fetchResponse = await fetch(`${BASE_URL_READINGS}/${reading.id}`);
  if (!fetchResponse.ok) {
    throw new Error('Fehler beim Abrufen des aktualisierten Messwerts');
  }

  const responseData = await fetchResponse.json();
  return responseData.reading ?? responseData;
};

export const createReading = async (reading: Reading): Promise<Reading> => {
  const response = await fetch(BASE_URL_READINGS, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ reading }),
  });

  if (!response.ok) throw new Error('Fehler beim Erstellen des Messwerts');

  // Fetch full reading data by id
  const fetchResponse = await fetch(`${BASE_URL_READINGS}/${reading.meterId}`);
  if (!fetchResponse.ok) throw new Error('Fehler beim Abrufen des erstellten Messwerts');

  const fullReadingData = await fetchResponse.json();
  return fullReadingData.reading ?? fullReadingData;
};

export const deleteReading = async (id: string): Promise<void> => {
  const response = await fetch(`${BASE_URL_READINGS}/${id}`, {
    method: 'DELETE',
  });
  if (!response.ok) throw new Error('Fehler beim Löschen des Messwerts');
};
