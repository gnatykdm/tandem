import UserCardComponent from "../../components/usercard/UserCardComponent";
import SideBarComponent from "../../components/sidebar/SideBarComponent";
import ContentComponent from "../../components/content/ContentComponent";
import "./ProfilePage.css";

export default function ProfilePage() {
  return (
    <div className="profile-page-container">
      <div className="profile-sidebar">
        <SideBarComponent />
      </div>
      <div className="profile-content">
        <UserCardComponent />
        <ContentComponent />
      </div>
    </div>
  );
}

