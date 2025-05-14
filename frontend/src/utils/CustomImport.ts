// src/utils/CustomImport.ts

/**
 * Automatischer Import via POST-Request.
 * Versendet die Datei im Originalformat, damit Wrapper-Objekte erhalten bleiben.
 */
const API_BASE = process.env.REACT_APP_API_URL || 'http://localhost:8080';

export const importJson = async (file: File): Promise<void> => {
  // Original-JSON-Text beibehalten, damit Jackson Wrapper-Objekte erwarten kann
  const text = await file.text();

  // Im File muss bereits das Wrapper-Objekt enthalten sein, z.B. { "customer": { ... } }
  // Endpoint ableiten: /customers oder /readings
  const rootKey = Object.keys(JSON.parse(text))[0];
  let endpoint: string;
  if (rootKey === 'customer') endpoint = '/customers';
  else if (rootKey === 'reading') endpoint = '/readings';
  else throw new Error("Unbekannter Root-Key '" + rootKey + "' im JSON.");

  const response = await fetch(`${API_BASE}${endpoint}`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: text,
  });

  if (!response.ok) {
    const err = await response.text();
    throw new Error(`Import fehlgeschlagen: ${response.status} ${err}`);
  }
};

export const importXml = async (file: File): Promise<void> => {
  const text = await file.text();
  const parser = new DOMParser();
  const xmlDoc = parser.parseFromString(text, "application/xml");

  let endpoint: string;
  let obj: any = {};

  // Prüfe, ob es <customer> oder <reading> ist
  const custEl = xmlDoc.querySelector("customer");
  if (custEl) {
    endpoint = "/customers";
    Array.from(custEl.children).forEach(c => { obj[c.tagName] = c.textContent; });
    // wrap it just like JSON erwartet: { customer: { ... } }
    obj = { customer: obj };
  } else {
    const readEl = xmlDoc.querySelector("reading");
    if (!readEl) throw new Error("Kein <customer> oder <reading> im XML");
    endpoint = "/readings";
    Array.from(readEl.children).forEach(c => {
      if (c.tagName === "customer") {
        const sub: any = {};
        Array.from(c.children).forEach(cc => sub[cc.tagName] = cc.textContent);
        obj.customer = sub;
      } else {
        obj[c.tagName] = c.textContent;
      }
    });
    obj = { reading: obj };
  }

  const response = await fetch(`${API_BASE}${endpoint}`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(obj),
  });

  if (!response.ok) {
    const err = await response.text();
    throw new Error(`Import fehlgeschlagen: ${response.status} ${err}`);
  }
};
