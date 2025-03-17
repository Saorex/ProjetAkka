import './style.css';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

const Login = () => {
  const navigate = useNavigate();
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [isLeaving, setIsLeaving] = useState(false);

  const handleSubmit = (e) => {
    e.preventDefault();
    setIsLeaving(true);

    //TODO rajouter verification backend
    setTimeout(() => {
      navigate('/board'); // Navigation après l'animation
    }, 500); 
  };

  return (
    <>
    {/*<Link to="/board">Tableau de bord</Link>*/}
    <div className={`login ${isLeaving ? 'fade-out-top' : ''}`}>
      <h1>Donne ta tune.net</h1>
      <div className={`login-div ${isLeaving ? 'fade-out-bottom' : ''}`}>
        <h2>Connexion</h2>
        <form className='login-form' onSubmit={handleSubmit}>
          <div>
            <input name='username' type='text' placeholder='Username' value={username} onChange={(e) => setUsername(e.target.value)}/>
            <label className='login-label' htmlFor='username'>Username</label>
          </div>
          
          <div>
            <input name='password' type='password' placeholder='Mot de passe' value={password} onChange={(e) => setPassword(e.target.value)}/>
            <label className='login-label' htmlFor='password'>Mot de passe</label>
          </div>
          <button className='login-button'>
            Se connecter
          </button>
        </form>
      </div>
    </div>
    </>
  );
}

export default Login;
