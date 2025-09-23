import { app, BrowserWindow, Menu, ipcMain, shell } from 'electron';
import * as path from 'path';
import { spawn, ChildProcess } from 'child_process';

/**
 * Electron Main Process
 * Manages the backend Java process and creates the application window
 */
class StudentManagementApp {
  private mainWindow: BrowserWindow | null = null;
  private backendProcess: ChildProcess | null = null;
  private readonly BACKEND_PORT = 8080;
  private readonly isDev = process.env.NODE_ENV === 'development';

  constructor() {
    this.initializeApp();
  }

  private initializeApp(): void {
    // This method will be called when Electron has finished initialization
    app.whenReady().then(() => {
      this.startBackend();
      this.createWindow();
      this.createMenu();

      app.on('activate', () => {
        if (BrowserWindow.getAllWindows().length === 0) {
          this.createWindow();
        }
      });
    });

    // Quit when all windows are closed
    app.on('window-all-closed', () => {
      if (process.platform !== 'darwin') {
        this.cleanup();
        app.quit();
      }
    });

    // Clean up backend process when app is quitting
    app.on('before-quit', () => {
      this.cleanup();
    });

    // Handle IPC communication
    this.setupIpcHandlers();
  }

  private createWindow(): void {
    // Create the browser window with VS Code-like appearance
    this.mainWindow = new BrowserWindow({
      width: 1400,
      height: 900,
      minWidth: 1000,
      minHeight: 600,
      webPreferences: {
        nodeIntegration: false,
        contextIsolation: true,
        preload: path.join(__dirname, 'preload.js'), // We'll create this later if needed
      },
      titleBarStyle: 'default',
      backgroundColor: '#1e1e1e', // VS Code dark theme background
      show: false, // Don't show until ready
    });

    // Load the React app
    if (this.isDev) {
      this.mainWindow.loadURL('http://localhost:3000');
      this.mainWindow.webContents.openDevTools();
    } else {
      this.mainWindow.loadFile(path.join(__dirname, 'index.html'));
    }

    // Show window when ready to prevent visual flash
    this.mainWindow.once('ready-to-show', () => {
      this.mainWindow?.show();
    });

    // Handle window closed
    this.mainWindow.on('closed', () => {
      this.mainWindow = null;
    });

    // Handle external links
    this.mainWindow.webContents.setWindowOpenHandler(({ url }) => {
      shell.openExternal(url);
      return { action: 'deny' };
    });
  }

  private createMenu(): void {
    const template: Electron.MenuItemConstructorOptions[] = [
      {
        label: 'File',
        submenu: [
          {
            label: 'New Student',
            accelerator: 'CmdOrCtrl+N',
            click: () => {
              this.mainWindow?.webContents.send('menu-action', 'new-student');
            }
          },
          { type: 'separator' },
          {
            label: 'Import CSV',
            accelerator: 'CmdOrCtrl+I',
            click: () => {
              this.mainWindow?.webContents.send('menu-action', 'import-csv');
            }
          },
          {
            label: 'Export CSV',
            accelerator: 'CmdOrCtrl+E',
            click: () => {
              this.mainWindow?.webContents.send('menu-action', 'export-csv');
            }
          },
          { type: 'separator' },
          {
            label: 'Quit',
            accelerator: process.platform === 'darwin' ? 'Cmd+Q' : 'Ctrl+Q',
            click: () => {
              this.cleanup();
              app.quit();
            }
          }
        ]
      },
      {
        label: 'Edit',
        submenu: [
          { role: 'undo' },
          { role: 'redo' },
          { type: 'separator' },
          { role: 'cut' },
          { role: 'copy' },
          { role: 'paste' },
          { role: 'selectAll' }
        ]
      },
      {
        label: 'View',
        submenu: [
          {
            label: 'Command Palette',
            accelerator: 'CmdOrCtrl+Shift+P',
            click: () => {
              this.mainWindow?.webContents.send('menu-action', 'command-palette');
            }
          },
          { type: 'separator' },
          { role: 'reload' },
          { role: 'forceReload' },
          { role: 'toggleDevTools' },
          { type: 'separator' },
          { role: 'resetZoom' },
          { role: 'zoomIn' },
          { role: 'zoomOut' },
          { type: 'separator' },
          { role: 'togglefullscreen' }
        ]
      },
      {
        label: 'Window',
        submenu: [
          { role: 'minimize' },
          { role: 'close' }
        ]
      }
    ];

    const menu = Menu.buildFromTemplate(template);
    Menu.setApplicationMenu(menu);
  }

  private startBackend(): void {
    try {
      console.log('Starting backend Java service...');
      
      // In production, the JAR file should be packaged with the app
      const jarPath = this.isDev 
        ? path.join(__dirname, '../../build/libs/StudentManagementSystem-1.0-SNAPSHOT.jar')
        : path.join(process.resourcesPath, 'app/backend/StudentManagementSystem.jar');

      // Start the Java backend process
      this.backendProcess = spawn('java', [
        '-jar', 
        jarPath,
        '--server.port=' + this.BACKEND_PORT
      ], {
        stdio: ['pipe', 'pipe', 'pipe']
      });

      this.backendProcess.stdout?.on('data', (data) => {
        console.log(`Backend stdout: ${data}`);
      });

      this.backendProcess.stderr?.on('data', (data) => {
        console.error(`Backend stderr: ${data}`);
      });

      this.backendProcess.on('close', (code) => {
        console.log(`Backend process exited with code ${code}`);
        this.backendProcess = null;
      });

      this.backendProcess.on('error', (error) => {
        console.error('Failed to start backend process:', error);
      });

      console.log(`Backend service started on port ${this.BACKEND_PORT}`);
    } catch (error) {
      console.error('Error starting backend:', error);
    }
  }

  private setupIpcHandlers(): void {
    // Handle backend status requests
    ipcMain.handle('backend-status', async () => {
      return {
        running: this.backendProcess !== null,
        port: this.BACKEND_PORT,
        url: `http://localhost:${this.BACKEND_PORT}`
      };
    });

    // Handle restart backend request
    ipcMain.handle('restart-backend', async () => {
      this.cleanup();
      await new Promise(resolve => setTimeout(resolve, 2000)); // Wait 2 seconds
      this.startBackend();
      return { success: true };
    });
  }

  private cleanup(): void {
    if (this.backendProcess) {
      console.log('Terminating backend process...');
      this.backendProcess.kill('SIGTERM');
      this.backendProcess = null;
    }
  }
}

// Create and start the application
new StudentManagementApp();