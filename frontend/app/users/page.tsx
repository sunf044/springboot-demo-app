'use client'

import { useEffect, useState } from 'react';
import API from '../../lib/api';
import { User } from '../../types/auth';

export default function Users() {
  const [users, setUsers] = useState<User[]>([]);

  useEffect(() => {
    const fetchUsers = async () => {
      try {
        const res = await API.get<User[]>('/users');
        setUsers(res.data);
      } catch (err) {
        console.error(err);
        alert('❌ Failed to fetch users. Please login.');
      }
    };

    fetchUsers();
  }, []);

  return (
    <div style={{ padding: '20px' }}>
      <h2>👥 User List</h2>
      <ul>
        {users.map((u) => (
          <li key={u.id}>
            {u.name} ({u.email})
          </li>
        ))}
      </ul>
    </div>
  );
}
