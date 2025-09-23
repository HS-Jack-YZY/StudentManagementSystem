import { configureStore } from '@reduxjs/toolkit';
import uiSlice from './slices/uiSlice';
import studentSlice from './slices/studentSlice';
import statisticsSlice from './slices/statisticsSlice';

export const store = configureStore({
  reducer: {
    ui: uiSlice,
    students: studentSlice,
    statistics: statisticsSlice,
  },
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware({
      serializableCheck: {
        ignoredActions: ['persist/PERSIST'],
      },
    }),
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;