import React, { useEffect, useState } from "react";

// Define the type for a person object
interface Person {
    firstName: string;
    lastName: string;
    gender: string;
    id: string;
    birthDate: number[] | null;
  }

const MyComponent = () => {
  const [data, setData] = useState<Person | Person[] | null>(null); // State to store the API data
  const [loading, setLoading] = useState<boolean>(true); // Loading state

  useEffect(() => {
    fetch("http://localhost:8080/customers")
      .then((response) => {
        console.log("Raw response:", response);
        return response.json();
      })
      .then((json) => {
        console.log("Parsed JSON:", json);
        setData(json);
        setLoading(false);
      })
      .catch((error) => {
        console.error("Error fetching data:", error);
        setLoading(false);
      });
  }, []); // Empty dependency array = fetch only on first render

  if (loading) return <p>Loading...</p>;
  if (!data) return <p>No data available.</p>;

  const renderPerson = (person: Person) => (
    <li key={person.id}>
      {person.firstName} {person.lastName} ({person.gender})
    </li>
  );

  return (
    <ul>
      {Array.isArray(data) ? data.map(renderPerson) : renderPerson(data)}
    </ul>
  );
};

export default MyComponent;