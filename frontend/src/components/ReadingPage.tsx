import React, { useState } from "react";
import { useGetData } from "../hooks/data";
import { Box, CircularProgress, MenuItem, radioClasses, Skeleton } from "@mui/material";
import { DataGrid, GridColDef, GridCsvExportMenuItem, GridExportMenuItemProps, gridFilteredSortedRowIdsSelector, GridToolbar, GridToolbarColumnsButton, GridToolbarContainer, GridToolbarDensitySelector, GridToolbarExportContainer, GridToolbarFilterButton, gridVisibleColumnFieldsSelector, useGridApiContext } from "@mui/x-data-grid";
import { red } from "@mui/material/colors";
import { CustomToolbar } from "./CustomToolbar";
import { read } from "node:fs";

interface Reading {
  customerID: string;
  id: string;
  kindOfMeter: string;
  dateOfReading: number[]; 
  meterCount: number;      
  comment: string;
}

export enum DataType {
  Customers = 'customers',
  Readings = 'readings',
}

const ReadingPage = () => {
  const { readingsData, loading } = useGetData();
  const [filter, setFilter] = useState("");
  const [selectedRows, setSelectedRows] = useState<string[]>([]);
  const handleFilterChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setFilter(event.target.value);
  };

  const filteredData = readingsData.filter((reading) => {

    const formattedDate = reading.dateOfReading
    ? `${reading.dateOfReading[0]}-${String(reading.dateOfReading[1]).padStart(2, '0')}-${String(reading.dateOfReading[2]).padStart(2, '0')}`
    : "";

    return (
      (reading.comment && reading.comment.toLowerCase().includes(filter.toLowerCase())) ||
      (reading.kindOfMeter && reading.kindOfMeter.toLowerCase().includes(filter.toLowerCase())) ||
      (reading.id && reading.id.toLowerCase().includes(filter.toLowerCase())) ||
      (reading.meterCount !== null && reading.meterCount !== undefined && reading.meterCount.toString().toLowerCase().includes(filter.toLowerCase())) ||
      formattedDate.includes(filter)
    );
  });

  const readingsColumns: GridColDef[] = [
    { field: "customer.id", headerName: "Customer ID", width: 300, valueGetter: (value, row) => {
      const customerID = row.customer.id;
      return customerID
      }, 
    },
    { field: "id", headerName: "ID", width: 300 },
    { field: "kindOfMeter", headerName: "Kind of Meter", width: 150 },
    { field: "dateOfReading", headerName: "Date", width: 150,
        valueGetter: (value, row) => {
            const readingDate = row.dateOfReading;
            if (readingDate) {
              const year = readingDate[0];
              const month = String(readingDate[1]).padStart(2, '0');
              const day = String(readingDate[2]).padStart(2, '0');
              return `${year}-${month}-${day}`;
            }
            return "N/A";
          },
        },
    { field: "meterCount", headerName: "Meter Count", width: 150 },
    { field: "comment", headerName: "Comment", width: 150 },
    
  ];

  if (loading) return (
  <Box sx={{ width: "90%", marginBottom: 10 }}>
    <Skeleton animation="wave" variant="rectangular" height={50} sx={{ marginBottom: 2, bgcolor: 'grey.900' }} />
    <Skeleton animation="wave" variant="rectangular" height={600} sx={{bgcolor: 'grey.900' }}/>
  </Box>);

  if (readingsData.length === 0) return <p>No data available.</p>;

  return (
    <Box sx={{ height: 600, width: "90%"}}>
      {/* Filter Input */}
      <Box sx={{ marginBottom: 2 }}>
        <input
          type="text"
          placeholder="Filter"
          value={filter}
          onChange={handleFilterChange}
          style={{
            width: "98%",
            padding: "10px",
            fontSize: "16px",
            marginBottom: "10px",
          }}
        />
      </Box>

      {/* DataGrid */}
      <DataGrid
        rows={filteredData}
        columns={readingsColumns}
        slots={{ toolbar: CustomToolbar }}
        checkboxSelection
        disableRowSelectionOnClick
        onRowSelectionModelChange={(selection) => setSelectedRows(selection as string[])}
        sx={{
          bgcolor: 'background.paper'
        }}
      />
    </Box>
  );
};

export default ReadingPage;