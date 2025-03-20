import { gridFilteredSortedRowIdsSelector, gridVisibleColumnFieldsSelector } from "@mui/x-data-grid";

export const getJson = (apiRef: React.MutableRefObject<any>) => {
  const filteredSortedRowIds = gridFilteredSortedRowIdsSelector(apiRef);
  const visibleColumnsField = gridVisibleColumnFieldsSelector(apiRef);

  const data = filteredSortedRowIds.map((id) => {
    const row: Record<string, any> = {};
    visibleColumnsField.forEach((field) => {
      row[field] = apiRef.current.getCellParams(id, field).value;
    });
    return row;
  });

  return JSON.stringify(data, null, 2);
};

export const getXml = (apiRef: React.MutableRefObject<any>) => {
  const filteredSortedRowIds = gridFilteredSortedRowIdsSelector(apiRef);
  const visibleColumnsField = gridVisibleColumnFieldsSelector(apiRef);

  let xmlString = `<?xml version="1.0" encoding="UTF-8"?>\n<rows>\n`;

  filteredSortedRowIds.forEach((id) => {
    xmlString += `  <row>\n`;
    visibleColumnsField.forEach((field) => {
      const value = apiRef.current.getCellParams(id, field).value || "";
      xmlString += `    <${field}>${value}</${field}>\n`;
    });
    xmlString += `  </row>\n`;
  });

  xmlString += `</rows>`;
  return xmlString;
};

export const exportBlob = (blob: Blob, filename: string) => {
  const url = URL.createObjectURL(blob);
  const a = document.createElement('a');
  a.href = url;
  a.download = filename;
  a.click();
  setTimeout(() => {
    URL.revokeObjectURL(url);
  });
};
