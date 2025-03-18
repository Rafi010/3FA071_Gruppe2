import React, { useState } from "react";
import { useGetData } from "../hooks/data";
import { Box, CircularProgress, MenuItem, Skeleton } from "@mui/material";
import { DataGrid, GridColDef, GridCsvExportMenuItem, GridExportMenuItemProps, gridFilteredSortedRowIdsSelector, GridToolbar, GridToolbarColumnsButton, GridToolbarContainer, GridToolbarDensitySelector, GridToolbarExportContainer, GridToolbarFilterButton, gridVisibleColumnFieldsSelector, useGridApiContext } from "@mui/x-data-grid";
import { red } from "@mui/material/colors";
import { CustomToolbar } from "./CustomToolbar";

interface Person {
  firstName: string;
  lastName: string;
  gender: string;
  id: string;
  birthDate: number[] | null;
}

interface VisualizationProps {
  dataType: DataType;
}

export enum DataType {
  Customers = 'customers',
  Readings = 'readings',
}

const Visualization: React.FC<VisualizationProps> = ({dataType}) => {
  const { data, loading } = useGetData(dataType);
  const [filter, setFilter] = useState("");
  const [selectedRows, setSelectedRows] = useState<string[]>([]);
  const handleFilterChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setFilter(event.target.value);
  };

  // Filter the data based on the filter input
  const filteredData = data.filter((person: Person) => {
    const name = `${person.firstName} ${person.lastName}`;
    return (
      name.toLowerCase().includes(filter.toLowerCase()) ||
      person.gender.toLowerCase().includes(filter.toLowerCase())
    );
  });

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
      return readingDate ? readingDate.join("-") : "N/A";
    }, },
    { field: "meterCount", headerName: "Meter Count", width: 150 },
    { field: "comment", headerName: "Comment", width: 150 },
    
  ];

  if (loading) return (
  <Box sx={{ width: "90%", marginBottom: 10 }}>
    <Skeleton animation="wave" variant="rectangular" height={50} sx={{ marginBottom: 2, bgcolor: 'grey.900' }} />
    <Skeleton animation="wave" variant="rectangular" height={600} sx={{bgcolor: 'grey.900' }}/>
  </Box>);

  if (data.length === 0) return <p>No data available.</p>;

  return (
    <Box sx={{ height: 600, width: "90%", marginBottom: 10}}>
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
        columns={dataType === DataType.Customers ? customersColumns : readingsColumns}
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

export default Visualization;