import { useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import axios from "axios";
import './VerifyWindow.css';

const VerifyWindow = () => {
    const location = useLocation();
    const navigate = useNavigate();

    const queryParams = new URLSearchParams(location.search);
    const login = queryParams.get("login");

    const [verificationCode, setVerificationCode] = useState("");
    const [errorMessage, setErrorMessage] = useState("");
    const [successMessage, setSuccessMessage] = useState("");
    const [isSubmitting] = useState(false);

    if (!login) {
        return <div className="error-message">Login parameter is missing.</div>;
    }

    const handleSubmit = async (e) => {
        e.preventDefault();
    
        try {
            const response = await axios.put('http://localhost:8080/api/v1/auth/validate_key', null, {
                params: { k: verificationCode }
            });
    
            if (response.status === 200) {
                
                localStorage.setItem("userLogin", login);
                setSuccessMessage("Verification successful! Your account has been activated.");
                setErrorMessage("");
                setTimeout(() => {
                    navigate(`/profile?login=${login}`);
                }, 3000);
            }
        } catch (error) {
            setErrorMessage(error.response?.data?.message || "Verification failed. Please try again.");
            setSuccessMessage("");
        }
    };
    
    

    return (
        <div className="verify__window-overlay">
            <div className="verify__window">
                <form onSubmit={handleSubmit}>
                    <label htmlFor="verify-code">Verify Tandem</label>
                    <input
                        type="text"
                        placeholder="Enter verification code"
                        id="verify-code"
                        value={verificationCode}
                        onChange={(e) => setVerificationCode(e.target.value)}
                        required
                    />
                    {errorMessage && <div className="error-message">{errorMessage}</div>}
                    {successMessage && <div className="success-message">{successMessage}</div>}
                    {isSubmitting && <div className="loading-indicator">Submitting...</div>}
                    <button className="submit-btn" type="submit" disabled={isSubmitting}>Submit</button>
                </form>
            </div>
        </div>
    );
};

export default VerifyWindow;
