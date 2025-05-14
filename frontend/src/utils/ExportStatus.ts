// src/utils/ExportStatus.ts
export class ExportStatus {
  private static success = false;
  private static errorMessage: string | null = null;

  static setSuccess(status: boolean) {
    ExportStatus.success = status;
  }

  static setError(message: string) {
    ExportStatus.success = false;
    ExportStatus.errorMessage = message;
  }

  static wasSuccessful(): boolean {
    return ExportStatus.success;
  }

  static getError(): string | null {
    return ExportStatus.errorMessage;
  }

  static reset() {
    ExportStatus.success = false;
    ExportStatus.errorMessage = null;
  }
}
