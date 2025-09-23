import React, { useState } from 'react';
import {
  Box,
  Typography,
  Paper,
  Button,
  Alert,
  LinearProgress,
  Divider,
  FormControlLabel,
  Switch,
} from '@mui/material';
import { CloudUpload, CloudDownload } from '@mui/icons-material';
import { csvApi } from '../services/csvApi';
import { useAppDispatch } from '../store/hooks';
import { fetchStudents } from '../store/slices/studentSlice';

const ImportExportPanel: React.FC = () => {
  const dispatch = useAppDispatch();
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState<{ type: 'success' | 'error'; text: string } | null>(null);
  const [appendMode, setAppendMode] = useState(false);

  const handleExport = async () => {
    try {
      setLoading(true);
      setMessage(null);
      
      const blob = await csvApi.exportToCsv('students.csv');
      
      // Create download link
      const url = window.URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = url;
      link.download = 'students.csv';
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
      window.URL.revokeObjectURL(url);
      
      setMessage({ type: 'success', text: 'Students exported successfully!' });
    } catch (error) {
      setMessage({ type: 'error', text: 'Failed to export students: ' + (error as Error).message });
    } finally {
      setLoading(false);
    }
  };

  const handleImport = async (event: React.ChangeEvent<HTMLInputElement>) => {
    const file = event.target.files?.[0];
    if (!file) return;

    if (!file.name.toLowerCase().endsWith('.csv')) {
      setMessage({ type: 'error', text: 'Please select a CSV file.' });
      return;
    }

    try {
      setLoading(true);
      setMessage(null);
      
      const response = await csvApi.importFromCsv(file, appendMode);
      
      if (response.success) {
        setMessage({ type: 'success', text: response.message });
        // Refresh the student list
        dispatch(fetchStudents());
      } else {
        setMessage({ type: 'error', text: response.error || 'Import failed' });
      }
    } catch (error) {
      setMessage({ type: 'error', text: 'Failed to import students: ' + (error as Error).message });
    } finally {
      setLoading(false);
      // Clear the file input
      event.target.value = '';
    }
  };

  return (
    <Box sx={{ padding: 3, maxWidth: 800, margin: '0 auto' }}>
      <Typography variant="h4" component="h1" sx={{ mb: 3 }}>
        Import & Export
      </Typography>

      {loading && <LinearProgress sx={{ mb: 2 }} />}

      {message && (
        <Alert severity={message.type} sx={{ mb: 3 }} onClose={() => setMessage(null)}>
          {message.text}
        </Alert>
      )}

      <Paper sx={{ p: 3, mb: 3 }}>
        <Typography variant="h6" sx={{ mb: 2 }}>
          Export Students to CSV
        </Typography>
        <Typography variant="body2" sx={{ mb: 3, color: 'text.secondary' }}>
          Download all student data as a CSV file. This includes student ID, name, gender, age, and score.
        </Typography>
        <Button
          variant="contained"
          startIcon={<CloudDownload />}
          onClick={handleExport}
          disabled={loading}
          size="large"
        >
          Export to CSV
        </Button>
      </Paper>

      <Paper sx={{ p: 3 }}>
        <Typography variant="h6" sx={{ mb: 2 }}>
          Import Students from CSV
        </Typography>
        <Typography variant="body2" sx={{ mb: 3, color: 'text.secondary' }}>
          Upload a CSV file to import student data. The file should have columns: id, name, gender, age, score.
        </Typography>
        
        <FormControlLabel
          control={
            <Switch
              checked={appendMode}
              onChange={(e) => setAppendMode(e.target.checked)}
              color="primary"
            />
          }
          label="Append mode (keep existing data)"
          sx={{ mb: 2 }}
        />
        
        <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
          <Button
            variant="contained"
            component="label"
            startIcon={<CloudUpload />}
            disabled={loading}
            size="large"
          >
            Select CSV File
            <input
              type="file"
              hidden
              accept=".csv"
              onChange={handleImport}
            />
          </Button>
          <Typography variant="body2" color="text.secondary">
            {appendMode ? 'Data will be added to existing records' : 'Existing data will be replaced'}
          </Typography>
        </Box>
      </Paper>

      <Paper sx={{ p: 3, mt: 3, backgroundColor: 'background.default' }}>
        <Typography variant="h6" sx={{ mb: 2 }}>
          CSV Format Requirements
        </Typography>
        <Typography variant="body2" component="div">
          Your CSV file should have the following structure:
          <Box component="pre" sx={{ mt: 1, p: 2, backgroundColor: 'action.hover', borderRadius: 1, fontSize: '0.875rem' }}>
{`id,name,gender,age,score
2023001,张三,男,20,85.5
2023002,李四,女,19,92.0
2023003,王五,男,21,78.5`}
          </Box>
        </Typography>
      </Paper>
    </Box>
  );
};

export default ImportExportPanel;