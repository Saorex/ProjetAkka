import './AddFunds.css';
import { useState, useEffect } from 'react';
import Navbar from "../components/Navbar";
import { useNavigate } from 'react-router-dom';
import Cookies from 'js-cookie';
import { useAuth } from '../context/AuthProvider';

const AddFunds = () => {
  const navigate = useNavigate();

  const { user, logout, setUser } = useAuth();

  const [isLeaving, setIsLeaving] = useState(false);

  useEffect(() => {
      if (Cookies.get('user')) {
        setUser(Cookies.get('user'));
      } else if (!user) {
        navigate('/');
      }
    }, [user, navigate]);

  return (
    <>
      <div className='add-funds'>
            <Navbar className="navbar" onLeavingChange={setIsLeaving}/>
                <div className='div-funds'>
                    <form className='form-addfunds'>
                        <h2>Ajout de fond</h2>
                        <label>
                        Combien voulez vous rajouter ?
                        </label>
                        <input name='howmany' type='number'/>
                        <button type='submit'>Payer</button>
                    </form>
                </div>
            <footer className="footer">
                <p>®CyTech</p>
            </footer>
      </div>
    </>
  );
}

export default AddFunds;
