import { Helmet } from 'react-helmet';
import logo from './assets/icons/tandemlogo.png';
import Profile from './pages/profile/ProfilePage.jsx';
import './App.css';

function App() {
  return (
    <div className='app'>
      <Helmet>
        <link rel="icon" href={logo} />
      </Helmet>
      <Profile/>
    </div>
  );
}

export default App;