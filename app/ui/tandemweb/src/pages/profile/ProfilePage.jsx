import { useState, useEffect } from "react";
import { useLocation } from "react-router-dom";
import axios from "axios";
import UserCardComponent from "../../widgets/usercard/UserCardComponent";
import SideBarComponent from "../../shared/sidebar/SideBarComponent";
import ContentComponent from "../../widgets/content/ContentComponent";
import EditModal from "../../widgets/editmodal/EditModal"; 
import { Helmet } from "react-helmet";
import "./ProfilePage.css";

export default function ProfilePage() {
  const [user, setUser] = useState(null);
  const [isEditModalOpen, setIsEditModalOpen] = useState(false);
  const [isError, setIsError] = useState(false);
  const [currentUserLogin, setCurrentUserLogin] = useState(null);
  const location = useLocation();

  const queryParams = new URLSearchParams(location.search);
  const login = queryParams.get("login");

  useEffect(() => {
    setCurrentUserLogin(localStorage.getItem("userLogin"));

    const fetchUserData = async () => {
      try {
        const response = await axios.get(`http://localhost:8080/api/v1/user/find_by_login/${login}`,{
    });

        if (response.status === 200) {
          setUser(response.data);
          setIsError(false);
        }
      } catch (error) {
        console.error("Error fetching user data:", error);
        setIsError(true);
      }
    };

    if (login) {
      fetchUserData();
    }
  }, [login]);

  const handleOpenModal = () => {
    setIsEditModalOpen(true);
  };

  const handleCloseModal = () => {
    setIsEditModalOpen(false);
  };

  if (isError) {
    return <div>Error loading user data.</div>;
  }

  if (!user) {
    return <div>Loading...</div>; 
  }

  return (
    <div className="profile-page-container">
      <Helmet>
        <title>Profile: @{login}</title>
      </Helmet>
      <div className="profile-sidebar">
        <SideBarComponent />
      </div>
      <div className="profile-content">
        <UserCardComponent user={user} onEdit={handleOpenModal} />
        <ContentComponent userLogin={login} currentUserLogin={currentUserLogin}/>
      </div>
      {isEditModalOpen && <EditModal onClose={handleCloseModal} />}
    </div>
  );
}
