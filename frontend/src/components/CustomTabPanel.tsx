import { Box } from "@mui/material";

interface TabPanelProps {
    children?: React.ReactNode;
    index: number;
    value: number;
  }
  
  export function CustomTabPanel(props: TabPanelProps) {
    const { children, value, index, ...other } = props;
  
    return (
      <div
        role="tabpanel"
        hidden={value !== index}
        id={`simple-tabpanel-${index}`}
        aria-labelledby={`simple-tab-${index}`}
        {...other}
        style={{ width: '100%', height: '100%', overflowY: 'auto' }}
      >
        {value === index && <Box sx={{height: '100%', display: 'flex', flexDirection: 'column', alignItems: 'center' }}>{children}</Box>}
      </div>
    );
  }