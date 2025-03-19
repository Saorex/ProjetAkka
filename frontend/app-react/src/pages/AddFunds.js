import './AddFunds.css';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Navbar from "../components/Navbar";

const AddFunds = () => {
  const navigate = useNavigate();

  const [isLeaving, setIsLeaving] = useState(false);

  return (
    <>
      <div className='add-funds'>
            <Navbar className="navbar" onLeavingChange={setIsLeaving}/>
                <div className='div-funds'>
                    <form>
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
