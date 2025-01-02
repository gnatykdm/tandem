import { useState } from "react";
import UserCardComponent from "../../widgets/usercard/UserCardComponent";
import SideBarComponent from "../../shared/sidebar/SideBarComponent";
import ContentComponent from "../../widgets/content/ContentComponent";
import EditModal from "../../widgets/editmodal/EditModal"; 
import "./ProfilePage.css";

export default function ProfilePage() {
  const [isEditModalOpen, setIsEditModalOpen] = useState(false);

  const handleOpenModal = () => {
    setIsEditModalOpen(true);
  };

  const handleCloseModal = () => {
    setIsEditModalOpen(false);
  };

  return (
    <div className="profile-page-container">
      <div className="profile-sidebar">
        <SideBarComponent />
      </div>
      <div className="profile-content">
        <UserCardComponent onEdit={handleOpenModal} />
        <ContentComponent />
      </div>
      {isEditModalOpen && <EditModal onClose={handleCloseModal} />}
    </div>
  );
}
