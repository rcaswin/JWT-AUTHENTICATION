// App.jsx
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Login from './pages/Login';
import Register from './pages/Register';
import { AdminPage, UserPage } from './pages/AdminUserPages';
import PrivateRoute from './components/PrivateRoute';
import { AuthProvider } from './context/AuthContext';

function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="/admin" element={
            <PrivateRoute allowedRoles={['ADMIN']}>
              <AdminPage />
            </PrivateRoute>
          } />
          <Route path="/user" element={
            <PrivateRoute allowedRoles={['USER', 'ADMIN']}>
              <UserPage />
            </PrivateRoute>
          } />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}

export default App;
