import React from 'react';
import { render, screen } from '@testing-library/react';
import { DataGrid } from '@mui/x-data-grid';
import { CustomToolbar } from '../../components/CustomToolbar';

describe('CustomToolbar', () => {
  test('renders toolbar with export button', () => {
    render(
      <div style={{ height: 400, width: '100%' }}>
        <DataGrid
          rows={[]}
          columns={[]}
          slots={{ toolbar: CustomToolbar }}
        />
      </div>
    );

    expect(screen.getByRole('button', { name: /Export/i })).toBeInTheDocument();
  });
});
