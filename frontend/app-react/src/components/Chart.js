import React from 'react';
import { Line } from 'react-chartjs-2';
import { Chart as ChartJS, LineElement, CategoryScale, LinearScale, PointElement } from 'chart.js';
import './Chart.css'; // Assurez-vous que ce chemin est correct

ChartJS.register(LineElement, CategoryScale, LinearScale, PointElement);

export default function Chart() {
  const data = {
    labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May'],
    datasets: [
      {
        label: 'Portfolio',
        data: [100, 120, 150, 130, 170],
        borderColor: 'blue',
        fill: false,
      },
    ],
  };

  return (
    <div className="chart-container">
      <Line data={data} />
    </div>
  );
}
