// Global type definitions for Electron APIs
declare global {
  interface Window {
    electronAPI?: {
      onMenuAction: (callback: (event: any, action: string) => void) => void;
      getBackendStatus: () => Promise<{ running: boolean; port: number; url: string }>;
      restartBackend: () => Promise<{ success: boolean }>;
    };
  }
}

export {};