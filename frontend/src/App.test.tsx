import React from 'react';
import { render, screen } from '@testing-library/react';
import App from './App';

test('renders the main tabs', () => {
  render(<App />);
  expect(screen.getByText('Data Management')).toBeInTheDocument();
  expect(screen.getByText('Charts')).toBeInTheDocument();
  expect(screen.getByText('Item Three')).toBeInTheDocument();
});
