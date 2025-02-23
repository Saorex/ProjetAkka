import React from 'react';

export default function Navbar() {
    return (
      <nav className="navbar">
        <div className="flex items-center space-x-4">
          <span>Dashboard</span>
          <span>Manage Assets</span>
          <span className="font-bold">Test</span>
        </div>
      </nav>
    );
  }