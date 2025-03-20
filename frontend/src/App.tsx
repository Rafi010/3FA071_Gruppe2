import React, { useState } from 'react';
import './App.css';
import DataSelection from './components/DataSelection';
import CustomerPage from './pages/CustomerPage';
import ReadingPage from './pages/ReadingPage';
import { Box, Tabs, Tab, createTheme, ThemeProvider } from '@mui/material';
import ChartComponent from './pages/ChartPage';

const darkTheme = createTheme({
  palette: {
    mode: 'dark',  // Set dark mode
    background: {
      default: '#121212',
      paper: '#1f1f1f',  // Darker background for components
    },
    text: {
      primary: '#ffffff',  // Ensure text is white
    },
  },
});




function App() {

  interface TabPanelProps {
    children?: React.ReactNode;
    index: number;
    value: number;
  }
  
  function CustomTabPanel(props: TabPanelProps) {
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
  
  function a11yProps(index: number) {
    return {
      id: `simple-tab-${index}`,
      'aria-controls': `simple-tabpanel-${index}`,
    };
  }
  
  const [value, setValue] = React.useState(0);
  const handleChange = (event: React.SyntheticEvent, newValue: number) => {
    setValue(newValue);
  };

  const [alignment, setAlignment] = useState("customers");

  return (
    <ThemeProvider theme={darkTheme}>
    <div className="App" style={{ height: '100vh' }}> {/* Full height of the page */}
      <header className="App-header" style={{ height: '100%' }}>
        <Box sx={{ marginBottom: 3, borderColor: 'divider', width: '100%' }}>
          <Tabs value={value} onChange={handleChange} aria-label="basic tabs example" sx={{ width: '100%'}} centered>
            <Tab label="Data Management" {...a11yProps(0)} />
            <Tab label="Charts" {...a11yProps(1)} />
            <Tab label="Item Three" {...a11yProps(2)} />
          </Tabs>
        </Box>
        
        {/* Tab Panel for Customers and Readings */}
        <CustomTabPanel value={value} index={0}>
          <DataSelection alignment={alignment} setAlignment={setAlignment}/>
          {alignment === "customers" ? (<CustomerPage />) : (<ReadingPage/>)}
          
        </CustomTabPanel>
        <CustomTabPanel value={value} index={1}>
          <ChartComponent/>
        </CustomTabPanel>
        <CustomTabPanel value={value} index={2}>
          Item Three Content
        </CustomTabPanel>
      </header>
    </div>
    </ThemeProvider>
  );
}

export default App;
