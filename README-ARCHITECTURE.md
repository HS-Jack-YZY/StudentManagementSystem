# Student Management System - Frontend-Backend Separation

This repository has been refactored to implement a **VS Code-inspired frontend-backend architecture** with complete separation of concerns.

## Architecture Overview

```
┌─────────────────────────────────────────────────────────────┐
│                    Electron Main Process                    │
│  ┌─────────────────┐    ┌─────────────────────────────────┐  │
│  │   Java Backend  │    │       React Renderer           │  │
│  │   (Spring Boot) │◄──►│     (VS Code-inspired UI)      │  │
│  │   Port: 8080    │    │                                 │  │
│  └─────────────────┘    └─────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
          ▲                              ▲
          │                              │
    PostgreSQL                    REST API (HTTP)
     Database                      JSON Communication
```

### Key Components

1. **Backend (Java + Spring Boot)**
   - Maintains all existing business logic
   - PostgreSQL database integration
   - RESTful API endpoints
   - Automatic lifecycle management via Electron

2. **Frontend (Electron + React + TypeScript)**
   - VS Code-inspired dark theme and layout
   - Sidebar navigation with contextual panels
   - Command palette with keyboard shortcuts
   - Status bar with real-time backend connectivity
   - Material-UI components with custom theming

3. **Communication Layer**
   - REST API over HTTP (localhost:8080)
   - Type-safe API client with error handling
   - Redux state management with async thunks
   - Real-time status monitoring

## Quick Start

### Prerequisites
- Java 21 (OpenJDK or Oracle JDK)
- Node.js 18+ and npm
- PostgreSQL database (configured in application.properties)

### Backend Setup
```bash
# Navigate to project root
cd StudentManagementSystem

# Build the Java backend
JAVA_HOME=/usr/lib/jvm/temurin-21-jdk-amd64 ./gradlew build

# Run backend standalone (optional, Electron will manage this)
JAVA_HOME=/usr/lib/jvm/temurin-21-jdk-amd64 ./gradlew bootRun
```

### Frontend Setup
```bash
# Navigate to frontend directory
cd frontend

# Install dependencies
npm install

# Build frontend
npm run build

# Run in development mode (with hot reload)
npm run dev

# Start Electron application
npm start
```

### Integrated Desktop Application
The Electron main process automatically manages the Java backend lifecycle:

```bash
cd frontend
npm run build    # Build React app
npm start        # Start Electron with integrated backend
```

## API Endpoints

### Student Management
- `GET /api/students` - List all students
- `GET /api/students/{id}` - Get student by ID
- `POST /api/students` - Add new student
- `PUT /api/students/{id}` - Update student
- `DELETE /api/students/{id}` - Delete student

### Statistics & Analytics
- `GET /api/statistics/summary` - Get overall statistics
- `GET /api/statistics/distribution` - Get score distribution
- `GET /api/statistics/by-gender` - Get gender-based statistics
- `GET /api/statistics/average` - Get average score
- `GET /api/statistics/highest` - Get highest score
- `GET /api/statistics/lowest` - Get lowest score

### Import/Export
- `GET /api/csv/export?filename=students.csv` - Export to CSV
- `POST /api/csv/import` - Import from CSV (multipart/form-data)
- `GET /api/csv/status` - Check CSV service status

## Frontend Features

### VS Code-Inspired UI
- **Dark Theme**: Professional appearance with syntax-highlighting colors
- **Sidebar Navigation**: Collapsible sidebar with contextual icons
- **Status Bar**: Real-time backend connectivity and system status
- **Command Palette**: `Ctrl+Shift+P` for quick actions and navigation
- **Menu Integration**: Native menu with keyboard shortcuts

### React Components
- **StudentList**: Table view with CRUD operations and dialogs
- **StatisticsPanel**: Interactive charts and analytics dashboards
- **ImportExportPanel**: File upload/download with progress indicators
- **CommandPalette**: VS Code-style quick command interface

### State Management
- **Redux Toolkit**: Type-safe state management
- **Async Thunks**: API integration with loading states
- **Error Handling**: Centralized error management and user feedback

## Development Workflow

