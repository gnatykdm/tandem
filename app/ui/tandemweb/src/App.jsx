import { Helmet } from 'react-helmet';
import logo from './assets/icons/tandemlogo.png';
import VerifyPage from './pages/verify/VerifyPage';
import './App.css';

function App() {
  return (
    <div className='app'>
      <Helmet>
        <link rel="icon" href={logo} />
      </Helmet>
      <VerifyPage/>
    </div>
  );
}

export default App;