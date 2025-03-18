import React, { useState } from 'react';
import './App.css';
import DataSelection from './components/DataSelection';
import CustomerPage from './components/CustomerPage';
import ReadingPage from './components/ReadingPage';
import { Box, Tabs, Tab } from '@mui/material';
import ChartComponent from './components/ChartPage';

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
    <div className="App" style={{ height: '100vh' }}> {/* Full height of the page */}
      <header className="App-header" style={{ height: '100%' }}>
        <Box sx={{ marginBottom: 3, borderColor: 'divider', width: '100%' }}>
          <Tabs value={value} onChange={handleChange} aria-label="basic tabs example" sx={{ width: '100%' }} centered>
            <Tab label="Customers" {...a11yProps(0)} />
            <Tab label="Item Two" {...a11yProps(1)} />
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
  );
}

export default App;
