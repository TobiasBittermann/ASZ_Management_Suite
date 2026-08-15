import './App.css'
import {BrowserRouter, Routes, Route} from "react-router-dom"
import LoginPage from "./pages/LoginPage.jsx"
import MembersPage from "./pages/MembersPage.jsx";
import HomePage from "./pages/HomePage.jsx";
import AppLayout from "./components/layout/AppLayout.jsx";
import DrinksPage from "./pages/Bierwart/DrinksPage.jsx";
import AccountBookingsPage from "./pages/Bierwart/AccountBookingsPage.jsx";
import BookingsPage from "./pages/Bierwart/BookingsPage.jsx";
import DepositsPage from "./pages/Bierwart/DepositsPage.jsx";
import SnapshotsPage from "./pages/Bierwart/SnapshotsPage.jsx";
import VendorsPage from "./pages/Bierwart/VendorsPage.jsx";
import InventoryPage from "./pages/Bierwart/InventoryPage.jsx";


function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<LoginPage/>}/>

                <Route element={<AppLayout/>}>
                    <Route path={"/home"} element={<HomePage/>}/>
                    <Route path="/member" element={<MembersPage/>}/>

                    <Route path={"/bierwart/accountBookings"} element={<AccountBookingsPage/>}/>
                    <Route path={"/bierwart/bookings"} element={<BookingsPage/>}/>
                    <Route path={"/bierwart/deposits"} element={<DepositsPage/>}/>
                    <Route path={"/bierwart/drinks"} element={<DrinksPage/>}/>
                    <Route path={"/bierwart/snapshots"} element={<SnapshotsPage/>}/>
                    <Route path={"/bierwart/vendors"} element={<VendorsPage/>}/>
                    <Route path={"/bierwart/inventories"} element={<InventoryPage/>}/>
                </Route>
            </Routes>
        </BrowserRouter>
    );
}

export default App;