// src/components/CustomToolbar.tsx
import React from 'react';
import { GridToolbarContainer, GridToolbarColumnsButton, GridToolbarFilterButton, GridToolbarDensitySelector, GridToolbarExportContainer, GridCsvExportMenuItem, GridExportMenuItemProps, useGridApiContext } from "@mui/x-data-grid";
import { Button, MenuItem } from "@mui/material";
import { getJson, getXml, exportBlob } from '../utils/CustomExport';
import { ExportStatus } from '../utils/ExportStatus'; // Importiere ExportStatus
import ImportButton from './CustomImportButton';

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

export function CustomToolbar() {
  return (
    <GridToolbarContainer>
      <GridToolbarColumnsButton />
      <GridToolbarFilterButton />
      <GridToolbarDensitySelector />
      <CustomExportButton />
      <ImportButton/>
    </GridToolbarContainer>
  );
}
