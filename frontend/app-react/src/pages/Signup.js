import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './Signup.css';

const Signup = () => {
  const navigate = useNavigate();
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [isLeaving, setIsLeaving] = useState(false);

  const handleSubmit = (e) => {
    e.preventDefault();
    setIsLeaving(true);

    // Vérification que les mots de passe correspondent
    if (password !== confirmPassword) {
      alert("Les mots de passe ne correspondent pas.");
      return;
    }

    // Simuler une requête backend
    setTimeout(() => {
      navigate('/'); // Rediriger vers la page de connexion après l'inscription
    }, 500);
  };

  return (
    <div className={`signup ${isLeaving ? 'fade-out-top' : ''}`}>
      <h1>Donne ta tune.net</h1>
      <div className={`signup-div ${isLeaving ? 'fade-out-bottom' : ''}`}>
        <h2>Inscription</h2>
        <form className='signup-form' onSubmit={handleSubmit}>
          <div>
            <input
              name='username'
              type='text'
              placeholder="Nom d'utilisateur"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              required
            />
            <label className='signup-label' htmlFor='username'>Nom d'utilisateur</label>
          </div>

          <div>
            <input
              name='password'
              type='password'
              placeholder='Mot de passe'
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
            <label className='signup-label' htmlFor='password'>Mot de passe</label>
          </div>

          <div>
            <input
              name='confirmPassword'
              type='password'
              placeholder='Confirmer le mot de passe'
              value={confirmPassword}
              onChange={(e) => setConfirmPassword(e.target.value)}
              required
            />
            <label className='signup-label' htmlFor='confirmPassword'>Confirmer le mot de passe</label>
          </div>

          <button className='signup-button' type="submit">
            S'inscrire
          </button>
        </form>
      </div>
    </div>
  );
}

export default Signup;
