import React, { useState } from "react";
import { useGetData } from "../hooks/data";
import { Box, CircularProgress, MenuItem, radioClasses, Skeleton, TextField } from "@mui/material";
import { DataGrid, GridColDef, GridCsvExportMenuItem, GridExportMenuItemProps, gridFilteredSortedRowIdsSelector, GridToolbar, GridToolbarColumnsButton, GridToolbarContainer, GridToolbarDensitySelector, GridToolbarExportContainer, GridToolbarFilterButton, gridVisibleColumnFieldsSelector, useGridApiContext } from "@mui/x-data-grid";
import { red } from "@mui/material/colors";
import { CustomToolbar } from "../components/CustomToolbar";
import { read } from "node:fs";
import { useGetReadings } from "../hooks/useGetReadings";

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
  const [filters, setFilters] = useState({
    kindOfMeter: '',
    start: '',
    end: '',
    customer: '',
  });
  const { readingsData, loading } = useGetReadings(filters);

  const readingsColumns: GridColDef[] = [
    { field: "id", headerName: "ID", width: 300 },
    { field: "customer.id", headerName: "Customer ID", width: 300, valueGetter: (value, row) => {
      const customerID = row.customer?.id;
      return customerID
      }, 
    },
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
    <Skeleton animation="wave" variant="rectangular" height={50} sx={{ marginBottom: 2, bgcolor: '#1f1f1f' }} />
    <Skeleton animation="wave" variant="rectangular" height={600} sx={{bgcolor: '#1f1f1f' }}/>
  </Box>);

  if (readingsData.length === 0) return <p>No data available.</p>;

  return (
    <Box sx={{ height: 600, width: "90%"}}>
      {/* DataGrid */}
      <DataGrid
        rows={readingsData}
        columns={readingsColumns}
        slots={{ toolbar: () => <CustomToolbar onAddClick={function (): void {
          throw new Error("Function not implemented.");
        } }  /> }}
        disableRowSelectionOnClick
        sx={{
          bgcolor: 'background.paper'
        }}
      />
    </Box>
  );
};

export default ReadingPage;