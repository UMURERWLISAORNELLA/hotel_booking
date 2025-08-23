import React from "react";
import { NavLink } from "react-router-dom";

const links = [
  { to: "/admin/dashboard", label: "Dashboard" },
  { to: "/admin/users", label: "Users" },
  { to: "/admin/hotels", label: "Hotels" },
  { to: "/admin/bookings", label: "Bookings" },
  { to: "/admin/payments", label: "Payments" },
];

const AdminSidebar = () => {
  const user = JSON.parse(localStorage.getItem("user") || "{}");

  return (
    <div className="bg-[#181f2a] min-h-screen w-64 p-6 flex flex-col border-r border-gray-800">
      {/* Admin Header */}
      <div className="mb-8">
        <div className="text-2xl font-bold text-[#00df9a] mb-4">Admin Panel</div>
        
        {/* User Profile Section */}
        <div className="bg-[#101e36] rounded-lg p-4 mb-4">
          <div className="flex items-center space-x-3">
            <div className="w-10 h-10 bg-[#00df9a] rounded-full flex items-center justify-center">
              <span className="text-black font-bold text-lg">
                {user.firstName ? user.firstName.charAt(0).toUpperCase() : 
                 user.fullName ? user.fullName.charAt(0).toUpperCase() : "A"}
              </span>
            </div>
            <div className="flex-1">
              <div className="text-white font-semibold">
                {user.fullName || `${user.firstName || ""} ${user.lastName || ""}`.trim() || "Admin User"}
              </div>
              <div className="text-gray-400 text-sm">{user.email}</div>
              <div className="text-[#00df9a] text-xs font-medium">
                {user.roles && user.roles.includes("ADMIN") ? "Administrator" : "User"}
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* Navigation Links */}
      <nav className="flex-1">
        {links.map(link => (
          <NavLink
            key={link.to}
            to={link.to}
            className={({ isActive }) =>
              `block px-4 py-3 rounded-lg font-semibold mb-2 transition-colors ${
                isActive
                  ? "bg-[#00df9a] text-black"
                  : "text-gray-200 hover:bg-[#101e36] hover:text-[#00df9a]"
              }`
            }
          >
            {link.label}
          </NavLink>
        ))}
      </nav>

      {/* Footer */}
      <div className="mt-auto pt-4 border-t border-gray-700">
        <div className="text-center text-gray-400 text-sm">
          TRAVELLA Admin
        </div>
      </div>
    </div>
  );
};

export default AdminSidebar;