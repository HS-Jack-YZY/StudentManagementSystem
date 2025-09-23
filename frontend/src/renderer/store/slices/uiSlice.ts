import { createSlice, PayloadAction } from '@reduxjs/toolkit';

interface UiState {
  activeView: string;
  sidebarOpen: boolean;
  loading: boolean;
  error: string | null;
  commandPaletteOpen: boolean;
}

const initialState: UiState = {
  activeView: 'students',
  sidebarOpen: true,
  loading: false,
  error: null,
  commandPaletteOpen: false,
};

const uiSlice = createSlice({
  name: 'ui',
  initialState,
  reducers: {
    setActiveView: (state, action: PayloadAction<string>) => {
      state.activeView = action.payload;
    },
    toggleSidebar: (state) => {
      state.sidebarOpen = !state.sidebarOpen;
    },
    setSidebarOpen: (state, action: PayloadAction<boolean>) => {
      state.sidebarOpen = action.payload;
    },
    setLoading: (state, action: PayloadAction<boolean>) => {
      state.loading = action.payload;
    },
    setError: (state, action: PayloadAction<string | null>) => {
      state.error = action.payload;
    },
    setCommandPaletteOpen: (state, action: PayloadAction<boolean>) => {
      state.commandPaletteOpen = action.payload;
    },
  },
});

export const {
  setActiveView,
  toggleSidebar,
  setSidebarOpen,
  setLoading,
  setError,
  setCommandPaletteOpen,
} = uiSlice.actions;

export default uiSlice.reducer;