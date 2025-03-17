import React from "react";
import Navbar from "../components/Navbar";
import Chart from "../components/Chart";
import Card from "../components/Card";
import PieChart from "../components/PieChart";
import './Board.css';

const Board = () => {
  return (
    <div className="Board">
      <Navbar className="navbar" />
      <div className="card-container">
        <Card title="Renault" description="Augmentation de 3%" />
        <Card title="Sopra Steria" description="Diminution de 1%" />
        <Card title="Alten" description="Augmentation de 7%" />
      </div>
      <main>
        <div className="dashboard">
          <Chart className="chart-container" />
          <div className="cards">
            <PieChart />
          </div>
        </div>
      </main>
      <footer className="footer">
        <p>®CyTech</p>
      </footer>
    </div>
  );
};

export default Board;
