import './Footes.css';

const Footer = () => {
  return (
    <footer className="footer">
      <div className="footer-content">
        <div className="footer-section">
          <h3>About</h3>
          <p>Connect with friends, share moments, and discover new connections on our social platform.</p>
        </div>

        <div className="footer-section">
          <h3>Quick Links</h3>
          <ul>
            <li><a href="/explore">Explore</a></li>
            <li><a href="/messages">Messages</a></li>
            <li><a href="/notifications">Notifications</a></li>
            <li><a href="/profile">Profile</a></li>
          </ul>
        </div>

        <div className="footer-section">
          <h3>Help & Support</h3>
          <ul>
            <li><a href="/help">Help Center</a></li>
            <li><a href="/safety">Safety Center</a></li>
            <li><a href="/community">Community Guidelines</a></li>
            <li><a href="/privacy">Privacy Policy</a></li>
          </ul>
        </div>

        <div className="footer-section">
          <h3>Connect With Us</h3>
          <ul>
            <li><a href="https://instagram.com">Instagram</a></li>
            <li><a href="https://facebook.com">Facebook</a></li>
            <li><a href="https://twitter.com">Twitter</a></li>
            <li><a href="https://linkedin.com">LinkedIn</a></li>
          </ul>
        </div>
      </div>

      <div className="footer-bottom">
        <p>© {new Date().getFullYear()} Tandem. All rights reserved.</p>
      </div>
    </footer>
  );
};

export default Footer;
