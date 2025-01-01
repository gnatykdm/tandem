import RegisterComponent from "../../widgets/registration/RegisterFormComponent";
import FooterComponent from '../../shared/footer/Footer';
import './RegisterPage.css';

const RegisterPage = () => {
  return (
    <div className="register-page">
      <div className="register-page-content">
        <div className="info-side">
          <h1>Join Our Community</h1>
          <div className="features">
            <div className="feature-item">
              <h3>Personalized Experience</h3>
              <p>Customize your dashboard and get recommendations tailored just for you</p>
            </div>
            <div className="feature-item">
              <h3>Seamless Integration</h3>
              <p>Connect with your favorite tools and services effortlessly</p>
            </div>
            <div className="feature-item">
              <h3>Premium Benefits</h3>
              <p>Access exclusive features and priority support as a registered member</p>
            </div>
            <div className="feature-item">
              <h3>Secure Platform</h3>
              <p>Your data is protected with enterprise-grade security measures</p>
            </div>
          </div>
        </div>
        <div className="register-side">
          <RegisterComponent />
        </div>
      </div>
      <FooterComponent />
    </div>
  );
};

export default RegisterPage;