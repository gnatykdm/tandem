import "./GroupCardPage.css";
import Group from "../../widgets/group/Group";
import { useState, useEffect } from "react";
import SideBarComponent from "../../shared/sidebar/SideBarComponent";

const GroupCardPage = () => {

  const [userLogin, setUserLogin] = useState(null);
  
  const queryParams = new URLSearchParams(location.search);
  const groupName = queryParams.get("name");

  useEffect(() => {
    setUserLogin(localStorage.getItem("userLogin"));
  }, [])

  return (
    <div className="group_card_page">
      <SideBarComponent />

      <div className="group-card-info">
        <Group groupName={groupName} currentUserLogin={userLogin}/>
      </div>
    </div>
  );
};

export default GroupCardPage;