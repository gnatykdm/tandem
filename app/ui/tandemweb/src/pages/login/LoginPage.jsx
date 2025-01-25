import LoginComponent from "../../widgets/login/LoginComponent";
import FooterComponent from "../../shared/footer/Footer";
import './LoginPage.css';

const LoginPage = () => {
  return (
    <div className="login-page">
      <div className="login-page-content">
        <div className="info-side">
          <h1>Welcome to the Future</h1>
          <div className="features">
            <div className="feature-item">
              <h3>Advanced Security</h3>
              <p>Military-grade encryption and biometric authentication keep your data fortress-safe</p>
            </div>
            <div className="feature-item">
              <h3>Smart Dashboard</h3>
              <p>AI-powered interface that adapts to your usage patterns</p>
            </div>
            <div className="feature-item">
              <h3>Real-time Sync</h3>
              <p>Stay connected across all your devices with instant synchronization</p>
            </div>
            <div className="feature-item">
              <h3>24/7 Support</h3>
              <p>Round-the-clock assistance from our dedicated support team</p>
            </div>
          </div>
        </div>
        <div className="login-side">
          <LoginComponent />
        </div>
      </div>
      <FooterComponent/>
    </div>
  );
};
export default LoginPage;
