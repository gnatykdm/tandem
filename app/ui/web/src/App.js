import logo from './logo.svg';
import './App.css';
import LoginComponent from './components/login/LoginComponent';
import ProfilePage from './pages/profile/ProfilePage';
import LoginPage from './pages/login/LoginPage';
import UserCardComponent from './components/usercard/UserCardComponent';
import RegisterPage from './pages/register/RegisterPage';
import SideBarComponent from './components/sidebar/SideBarComponent';
import Footer from './components/footer/Footer';
import { RouterProvider, createBrowserRouter } from 'react-router-dom';
import tandem_logo from './shared/assets/icons/tandemlogo.png';
import {Helmet} from 'react-helmet';

const router = createBrowserRouter([
  {
    path: "/",
    element: <LoginPage />,
  },
  {
    path: "/register",
    element: <RegisterPage />,
  },
  {
    path: "/profile",
    element: <ProfilePage />,
  },
]);


function App() {
  return (
    <div className="App">
      <Helmet>
        <link rel="icon" href={tandem_logo} />
      </Helmet> 
      <RouterProvider router={router} />
    </div>
  );
}

export default App;
