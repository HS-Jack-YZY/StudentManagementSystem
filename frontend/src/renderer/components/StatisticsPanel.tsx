import React, { useEffect } from 'react';
import { Box, Typography, Grid2 as Grid, Paper, CircularProgress, Alert } from '@mui/material';
import { PieChart, Pie, Cell, BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';
import { useAppDispatch, useAppSelector } from '../store/hooks';
import { fetchStatisticsSummary, fetchScoreDistribution, fetchGenderStatistics } from '../store/slices/statisticsSlice';

const COLORS = ['#0088FE', '#00C49F', '#FFBB28', '#FF8042', '#8884D8'];

const StatisticsPanel: React.FC = () => {
  const dispatch = useAppDispatch();
  const { summary, distribution, genderStats, loading, error } = useAppSelector((state) => state.statistics);

  useEffect(() => {
    dispatch(fetchStatisticsSummary());
    dispatch(fetchScoreDistribution());
    dispatch(fetchGenderStatistics());
  }, [dispatch]);

  if (loading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100%' }}>
        <CircularProgress />
      </Box>
    );
  }

  if (error) {
    return (
      <Box sx={{ padding: 3 }}>
        <Alert severity="error">{error}</Alert>
      </Box>
    );
  }

  // Transform distribution data for chart
  const distributionData = distribution ? Object.entries(distribution).map(([key, value]) => ({
    name: key,
    value: value,
  })) : [];

  // Transform gender stats for chart
  const genderData = genderStats ? Object.entries(genderStats).map(([gender, stats]: [string, any]) => ({
    name: gender,
    count: stats.人数,
    average: stats.平均分,
  })) : [];

  return (
    <Box sx={{ padding: 3, height: '100%', overflow: 'auto' }}>
      <Typography variant="h4" component="h1" sx={{ mb: 3 }}>
        Statistics & Analytics
      </Typography>

      <Grid container spacing={3}>
        {/* Summary Cards */}
        {summary && (
          <Grid item xs={12}>
            <Grid container spacing={2}>
              <Grid xs={3}>
                <Paper sx={{ p: 2, textAlign: 'center' }}>
                  <Typography variant="h6" color="primary">
                    {summary.总人数}
                  </Typography>
                  <Typography variant="body2">Total Students</Typography>
                </Paper>
              </Grid>
              <Grid xs={3}>
                <Paper sx={{ p: 2, textAlign: 'center' }}>
                  <Typography variant="h6" color="secondary">
                    {Number(summary.平均分).toFixed(2)}
                  </Typography>
                  <Typography variant="body2">Average Score</Typography>
                </Paper>
              </Grid>
              <Grid xs={3}>
                <Paper sx={{ p: 2, textAlign: 'center' }}>
                  <Typography variant="h6" color="success.main">
                    {summary.最高分}
                  </Typography>
                  <Typography variant="body2">Highest Score</Typography>
                </Paper>
              </Grid>
              <Grid xs={3}>
                <Paper sx={{ p: 2, textAlign: 'center' }}>
                  <Typography variant="h6" color="error.main">
                    {summary.最低分}
                  </Typography>
                  <Typography variant="body2">Lowest Score</Typography>
                </Paper>
              </Grid>
            </Grid>
          </Grid>
        )}

        {/* Score Distribution Chart */}
        <Grid item xs={12} md={6}>
          <Paper sx={{ p: 2, height: 400 }}>
            <Typography variant="h6" sx={{ mb: 2 }}>
              Score Distribution
            </Typography>
            {distributionData.length > 0 && (
              <ResponsiveContainer width="100%" height="90%">
                <PieChart>
                  <Pie
                    data={distributionData}
                    cx="50%"
                    cy="50%"
                    labelLine={false}
                    label={({ name, percent }) => `${name} ${((percent as number) * 100).toFixed(0)}%`}
                    outerRadius={80}
                    fill="#8884d8"
                    dataKey="value"
                  >
                    {distributionData.map((entry, index) => (
                      <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                    ))}
                  </Pie>
                  <Tooltip />
                </PieChart>
              </ResponsiveContainer>
            )}
          </Paper>
        </Grid>

        {/* Gender Statistics Chart */}
        <Grid xs={12} md={6}>
          <Paper sx={{ p: 2, height: 400 }}>
            <Typography variant="h6" sx={{ mb: 2 }}>
              Statistics by Gender
            </Typography>
            {genderData.length > 0 && (
              <ResponsiveContainer width="100%" height="90%">
                <BarChart data={genderData}>
                  <CartesianGrid strokeDasharray="3 3" />
                  <XAxis dataKey="name" />
                  <YAxis />
                  <Tooltip />
                  <Legend />
                  <Bar dataKey="count" fill="#8884d8" name="Student Count" />
                  <Bar dataKey="average" fill="#82ca9d" name="Average Score" />
                </BarChart>
              </ResponsiveContainer>
            )}
          </Paper>
        </Grid>
      </Grid>
    </Box>
  );
};

export default StatisticsPanel;