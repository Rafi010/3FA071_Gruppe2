import React, { useState } from "react";
import { useGetCustomers } from "../hooks/useGetCustomers";
import Skeleton from '@mui/material/Skeleton';
import { createCustomer, deleteCustomer, updateCustomer } from "../api/customers";

import { Person } from '../types/Person'
import { v4 as uuidv4 } from 'uuid';
import Box from '@mui/material/Box';
import Tooltip from '@mui/material/Tooltip';
import AddIcon from '@mui/icons-material/Add';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/DeleteOutlined';
import SaveIcon from '@mui/icons-material/Save';
import CancelIcon from '@mui/icons-material/Close';
import {
  GridRowsProp,
  GridRowModesModel,
  GridRowModes,
  DataGrid,
  GridColDef,
  GridActionsCellItem,
  GridEventListener,
  GridRowId,
  GridRowModel,
  GridRowEditStopReasons,
  GridSlotProps
} from '@mui/x-data-grid';  
import { Snackbar, Alert } from "@mui/material";
import { CrudDataGrid } from "../components/CrudDataGrid";

export enum DataType {
  Customers = 'customers',
  Readings = 'readings',
}

type PersonForGrid = Omit<Person, 'birthDate'> & { birthDate: Date | null };

const CustomerPage = () => {
  const { customerData, loading } = useGetCustomers();
  const [rowModesModel, setRowModesModel] = React.useState<GridRowModesModel>({});
  const [rows, setRows] = useState<PersonForGrid[]>([]);

  const [errorOpen, setErrorOpen] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');
  
  React.useEffect(() => {
  // Convert birthDate array to Date object for editing in grid
  const normalizedRows = customerData.map((c) => ({
    ...c,
    birthDate: Array.isArray(c.birthDate) 
      ? new Date(c.birthDate[0], c.birthDate[1] - 1, c.birthDate[2])
      : c.birthDate ? new Date(c.birthDate) : null,
  }));
  setRows(normalizedRows);
}, [customerData]);

  const addCustomer = (): string => {
    const id = uuidv4();
    const newCustomer: PersonForGrid = {
      id,
      firstName: '',
      lastName: '',
      gender: '',
      birthDate: null,
      isNew: true,
    };
  
    setRows((prev) => [newCustomer, ...prev]);
    return id;
  };
  
  
  const saveCustomer = async (row: PersonForGrid): Promise<PersonForGrid> => {
    const { isNew, birthDate, ...data } = row;
  
    const birthDateArray = birthDate instanceof Date
      ? [birthDate.getFullYear(), birthDate.getMonth() + 1, birthDate.getDate()]
      : null;
  
    const sanitized: Person = {
      ...data,
      birthDate: birthDateArray,
    };
  
    const saved = isNew
      ? await createCustomer(sanitized)
      : await updateCustomer(sanitized);
  
    // Convert backend response (with birthDate as number[]) back to PersonForGrid
    return {
      ...saved,
      birthDate: Array.isArray(saved.birthDate)
        ? new Date(saved.birthDate[0], saved.birthDate[1] - 1, saved.birthDate[2])
        : saved.birthDate
          ? new Date(saved.birthDate)
          : null,
    };
  };

  const removeCustomer = (id: GridRowId): void => {
    deleteCustomer(id.toString())
      .then(() => {
        setRows((prevRows) => prevRows.filter((row) => row.id !== id));
      })
      .catch((error) => {
        setErrorMessage('Failed to delete customer');
        setErrorOpen(true);
        console.error(error);
      });
  };

  const customerColumns: GridColDef[] = [
    { field: "id", headerName: "ID", width: 400 },
    { field: "firstName", headerName: "First Name", width: 200, editable: true },
    { field: "lastName", headerName: "Last Name", width: 200, editable: true },
    { field: "gender", headerName: "Gender", width: 120, editable: true },
    {
      field: "birthDate",
      headerName: "Birth Date",
      type: 'date',
      editable: true,
      width: 150,
      valueGetter: (value, row) => {
        const birthDate = row?.birthDate;
        if (Array.isArray(birthDate)) {
          // Assume format: [YYYY, MM, DD]
          return new Date(birthDate[0], birthDate[1] - 1, birthDate[2]);
        }
    
        if (typeof birthDate === 'string' || birthDate instanceof Date) {
          return new Date(birthDate);
        }
    
        return null;
      }
    }
  ];


  if (loading) return (
  <Box sx={{ width: "90%", marginBottom: 10 }}>
    <Skeleton animation="wave" variant="rectangular" height={50} sx={{ marginBottom: 2, bgcolor: '#1f1f1f' }} />
    <Skeleton animation="wave" variant="rectangular" height={600} sx={{bgcolor: '#1f1f1f' }}/>
  </Box>);

  if (customerData.length === 0) return <p>No data available.</p>;

  return (
    <Box sx={{ height: 600, width: "90%"}}>
      <Snackbar
    open={errorOpen}
    autoHideDuration={6000}
    onClose={() => setErrorOpen(false)}
    anchorOrigin={{ vertical: 'bottom', horizontal: 'center' }}
  >
    <Alert
      onClose={() => setErrorOpen(false)}
      severity="error"
      sx={{ width: '100%' }}
    >
      {errorMessage}
    </Alert>
  </Snackbar>
      <CrudDataGrid<PersonForGrid>
            rows={rows}
            setRows={setRows}
            columns={customerColumns} // defined same way as before
            onAdd={addCustomer}
            onSave={saveCustomer}
            onDelete={removeCustomer}
        />
    </Box>
  );
};

export default CustomerPage;