import React from 'react';
import { useNavigate } from 'react-router-dom';
import './Navbar.css';
import { useAuth } from '../context/AuthProvider';

export default function Navbar({onLeavingChange}) {
  const navigate = useNavigate();
  const { user, logout } = useAuth();

  const handleAddFunds = (e) => {
    e.preventDefault();
    onLeavingChange(true);

    setTimeout(() => {
      navigate('/addfunds');
    }, 500);
  };

  const handleDashboard = (e) => {
    e.preventDefault();
    onLeavingChange(true);

    setTimeout(() => {
      navigate('/board');
    }, 500);
  };

    return (
      <nav className="navbar">
        <div className='nav-div'>
          <span className='username'>{user}</span>
          <span className='nav-span' onClick={handleDashboard}>Dashboard</span>
          <span className='nav-span' onClick={handleAddFunds}>Ajouter des fonds</span>
          <span className='nav-span' onClick={logout}>Déconnexion</span>
        </div>
      </nav>
    );
  }