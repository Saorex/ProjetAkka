import './style.css';
import { Link,Routes,Route } from 'react-router-dom';

const Login = () => {
  return (
    <>
    {/*<Link to="/tableaubord">Tableau de bord</Link>*/}
    <div className='login'>
      <h1>Donne ta tune.net</h1>
      <div className='login-div'>
        <h2>Connexion</h2>
        <form className='login-form'>
          <div>
            <input name='username' type='text' placeholder='Username'/>
            <label className='login-label' htmlFor='username'>Username</label>
          </div>
          
          <div>
            <input name='password' type='password' placeholder='Mot de passe'/>
            <label className='login-label' htmlFor='password'>Mot de passe</label>
          </div>
          <button>
            Se connecter
          </button>
        </form>
      </div>
    </div>
    </>
  );
}

export default Login;
