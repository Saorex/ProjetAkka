import './style.css';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

const Login = () => {
  const navigate = useNavigate();
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [errorMessage, setErrorMessage] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const response = await fetch("http://localhost:8080/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({ username, password })
      });

      if (response.ok) {
        const data = await response.json();
        localStorage.setItem("authToken", data.token); // Stocker le token
        navigate('/board'); // Rediriger après succès
      } else {
        const data = await response.text(); // Récupérer l'erreur envoyée par le backend
        setErrorMessage(data); // Afficher le message d'erreur
      }
    } catch (error) {
      setErrorMessage("Erreur de connexion au serveur.");
    }
  };

  return (
    <div className='login'>
      <h1>Donne ta tune.net</h1>
      <div className='login-div'>
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

          {errorMessage && <p className="error-message">{errorMessage}</p>}

          <button className='login-button'>
            Se connecter
          </button>
        </form>
      </div>
    </div>
  );
};

export default Login;
