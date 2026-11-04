import {Outlet} from "react-router-dom";
import Sidebar from "./SideBar.tsx";

function AppLayout(){
    return (
        <div className="h-screen flex">
            <Sidebar/>

            <main className={"flex-1 overflow-y-auto"}>
                <Outlet/>
            </main>
        </div>
    )
}

export default AppLayout