import * as React from "react";
import { GridToolbarContainer } from "@mui/x-data-grid";
import Button from "@mui/material/Button";
import UploadFileIcon from "@mui/icons-material/UploadFile";

function ImportButton() {
  const handleImport = () => {
    alert("Import functionality not implemented yet!");
  };

  return (
    <Button
      variant="text"
      size="small"
      startIcon={<UploadFileIcon />}
      onClick={handleImport}
    >
      Import
    </Button>
  );
}

export default ImportButton;
