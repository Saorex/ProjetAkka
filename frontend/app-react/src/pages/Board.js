import React, { useState, useEffect } from "react";
import Navbar from "../components/Navbar";
import Chart from "../components/Chart";
import Card from "../components/Card";
import PieChart from "../components/PieChart";
import './Board.css';
import { useAuth } from '../context/AuthProvider';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import Cookies from 'js-cookie';

const Board = () => {
  const navigate = useNavigate();

  const [isLeaving, setIsLeaving] = useState(false);
  const { user, logout, setUser } = useAuth();
  const [error, setError] = useState('');
  const [cardData, setCardData] = useState({
    BTC: { title: "BitCoin", description: "" },
    ETH: { title: "Ethereum", description: "" },
    BNB: { title: "Binance Coin", description: "" },
    SOL: { title: "Solana", description: "" },
    AVA: { title: "Avalanche", description: "" },
  });
  const [select, setSelect] = useState('BTC');

  const [cdata, setcData] = useState({
    BTC: { data: "" },
    ETH: { data: "" },
    BNB: { data: "" },
    SOL: { data: "" },
    AVA: { data: "" },
  });

  const symbols = [
    { symbol: "BTCUSDT", key: "BTC" },
    { symbol: "ETHUSDT", key: "ETH" },
    { symbol: "BNBUSDT", key: "BNB" },
    { symbol: "SOLUSDT", key: "SOL" },
    { symbol: "AVAXUSDT", key: "AVA" },
  ];

  useEffect(() => {
    if (Cookies.get('user')) {
      setUser(Cookies.get('user'));
    } else if (!user) {
      navigate('/');
    }
  }, [user, navigate]);

  useEffect(() => {
    const fetchData = async () => {
      try {
        for (const { symbol, key } of symbols) {
          const response = await axios.get(`http://localhost:8080/api/data?symbol=${symbol}`);
          setcData(prevState => ({
            ...prevState,
            [key]: {
              data: response.data.slice(-10),
            }
          }));
          const lastData = response.data[response.data.length - 1];
          setCardData(prevState => ({
            ...prevState,
            [key]: {
              title: prevState[key].title,
              description: `Open: ${lastData.open} Close: ${lastData.close}`,
            }
          }));
        }
      } catch (err) {
        setError('Erreur lors de la récupération des données.');
        console.error(err);
      }
    };
    fetchData();
  }, []);

  const handleCardClick = (key) => {
    setSelect(key);
  };

  return (
    <div className="Board">
      <Navbar className="navbar" onLeavingChange={setIsLeaving} />
      <div className="card-container">
        {symbols.map(({ key }) => (
          <div className="card-crypto" key={key} onClick={() => handleCardClick(key)}>
            {cardData[key].description ? (
              <Card
                title={cardData[key].title}
                description={cardData[key].description}
              />
            ) : (
              <p>Loading...</p>
            )}
          </div>
        ))}
      </div>
      <main>
        <div className="dashboard">
          {cdata[select].data && cdata[select].data.length > 0 ? (
            <Chart className="chart-container" tmpdata={cdata[select].data} name={select}/>
          ) : (
            <p className="loding-chart">Loading chart data...</p>
          )}
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
