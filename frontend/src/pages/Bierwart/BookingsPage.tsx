import {useEffect, useState} from "react";
import {FiEdit3, FiPlusCircle, FiTrash2} from "react-icons/fi";
import {Tooltip} from "react-tooltip";
import BwBookingAddEdit from "../../components/bierwart/BwBookingAddEdit.tsx";
import {loadBwBookings, loadDrinks, loadMembers} from "../../utils/loadUtils.jsx";
import {deleteEntity, saveEntity} from "../../utils/crudUtils.jsx";
import {getDrinkName, getMemberName} from "../../utils/namingUtils.jsx";
import {FaRegFilePdf} from "react-icons/fa6";
import {getCurrentLocalDateDaysAgo} from "../../utils/dateUtils.jsx";
import {Member} from "../../types/Member.ts";
import {Drink} from "../../types/Drink.ts";
import {BwBooking} from "../../types/BwBooking.ts";

type SortKey = "id" | "memberName" | "drinkName" | "date";

function BookingsPage() {
    const [bwBookings, setBwBookings] = useState<BwBooking[]>([]);
    const [members, setMembers] = useState<Member[]>([])
    const [drinks, setDrinks] = useState<Drink[]>([])
    const [selectedBwBooking, setSelectedBwBooking] = useState<BwBooking | null>(null);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [dateFrom, setDateFrom] = useState(getCurrentLocalDateDaysAgo(14));
    const [dateTo, setDateTo] = useState(getCurrentLocalDateDaysAgo(0));
    const [sortKey, setSortKey] = useState<SortKey>("date")
    const [sortAsc, setSortAsc] = useState(false)
    const [search, setSearch] = useState("")

    useEffect(() => {
        loadBwBookings(setBwBookings);
        loadMembers(setMembers);
        loadDrinks(setDrinks);
    }, [])

    async function handleSaveBwBooking(booking: BwBooking) {
        await saveEntity(booking, "/bwbookings", loadBwBookings, setBwBookings)
    }

    async function handleDeleteBwBooking(id: number) {
        await deleteEntity(id, "/bwbookings", loadBwBookings, setBwBookings)
    }

    function handleEditClick(booking: BwBooking) {
        setSelectedBwBooking(booking);
        setIsModalOpen(true);
    }

    function handleAddClick() {
        setSelectedBwBooking(null);
        setIsModalOpen(true);
    }

    function handleOpenReport() {
        window.open(`/reports/bookings?dateFrom=${dateFrom}&dateTo=${dateTo}`, "_blank");
    }

    function handleSort(key: SortKey) {
        if (key === sortKey) {
            setSortAsc(!sortAsc);
        } else {
            setSortKey(key);
            setSortAsc(true);
        }
    }

    const term = search.trim().toLowerCase();
    const filteredBwBookings = bwBookings.filter(booking => [getMemberName(members, booking.memberId), getDrinkName(drinks, booking.drinkId)].some(value => value?.toLowerCase().includes(term)));

    function getSortValue(booking: BwBooking) {
        if (sortKey === "memberName") return getMemberName(members, booking.memberId);
        if (sortKey === "drinkName") return getDrinkName(drinks, booking.drinkId);
        if (sortKey === "date") return booking.bookingDate;
        return booking[sortKey];
    }

    const sortedBwBookings = filteredBwBookings.sort((a, b) => {
        const valueA = getSortValue(a);
        const valueB = getSortValue(b);

        if (valueA == null) return 1;
        if (valueB == null) return -1;

        const result = typeof valueA === "number" && typeof valueB === "number" ? valueA - valueB : String(valueA).localeCompare(String(valueB), "de");

        return sortAsc ? result : -result
    })

    function sortArrow(key: SortKey) {
        return sortKey === key ? (sortAsc ? " ▲" : " ▼") : "";
    }

    return (
        <div>
            <h3 className={"text-3xl font-bold text-gray-800 text-center"}>
                Kühlschrankbuchungen
            </h3>

            <button
                className={"hover:bg-green-500 hover:scale-105 bg-green-300 text-black shadow-md justify-self-start rounded px-6 py-2 m-3 transition"}
                data-tooltip-id={"add-tip"}
                data-tooltip-content={"Add a new booking"}
                onClick={handleAddClick}>
                <FiPlusCircle/>
            </button>
            <Tooltip id={"add-tip"}/>

            <input
                className={"border border-gray-300 rounded-lg px-3 py-2 m-3 focus:outline-none focus:ring-2 focus:ring-blue-400"}
                type={"text"}
                placeholder={"Buchung suchen"}
                value={search}
                onChange={(event) => setSearch(event.target.value)}
            />


            <input
                type={"date"}
                value={dateFrom}
                onChange={(e) => setDateFrom(e.target.value)}
                className={"border rounded px-2 py-1"}
            />
            <input
                type={"date"}
                value={dateTo}
                onChange={(e) => setDateTo(e.target.value)}
                className={"border rounded px-2 py-1"}
            />
            <button
                className={"hover:bg-green-500 hover:scale-105 bg-green-300 text-black shadow-md justify-self-start rounded px-6 py-2 m-3 transition"}
                data-tooltip-id={"report-tip"}
                data-tooltip-content={"Get a bookings report"}
                onClick={handleOpenReport}>
                <FaRegFilePdf/>
            </button>
            <Tooltip id={"report-tip"}/>

            {
                isModalOpen && (
                    <BwBookingAddEdit
                        booking={selectedBwBooking}
                        members={members}
                        drinks={drinks}
                        onClose={() => setIsModalOpen(false)}
                        onSave={handleSaveBwBooking}/>
                )}

            <div className={"overflow-x-auto rounded-xl shadow"}>

                <table className={"min-w-full bg-white text-sm text-left"}>
                    <thead className={"bg-gray-200 text-gray-600 uppercase text-xs"}>
                    <tr>
                        <th className={"px-6 py-3 cursor-pointer select-none"}
                            onClick={() => handleSort("id")}>Id{sortArrow("id")}</th>
                        <th className={"px-6 py-3 cursor-pointer select-none"}
                            onClick={() => handleSort("date")}>Buchungsdatum{sortArrow("date")}</th>
                        <th className={"px-6 py-3 cursor-pointer select-none"}
                            onClick={() => handleSort("memberName")}>Mitglied{sortArrow("memberName")}</th>
                        <th className={"px-6 py-3 cursor-pointer select-none"}
                            onClick={() => handleSort("drinkName")}>Getränk{sortArrow("drinkName")}</th>
                        <th className={"px-6 py-3 cursor-pointer select-none"}>Anzahl Getränk</th>
                        <th className={"px-6 py-3 cursor-pointer select-none"}>Preis</th>
                        <th className={"px-6 py-3 cursor-pointer select-none"}>Aktionen</th>
                    </tr>
                    </thead>
                    <tbody className={"divide-y divide-gray-100"}>
                    {sortedBwBookings.map(booking => (
                        <tr key={booking.id} className={"hover:bg-gray-50 transition"}>
                            <td className={"px-6 py-3"}>{booking.id}</td>
                            <td className={"px-6 py-3"}>{booking.bookingDate}</td>
                            <td className={"px-6 py-3"}>{getMemberName(members, booking.memberId)}</td>
                            <td className={"px-6 py-3"}>{getDrinkName(drinks, booking.drinkId)}</td>
                            <td className={"px-6 py-3"}>{booking.amountDrink}</td>
                            <td className={"px-6 py-3"}>{booking.bookingCost}</td>
                            <td>
                                <button
                                    className={"hover:bg-green-500 hover:scale-105 bg-green-300 text-black shadow-md rounded px-3 py-1 m-1 transition"}
                                    data-tooltip-id={"edit-tip"}
                                    data-tooltip-content={"Edit a booking entry"}
                                    onClick={() => handleEditClick(booking)}>
                                    <FiEdit3/>
                                </button>
                                <button
                                    className={"hover:bg-green-500 hover:scale-105 bg-green-300 text-black shadow-md rounded px-3 py-1 m-1 transition"}
                                    data-tooltip-id={"delete-tip"}
                                    data-tooltip-content={"Delete a booking entry"}
                                    onClick={() => handleDeleteBwBooking(booking.id)}>
                                    <FiTrash2/>
                                </button>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
                <Tooltip id={"edit-tip"}/>
                <Tooltip id={"delete-tip"}/>
            </div>
        </div>
    )
}

export default BookingsPage