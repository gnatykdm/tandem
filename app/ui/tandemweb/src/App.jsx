import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { Helmet } from 'react-helmet';
import logo from './assets/icons/tandemlogo.png';
import './App.css';
import RegisterPage from './pages/register/RegisterPage';
import ProfilePage from './pages/profile/ProfilePage';
import LoginPage from './pages/login/LoginPage';
import VerifyPage from './pages/verify/VerifyPage';
import SearchPage from './pages/search/SearchPage';
import GroupPage from './pages/group/GroupPage';
import GroupCardPage from './pages/groupcardpage/GroupCardPage';

function App() {
  return (
    <div className='app'>
      <Helmet>
        <link rel="icon" href={logo} />
      </Helmet>
      <Router>
        <Routes>
          <Route path="/profile" element={<ProfilePage />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/register" element={<RegisterPage />} />
          <Route path="/" element={<RegisterPage />} />
          <Route path="/verify" element={<VerifyPage />} />
          <Route path="/search" element={<SearchPage />} />
          <Route path="/groups" element={<GroupPage />} />
          <Route path="/group" element={<GroupCardPage />} />
        </Routes>
      </Router>
    </div>
  );
}

export default App;