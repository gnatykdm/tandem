import { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import "./LoginComponent.css";

const LoginComponent = () => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const [successMessage, setSuccessMessage] = useState("");
  const [isLoading, setIsLoading] = useState(false); 
  const navigate = useNavigate();

  const backendUrl = import.meta.env.VITE_BACKEND_URL;

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!username || !password) {
      setErrorMessage("Both username and password are required.");
      return;
    }

    setIsLoading(true);
    setErrorMessage(""); 
    setSuccessMessage(""); 

    try {
      const response = await axios.put(`${backendUrl}/api/v1/auth/login`, null, {
        params: {
          log: username,
          pass: password,
        }
      });

      if (response.status === 200) {

        localStorage.setItem("userLogin", username);
        setSuccessMessage("Login successful!");
        setErrorMessage(""); 

        setTimeout(() => {
          navigate(`/profile?login=${username}`); 
        }, 2000);
      }
    } catch (error) {
      if (error.response && error.response.data) {
        setErrorMessage(error.response.data.message || "An error occurred while logging in.");
      } else {
        setErrorMessage("An error occurred while logging in.");
      }
      setSuccessMessage(""); 
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="login-container">
      <form className="login-form" onSubmit={handleSubmit}>
        <h2>Login</h2>

        <div className="form-group">
          <input
            type="text"
            placeholder="Username"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            required
          />
        </div>

        <div className="form-group">
          <input
            type="password"
            placeholder="Password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
        </div>

        {errorMessage && <div className="error-message">{errorMessage}</div>}
        {successMessage && <div className="success-message">{successMessage}</div>}
        {isLoading && <div className="loader">Loading...</div>}

        <button type="submit" className="login-button" disabled={isLoading}>
          {isLoading ? "Logging in..." : "Login"}
        </button>

        <div className="forgot-password">
          <a href="/forgot-password">Forgot Password?</a>
        </div>
      </form>
    </div>
  );
};

export default LoginComponent;
