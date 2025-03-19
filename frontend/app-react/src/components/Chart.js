import React from 'react';
import { Line } from 'react-chartjs-2';
import { Chart as ChartJS, LineElement, CategoryScale, LinearScale, PointElement } from 'chart.js';
import './Chart.css';

ChartJS.register(LineElement, CategoryScale, LinearScale, PointElement);

export default function Chart({ tmpdata }) {
  if (!tmpdata || tmpdata.length === 0) return <p>No data available for the chart.</p>;
  const sortedData = [...tmpdata].sort((a, b) => a.timestamp - b.timestamp);
  const data = {
    labels: sortedData.map((item) => {
      const date = new Date(item.timestamp);
      return `${date.getHours()}:${String(date.getMinutes()).padStart(2, '0')}`;
    }),
    datasets: [
      {
        label: 'Portfolio',
        data: sortedData.map(item => item.open),
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
      <Line data={data} options={options} />
    </div>
  );
}
