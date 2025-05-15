// components/CrudDataGrid.tsx
import { DataGrid, GridColDef, GridRowModesModel, GridRowModes, GridRowId, GridRowEditStopReasons, GridActionsCellItem } from '@mui/x-data-grid';
import { Box, Snackbar, Alert } from '@mui/material';
import SaveIcon from '@mui/icons-material/Save';
import CancelIcon from '@mui/icons-material/Close';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/DeleteOutlined';
import { useState } from 'react';
import { CustomToolbar } from './CustomToolbar';
import { on } from 'events';

type CrudDataGridProps<T> = {
  rows: T[];
  setRows: (rows: T[]) => void;
  columns: GridColDef[];
  onAdd: () => string;
  onSave: (row: T) => Promise<T>;
  onDelete: (id: GridRowId) => void;
};

export function CrudDataGrid<T extends { id: string | number }>({
  rows,
  setRows,
  columns,
  onAdd,
  onSave,
  onDelete,
}: CrudDataGridProps<T>) {
  const [rowModesModel, setRowModesModel] = useState<GridRowModesModel>({});
  const [errorMessage, setErrorMessage] = useState('');
  const [errorOpen, setErrorOpen] = useState(false);

  const handleAddClick = () => {
    const id = onAdd(); // Make sure onAdd returns the id of the newly added row
    setRowModesModel((prev) => ({
      ...prev,
      [id]: { mode: GridRowModes.Edit, fieldToFocus: 'firstName' }
    }));
  };

  const handleEditClick = (id: GridRowId) => () => {
    setRowModesModel({ ...rowModesModel, [id]: { mode: GridRowModes.Edit } });
  };

  const handleSaveClick = (id: GridRowId) => () => {
    setRowModesModel({ ...rowModesModel, [id]: { mode: GridRowModes.View } });
  };

  const handleCancelClick = (id: GridRowId) => () => {
    const row = rows.find((r) => r.id === id);
  
    if (row && (row as any).isNew) {
      // Remove the new unsaved row on cancel
      setRows(rows.filter((r) => r.id !== id));
    }
  
    setRowModesModel({
      ...rowModesModel,
      [id]: { mode: GridRowModes.View, ignoreModifications: true },
    });
  };
  

  const handleDeleteClick = (id: GridRowId) => () => {
    onDelete(id); 
  };

  const handleRowModesModelChange = (model: GridRowModesModel) => setRowModesModel(model);

  const handleRowEditStop = (_params: any, event: any) => {
    if (_params.reason === GridRowEditStopReasons.rowFocusOut) {
      event.defaultMuiPrevented = true;
    }
  };

  const processRowUpdate = async (newRow: T) => {
    try {

      console.log("new", newRow)
      const updated = await onSave(newRow);
      setRows(rows.map((r) => (r.id === updated.id ? updated : r)));
      return updated;
    } catch (err: any) {
      setErrorMessage(err.message || 'Error updating row.');
      setErrorOpen(true);
      throw err;
    }
  };

  const actionCol: GridColDef = {
    field: 'actions',
    type: 'actions',
    headerName: 'Actions',
    width: 100,
    getActions: ({ id }) => {
      const isInEditMode = rowModesModel[id]?.mode === GridRowModes.Edit;

      return isInEditMode
        ? [
            <GridActionsCellItem icon={<SaveIcon />} label="Save" onClick={handleSaveClick(id)} />,
            <GridActionsCellItem icon={<CancelIcon />} label="Cancel" onClick={handleCancelClick(id)} color="inherit" />,
          ]
        : [
            <GridActionsCellItem icon={<EditIcon />} label="Edit" onClick={handleEditClick(id)} color="inherit" />,
            <GridActionsCellItem icon={<DeleteIcon />} label="Delete" onClick={handleDeleteClick(id)} color="inherit" />,
          ];
    },
  };

  return (
    <Box sx={{ height: 600, width: "100%" }}>
      <Snackbar
        open={errorOpen}
        autoHideDuration={6000}
        onClose={() => setErrorOpen(false)}
        anchorOrigin={{ vertical: 'bottom', horizontal: 'center' }}
      >
        <Alert onClose={() => setErrorOpen(false)} severity="error" sx={{ width: '100%' }}>
          {errorMessage}
        </Alert>
      </Snackbar>

      <DataGrid
        rows={rows}
        columns={[...columns, actionCol]}
        editMode="row"
        rowModesModel={rowModesModel}
        onRowModesModelChange={handleRowModesModelChange}
        onRowEditStop={handleRowEditStop}
        processRowUpdate={processRowUpdate}
        disableRowSelectionOnClick
        slots={{ toolbar: () => <CustomToolbar onAddClick={handleAddClick}/> }}
      />
    </Box>
  );
}
