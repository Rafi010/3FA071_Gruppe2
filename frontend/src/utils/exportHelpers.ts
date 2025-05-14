// src/utils/exportHelpers.ts
export const getJson = async (): Promise<string> => {
  // Dummy-Daten
  const data = [
    { id: 1, name: 'Max Mustermann' },
    { id: 2, name: 'Erika Musterfrau' }
  ];
  return JSON.stringify(data, null, 2);
};

export const getXml = async (): Promise<string> => {
  // Dummy-Daten
  const data = [
    { id: 1, name: 'Max Mustermann' },
    { id: 2, name: 'Erika Musterfrau' }
  ];
  const xml = `<data>\n${data.map((d) => `  <entry><id>${d.id}</id><name>${d.name}</name></entry>`).join('\n')}\n</data>`;
  return xml;
};

export const exportBlob = (blob: Blob, filename: string) => {
  const link = document.createElement('a');
  link.href = URL.createObjectURL(blob);
  link.download = filename;
  document.body.appendChild(link);
  link.click();
  link.remove();
};
