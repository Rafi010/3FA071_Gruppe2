import React, { useState } from "react";
import { useGetData } from "../hooks/data";
import { red } from "@mui/material/colors";
import { CustomToolbar } from "../components/CustomToolbar";
import { read } from "node:fs";
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

export enum DataType {
  Customers = 'customers',
  Readings = 'readings',
}

const CustomerPage = () => {
  const { customerData, loading } = useGetCustomers();

  const [rows, setRows] = React.useState<Person[]>([]);
  const [rowModesModel, setRowModesModel] = React.useState<GridRowModesModel>({});

  const [errorOpen, setErrorOpen] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');

  React.useEffect(() => {
    if (customerData.length > 0) {
      setRows(customerData);
    }
  }, [customerData]);

  const handleRowEditStop: GridEventListener<'rowEditStop'> = (params, event) => {
    if (params.reason === GridRowEditStopReasons.rowFocusOut) {
      event.defaultMuiPrevented = true;
    }
  };

  const handleAddCustomer = () => {
    const id = uuidv4();
    
    const newCustomer:Person = {
      id,
      firstName: '',
      lastName: '',
      gender: '',
      birthDate: [Date.now()],
      isNew: true,
    };

    setRows((prevRows) => [newCustomer, ...prevRows]);

    setRowModesModel((prevModel: GridRowModesModel) => ({
      ...prevModel,
      [id]: { mode: GridRowModes.Edit, fieldToFocus: 'firstName' },
    }));
  }

  const handleEditClick = (id: GridRowId) => () => {
    setRowModesModel({ ...rowModesModel, [id]: { mode: GridRowModes.Edit } });
  };

  const handleSaveClick = (id: GridRowId) => () => {
    setRowModesModel({
      ...rowModesModel,
      [id]: { mode: GridRowModes.View },
    });
  };

  const removeIsNewFlag = (customer: Person) => {
    const { isNew, ...customerWithoutIsNew } = customer;
    return customerWithoutIsNew;
  };

  const processRowUpdate = async (newRow: Person) => {
    const { isNew, ...rest } = newRow;
  
    let sanitizedBirthDate = null;
    const rawDate = newRow.birthDate;
  
    if (Array.isArray(rawDate)) {
      sanitizedBirthDate = rawDate;
    } else {
      const date = new Date(rawDate as any); // cast safely
      if (!isNaN(date.getTime())) {
        sanitizedBirthDate = [
          date.getFullYear(),
          date.getMonth() + 1,
          date.getDate(),
        ];
      }
    }
  
    const customerToSend = {
      ...rest,
      birthDate: sanitizedBirthDate,
    };
  
    try {
      let savedCustomer: Person;
      if (isNew) {
        savedCustomer = await createCustomer(customerToSend);
      } else {
        savedCustomer = await updateCustomer(customerToSend);
      }
  
      setRows(rows.map((row) => (row.id === newRow.id ? savedCustomer : row)));
      return savedCustomer;
    } catch (error: any) {
      setErrorMessage(error.message || 'Unbekannter Fehler');
      setErrorOpen(true);
      throw error;
    }
  };

  const handleDeleteClick = (id: GridRowId) => () => {
    let response = deleteCustomer(id.toString())
    setRows(rows.filter((row) => row.id !== id));
  };

  const handleCancelClick = (id: GridRowId) => () => {
    setRowModesModel({
      ...rowModesModel,
      [id]: { mode: GridRowModes.View, ignoreModifications: true },
    });
  };

  const handleRowModesModelChange = (newRowModesModel: GridRowModesModel) => {
    setRowModesModel(newRowModesModel);
  };

  const handleRowUpdateError = (error: any) => {
    console.error('Update-Fehler:', error);
    setErrorMessage(error.message || 'Unbekannter Fehler beim Aktualisieren.');
    setErrorOpen(true);
  };




  const customersColumns: GridColDef[] = [
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
    },
    {
      field: 'actions',
      type: 'actions',
      headerName: 'Actions',
      width: 100,
      cellClassName: 'actions',
      getActions: ({ id }) => {
        const isInEditMode = rowModesModel[id]?.mode === GridRowModes.Edit;

        if (isInEditMode) {
          return [
            <GridActionsCellItem
              icon={<SaveIcon />}
              label="Save"
              onClick={handleSaveClick(id)}
            />,
            <GridActionsCellItem
              icon={<CancelIcon />}
              label="Cancel"
              className="textPrimary"
              onClick={handleCancelClick(id)}
              color="inherit"
            />,
          ];
        }
    
        return [
          <GridActionsCellItem
            icon={<EditIcon />}
            label="Edit"
            className="textPrimary"
            onClick={handleEditClick(id)}
            color="inherit"
          />,
          <GridActionsCellItem
            icon={<DeleteIcon />}
            label="Delete"
            onClick={handleDeleteClick(id)}
            color="inherit"
          />,
        ];
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
      <DataGrid
        rows={rows}
        columns={customersColumns}
        slots={{ toolbar: () => <CustomToolbar onAddClick={handleAddCustomer} /> }}
        editMode="row"
        rowModesModel={rowModesModel}
        onRowModesModelChange={handleRowModesModelChange}
        onRowEditStop={handleRowEditStop}
        processRowUpdate={processRowUpdate}
        onProcessRowUpdateError={handleRowUpdateError}
        disableRowSelectionOnClick
        sx={{
          bgcolor: 'background.paper'
        }}
      />
    </Box>
  );
};

export default CustomerPage;