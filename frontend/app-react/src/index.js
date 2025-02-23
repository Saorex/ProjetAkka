import React from 'react';
import ReactDOM from 'react-dom/client';
import { Link,Routes,Route,BrowserRouter } from 'react-router-dom';
import './index.css';
import App from './Login';
import reportWebVitals from './reportWebVitals';
import Board from "./pages/Board";
import Test from "./pages/test";

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <BrowserRouter>
      <Routes>
          <Route path='/' element={<App/>}/>
          <Route path="/board" element={<Board />} />
          <Route path="/test" element={<Test />} />
      </Routes>
  </BrowserRouter>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
