import React from 'react';
import { LineChart } from '@mui/x-charts/LineChart';
import { useGetData } from '../hooks/data'; // Import the custom hook

// Define the type for each reading item
interface Reading {
  customerID: string;
  id: string;
  kindOfMeter: string;
  dateOfReading: number[]; 
  meterCount: number;      
  comment: string;
}

interface ProcessedData {
  dates: string[]; // Array of formatted date strings
  meterCounts: number[]; // Array of meter counts
}

// Process the data and return an object with dates and meter counts
const processData = (readingsData: Reading[]): ProcessedData => {
  const dates: string[] = [];
  const meterCounts: number[] = [];

  readingsData.forEach((item) => {
    const [year, month, day] = item.dateOfReading;
    const date = new Date(year, month - 1, day); // Month is 0-indexed in JS Date
    dates.push(date.toISOString().split('T')[0]); // Format: YYYY-MM-DD
    meterCounts.push(item.meterCount);
  });

  return { dates, meterCounts };
};

// ChartComponent that fetches data from useGetData hook
const ChartComponent: React.FC = () => {
  // Get the data from the custom hook
  const { readingsDataStrom, loading } = useGetData();

  if (loading) {
    return <div>Loading...</div>;
  }

  // Process the data to extract dates and meter counts
  const { dates, meterCounts } = processData(readingsDataStrom);
  console.log('Dates:', dates); // Should show an array of strings like ['2021-05-01', '2021-06-01', ...]
  console.log('Meter Counts:', meterCounts); 

  return (
    <LineChart
      xAxis={[{ data: dates }]} // Dates as the x-axis
      series={[{ data: meterCounts, area: true, }]} // Meter counts as the series data
      width={1000}
      height={600}
    />
  );
};

export default ChartComponent;
