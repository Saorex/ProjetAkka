import React from 'react';
import { Line } from 'react-chartjs-2';
import { Chart as ChartJS, LineElement, CategoryScale, LinearScale, PointElement } from 'chart.js';
import './Chart.css';

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

  const options = {
    plugins: {
      legend: {
        labels: {
          color: '#f0f0f0',
        },
      },
    },
    scales: {
      x: {
        ticks: {
          color: '#f0f0f0',
        },
      },
      y: {
        ticks: {
          color: '#f0f0f0',
        },
      },
    },
  };


  return (
    <div className="chart-container">
      <Line data={data} options={options}/>
    </div>
  );
}
