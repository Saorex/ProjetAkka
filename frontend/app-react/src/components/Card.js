import React from 'react';

export default function Card({ title, description }) {
    return (
      <div className="bg-white p-4 rounded-lg shadow-md">
        <h2 className="text-lg font-semibold">{title}</h2>
        <p className="text-gray-600">{description}</p>
      </div>
    );
  }
  