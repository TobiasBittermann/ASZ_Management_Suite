import {useEffect, useState} from "react";
import {FiEdit3, FiPlusCircle, FiTrash2} from "react-icons/fi";
import {Tooltip} from "react-tooltip";
import BwBookingAddEdit from "../../components/bierwart/BwBookingAddEdit.jsx";
import {loadBwBookings, loadDrinks, loadMembers} from "../../utils/loadUtils.jsx";
import {deleteEntity, saveEntity} from "../../utils/crudUtils.jsx";
import {getDrinkName, getMemberName} from "../../utils/namingUtils.jsx";

function BwBookingsTab() {
    const [bwBookings, setBwBookings] = useState([]);
    const [members, setMembers] = useState([])
    const [drinks, setDrinks] = useState([])
    const [selectedBwBooking, setSelectedBwBooking] = useState(null);
    const [isModalOpen, setIsModalOpen] = useState(false);

    useEffect(() => {
        loadBwBookings(setBwBookings);
        loadMembers(setMembers);
        loadDrinks(setDrinks);
    }, [])

    async function handleSaveBwBooking(booking) {
        await saveEntity(booking, "/bwbookings", loadBwBookings, setBwBookings)
    }

    async function handleDeleteBwBooking(id){
        await deleteEntity(id, "/bwbookings", loadBwBookings, setBwBookings)
    }

    function handleEditClick(booking) {
        setSelectedBwBooking(booking);
        setIsModalOpen(true);
    }

    function handleAddClick() {
        setSelectedBwBooking(null);
        setIsModalOpen(true);
    }

    return (
        <div>
            <h3 className={"text-3xl font-bold text-gray-800 text-center"}>
                Getränkebuchungen
            </h3>

            <button
                className={"hover:bg-green-500 hover:scale-105 bg-green-300 text-black shadow-md justify-self-start rounded px-6 py-2 m-3 transition"}
                data-tooltip-id={"add-tip"}
                data-tooltip-content={"Add a new booking"}
                onClick={handleAddClick}>
                <FiPlusCircle/>
            </button>
            <Tooltip id={"add-tip"}/>

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
                        <th className={"px-6 py-3"}>Id</th>
                        <th className={"px-6 py-3"}>Buchungsdatum</th>
                        <th className={"px-6 py-3"}>Mitglied</th>
                        <th className={"px-6 py-3"}>Getränk</th>
                        <th className={"px-6 py-3"}>Anzahl Getränk</th>
                        <th className={"px-6 py-3"}>Preis</th>
                        <th className={"px-6 py-3"}>Aktionen</th>
                    </tr>
                    </thead>
                    <tbody className={"divide-y divide-gray-100"}>
                    {bwBookings.map(booking => (
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

export default BwBookingsTab