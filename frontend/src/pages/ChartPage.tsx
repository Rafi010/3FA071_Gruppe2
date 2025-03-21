import React, { useState } from "react";
import { LineChart } from "@mui/x-charts/LineChart";
import { useGetData } from "../hooks/data"; // Import the custom hook
import { useGetReadings } from "../hooks/useGetReadings";
import { Box, FormControl, InputLabel, MenuItem, Select, SelectChangeEvent } from "@mui/material";
import { WidthNormal } from "@mui/icons-material";

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
const ChartPage: React.FC = () => {
  // Get the data from the custom hook
  const [filters, setFilters] = useState({
    kindOfMeter: 'STROM',
    start: '',
    end: '',
    customer: '',
  });

  const updateFilter = (key: keyof typeof filters, value: string) => {
    setFilters((prev) => ({
      ...prev,
      [key]: value, // Nur das angegebene Feld ändern
    }));
  };

  const { readingsData, loading } = useGetReadings(filters);
  const { dates, meterCounts } = processData(readingsData);

  const [meter, setMeter
  ] = React.useState('STROM');

  const handleChange = (event: SelectChangeEvent) => {
    const newMeter = event.target.value as string;
    setMeter(newMeter);
    updateFilter("kindOfMeter", newMeter)
  };

  return (
    <Box p={1}>
    <FormControl sx={{ width: '120px' }}>
      <InputLabel id="demo-simple-select-label">Meter</InputLabel>
      <Select
        labelId="demo-simple-select-label"
        id="demo-simple-select"
        value={meter}
        label="Meter"
        onChange={handleChange}
      >
        <MenuItem value={"STROM"}>Strom</MenuItem>
        <MenuItem value={"HEIZUNG"}>Heizung</MenuItem>
        <MenuItem value={"WASSER"}>Wasser</MenuItem>
      </Select>
    </FormControl>
    <LineChart
      xAxis={[{ data: dates, scaleType: "point" }]} // X-axis labels (months)
      series={[{ data: meterCounts, area: true }]} // Y-axis values (average meter counts)
      width={1000}
      height={600}
    />
    </Box>
  );
};

export default ChartPage;
