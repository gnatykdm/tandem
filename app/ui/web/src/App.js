import React from 'react';
import './App.css';
import ProfilePage from './pages/profile/ProfilePage';
import LoginPage from './pages/login/LoginPage';
import RegisterPage from './pages/register/RegisterPage';
import { RouterProvider, createBrowserRouter } from 'react-router-dom';
import tandem_logo from './shared/assets/icons/tandemlogo.png';
import { Helmet } from 'react-helmet';

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