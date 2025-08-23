import React from "react";
import AdminSidebar from "./AdminSidebar";

const AdminLayout = ({ children }) => {
  const user = JSON.parse(localStorage.getItem("user") || "{}");
  
  const handleLogout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("user");
    window.location.href = "/login";
  };

  return (
    <div className="flex flex-col min-h-screen bg-black">
      {/* Top Admin Header Bar - replacing the public Navbar */}
      <div className="bg-[#181f2a] border-b border-gray-800 px-6 py-4">
        <div className="flex justify-between items-center">
          {/* Brand */}
          <div className="text-2xl font-bold text-[#00df9a]">TRAVELLA.</div>
          
          {/* User Info and Logout */}
          <div className="flex items-center space-x-4">
            <div className="flex items-center space-x-3">
              <div className="w-8 h-8 bg-[#00df9a] rounded-full flex items-center justify-center">
                <span className="text-black font-bold text-sm">
                  {user.firstName ? user.firstName.charAt(0).toUpperCase() : "A"}
                </span>
              </div>
              <div className="text-white text-sm">
                {user.firstName} {user.lastName}
              </div>
            </div>
            <button
              onClick={handleLogout}
              className="bg-red-600 text-white px-4 py-2 rounded font-semibold hover:bg-red-700 transition-colors"
            >
              Logout
            </button>
          </div>
        </div>
      </div>

      {/* Main Content with Sidebar */}
      <div className="flex flex-1">
        <AdminSidebar />
        <div className="flex-1 p-8">{children}</div>
      </div>
    </div>
  );
};

export default AdminLayout;