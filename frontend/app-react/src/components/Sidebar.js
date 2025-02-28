import React from 'react';
import './Sidebar.css';

export default function Sidebar() {
    return (
      <aside className="sidebar">
        <ul>
          <li className="p-2 hover:bg-gray-700">Home</li>
          <li className="p-2 hover:bg-gray-700">Portfolio</li>
          <li className="p-2 hover:bg-gray-700">Market Data</li>
        </ul>
      </aside>
    );
  }
  