// src/components/CustomImportButton.tsx

import React, { useRef, useState } from 'react';
import { Button, Menu, MenuItem } from '@mui/material';
import UploadFileIcon from '@mui/icons-material/UploadFile';
import { useMutation, useQueryClient } from '@tanstack/react-query';

const API_BASE = process.env.REACT_APP_API_URL || 'http://localhost:8080';

// Hilfsfunktionen zur Normalisierung
const normalizeReading = (r: any) => {
  const fallback = { ...r };
  if (!r.kindOfMeter || !["STROM", "HEIZUNG", "WASSER", "UNBEKANNT"].includes(r.kindOfMeter.toUpperCase())) {
    fallback.kindOfMeter = "UNBEKANNT";
  }
  if (fallback.substitute === undefined) fallback.substitute = false;
  if (fallback.comment === undefined) fallback.comment = null;
  return fallback;
};

const normalizeCustomer = (c: any) => {
  const fallback = { ...c };
  if (!["M", "W", "D", "U"].includes(fallback.gender)) {
    fallback.gender = "U";
  }
  return fallback;
};

const postEntity = async (endpoint: string, payload: any) => {
  const response = await fetch(`${API_BASE}${endpoint}`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(payload),
  });

  if (!response.ok) {
    const err = await response.text();
    throw new Error(`${response.status} ${err}`);
  }

  return await response.json().catch(() => ({}));
};

const useImportMutation = () => {
  return useMutation({
    mutationFn: async (params: { endpoint: string; data: any }) => {
      return await postEntity(params.endpoint, params.data);
    },
  });
};

function CustomImportButton() {
  const fileInputRef = useRef<HTMLInputElement | null>(null);
  const mutation = useImportMutation();
  const queryClient = useQueryClient();

  const [importType, setImportType] = useState<'jsonXml' | 'csvCustomer' | 'csvReading'>('jsonXml');
  const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);
  const open = Boolean(anchorEl);

  const handleMenuClick = (event: React.MouseEvent<HTMLButtonElement>) => {
    setAnchorEl(event.currentTarget);
  };

  const handleMenuClose = (type?: 'jsonXml' | 'csvCustomer' | 'csvReading') => {
    setAnchorEl(null);
    if (type) {
      setImportType(type);
      fileInputRef.current?.click();
    }
  };

  const handleFileChange = async (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (!file) return;

    try {
      const isJson = file.name.endsWith('.json');
      const text = await file.text();

      if (importType === 'jsonXml') {
        if (isJson) {
          const data = JSON.parse(text);
          await importJson(data);
        } else {
          const parser = new DOMParser();
          const xmlDoc = parser.parseFromString(text, 'application/xml');
          await importXml(xmlDoc);
        }
      } else {
        alert('CSV-Import wird noch implementiert.');
      }

      alert('Import erfolgreich!');
    } catch (err: any) {
      console.error(err);
      alert('Import fehlgeschlagen: ' + err.message);
    } finally {
      e.target.value = '';
    }
  };

  const importJson = async (data: any) => {
    if (data.customer) {
      const normalized = { customer: normalizeCustomer(data.customer) };
      await mutation.mutateAsync({ endpoint: '/customers', data: normalized });
      queryClient.invalidateQueries({ queryKey: ['customers'] });
    } else if (data.reading) {
      const normalized = { reading: normalizeReading(data.reading) };
      await mutation.mutateAsync({ endpoint: '/readings', data: normalized });
      queryClient.invalidateQueries({ queryKey: ['readings'] });
    } else if (data.customers && Array.isArray(data.customers)) {
      for (const c of data.customers) {
        await mutation.mutateAsync({ endpoint: '/customers', data: { customer: normalizeCustomer(c) } });
      }
      queryClient.invalidateQueries({ queryKey: ['customers'] });
    } else if (data.readings && Array.isArray(data.readings)) {
      for (const r of data.readings) {
        await mutation.mutateAsync({ endpoint: '/readings', data: { reading: normalizeReading(r) } });
      }
      queryClient.invalidateQueries({ queryKey: ['readings'] });
    } else {
      throw new Error("Unbekanntes JSON-Format");
    }
  };

  const importXml = async (xmlDoc: Document) => {
    const customers = xmlDoc.getElementsByTagName('customers')[0];
    const readings = xmlDoc.getElementsByTagName('readings')[0];
    const singleCustomer = xmlDoc.getElementsByTagName('customer')[0];
    const singleReading = xmlDoc.getElementsByTagName('reading')[0];

    if (customers) {
      const items = customers.getElementsByTagName('customer');
      for (const el of Array.from(items)) {
        const obj: any = {};
        Array.from(el.children).forEach(c => obj[c.tagName] = c.textContent);
        await mutation.mutateAsync({ endpoint: '/customers', data: { customer: normalizeCustomer(obj) } });
      }
      queryClient.invalidateQueries({ queryKey: ['customers'] });
    } else if (readings) {
      const items = readings.getElementsByTagName('reading');
      for (const el of Array.from(items)) {
        const obj: any = {};
        Array.from(el.children).forEach(c => {
          if (c.tagName === 'customer') {
            const cust: any = {};
            Array.from(c.children).forEach(cc => cust[cc.tagName] = cc.textContent);
            obj.customer = normalizeCustomer(cust);
          } else {
            obj[c.tagName] = c.textContent;
          }
        });
        await mutation.mutateAsync({ endpoint: '/readings', data: { reading: normalizeReading(obj) } });
      }
      queryClient.invalidateQueries({ queryKey: ['readings'] });
    } else if (singleCustomer && !singleCustomer.parentElement?.tagName.toLowerCase().includes('customers')) {
      const obj: any = {};
      Array.from(singleCustomer.children).forEach(c => obj[c.tagName] = c.textContent);
      await mutation.mutateAsync({ endpoint: '/customers', data: { customer: normalizeCustomer(obj) } });
      queryClient.invalidateQueries({ queryKey: ['customers'] });
    } else if (singleReading && !singleReading.parentElement?.tagName.toLowerCase().includes('readings')) {
      const obj: any = {};
      Array.from(singleReading.children).forEach(c => {
        if (c.tagName === 'customer') {
          const cust: any = {};
          Array.from(c.children).forEach(cc => cust[cc.tagName] = cc.textContent);
          obj.customer = normalizeCustomer(cust);
        } else {
          obj[c.tagName] = c.textContent;
        }
      });
      await mutation.mutateAsync({ endpoint: '/readings', data: { reading: normalizeReading(obj) } });
      queryClient.invalidateQueries({ queryKey: ['readings'] });
    } else {
      throw new Error("Unbekanntes XML-Format");
    }
  };

  return (
    <>
      <input
        type="file"
        ref={fileInputRef}
        style={{ display: 'none' }}
        accept=".json,.xml"
        onChange={handleFileChange}
      />
      <Button
        variant="text"
        size="small"
        startIcon={<UploadFileIcon />}
        onClick={handleMenuClick}
        disabled={mutation.isPending}
      >
        Import
      </Button>
      <Menu anchorEl={anchorEl} open={open} onClose={() => handleMenuClose()}>
        <MenuItem onClick={() => handleMenuClose('jsonXml')}>JSON / XML</MenuItem>
        <MenuItem onClick={() => handleMenuClose('csvCustomer')}>CSV Customer</MenuItem>
        <MenuItem onClick={() => handleMenuClose('csvReading')}>CSV Reading</MenuItem>
      </Menu>
    </>
  );
}

export default CustomImportButton;
