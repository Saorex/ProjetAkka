import React from 'react';
import { useNavigate } from 'react-router-dom';
import './Navbar.css';

export default function Navbar({onLeavingChange}) {
  const navigate = useNavigate();

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
        <div className="flex items-center space-x-4">
          <span onClick={handleDashboard}>Dashboard</span>
          <span>Manage Assets</span>
          <span onClick={handleAddFunds}>Ajouter des fonds</span>
        </div>
      </nav>
    );
  }