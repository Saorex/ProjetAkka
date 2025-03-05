import React, { useState } from 'react';
import { Line } from 'react-chartjs-2';
import 'chart.js/auto';
import './Simulation.css';

export default function Simulation() {
  const [formData, setFormData] = useState({
    initialAmount: 10000,
    timeHorizon: 10,
    annualReturnRate: 5,
    annualFees: 1,
  });

  const [results, setResults] = useState(null);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value,
    });
  };

  const handleSimulate = (e) => {
    e.preventDefault();
    const { initialAmount, timeHorizon, annualReturnRate, annualFees } = formData;

    const data = [];
    let amountWithFees = initialAmount;
    let amountWithoutFees = initialAmount;

    for (let year = 0; year < timeHorizon; year++) {
      amountWithFees = amountWithFees * (1 + (annualReturnRate - annualFees) / 100);
      amountWithoutFees = amountWithoutFees * (1 + annualReturnRate / 100);
      data.push({ year, amountWithFees, amountWithoutFees });
    }

    setResults(data);
  };

  const chartData = results && {
    labels: results.map((r, index) => `Year ${index + 1}`),
    datasets: [
      {
        label: 'With Fees',
        data: results.map(r => r.amountWithFees),
        borderColor: 'rgba(255, 99, 132, 1)',
        fill: false,
      },
      {
        label: 'Without Fees',
        data: results.map(r => r.amountWithoutFees),
        borderColor: 'rgba(54, 162, 235, 1)',
        fill: false,
      },
    ],
  };

  return (
    <div className="investment-simulator">
      <h1>Simulateur d'Investissement</h1>
      <form onSubmit={handleSimulate}>
        <div className="form-group">
          <label>
            Montant initial:
            <input
              type="number"
              name="initialAmount"
              value={formData.initialAmount}
              onChange={handleChange}
              required
            />
          </label>
        </div>
        <div className="form-group">
          <label>
            Horizon temporel (années):
            <input
              type="number"
              name="timeHorizon"
              value={formData.timeHorizon}
              onChange={handleChange}
              required
            />
          </label>
        </div>
        <div className="form-group">
          <label>
            Taux de rendement annuel (%):
            <input
              type="number"
              name="annualReturnRate"
              value={formData.annualReturnRate}
              onChange={handleChange}
              required
            />
          </label>
        </div>
        <div className="form-group">
          <label>
            Frais annuels (%):
            <input
              type="number"
              name="annualFees"
              value={formData.annualFees}
              onChange={handleChange}
              required
            />
          </label>
        </div>
        <button type="submit" className="submit-button">
          Simuler
        </button>
      </form>
      {results && (
        <div className="results">
          <h2>Résultats de la Simulation</h2>
          <Line data={chartData} />
          <table>
            <thead>
              <tr>
                <th>Année</th>
                <th>Avec Frais</th>
                <th>Sans Frais</th>
              </tr>
            </thead>
            <tbody>
              {results.map((r, index) => (
                <tr key={index}>
                  <td>{r.year + 1}</td>
                  <td>{r.amountWithFees.toFixed(2)}</td>
                  <td>{r.amountWithoutFees.toFixed(2)}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}
