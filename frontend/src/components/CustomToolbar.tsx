// src/components/CustomToolbar.tsx
import React from 'react';
import { GridToolbarContainer, GridToolbarColumnsButton, GridToolbarFilterButton, GridToolbarDensitySelector, GridToolbarExportContainer, GridCsvExportMenuItem, GridExportMenuItemProps, useGridApiContext } from "@mui/x-data-grid";
import { Box, Button, IconButton, MenuItem, Tooltip } from "@mui/material";
import { getJson, getXml, exportBlob } from '../utils/CustomExport';
import { ExportStatus } from '../utils/ExportStatus'; // Importiere ExportStatus
import ImportButton from './CustomImportButton';
import AddIcon from '@mui/icons-material/Add';

function JsonExportMenuItem(props: GridExportMenuItemProps<{}>) {
  const apiRef = useGridApiContext();
  const { hideMenu } = props;

  return (
    <MenuItem
      onClick={() => {
        try {
          const jsonString = getJson(apiRef);
          const blob = new Blob([jsonString], { type: 'text/json' });
          exportBlob(blob, 'DataGrid_export.json');
          ExportStatus.setSuccess(true); // Erfolgreicher Export
        } catch (error) {
          ExportStatus.setError('Export JSON failed');
        } finally {
          hideMenu?.();
        }
      }}
    >
      Export JSON
    </MenuItem>
  );
}

function XmlExportMenuItem(props: GridExportMenuItemProps<{}>) {
  const apiRef = useGridApiContext();
  const { hideMenu } = props;

  return (
    <MenuItem
      onClick={() => {
        try {
          const xmlString = getXml(apiRef);
          const blob = new Blob([xmlString], { type: "application/xml" });
          exportBlob(blob, "DataGrid_export.xml");
          ExportStatus.setSuccess(true); // Erfolgreicher Export
        } catch (error) {
          ExportStatus.setError('Export XML failed');
        } finally {
          hideMenu?.();
        }
      }}
    >
      Export XML
    </MenuItem>
  );
}

function CustomExportButton(props: any) {
  return (
    <GridToolbarExportContainer {...props}>
      <GridCsvExportMenuItem />
      <JsonExportMenuItem />
      <XmlExportMenuItem/>
    </GridToolbarExportContainer>
  );
}

function CustomAddButton({ onClick }: { onClick: () => void }) {
  return (
    <Tooltip title="Add record">
      <IconButton onClick={onClick}>
        <AddIcon />
      </IconButton>
    </Tooltip>
  );
}

type CustomToolbarProps = {
  onAddClick: () => void;
};



export function CustomToolbar({ onAddClick }: CustomToolbarProps) {
  return (
    <GridToolbarContainer>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', width: '100%' }}>
        <Box>
          <GridToolbarColumnsButton />
          <GridToolbarFilterButton />
          <GridToolbarDensitySelector />
          <CustomExportButton />
          <ImportButton />
        </Box>
        <Box>
          <CustomAddButton onClick={onAddClick} />
        </Box>
      </Box>
    </GridToolbarContainer>
  );
}
