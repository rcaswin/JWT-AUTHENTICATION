// pages/Login.jsx
import { useState } from 'react';
import api from '../api'
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export default function Login() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const { login } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    const params = new URLSearchParams({ email, password });
    try {
      const res = await api.post(`/login?${params}`);
      if (res.data.token && res.data.role) {
        login(res.data.token, res.data.role);
        navigate(res.data.role === 'ADMIN' ? '/admin' : '/user');
      } else {
        alert(res.data.message || "Login failed");
      }
    } catch (err) {
      alert("Error logging in");
    }
  };

  return (
    <div className="container mt-5">
      <h2>Login</h2>
      <form onSubmit={handleSubmit}>
        <input className="form-control my-2" placeholder="Email" onChange={(e) => setEmail(e.target.value)} />
        <input className="form-control my-2" placeholder="Password" type="password" onChange={(e) => setPassword(e.target.value)} />
        <button className="btn btn-primary">Login</button>
      </form>
    </div>
  );
}
