import { createSlice, PayloadAction } from '@reduxjs/toolkit';
import { jwtDecode } from 'jwt-decode';
import API from '../lib/api';
import { JwtPayload } from 'jwt-decode';

let refreshTimer: NodeJS.Timeout;

interface AuthState {
  token: string | null;
  user: string | null;
}

const initialState: AuthState = {
  token: typeof window !== 'undefined' ? localStorage.getItem('token') : null,
  user: null,
};

const setAutoRefresh = (token: string, dispatch: any) => {
  const decoded = jwtDecode<JwtPayload>(token);
  if (!decoded.exp) throw new Error('Token has no exp');
  const timeout = decoded.exp * 1000 - Date.now() - 60000;

  if (refreshTimer) clearTimeout(refreshTimer);
  refreshTimer = setTimeout(async () => {
    try {
      const res = await API.post('/auth/refresh');
      dispatch(authSlice.actions.loginSuccess(res.data.accessToken));
      setAutoRefresh(res.data.accessToken, dispatch);
    } catch {
      dispatch(authSlice.actions.logout());
    }
  }, timeout);
};

const authSlice = createSlice({
  name: 'auth',
  initialState,
  reducers: {
    loginSuccess(state, action: PayloadAction<string>) {
      state.token = action.payload;
      localStorage.setItem('token', action.payload);
    },
    logout(state) {
      state.token = null;
      localStorage.removeItem('token');
      if (refreshTimer) clearTimeout(refreshTimer);
    },
    startAutoRefresh(state, action: PayloadAction<any>) {
      if (state.token) {
        setAutoRefresh(state.token, action.payload);
      }
    },
  },
});

export const { loginSuccess, logout, startAutoRefresh } = authSlice.actions;
export default authSlice.reducer;
