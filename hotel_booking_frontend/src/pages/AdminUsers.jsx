import React, { useEffect, useState } from "react";
import axios from "axios";

const API_BASE = "http://localhost:9090/api";

const AdminUsers = ({ getUser }) => {
  const [users, setUsers] = useState([]);
  const [page, setPage] = useState(0);
  const [size, setSize] = useState(10);
  const [totalPages, setTotalPages] = useState(1);
  const [loading, setLoading] = useState(true);

  // Modal state
  const [showModal, setShowModal] = useState(false);
  const [editingUser, setEditingUser] = useState(null);
  const [formUser, setFormUser] = useState({ 
    firstName: "", 
    lastName: "", 
    email: "", 
    roles: ["USER"] 
  });
  const [showDelete, setShowDelete] = useState(false);
  const [userToDelete, setUserToDelete] = useState(null);

  useEffect(() => {
    fetchUsers();
  }, [page, size]);

  const fetchUsers = async () => {
    setLoading(true);
    try {
      const token = localStorage.getItem("token");
      const res = await axios.get(`${API_BASE}/users?page=${page}&size=${size}`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      
      let usersData = [];
      if (Array.isArray(res.data)) {
        usersData = res.data;
      } else if (res.data && Array.isArray(res.data.content)) {
        usersData = res.data.content;
      }
      
      setUsers(usersData);
      const newTotalPages = res.data.totalPages || 1;
      setTotalPages(newTotalPages);
      if (page >= newTotalPages && newTotalPages > 0) {
        setPage(newTotalPages - 1);
      }
    } catch (err) {
      console.error("Error fetching users:", err);
      setUsers([]);
    } finally {
      setLoading(false);
    }
  };

  // Create or Update
  const handleSave = async () => {
    const token = localStorage.getItem("token");
    try {
      if (editingUser) {
        // Update
        await axios.put(`${API_BASE}/users/${editingUser.id}`, formUser, {
          headers: { Authorization: `Bearer ${token}` },
        });
      } else {
        // Create
        await axios.post(`${API_BASE}/users`, formUser, {
          headers: { Authorization: `Bearer ${token}` },
        });
      }
      setShowModal(false);
      setEditingUser(null);
      setFormUser({ firstName: "", lastName: "", email: "", roles: ["USER"] });
      fetchUsers();
    } catch (err) {
      alert("Failed to save user.");
    }
  };

  // Delete
  const handleDelete = async () => {
    const token = localStorage.getItem("token");
    try {
      await axios.delete(`${API_BASE}/users/${userToDelete.id}`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setShowDelete(false);
      setUserToDelete(null);
      fetchUsers();
    } catch (err) {
      alert("Failed to delete user.");
    }
  };

  // Admin check
  const user = getUser ? getUser() : JSON.parse(localStorage.getItem("user") || "{}");
  if (!user || !user.roles || !user.roles.includes("ADMIN")) {
    return (
      <div className="flex items-center justify-center min-h-screen bg-black">
        <div className="text-2xl text-[#00df9a] font-bold">Access Denied: Admins Only</div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-black py-10 px-4">
      <div className="max-w-6xl mx-auto bg-[#181f2a] rounded-xl shadow-xl p-8 border border-gray-800">
        <div className="flex justify-between items-center mb-6">
          <h1 className="text-3xl font-bold text-[#00df9a]">Users</h1>
          <button
            className="bg-[#00df9a] text-black px-4 py-2 rounded font-semibold hover:bg-[#00c97a]"
            onClick={() => {
              setEditingUser(null);
              setFormUser({ firstName: "", lastName: "", email: "", roles: ["USER"] });
              setShowModal(true);
            }}
          >
            + Add User
          </button>
        </div>
        
        {loading ? (
          <div className="text-center text-[#00df9a]">Loading...</div>
        ) : (
          <>
            <div className="overflow-x-auto rounded-lg shadow-inner mb-4">
              <table className="min-w-full bg-[#101e36] rounded-lg overflow-hidden">
                <thead>
                  <tr>
                    <th className="px-4 py-2 text-left text-xs font-semibold text-gray-300 uppercase">Name</th>
                    <th className="px-4 py-2 text-left text-xs font-semibold text-gray-300 uppercase">Email</th>
                    <th className="px-4 py-2 text-left text-xs font-semibold text-gray-300 uppercase">Role</th>
                    <th className="px-4 py-2 text-left text-xs font-semibold text-gray-300 uppercase">Actions</th>
                  </tr>
                </thead>
                <tbody>
                  {users.map((u, index) => (
                    <tr key={u.id} className="border-b border-gray-800">
                      <td className="px-4 py-2 text-[#00df9a]">
                        {u.firstName && u.lastName ? `${u.firstName} ${u.lastName}` : u.fullName || u.username}
                      </td>
                      <td className="px-4 py-2 text-white">{u.email}</td>
                      <td className="px-4 py-2 text-white">
                        {Array.isArray(u.roles) ? u.roles.join(", ") : u.roles}
                      </td>
                      <td className="px-4 py-2">
                        <button
                          className="bg-gray-700 text-gray-200 px-3 py-1 rounded mr-2 hover:bg-[#00df9a] hover:text-black"
                          onClick={() => {
                            setEditingUser(u);
                            setFormUser({ 
                              firstName: u.firstName || u.fullName?.split(' ')[0] || '', 
                              lastName: u.lastName || u.fullName?.split(' ').slice(1).join(' ') || '', 
                              email: u.email, 
                              roles: Array.isArray(u.roles) ? u.roles : [u.roles] 
                            });
                            setShowModal(true);
                          }}
                        >
                          Edit
                        </button>
                        <button
                          className="bg-red-600 text-white px-3 py-1 rounded hover:bg-red-700"
                          onClick={() => {
                            setUserToDelete(u);
                            setShowDelete(true);
                          }}
                        >
                          Delete
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
            
            {/* Pagination Controls */}
            <div className="flex items-center justify-between mt-4">
              <div>
                <button
                  className="bg-gray-700 text-gray-200 px-3 py-1 rounded mr-2 disabled:opacity-50"
                  onClick={() => setPage((p) => Math.max(0, p - 1))}
                  disabled={page === 0}
                >
                  Prev
                </button>
                <span className="text-gray-300 font-semibold">
                  Page {page + 1} of {totalPages}
                </span>
                <button
                  className="bg-gray-700 text-gray-200 px-3 py-1 rounded ml-2 disabled:opacity-50"
                  onClick={() => setPage((p) => Math.min(totalPages - 1, p + 1))}
                  disabled={page >= totalPages - 1}
                >
                  Next
                </button>
              </div>
              <div>
                <label className="text-gray-300 mr-2">Rows per page:</label>
                <select
                  className="bg-gray-700 text-gray-200 px-2 py-1 rounded"
                  value={size}
                  onChange={(e) => setSize(Number(e.target.value))}
                >
                  {[5, 10, 20, 50].map((s) => (
                    <option key={s} value={s}>{s}</option>
                  ))}
                </select>
              </div>
            </div>
          </>
        )}

        {/* Modal for Create/Edit */}
        {showModal && (
          <div className="fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center z-50">
            <div className="bg-white rounded-lg shadow-lg p-8 max-w-md w-full">
              <h2 className="text-xl font-bold mb-4">{editingUser ? "Edit User" : "Add User"}</h2>
              <div className="mb-4">
                <label className="block text-gray-700 mb-1">First Name</label>
                <input
                  className="w-full border px-3 py-2 rounded"
                  value={formUser.firstName}
                  onChange={e => setFormUser({ ...formUser, firstName: e.target.value })}
                />
              </div>
              <div className="mb-4">
                <label className="block text-gray-700 mb-1">Last Name</label>
                <input
                  className="w-full border px-3 py-2 rounded"
                  value={formUser.lastName}
                  onChange={e => setFormUser({ ...formUser, lastName: e.target.value })}
                />
              </div>
              <div className="mb-4">
                <label className="block text-gray-700 mb-1">Email</label>
                <input
                  className="w-full border px-3 py-2 rounded"
                  value={formUser.email}
                  onChange={e => setFormUser({ ...formUser, email: e.target.value })}
                />
              </div>
              <div className="mb-4">
                <label className="block text-gray-700 mb-1">Roles</label>
                <select
                  className="w-full border px-3 py-2 rounded"
                  value={formUser.roles[0]}
                  onChange={e => setFormUser({ ...formUser, roles: [e.target.value] })}
                >
                  <option value="USER">USER</option>
                  <option value="ADMIN">ADMIN</option>
                </select>
              </div>
              <div className="flex justify-end">
                <button
                  className="bg-gray-300 text-gray-700 px-4 py-2 rounded mr-2"
                  onClick={() => setShowModal(false)}
                >
                  Cancel
                </button>
                <button
                  className="bg-[#00df9a] text-black px-4 py-2 rounded font-semibold"
                  onClick={handleSave}
                >
                  Save
                </button>
              </div>
            </div>
          </div>
        )}

        {/* Delete Confirmation */}
        {showDelete && (
          <div className="fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center z-50">
            <div className="bg-white rounded-lg shadow-lg p-8 max-w-md w-full text-center">
              <h2 className="text-xl font-bold mb-4">Delete User</h2>
              <p className="mb-6">Are you sure you want to delete <span className="font-semibold">
                {userToDelete?.firstName && userToDelete?.lastName ? 
                  `${userToDelete.firstName} ${userToDelete.lastName}` : 
                  userToDelete?.fullName || userToDelete?.username}
              </span> ({userToDelete?.email})?</p>
              <div className="flex justify-center">
                <button
                  className="bg-gray-300 text-gray-700 px-4 py-2 rounded mr-2"
                  onClick={() => setShowDelete(false)}
                >
                  Cancel
                </button>
                <button
                  className="bg-red-600 text-white px-4 py-2 rounded font-semibold"
                  onClick={handleDelete}
                >
                  Delete
                </button>
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default AdminUsers;