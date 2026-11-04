import {useEffect, useState} from "react";
import {Link, useNavigate} from "react-router-dom";
import {FiHome, FiUsers, FiChevronDown, FiLogOut} from "react-icons/fi";
import {BiBeer} from "react-icons/bi";

type CurrentMember = {
    firstName: string;
    lastName: string;
};

function Sidebar() {
    const [bierwartOpen, setBierwartOpen] = useState(false);
    const [member, setMember] = useState<CurrentMember | null>(null);
    const navigate = useNavigate();

    useEffect(() => {
        fetch("/members/me")
            .then(response => response.ok ? response.json() : null)
            .then(data => setMember(data));
    }, []);

    async function logout() {
        await fetch("/webauthn/logout", {method: "POST"});
        navigate("/");
    }

    return (
        <aside className="h-screen w-64 bg-gray-800 text-white flex flex-col">
            <div className="p-4 text-xl font-bold">
                ASZ Management
            </div>

            <nav className={"flex-1 px-3"}>
                <Link
                    to={"/home"}
                    className={"flex items-center gap-3 px-3 py-2 rounded-lg hover:bg-green-700 transition"}>
                    <FiHome/>
                    <span>Home</span>
                </Link>

                <Link
                    to="/member"
                    className="flex items-center gap-3 px-3 py-2 rounded-lg hover:bg-green-700 transition"
                >
                    <FiUsers/>
                    <span>Member</span>
                </Link>

                <button
                    onClick={() => setBierwartOpen(!bierwartOpen)}
                    className="w-full flex items-center justify-between px-3 py-2 rounded-lg hover:bg-green-700 transition"
                >
                    <div className="flex items-center gap-3">
                        <BiBeer/>
                        <span>Bierwart</span>
                    </div>

                    <FiChevronDown
                        className={`transition-transform ${
                            bierwartOpen ? "rotate-180" : ""
                        }`}
                    />
                </button>

                {bierwartOpen &&
                    <div className={"ml-9 mt-1 space-y-1"}>
                        <Link to={"/bierwart/snapshots"}
                              className={"block px-3 py-2 rounded-lg hover:bg-green-700 transition"}>
                            Übersicht
                        </Link>

                        <Link to={"/bierwart/bookings"}
                              className={"block px-3 py-2 rounded-lg hover:bg-green-700 transition"}>
                            Kühlschrankbuchungen
                        </Link>

                        <Link to={"/bierwart/deposits"}
                              className={"block px-3 py-2 rounded-lg hover:bg-green-700 transition"}>
                            Einzahlungen
                        </Link>

                        <Link to={"/bierwart/accountBookings"}
                              className={"block px-3 py-2 rounded-lg hover:bg-green-700 transition"}>
                            Kontobewegungen
                        </Link>

                        <Link to={"/bierwart/drinks"}
                              className={"block px-3 py-2 rounded-lg hover:bg-green-700 transition"}>
                            Getränke
                        </Link>

                        <Link to={"/bierwart/vendors"}
                              className={"block px-3 py-2 rounded-lg hover:bg-green-700 transition"}>
                            Lieferanten
                        </Link>

                        <Link to={"/bierwart/inventories"}
                              className={"block px-3 py-2 rounded-lg hover:bg-green-700 transition"}>
                            Inventur
                        </Link>
                    </div>

                }

            </nav>

            {member && (
                <div className={"flex items-center gap-3 p-4 border-t border-gray-700"}>
                    <div className={"w-10 h-10 shrink-0 rounded-full bg-green-700 flex items-center justify-center font-bold"}>
                        {member.firstName[0]}{member.lastName[0]}
                    </div>
                    <span className={"flex-1 truncate"}>
                        {member.firstName} {member.lastName}
                    </span>
                    <button onClick={logout}
                            title={"Logout"}
                            className={"p-2 rounded-lg hover:bg-green-700 transition"}>
                        <FiLogOut/>
                    </button>
                </div>
            )}
        </aside>
    );
}

export default Sidebar;