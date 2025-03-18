import React from "react";
import { LineChart } from "@mui/x-charts/LineChart";
import { useGetData } from "../hooks/data"; // Import the custom hook

// Define the type for each reading item
interface Reading {
  customerID: string;
  id: string;
  kindOfMeter: string;
  dateOfReading: number[]; // Format: [year, month, day]
  meterCount: number;
  comment: string;
}

// Function to group data by month and calculate averages
const processData = (readingsData: Reading[]) => {
  const monthlyData: Record<string, { sum: number; count: number }> = {};

  readingsData.forEach((item) => {
    const [year, month] = item.dateOfReading; // Extract year & month
    const monthKey = `${year}-${String(month).padStart(2, "0")}`; // Format: YYYY-MM

    if (!monthlyData[monthKey]) {
      monthlyData[monthKey] = { sum: 0, count: 0 };
    }

    monthlyData[monthKey].sum += item.meterCount;
    monthlyData[monthKey].count += 1;
  });

  // Convert the aggregated data to arrays
  const dates = Object.keys(monthlyData).sort(); // Sorted months (YYYY-MM)
  const meterCounts = dates.map(
    (date) => monthlyData[date].sum / monthlyData[date].count // Calculate average
  );

  return { dates, meterCounts };
};

// ChartComponent that fetches data from useGetData hook
const ChartComponent: React.FC = () => {
  // Get the data from the custom hook
  const { readingsDataStrom, loading } = useGetData();

  if (loading) {
    return <div>Loading...</div>;
  }

  // Process the data to extract monthly averages
  const { dates, meterCounts } = processData(readingsDataStrom);

  return (
    <LineChart
      xAxis={[{ data: dates, scaleType: "point" }]} // X-axis labels (months)
      series={[{ data: meterCounts, area: true }]} // Y-axis values (average meter counts)
      width={1000}
      height={600}
    />
  );
};

export default ChartComponent;
