import React from 'react';
import ReactDOM from 'react-dom/client';
import { Link,Routes,Route,BrowserRouter } from 'react-router-dom';
import './index.css';
import Login from './Login';
import reportWebVitals from './reportWebVitals';
import Board from "./pages/Board";
import Test from "./pages/test";
import AssetForm from './pages/AssetForm';
import Simulation from './pages/Simulation';
import Signup from './pages/Signup';
import AddFunds from './pages/AddFunds';
import { AuthProvider } from './context/AuthProvider';


const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <AuthProvider>
    <BrowserRouter>
        <Routes>
            <Route path="/signup" element={<Signup />} />
            <Route path="/board" element={<Board />} />
            <Route path="/addfunds" element={<AddFunds />} />
            <Route path="/test" element={<Test />} />
            <Route path="/assetform" element={<AssetForm />} />
            <Route path="/simulations" element={<Simulation />} />
            <Route path="/" element={<Login />} />
            
        </Routes>
    </BrowserRouter>
  </AuthProvider>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
