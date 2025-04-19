'use client'

import { useState, FormEvent } from 'react';
import API from '../../lib/api';
import { RegisterForm } from '../../types/auth';

export default function Register() {
  const [form, setForm] = useState<RegisterForm>({
    name: '',
    email: '',
    password: '',
  });

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    try {
      await API.post('/auth/register', form);
      alert('✅ Registered successfully!');
      setForm({ name: '', email: '', password: '' });
    } catch (err) {
      console.error(err);
      alert('❌ Registration failed.');
    }
  };

  return (
    <form onSubmit={handleSubmit} style={{ maxWidth: 400 }}>
      <h2>Register</h2>
      <input
        type="text"
        placeholder="Name"
        value={form.name}
        onChange={(e) => setForm({ ...form, name: e.target.value })}
        required
      />
      <input
        type="email"
        placeholder="Email"
        value={form.email}
        onChange={(e) => setForm({ ...form, email: e.target.value })}
        required
      />
      <input
        type="password"
        placeholder="Password"
        value={form.password}
        onChange={(e) => setForm({ ...form, password: e.target.value })}
        required
      />
      <button type="submit">Register</button>
    </form>
  );
}
