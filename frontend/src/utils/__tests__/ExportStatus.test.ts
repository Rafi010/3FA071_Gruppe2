// __tests__/ExportStatus.test.ts
import { ExportStatus } from '../ExportStatus';
// Correct the import path to match the actual file structure

describe('ExportStatus', () => {
  beforeEach(() => {
    ExportStatus.reset(); // Wichtig, um Seiteneffekte zwischen Tests zu vermeiden
  });

  test('initial state is unsuccessful with no error', () => {
    expect(ExportStatus.wasSuccessful()).toBe(false);
    expect(ExportStatus.getError()).toBeNull();
  });

  test('setSuccess(true) marks status as successful', () => {
    ExportStatus.setSuccess(true);
    expect(ExportStatus.wasSuccessful()).toBe(true);
    expect(ExportStatus.getError()).toBeNull();
  });

  test('setSuccess(false) keeps status unsuccessful', () => {
    ExportStatus.setSuccess(false);
    expect(ExportStatus.wasSuccessful()).toBe(false);
  });

  test('setError(message) sets status to unsuccessful and stores message', () => {
    ExportStatus.setError('Export failed');
    expect(ExportStatus.wasSuccessful()).toBe(false);
    expect(ExportStatus.getError()).toBe('Export failed');
  });

  test('reset() clears success and error state', () => {
    ExportStatus.setSuccess(true);
    ExportStatus.setError('Some error');
    ExportStatus.reset();
    expect(ExportStatus.wasSuccessful()).toBe(false);
    expect(ExportStatus.getError()).toBeNull();
  });
});
