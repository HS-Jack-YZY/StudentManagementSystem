import React, { useState, useEffect } from 'react';
import { Box, Typography, Chip, IconButton } from '@mui/material';
import { Circle, Wifi, WifiOff } from '@mui/icons-material';

const StatusBar: React.FC = () => {
  const [backendStatus, setBackendStatus] = useState<'connected' | 'disconnected' | 'connecting'>('connecting');
  const [studentCount, setStudentCount] = useState<number>(0);

  useEffect(() => {
    // Check backend status periodically
    const checkBackendStatus = async () => {
      try {
        const response = await fetch('http://localhost:8080/api/students');
        if (response.ok) {
          setBackendStatus('connected');
          const data = await response.json();
          if (data.success && Array.isArray(data.data)) {
            setStudentCount(data.data.length);
          }
        } else {
          setBackendStatus('disconnected');
        }
      } catch (error) {
        setBackendStatus('disconnected');
      }
    };

    checkBackendStatus();
    const interval = setInterval(checkBackendStatus, 10000); // Check every 10 seconds

    return () => clearInterval(interval);
  }, []);

  const getStatusColor = () => {
    switch (backendStatus) {
      case 'connected':
        return 'success.main';
      case 'disconnected':
        return 'error.main';
      case 'connecting':
        return 'warning.main';
      default:
        return 'text.secondary';
    }
  };

  const getStatusText = () => {
    switch (backendStatus) {
      case 'connected':
        return 'Backend Connected';
      case 'disconnected':
        return 'Backend Disconnected';
      case 'connecting':
        return 'Connecting...';
      default:
        return 'Unknown';
    }
  };

  const getStatusIcon = () => {
    switch (backendStatus) {
      case 'connected':
        return <Wifi fontSize="small" />;
      case 'disconnected':
        return <WifiOff fontSize="small" />;
      case 'connecting':
        return <Circle fontSize="small" />;
      default:
        return <Circle fontSize="small" />;
    }
  };

  return (
    <Box
      className="status-bar"
      sx={{
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'space-between',
        px: 2,
        py: 0.5,
        borderTop: '1px solid',
        borderColor: 'divider',
        backgroundColor: '#007acc',
        color: 'white',
        minHeight: 28,
        fontSize: '0.875rem',
      }}
    >
      <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
        <Box sx={{ display: 'flex', alignItems: 'center', gap: 0.5 }}>
          {getStatusIcon()}
          <Typography variant="caption" sx={{ color: 'inherit' }}>
            {getStatusText()}
          </Typography>
        </Box>
        
        <Box sx={{ display: 'flex', alignItems: 'center', gap: 0.5 }}>
          <Circle sx={{ fontSize: 8, color: getStatusColor() }} />
          <Typography variant="caption" sx={{ color: 'inherit' }}>
            Port: 8080
          </Typography>
        </Box>
      </Box>

      <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
        <Typography variant="caption" sx={{ color: 'inherit' }}>
          Students: {studentCount}
        </Typography>
        
        <Typography variant="caption" sx={{ color: 'inherit' }}>
          Student Management System v1.0
        </Typography>
      </Box>
    </Box>
  );
};

export default StatusBar;