import React, { useState, useEffect } from 'react';
import { Box, AppBar, Toolbar, Typography, IconButton, Drawer, List, ListItem, ListItemIcon, ListItemText, Divider } from '@mui/material';
import { School, Analytics, ImportExport, Settings, Menu as MenuIcon, Person } from '@mui/icons-material';
import StatusBar from './components/StatusBar';
import CommandPalette from './components/CommandPalette';
import StudentList from './components/StudentList';
import StatisticsPanel from './components/StatisticsPanel';
import ImportExportPanel from './components/ImportExportPanel';
import { useAppDispatch, useAppSelector } from './store/hooks';
import { setActiveView } from './store/slices/uiSlice';
import './App.css';

/**
 * Main Application Component
 * Implements VS Code-inspired layout with sidebar, main content area, and status bar
 */
const App: React.FC = () => {
  const dispatch = useAppDispatch();
  const { activeView, sidebarOpen } = useAppSelector((state) => state.ui);
  const [commandPaletteOpen, setCommandPaletteOpen] = useState(false);

  // Handle menu actions from Electron main process
  useEffect(() => {
    const handleMenuAction = (event: any, action: string) => {
      switch (action) {
        case 'new-student':
          dispatch(setActiveView('students'));
          // TODO: Open new student dialog
          break;
        case 'import-csv':
          dispatch(setActiveView('import-export'));
          break;
        case 'export-csv':
          dispatch(setActiveView('import-export'));
          break;
        case 'command-palette':
          setCommandPaletteOpen(true);
          break;
      }
    };

    // Listen for menu actions from main process
    if (window.electronAPI) {
      window.electronAPI.onMenuAction(handleMenuAction);
    }

    return () => {
      // Cleanup listener if needed
    };
  }, [dispatch]);

  const sidebarItems = [
    { id: 'students', label: 'Students', icon: <Person />, view: 'students' },
    { id: 'statistics', label: 'Statistics', icon: <Analytics />, view: 'statistics' },
    { id: 'import-export', label: 'Import/Export', icon: <ImportExport />, view: 'import-export' },
    { id: 'settings', label: 'Settings', icon: <Settings />, view: 'settings' },
  ];

  const handleSidebarItemClick = (view: string) => {
    dispatch(setActiveView(view));
  };

  const renderMainContent = () => {
    switch (activeView) {
      case 'students':
        return <StudentList />;
      case 'statistics':
        return <StatisticsPanel />;
      case 'import-export':
        return <ImportExportPanel />;
      case 'settings':
        return (
          <Box sx={{ padding: 3 }}>
            <Typography variant="h4">Settings</Typography>
            <Typography variant="body1" sx={{ mt: 2 }}>
              Settings panel coming soon...
            </Typography>
          </Box>
        );
      default:
        return <StudentList />;
    }
  };

  return (
    <Box sx={{ display: 'flex', height: '100vh', flexDirection: 'column' }}>
      {/* Top App Bar */}
      <AppBar position="static" sx={{ zIndex: (theme) => theme.zIndex.drawer + 1 }}>
        <Toolbar variant="dense">
          <IconButton
            edge="start"
            color="inherit"
            aria-label="menu"
            onClick={() => dispatch({ type: 'ui/toggleSidebar' })}
            sx={{ mr: 2 }}
          >
            <MenuIcon />
          </IconButton>
          <School sx={{ mr: 2 }} />
          <Typography variant="h6" noWrap component="div" sx={{ flexGrow: 1 }}>
            Student Management System
          </Typography>
          <Typography variant="body2" sx={{ opacity: 0.7 }}>
            {activeView.charAt(0).toUpperCase() + activeView.slice(1)}
          </Typography>
        </Toolbar>
      </AppBar>

      <Box sx={{ display: 'flex', flex: 1 }}>
        {/* Sidebar */}
        <Drawer
          variant="persistent"
          anchor="left"
          open={sidebarOpen}
          sx={{
            width: 240,
            flexShrink: 0,
            '& .MuiDrawer-paper': {
              width: 240,
              boxSizing: 'border-box',
              position: 'relative',
              backgroundColor: '#252526',
              borderRight: '1px solid #3e3e42',
            },
          }}
        >
          <List sx={{ pt: 0 }}>
            {sidebarItems.map((item) => (
              <ListItem
                key={item.id}
                selected={activeView === item.view}
                onClick={() => handleSidebarItemClick(item.view)}
                sx={{
                  cursor: 'pointer',
                  '&.Mui-selected': {
                    backgroundColor: '#37373d',
                    borderLeft: '3px solid #007acc',
                  },
                  '&:hover': {
                    backgroundColor: '#2a2d2e',
                  },
                }}
              >
                <ListItemIcon sx={{ color: activeView === item.view ? '#007acc' : 'inherit' }}>
                  {item.icon}
                </ListItemIcon>
                <ListItemText 
                  primary={item.label}
                  primaryTypographyProps={{
                    fontSize: 14,
                    color: activeView === item.view ? '#ffffff' : '#cccccc'
                  }}
                />
              </ListItem>
            ))}
          </List>
          <Divider sx={{ backgroundColor: '#3e3e42' }} />
        </Drawer>

        {/* Main Content Area */}
        <Box
          component="main"
          sx={{
            flexGrow: 1,
            backgroundColor: '#1e1e1e',
            overflow: 'hidden',
            display: 'flex',
            flexDirection: 'column',
          }}
        >
          {renderMainContent()}
        </Box>
      </Box>

      {/* Status Bar */}
      <StatusBar />

      {/* Command Palette */}
      <CommandPalette
        open={commandPaletteOpen}
        onClose={() => setCommandPaletteOpen(false)}
      />
    </Box>
  );
};

export default App;