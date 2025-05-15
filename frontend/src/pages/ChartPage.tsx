import React, { useState, useMemo } from "react";
import { LineChart } from "@mui/x-charts/LineChart";
import { useGetReadings } from "../hooks/readingHooks";
import {
  Autocomplete,
  Box,
  FormControl,
  InputLabel,
  MenuItem,
  Select,
  SelectChangeEvent,
  TextField,
  Typography,
} from "@mui/material";
import { Reading } from "../types/Reading";

const formatDate = (dateArray: number[]) => {
  const [year, month, day] = dateArray;
  return `${year}-${String(month).padStart(2, "0")}-${String(day).padStart(2, "0")}`;
};

const ChartPage: React.FC = () => {
  const [meter, setMeter] = useState<"STROM" | "WASSER" | "HEIZUNG">("STROM");
  const [selectedCustomerId, setSelectedCustomerId] = useState<string | null>(null);
  const { data: readingsData = [], isLoading } = useGetReadings();

  const handleMeterChange = (event: SelectChangeEvent) => {
    setMeter(event.target.value as "STROM" | "WASSER" | "HEIZUNG");
  };

  const uniqueCustomers = useMemo(() => {
    const seen = new Map<string, { id: string; label: string }>();
    readingsData.forEach((r) => {
      if (r.customer?.id && !seen.has(r.customer.id)) {
        seen.set(r.customer.id, {
          id: r.customer.id,
          label: `(${r.customer.id})`,
        });
      }
    });
    return Array.from(seen.values());
  }, [readingsData]);

  const filtered = useMemo(() => {
    if (!selectedCustomerId) return [];

    return readingsData
      .filter((r) => r.kindOfMeter === meter && r.customer?.id === selectedCustomerId)
      .sort((a, b) => {
        const [ay, am, ad] = a.dateOfReading;
        const [by, bm, bd] = b.dateOfReading;
        return new Date(ay, am - 1, ad).getTime() - new Date(by, bm - 1, bd).getTime();
      });
  }, [readingsData, meter, selectedCustomerId]);

  const xLabels = filtered.map((r) => formatDate(r.dateOfReading));
  const yValues = filtered.map((r) => r.meterCount);

  return (
    <Box p={2}>
      <Box display="flex" justifyContent="center" gap={2} mb={2} flexWrap="wrap">
        <FormControl sx={{ width: 150 }}>
          <InputLabel id="meter-label">Meter</InputLabel>
          <Select
            labelId="meter-label"
            value={meter}
            label="Meter"
            onChange={handleMeterChange}
          >
            <MenuItem value="STROM">Strom</MenuItem>
            <MenuItem value="HEIZUNG">Heizung</MenuItem>
            <MenuItem value="WASSER">Wasser</MenuItem>
          </Select>
        </FormControl>

        <Autocomplete
          sx={{ minWidth: 350 }}
          options={uniqueCustomers}
          getOptionLabel={(option) => option.label}
          renderInput={(params) => <TextField {...params} label="Kunde auswählen" />}
          onChange={(_, value) => setSelectedCustomerId(value?.id ?? null)}
          isOptionEqualToValue={(option, value) => option.id === value.id}
        />
      </Box>

      {isLoading ? (
        <Typography>Lade Daten…</Typography>
      ) : !selectedCustomerId ? (
        <Typography>Bitte einen Kunden auswählen.</Typography>
      ) : (
        <LineChart
          xAxis={[{ data: xLabels, scaleType: "point", label: "Datum" }]}
          series={[{ data: yValues, label: meter, area: true }]}
          width={1000}
          height={600}
        />
      )}
    </Box>
  );
};

export default ChartPage;
