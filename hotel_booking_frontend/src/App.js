import React, { useRef } from "react";
import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import Navbar from "./components/Navbar";
import Hero from "./components/Hero";
import Search from "./pages/Search";
import Login from "./pages/Login";
import Register from "./pages/Register";
import ForgotPassword from "./pages/ForgotPassword";
import HotelList from "./pages/HotelList";
import HotelDetails from "./pages/HotelDetails";
import CheckEmail from "./pages/CheckEmail";
import Account from "./pages/Account";
import PaymentConfirm from "./pages/PaymentConfirm";
import AdminDashboard from "./pages/AdminDashboard";
import AdminBookings from "./pages/AdminBookings";
import AdminHotels from "./pages/AdminHotels";
import AdminUsers from "./pages/AdminUsers";
import AdminPayments from "./pages/AdminPayments";
import AdminLayout from "./components/AdminLayout";
import ProtectedRoute from "./components/ProtectedRoute";
import ResetPassword from "./pages/ResetPassword";  

function App() {
  const contactSectionRef = useRef(null);
  const aboutSectionRef = useRef(null);

  const scrollToContact = () => {
    contactSectionRef.current?.scrollIntoView({ behavior: "smooth" });
  };
  const scrollToAbout = () => {
    aboutSectionRef.current?.scrollIntoView({ behavior: "smooth" });
  };

  const yourGetUserFunction = () => JSON.parse(localStorage.getItem("user") || "{}");

  const isLoggedIn = () => !!localStorage.getItem("token");
  const isAdmin = () => {
    try {
      const u = JSON.parse(localStorage.getItem("user") || "{}");
      return Array.isArray(u.roles) && u.roles.includes("ADMIN");
    } catch { return false; }
  };

  return (
    <Router>
      <Routes>
        {/* Public routes with Navbar */}
        <Route
          path="/"
          element={
            <>
              <Navbar onContactClick={scrollToContact} onAboutClick={scrollToAbout} />
              <Hero
                contactSectionRef={contactSectionRef}
                aboutSectionRef={aboutSectionRef}
              />
            </>
          }
        />
        <Route 
          path="/login" 
          element={
            isLoggedIn() ? (
              <Navigate to={isAdmin() ? "/admin/dashboard" : "/account"} replace />
            ) : (
              <>
                <Navbar onContactClick={scrollToContact} onAboutClick={scrollToAbout} />
                <Login contactSectionRef={contactSectionRef} aboutSectionRef={aboutSectionRef} />
              </>
            )
          } 
        />
        <Route 
          path="/register" 
          element={
            <>
              <Navbar onContactClick={scrollToContact} onAboutClick={scrollToAbout} />
              <Register contactSectionRef={contactSectionRef} aboutSectionRef={aboutSectionRef} />
            </>
          } 
        />
        <Route path="/forgot-password" element={<ForgotPassword />} />
        <Route 
          path="/hotels" 
          element={
            <>
              <Navbar onContactClick={scrollToContact} onAboutClick={scrollToAbout} />
              <HotelList />
            </>
          } 
        />
        <Route 
          path="/hotel/region" 
          element={
            <>
              <Navbar onContactClick={scrollToContact} onAboutClick={scrollToAbout} />
              <HotelList />
            </>
          } 
        />
        <Route
          path="/hotel/:id"
          element={
            <>
              <Navbar onContactClick={scrollToContact} onAboutClick={scrollToAbout} />
              <HotelDetails
                contactSectionRef={contactSectionRef}
                aboutSectionRef={aboutSectionRef}
              />
            </>
          }
        />
        <Route path="/check-email" element={<CheckEmail />} />
        <Route 
          path="/account" 
          element={
            <ProtectedRoute roles={["GUEST"]}>
              <>
                <Navbar onContactClick={scrollToContact} onAboutClick={scrollToAbout} />
                <Account contactSectionRef={contactSectionRef} aboutSectionRef={aboutSectionRef} />
              </>
            </ProtectedRoute>
          } 
        />
        <Route path="/payments/confirm" element={<PaymentConfirm />} />
        <Route
         path="/search"
          element={
        <ProtectedRoute roles={["GUEST"]}>
         <Search />
       </ProtectedRoute>
        }
      />
      <Route path="/reset-password" element={<ResetPassword />} />

        {/* Admin routes with sidebar/layout - NO Navbar */}
        <Route
          path="/admin/dashboard"
          element={
            <ProtectedRoute roles={["ADMIN"]}>
              <AdminLayout>
                <AdminDashboard getUser={yourGetUserFunction} />
              </AdminLayout>
            </ProtectedRoute>
          }
        />
        <Route
          path="/admin/users"
          element={
            <ProtectedRoute roles={["ADMIN"]}>
              <AdminLayout>
                <AdminUsers getUser={yourGetUserFunction} />
              </AdminLayout>
            </ProtectedRoute>
          }
        />
        <Route
          path="/admin/bookings"
          element={
            <ProtectedRoute roles={["ADMIN"]}>
              <AdminLayout>
                <AdminBookings getUser={yourGetUserFunction} />
              </AdminLayout>
            </ProtectedRoute>
          }
        />
        <Route
          path="/admin/hotels"
          element={
            <ProtectedRoute roles={["ADMIN"]}>
              <AdminLayout>
                <AdminHotels getUser={yourGetUserFunction} />
              </AdminLayout>
            </ProtectedRoute>
          }
        />
        <Route
          path="/admin/payments"
          element={
            <ProtectedRoute roles={["ADMIN"]}>
              <AdminLayout>
                <AdminPayments getUser={yourGetUserFunction} />
              </AdminLayout>
            </ProtectedRoute>
          }
        />

        {/* Fallback: direct based on role if user visits unknown path */}
        <Route path="*" element={<Navigate to={isAdmin() ? "/admin/dashboard" : "/account"} replace />} />
      </Routes>
    </Router>
  );
}

export default App;