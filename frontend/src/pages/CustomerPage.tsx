import React, { useState } from "react";
import { useGetData } from "../hooks/data";
import { Box, CircularProgress, MenuItem, radioClasses, Skeleton, TextField } from "@mui/material";
import { DataGrid, GridColDef, GridCsvExportMenuItem, GridExportMenuItemProps, gridFilteredSortedRowIdsSelector, GridToolbar, GridToolbarColumnsButton, GridToolbarContainer, GridToolbarDensitySelector, GridToolbarExportContainer, GridToolbarFilterButton, gridVisibleColumnFieldsSelector, useGridApiContext } from "@mui/x-data-grid";
import { red } from "@mui/material/colors";
import { CustomToolbar } from "../components/CustomToolbar";
import { read } from "node:fs";

interface Person {
  firstName: string;
  lastName: string;
  gender: string;
  id: string;
  birthDate: number[] | null;
}

export enum DataType {
  Customers = 'customers',
  Readings = 'readings',
}

const CustomerPage = () => {
  const { customerData, loading } = useGetData();
  const [filter, setFilter] = useState("");
  const [selectedRows, setSelectedRows] = useState<string[]>([]);
  const handleFilterChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setFilter(event.target.value);
  };

  // Filter the data based on the filter input
  const filteredData = customerData.filter((person) => {
    const name = `${person.firstName} ${person.lastName}`;
    return (
      name.toLowerCase().includes(filter.toLowerCase()) ||
      person.gender.toLowerCase().includes(filter.toLowerCase())
    );
  }
  );

  // Define the columns for the DataGrid
  const customersColumns: GridColDef[] = [
    { field: "id", headerName: "ID", width: 300 },
    { field: "firstName", headerName: "First Name", width: 150 },
    { field: "lastName", headerName: "Last Name", width: 150 },
    { field: "gender", headerName: "Gender", width: 120 },
    {
      field: "birthDate",
      headerName: "Birth Date",
      width: 150,
      valueGetter: (value, row) => {
        const birthDate = row.birthDate;
        return birthDate ? birthDate.join("-") : "N/A";
      },
    },
  ];


  if (loading) return (
  <Box sx={{ width: "90%", marginBottom: 10 }}>
    <Skeleton animation="wave" variant="rectangular" height={50} sx={{ marginBottom: 2, bgcolor: '#1f1f1f' }} />
    <Skeleton animation="wave" variant="rectangular" height={600} sx={{bgcolor: '#1f1f1f' }}/>
  </Box>);

  if (customerData.length === 0) return <p>No data available.</p>;

  return (
    <Box sx={{ height: 600, width: "90%"}}>
      {/* Filter Input */}
      <Box sx={{ marginBottom: 2 }}>
      <TextField
          label="Filter"
          variant="outlined"
          value={filter}
          onChange={handleFilterChange}
          fullWidth
          sx={{
            input: {
              color: 'white',  // Ensure text is white
            },
            '& .MuiInputBase-root': {
              backgroundColor: 'background.paper', // Match dark theme background
              borderRadius: '4px',
            },
            '& .MuiOutlinedInput-root': {
              borderColor: 'rgba(255, 255, 255, 0.23)',  // Light border for dark mode
            },
            '& .MuiInputLabel-root': {
              color: 'white',  // White label color
            },
            '& .MuiFormLabel-root.Mui-focused': {
              color: 'white',  // Keep label white when focused
            },
          }}
        />
      </Box>

      {/* DataGrid */}
      <DataGrid
        rows={filteredData}
        columns={customersColumns}
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

export default CustomerPage;