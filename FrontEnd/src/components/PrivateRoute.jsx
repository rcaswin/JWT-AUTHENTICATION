// components/PrivateRoute.jsx
import { Navigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export default function PrivateRoute({ children, allowedRoles }) {
  const { token, role } = useAuth();
  if (!token || !allowedRoles.includes(role)) return <Navigate to="/login" />;
  return children;
}
