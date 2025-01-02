import './VerifyWindow.css';

const VerifyWindow = () => {
    return (
        <div className="verify__window-overlay">
            <div className="verify__window">
                <form action="#" method="put">
                    <label htmlFor="verify-code">Verify Tandem</label>
                    <input type="text" placeholder="Enter verification code" id="verify-code" />
                    <button className="submit-btn">Submit</button>
                </form>
            </div>
        </div>
    );
};

export default VerifyWindow;
