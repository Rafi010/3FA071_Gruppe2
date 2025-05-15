// src/views/ExportView.tsx
import React from 'react';
import { DataGrid, GridColDef, GridRowsProp } from '@mui/x-data-grid';
import { CustomToolbar } from '../../components/CustomToolbar';

const columns: GridColDef[] = [
  { field: 'id', headerName: 'ID', width: 100 },
  { field: 'name', headerName: 'Name', width: 200 }
];

const rows: GridRowsProp = [
  { id: 1, name: 'Max Mustermann' },
  { id: 2, name: 'Erika Musterfrau' }
];

export const ExportView = () => {
  return (
    <div style={{ height: 400, width: '100%' }}>
      <DataGrid
        rows={rows}
        columns={columns}
        slots={{ toolbar: () => <CustomToolbar onAddClick={function (): void {
          throw new Error('Function not implemented.');
        } } /> }}
      />
    </div>
  );
};
