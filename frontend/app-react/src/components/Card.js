import React from 'react';
import './Card.css';

export default function Card({ title, description }) {
    return (
      <div className="card">
        <h2 className="text-lg font-semibold">{title}</h2>
        <p className="text-black-600">{description}</p>
      </div>
    );
  }
  