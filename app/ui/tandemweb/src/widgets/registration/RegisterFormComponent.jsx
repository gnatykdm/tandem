import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import axios from "axios";
import './RegisterFormComponent.css';

const RegisterFormComponent = () => {
  const [formData, setFormData] = useState({
    login: '',
    username: '',
    email: '',
    password: '',
    profileImage: '',
    acceptPolicy: false
  });

  const [errorMessage, setErrorMessage] = useState('');
  const [successMessage, setSuccessMessage] = useState('');
  
  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setFormData(prevState => ({
      ...prevState,
      [name]: type === 'checkbox' ? checked : value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
  
    try {
      const response = await axios.post('http://localhost:8080/api/v1/auth/signup', null, {
        params: {
          login: formData.login,
          username: formData.username,
          email: formData.email,
          password: formData.password
        }
      });
  
      if (response.status === 200) {
        setSuccessMessage("Registration successful! Please check your email for verification.");
        setErrorMessage('');
        setTimeout(() => {
          navigate(`/verify?login=${formData.login}`); 
        }, 3000);
      }      
    } catch (error) {
      setErrorMessage(error.response?.data?.message || "An unexpected error occurred. Please try again.");
    }
  };  

  return (
    <div className="register-container">
      <div className="register-form-box">
        <h2>Create Account</h2>

        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <input
              type="text"
              name="login"
              value={formData.login}
              onChange={handleChange}
              placeholder="Login"
              className="form-input"
              required
            />
          </div>
          
          <div className="form-group">
            <input
              type="text"
              name="username"
              value={formData.username}
              onChange={handleChange}
              placeholder="Full Name"
              className="form-input"
              required
            />
          </div>

          <div className="form-group">
            <input
              type="email"
              name="email"
              value={formData.email}
              onChange={handleChange}
              placeholder="Email Address"
              className="form-input"
              required
            />
          </div>

          <div className="form-group">
            <input
              type="password"
              name="password"
              value={formData.password}
              onChange={handleChange}
              placeholder="Password"
              className="form-input"
              required
            />
          </div>

          {errorMessage && <div className="error-message">{errorMessage}</div>}
          {successMessage && <div className="success-message">{successMessage}</div>}

          <button type="submit" className="register-button">
            Register
          </button>

          <div className="login-link">
            Already have an account? <Link to="/login">Login here</Link>
          </div>
        </form>
      </div>
    </div>
  );
};

export default RegisterFormComponent;
