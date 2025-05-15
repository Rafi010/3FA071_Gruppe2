import React, { useState, useEffect } from "react";
import { useGetReadings } from "../hooks/readingHooks";
import { createReading, updateReading, deleteReading } from "../api/readings";
import { Reading } from "../types/Reading";
import { v4 as uuidv4 } from "uuid";
import { GridRowId, GridColDef } from "@mui/x-data-grid";
import { CrudDataGrid } from "../components/CrudDataGrid";
import { Box, Snackbar, Alert } from "@mui/material";

type ReadingForGrid = Omit<Reading, "dateOfReading"> & {
  dateOfReading: Date | null;
  id: string;
};

const ReadingPage: React.FC = () => {
  const { data: readingData, isLoading: loading } = useGetReadings();
  const [rows, setRows] = useState<ReadingForGrid[]>([]);
  const [errorOpen, setErrorOpen] = useState(false);
  const [errorMessage, setErrorMessage] = useState("");

  // Normalize data from backend to ReadingForGrid (Date object & id)
  useEffect(() => {
    if (!loading && readingData) {
      const normalizedRows: ReadingForGrid[] = readingData.map((r: Reading) => ({
        ...r,
        id: r.id, // frontend uses id for keys
        dateOfReading: Array.isArray(r.dateOfReading)
          ? new Date(r.dateOfReading[0], r.dateOfReading[1] - 1, r.dateOfReading[2])
          : r.dateOfReading
          ? new Date(r.dateOfReading)
          : null,
      }));
      setRows(normalizedRows);
    }
  }, [readingData, loading]);

  // Add a new reading with a new UUID id
  const addReading = (): string => {
    const id = uuidv4();
    const newReading: ReadingForGrid = {
      id,
      meterId: "",
      meterCount: 0,
      dateOfReading: new Date(),
      isNew: true,
      kindOfMeter: "",
      substitute: false,
      comment: "",
      customer: { id: "" },
    };
    setRows((prev) => [newReading, ...prev]);
    return id;
  };

  // Save reading to backend (create or update) and return normalized row for grid
  const saveReading = async (row: ReadingForGrid): Promise<ReadingForGrid> => {
    const { isNew, dateOfReading, ...data } = row;
    const dateArray = dateOfReading instanceof Date
      ? [dateOfReading.getFullYear(), dateOfReading.getMonth() + 1, dateOfReading.getDate()]
      : [1970, 1, 1];
    console.log("data", row)
    const sanitized: Reading = {
      id: row.id,
      meterId: row.meterId,
      meterCount: row.meterCount,
      dateOfReading: dateArray,
      kindOfMeter: row.kindOfMeter,
      substitute: !!row.comment,
      comment: row.comment,
      customer: { id: row.customer?.id ?? "" },
    };
    console.log("sana", sanitized)
    const saved = isNew ? await createReading(sanitized) : await updateReading(sanitized);

    return {
      ...saved,
      id: saved.id,
      dateOfReading: Array.isArray(saved.dateOfReading)
        ? new Date(saved.dateOfReading[0], saved.dateOfReading[1] - 1, saved.dateOfReading[2])
        : saved.dateOfReading
        ? new Date(saved.dateOfReading)
        : null,
    };
  };

  // Remove reading by id and update state
  const removeReading = (id: GridRowId): void => {
    deleteReading(id.toString())
      .then(() => setRows((prev) => prev.filter((r) => r.id !== id)))
      .catch((err) => {
        setErrorMessage("Failed to delete reading");
        setErrorOpen(true);
        console.error(err);
      });
  };

  // Columns for DataGrid with proper getters and setters
  const readingColumns: GridColDef[] = [
    { field: "id", headerName: "ID", width: 250 },
    { field: "meterId", headerName: "Meter ID", width: 200, editable: true },
    {
      field: "meterCount",
      headerName: "Meter Count",
      editable: true,
      width: 150,
      type: "number",
    },
    {
      field: "dateOfReading",
      headerName: "Date Of Reading",
      editable: true,
      width: 180,
      type: "date",
      valueGetter: (value, row) => row.dateOfReading,
    },
    {
      field: "customerID",
      headerName: "Customer ID",
      editable: true,
      width: 200,
      valueGetter: (value, row) => row.customer?.id || "",
      valueSetter: (value, row) => {
        return {
          row,
          customer: { ...(row.customer ?? {}), id: value },
        };
      },    
    },
    {
      field: "kindOfMeter",
      headerName: "Kind of Meter",
      editable: true,
      width: 150,
    },
    {
      field: "comment",
      headerName: "Comment",
      editable: true,
      width: 250,
    },
  ];

  if (loading) return <div>Loading...</div>;
  if (!readingData || readingData.length === 0) return <p>No data available.</p>;

  return (
    <Box sx={{ height: 600, width: "90%" }}>
      <Snackbar
        open={errorOpen}
        autoHideDuration={6000}
        onClose={() => setErrorOpen(false)}
        anchorOrigin={{ vertical: "bottom", horizontal: "center" }}
      >
        <Alert
          onClose={() => setErrorOpen(false)}
          severity="error"
          sx={{ width: "100%" }}
        >
          {errorMessage}
        </Alert>
      </Snackbar>

      <CrudDataGrid<ReadingForGrid>
        rows={rows}
        setRows={setRows}
        columns={readingColumns}
        onAdd={addReading}
        onSave={saveReading}
        onDelete={removeReading}
      />
    </Box>
  );
};

export default ReadingPage;
