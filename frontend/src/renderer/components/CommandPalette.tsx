import React, { useState, useEffect } from 'react';
import {
  Dialog,
  DialogTitle,
  DialogContent,
  TextField,
  List,
  ListItem,
  ListItemText,
  ListItemIcon,
  Box,
  Typography,
  Chip,
} from '@mui/material';
import {
  Search,
  Add,
  Person,
  Analytics,
  ImportExport,
  Settings,
  Refresh,
} from '@mui/icons-material';
import { useAppDispatch } from '../store/hooks';
import { setActiveView } from '../store/slices/uiSlice';
import { fetchStudents } from '../store/slices/studentSlice';

interface CommandPaletteProps {
  open: boolean;
  onClose: () => void;
}

interface Command {
  id: string;
  title: string;
  description: string;
  icon: React.ReactNode;
  action: () => void;
  category: string;
  keywords: string[];
}

const CommandPalette: React.FC<CommandPaletteProps> = ({ open, onClose }) => {
  const dispatch = useAppDispatch();
  const [searchTerm, setSearchTerm] = useState('');
  const [filteredCommands, setFilteredCommands] = useState<Command[]>([]);

  const commands: Command[] = [
    {
      id: 'view-students',
      title: 'Show Students',
      description: 'Navigate to the students view',
      icon: <Person />,
      action: () => {
        dispatch(setActiveView('students'));
        onClose();
      },
      category: 'Navigation',
      keywords: ['students', 'view', 'list', 'navigate'],
    },
    {
      id: 'view-statistics',
      title: 'Show Statistics',
      description: 'Navigate to the statistics view',
      icon: <Analytics />,
      action: () => {
        dispatch(setActiveView('statistics'));
        onClose();
      },
      category: 'Navigation',
      keywords: ['statistics', 'analytics', 'charts', 'view', 'navigate'],
    },
    {
      id: 'view-import-export',
      title: 'Import/Export',
      description: 'Navigate to the import/export view',
      icon: <ImportExport />,
      action: () => {
        dispatch(setActiveView('import-export'));
        onClose();
      },
      category: 'Navigation',
      keywords: ['import', 'export', 'csv', 'file', 'view', 'navigate'],
    },
    {
      id: 'view-settings',
      title: 'Settings',
      description: 'Navigate to the settings view',
      icon: <Settings />,
      action: () => {
        dispatch(setActiveView('settings'));
        onClose();
      },
      category: 'Navigation',
      keywords: ['settings', 'preferences', 'configuration', 'view', 'navigate'],
    },
    {
      id: 'refresh-students',
      title: 'Refresh Students',
      description: 'Reload the student data from the server',
      icon: <Refresh />,
      action: () => {
        dispatch(fetchStudents());
        onClose();
      },
      category: 'Action',
      keywords: ['refresh', 'reload', 'students', 'data', 'sync'],
    },
    {
      id: 'new-student',
      title: 'Add New Student',
      description: 'Open the add student dialog',
      icon: <Add />,
      action: () => {
        dispatch(setActiveView('students'));
        // TODO: Trigger add student dialog
        onClose();
      },
      category: 'Action',
      keywords: ['add', 'new', 'student', 'create'],
    },
  ];

  useEffect(() => {
    if (!searchTerm.trim()) {
      setFilteredCommands(commands);
    } else {
      const filtered = commands.filter((command) =>
        command.title.toLowerCase().includes(searchTerm.toLowerCase()) ||
        command.description.toLowerCase().includes(searchTerm.toLowerCase()) ||
        command.keywords.some((keyword) =>
          keyword.toLowerCase().includes(searchTerm.toLowerCase())
        )
      );
      setFilteredCommands(filtered);
    }
  }, [searchTerm]);

  const handleKeyDown = (event: React.KeyboardEvent) => {
    if (event.key === 'Enter' && filteredCommands.length > 0) {
      filteredCommands[0].action();
    } else if (event.key === 'Escape') {
      onClose();
    }
  };

  const handleClose = () => {
    setSearchTerm('');
    onClose();
  };

  const groupedCommands = filteredCommands.reduce((groups, command) => {
    const category = command.category;
    if (!groups[category]) {
      groups[category] = [];
    }
    groups[category].push(command);
    return groups;
  }, {} as Record<string, Command[]>);

  return (
    <Dialog
      open={open}
      onClose={handleClose}
      maxWidth="md"
      fullWidth
      PaperProps={{
        className: 'command-palette',
        sx: { 
          position: 'fixed',
          top: '20%',
          margin: 0,
          borderRadius: 2,
        },
      }}
    >
      <DialogTitle sx={{ pb: 0 }}>
        <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
          <Search />
          <Typography variant="h6">Command Palette</Typography>
        </Box>
      </DialogTitle>
      <DialogContent sx={{ pt: 1 }}>
        <TextField
          fullWidth
          placeholder="Type a command..."
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          onKeyDown={handleKeyDown}
          autoFocus
          variant="outlined"
          sx={{ mb: 2 }}
        />
        
        {Object.entries(groupedCommands).map(([category, commands]) => (
          <Box key={category} sx={{ mb: 2 }}>
            <Box sx={{ display: 'flex', alignItems: 'center', gap: 1, mb: 1 }}>
              <Typography variant="subtitle2" color="text.secondary">
                {category}
              </Typography>
              <Chip label={commands.length} size="small" />
            </Box>
            <List dense>
              {commands.map((command) => (
                <ListItem
                  key={command.id}
                  button
                  onClick={command.action}
                  sx={{
                    borderRadius: 1,
                    mb: 0.5,
                    '&:hover': {
                      backgroundColor: 'action.hover',
                    },
                  }}
                >
                  <ListItemIcon sx={{ minWidth: 40 }}>
                    {command.icon}
                  </ListItemIcon>
                  <ListItemText
                    primary={command.title}
                    secondary={command.description}
                    secondaryTypographyProps={{
                      fontSize: '0.75rem',
                      color: 'text.secondary',
                    }}
                  />
                </ListItem>
              ))}
            </List>
          </Box>
        ))}
        
        {filteredCommands.length === 0 && searchTerm && (
          <Box sx={{ textAlign: 'center', py: 4 }}>
            <Typography variant="body2" color="text.secondary">
              No commands found for "{searchTerm}"
            </Typography>
          </Box>
        )}
      </DialogContent>
    </Dialog>
  );
};

export default CommandPalette;