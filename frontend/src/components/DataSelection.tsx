import { ToggleButtonGroup, ToggleButton } from "@mui/material";
import { useState } from "react";

interface ToggleSwitchProps {
    alignment: string;
    setAlignment: (value: string) => void;
  }

export default function ToggleSwitch({ alignment, setAlignment }: ToggleSwitchProps) {
    
  
    const handleChange = (_event: React.MouseEvent<HTMLElement>, newAlignment: string | null) => {
      if (newAlignment !== null) {
        setAlignment(newAlignment);
      }
    };
  
    return (
      <ToggleButtonGroup
        sx={{bgcolor: "background.paper", marginBottom: 2}}
        color="primary"
        value={alignment}
        exclusive
        onChange={handleChange}
        aria-label="toggle button group"
      >
        <ToggleButton value="customers">Customers</ToggleButton>
        <ToggleButton value="readings">Readings</ToggleButton>
      </ToggleButtonGroup>
    );
  }