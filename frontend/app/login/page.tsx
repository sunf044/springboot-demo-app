'use client'

import { useDispatch } from 'react-redux';
import { loginSuccess, startAutoRefresh } from '@/store/authSlice';
import { LoginForm } from '@/types/auth';
import API from '@/lib/api';
import { FormEvent, useState } from 'react';
import { AppDispatch } from '@/store';

export default function Login() {
  const [form, setForm] = useState<LoginForm>({ email: '', password: '' });
  const dispatch = useDispatch<AppDispatch>();

  const submit = async (e: FormEvent) => {
    e.preventDefault();
    const res = await API.post('/auth/login', form);
    dispatch(loginSuccess(res.data.accessToken));
    dispatch(startAutoRefresh(dispatch));
    alert('✅ Logged in!');
  };

  return (
    <form onSubmit={submit}>
      <h2>Login</h2>
      <input
        placeholder="Email"
        value={form.email}
        onChange={e => setForm({ ...form, email: e.target.value })}
      />
      <input
        type="password"
        placeholder="Password"
        value={form.password}
        onChange={e => setForm({ ...form, password: e.target.value })}
      />
      <button type="submit">Login</button>
    </form>
  );
}
