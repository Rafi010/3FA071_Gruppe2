import React, { useState } from 'react';
import './App.css';
import DataSelection from './components/DataSelection';
import CustomerPage from './pages/CustomerPage';
import ReadingPage from './pages/ReadingPage';
import ChartPage from './pages/ChartPage';
import { CustomTabPanel } from './components/CustomTabPanel';
import { Box, Tabs, Tab, createTheme, ThemeProvider } from '@mui/material';
import { QueryClientProvider } from '@tanstack/react-query';
import { darkTheme } from './theme';
import { queryClient } from './queryClient';
import { a11yProps } from './utils/a11yProps';


function App() {
  const [value, setValue] = React.useState(0);
  const handleChange = (event: React.SyntheticEvent, newValue: number) => setValue(newValue);
  const [alignment, setAlignment] = useState("customers");

  return (
    <QueryClientProvider client={queryClient}>
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
        <CustomTabPanel value={value} index={0}>
          <DataSelection alignment={alignment} setAlignment={setAlignment}/>
          {alignment === "customers" ? (<CustomerPage />) : (<ReadingPage/>)}
        </CustomTabPanel>
        <CustomTabPanel value={value} index={1}>
          <ChartPage/>
        </CustomTabPanel>
        <CustomTabPanel value={value} index={2}>
          Item Three Content
        </CustomTabPanel>
      </header>
    </div>
    </ThemeProvider>
    </QueryClientProvider>
  );
}

export default App;
