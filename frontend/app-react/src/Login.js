import './style.css';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import bcrypt from 'bcryptjs';
import axios from 'axios';
import Cookies from 'js-cookie';
import { useAuth } from './context/AuthProvider';

const Login = () => {
  const navigate = useNavigate();
  const [username, setUsername] = useState('');
  const { user, logout,setUser } = useAuth();
  const [password, setPassword] = useState('');
  const [isLeaving, setIsLeaving] = useState(false);
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();

    //const salt = await bcrypt.genSalt(10);
    //const hashedPassword = await bcrypt.hash(password, salt);
    try {
      const response = await axios.post('http://localhost:8080/api/login', {
        username,
        password,
      });
      console.log(response);
      Cookies.set('user', username, { expires: 7 });
      setUser(username);
      setIsLeaving(true);
      setTimeout(() => {
        navigate('/board');
      }, 500);
    } catch (err) {
      setError('Échec de connexion. Vérifiez vos identifiants.');
    }
  };

  /*const handleSignUpClick = () => {
    navigate('/signup'); // Rediriger vers la page d'inscription
  };*/

  return (
    <>
      <div className={`login`}>
        <h1 className={`login-h1 ${isLeaving ? 'fade-out-top' : ''}`} >Donne ta tune.net</h1>
        <div className={`login-div ${isLeaving ? 'fade-out-bottom' : ''}`}>
          <h2>Connexion</h2>
          <form className='login-form' onSubmit={handleSubmit}>
            <div>
              <input
                name='username'
                type='text'
                placeholder='Username'
                value={username}
                onChange={(e) => setUsername(e.target.value)}
              />
              <label className='login-label' htmlFor='username'>Username</label>
            </div>

            <div>
              <input
                name='password'
                type='password'
                placeholder='Mot de passe'
                value={password}
                onChange={(e) => setPassword(e.target.value)}
              />
              <label className='login-label' htmlFor='password'>Mot de passe</label>
            </div>
            <button className='login-button' type="submit">
              Se connecter
            </button>
          </form>
        </div>
      </div>
    </>
  );
}

export default Login;
