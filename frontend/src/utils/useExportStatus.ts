// src/utils/useExportStatus.ts
import { useState } from 'react';

export enum ExportStatus {
  IDLE = 'IDLE',
  EXPORTING = 'EXPORTING',
  ERROR = 'ERROR',
}

export const useExportStatus = () => {
  const [status, setStatus] = useState<ExportStatus>(ExportStatus.IDLE);

  return {
    status,
    startExport: () => setStatus(ExportStatus.EXPORTING),
    exportError: () => setStatus(ExportStatus.ERROR),
  };
};
