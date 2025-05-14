// src/components/CustomImportButton.tsx

import React, { useRef } from 'react';
import { Button } from '@mui/material';
import UploadFileIcon from '@mui/icons-material/UploadFile';
import { useMutation } from '@tanstack/react-query';

const API_BASE = process.env.REACT_APP_API_URL || 'http://localhost:8080';

// Generische POST-Funktion
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

  const handleFileChange = async (e: React.ChangeEvent<HTMLInputElement>) => {

    console.log("📂 Datei ausgewählt");

    const file = e.target.files?.[0];
    if (!file) return;

    try {
      const isJson = file.name.endsWith('.json');
      const text = await file.text();

      if (isJson) {
        const data = JSON.parse(text);
        await importJson(data);
      } else {
        const parser = new DOMParser();
        const xmlDoc = parser.parseFromString(text, 'application/xml');
        await importXml(xmlDoc);
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
      await mutation.mutateAsync({ endpoint: '/customers', data });
    } else if (data.reading) {
      await mutation.mutateAsync({ endpoint: '/readings', data });
    } else if (data.customers && Array.isArray(data.customers)) {
      for (const c of data.customers) {
        await mutation.mutateAsync({ endpoint: '/customers', data: { customer: c } });
      }
    } else if (data.readings && Array.isArray(data.readings)) {
      for (const r of data.readings) {
        await mutation.mutateAsync({ endpoint: '/readings', data: { reading: r} });
      }
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
        await mutation.mutateAsync({ endpoint: '/customers', data: { customer: obj } });
      }
    } else if (readings) {
      const items = readings.getElementsByTagName('reading');
      for (const el of Array.from(items)) {
        const obj: any = {};
        Array.from(el.children).forEach(c => {
          if (c.tagName === 'customer') {
            const cust: any = {};
            Array.from(c.children).forEach(cc => cust[cc.tagName] = cc.textContent);
            obj.customer = cust;
          } else {
            obj[c.tagName] = c.textContent;
          }
        });
        await mutation.mutateAsync({ endpoint: '/readings', data: { reading: obj } });
      }
    } else if (singleCustomer && !singleCustomer.parentElement?.tagName.toLowerCase().includes('customers')) {
      const obj: any = {};
      Array.from(singleCustomer.children).forEach(c => obj[c.tagName] = c.textContent);
      await mutation.mutateAsync({ endpoint: '/customers', data: { customer: obj } });
    } else if (singleReading && !singleReading.parentElement?.tagName.toLowerCase().includes('readings')) {
      const obj: any = {};
      Array.from(singleReading.children).forEach(c => {
        if (c.tagName === 'customer') {
          const cust: any = {};
          Array.from(c.children).forEach(cc => cust[cc.tagName] = cc.textContent);
          obj.customer = cust;
        } else {
          obj[c.tagName] = c.textContent;
        }
      });
      await mutation.mutateAsync({ endpoint: '/readings', data: { reading: obj } });
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
        onClick={() => fileInputRef.current?.click()}
        disabled={mutation.isPending}
      >
        Import
      </Button>
    </>
  );
}

export default CustomImportButton;
