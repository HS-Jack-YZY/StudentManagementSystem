import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import { statisticsApi } from '../../services/statisticsApi';

interface StatisticsState {
  summary: {
    totalStudents: number;
    averageScore: number;
    highestScore: number;
    lowestScore: number;
  } | null;
  distribution: Record<string, number> | null;
  genderStats: Record<string, any> | null;
  loading: boolean;
  error: string | null;
}

const initialState: StatisticsState = {
  summary: null,
  distribution: null,
  genderStats: null,
  loading: false,
  error: null,
};

// Async thunks for statistics API operations
export const fetchStatisticsSummary = createAsyncThunk(
  'statistics/fetchSummary',
  async () => {
    const response = await statisticsApi.getSummary();
    return response.data;
  }
);

export const fetchScoreDistribution = createAsyncThunk(
  'statistics/fetchDistribution',
  async () => {
    const response = await statisticsApi.getDistribution();
    return response.data;
  }
);

export const fetchGenderStatistics = createAsyncThunk(
  'statistics/fetchGenderStats',
  async () => {
    const response = await statisticsApi.getGenderStatistics();
    return response.data;
  }
);

const statisticsSlice = createSlice({
  name: 'statistics',
  initialState,
  reducers: {
    clearError: (state) => {
      state.error = null;
    },
  },
  extraReducers: (builder) => {
    builder
      // Fetch summary
      .addCase(fetchStatisticsSummary.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchStatisticsSummary.fulfilled, (state, action) => {
        state.loading = false;
        state.summary = action.payload;
      })
      .addCase(fetchStatisticsSummary.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message || 'Failed to fetch statistics summary';
      })
      
      // Fetch distribution
      .addCase(fetchScoreDistribution.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchScoreDistribution.fulfilled, (state, action) => {
        state.loading = false;
        state.distribution = action.payload;
      })
      .addCase(fetchScoreDistribution.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message || 'Failed to fetch score distribution';
      })
      
      // Fetch gender statistics
      .addCase(fetchGenderStatistics.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchGenderStatistics.fulfilled, (state, action) => {
        state.loading = false;
        state.genderStats = action.payload;
      })
      .addCase(fetchGenderStatistics.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message || 'Failed to fetch gender statistics';
      });
  },
});

export const { clearError } = statisticsSlice.actions;
export default statisticsSlice.reducer;