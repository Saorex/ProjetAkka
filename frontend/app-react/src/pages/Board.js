import React from "react";
import Navbar from "../components/Navbar";
import Sidebar from "../components/Sidebar";
import Chart from "../components/Chart";
import Card from "../components/Card";
import './Board.css';

const Board = () => {
  return (
    <div className="Board">
      <Navbar className="navbar" />
      <Sidebar className="sidebar" />
      <main>
        <div className="single-column">
          <p>Contenu en une seule colonne</p>
        </div>
        <div className="dashboard">
          <Chart className="chart-container" />
          <div className="cards">
            <Card title="Nouvelle info" description="Détail ici" />
            <Card title="Autre info" description="Détail ici" />
          </div>
        </div>
        <div className="single-column">
          <p>Autre contenu en une seule colonne</p>
        </div>
      </main>
      <footer className="footer">
        <p>Pied de page</p>
      </footer>
    </div>
  );
};

export default Board;