### Backend Development
```bash
# Run tests
JAVA_HOME=/usr/lib/jvm/temurin-21-jdk-amd64 ./gradlew test

# Build JAR
JAVA_HOME=/usr/lib/jvm/temurin-21-jdk-amd64 ./gradlew build

# Run with hot reload
JAVA_HOME=/usr/lib/jvm/temurin-21-jdk-amd64 ./gradlew bootRun --args='--spring.profiles.active=dev'
```

### Frontend Development
```bash
cd frontend

# Development with hot reload
npm run dev

# Build for production
npm run build

# Type checking
npx tsc --noEmit

# Lint code
npm run lint  # (add lint script to package.json)
```

### Integrated Development
```bash
# Terminal 1: Start backend
JAVA_HOME=/usr/lib/jvm/temurin-21-jdk-amd64 ./gradlew bootRun

# Terminal 2: Start frontend dev server
cd frontend && npm run dev

# Terminal 3: Start Electron (connects to both)
cd frontend && npm start
```

## Deployment

### Desktop Application Distribution
```bash
cd frontend

# Build for current platform
npm run dist

# Build for all platforms
npm run dist:all

# Package without installer
npm run pack
```

### Deployment Artifacts
- **Windows**: `.exe` installer and portable app
- **macOS**: `.dmg` disk image and `.app` bundle
- **Linux**: `.AppImage`, `.deb`, and `.rpm` packages

## Technology Stack

### Backend
- **Java 21**: Latest LTS with modern language features
- **Spring Boot 3.2**: REST API framework with auto-configuration
- **PostgreSQL**: Production-ready relational database
- **Gradle**: Build automation and dependency management

### Frontend
- **Electron**: Cross-platform desktop application framework
- **React 18**: Modern UI library with hooks and concurrent features
- **TypeScript**: Type-safe JavaScript with excellent IDE support
- **Material-UI v5**: Professional component library
- **Redux Toolkit**: Predictable state container with DevTools
- **Webpack**: Module bundler with development server

### Development Tools
- **Hot Reload**: Both backend (Spring DevTools) and frontend (Webpack)
- **Source Maps**: Full debugging support in development
- **ESLint & Prettier**: Code quality and formatting
- **TypeScript**: Compile-time type checking

## Extension Architecture

The frontend is designed to support future extensions similar to VS Code:

### Plugin Interface (Future)
```typescript
interface Extension {
  id: string;
  name: string;
  activate(context: ExtensionContext): void;
  deactivate(): void;
}

interface ExtensionContext {
  subscriptions: Disposable[];
  workspaceState: Memento;
  globalState: Memento;
}
```

### Command Registration
```typescript
// Extensions can register commands
commands.registerCommand('extension.example', () => {
  vscode.window.showInformationMessage('Hello from extension!');
});
```

## Migration from Console Application

The original console-based `Main.java` has been preserved for reference. The new architecture maintains 100% API compatibility while providing:

1. **Enhanced User Experience**: Modern desktop UI vs. console interface
2. **Better Performance**: Asynchronous operations with loading states
3. **Extensibility**: Plugin system for future enhancements
4. **Cross-Platform**: Single codebase for Windows, macOS, and Linux
5. **Professional Appearance**: VS Code-inspired design language

## Contributing

### Code Structure
```
├── src/main/java/           # Java backend source
│   ├── com/hsjack/
│   │   ├── api/            # REST controllers and DTOs
│   │   ├── service/        # Business logic services
│   │   ├── model/          # Data models
│   │   └── config/         # Configuration classes
├── frontend/               # Electron + React frontend
│   ├── src/
│   │   ├── main/          # Electron main process
│   │   └── renderer/      # React application
│   │       ├── components/ # React components
│   │       ├── services/  # API client services
│   │       ├── store/     # Redux store and slices
│   │       └── types/     # TypeScript type definitions
└── README.md              # This documentation
```

### Adding New Features
1. **Backend**: Add REST endpoints in `api/controller/`
2. **Frontend**: Create React components in `components/`
3. **API Client**: Add service methods in `services/`
4. **State Management**: Add Redux slices for new data

## License

MIT License - see LICENSE file for details.

## Acknowledgments

- VS Code team for UI/UX inspiration
- Spring Boot community for excellent documentation
- Electron team for cross-platform desktop framework
- React and Redux communities for robust frontend tools