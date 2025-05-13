// pages/Register.jsx
import { useState } from 'react';
import api from '../api'
import { useNavigate } from 'react-router-dom';

export default function Register() {
  const [form, setForm] = useState({ name: '', email: '', password: '', role: 'USER' });
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    const params = new URLSearchParams(form);
    try {
      const res = await api.post(`/register?${params}`);
      alert(res.data.message || "Registered");
      navigate('/login');
    } catch (err) {
      alert("Error registering");
    }
  };

  return (
    <div className="container mt-5">
      <h2>Register</h2>
      <form onSubmit={handleSubmit}>
        <input className="form-control my-2" placeholder="Name" onChange={(e) => setForm({...form, name: e.target.value})} />
        <input className="form-control my-2" placeholder="Email" onChange={(e) => setForm({...form, email: e.target.value})} />
        <input className="form-control my-2" type="password" placeholder="Password" onChange={(e) => setForm({...form, password: e.target.value})} />
        <select className="form-control my-2" onChange={(e) => setForm({...form, role: e.target.value})}>
          <option value="USER">USER</option>
          <option value="ADMIN">ADMIN</option>
        </select>
        <button className="btn btn-success">Register</button>
      </form>
    </div>
  );
}
