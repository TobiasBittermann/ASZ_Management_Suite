import {FormEvent, useEffect, useState} from "react";
import {BwBooking} from "../../types/BwBooking.ts";
import {Member} from "../../types/Member.ts";
import {Drink} from "../../types/Drink.ts";

/* Declaration out of function because of linter error: ESLint: Error: Cannot access variable before it is declared (react-hooks/immutability)*/
function getCurrentLocalDateTime() {
    const now = new Date();
    const local = new Date(now.getTime() - now.getTimezoneOffset() * 60000);
    return local.toISOString().slice(0, 16);
}

type BookingItem = {
    drinkId: string;
    amountDrink: string;
}

type BwBookingAddEditProps = {
    booking: BwBooking | null;
    members: Member[];
    drinks: Drink[];
    onClose: () => void;
    onSave: (booking: BwBooking) => Promise<void>
}

const emptyItem: BookingItem = {drinkId: "", amountDrink: ""}

function BwBookingAddEdit({booking, members, drinks, onClose, onSave}: BwBookingAddEditProps) {
    const [memberId, setMemberId] = useState("");
    const [bookingDate, setBookingDate] = useState("");
    const [items, setItems] = useState([emptyItem]);

    useEffect(() => {
        if (booking) {
            // eslint-disable-next-line react-hooks/set-state-in-effect
            setMemberId(booking.memberId.toString())
            setBookingDate(booking.bookingDate)
            setItems([{drinkId: booking.drinkId.toString(), amountDrink: booking.amountDrink.toString()}])
        } else {
            setMemberId("")
            setBookingDate(getCurrentLocalDateTime());
            setItems([emptyItem])
        }
    }, [booking]);

    function updateItem(index: number, field: keyof BookingItem, value: string) {
        const updated = items.map((item, i) => i === index ? {...item, [field]: value} : item);

        if (!booking && field === "drinkId" && value !== "" && index === items.length - 1) {
            updated.push(emptyItem);
        }
        setItems(updated);
    }

    async function handleSubmit(event: FormEvent<HTMLFormElement>) {
        event.preventDefault();

        const filledItems = items.filter(item => item.drinkId !== "" && item.amountDrink !== "");

        for (const item of filledItems) {
            const selectedDrink = drinks.find(drink => drink.id === Number(item.drinkId));
            if (!selectedDrink) continue;


            const savedBwBooking: BwBooking = {
                id: booking ? booking.id : 0,
                memberId: Number(memberId),
                drinkId: Number(item.drinkId),
                amountDrink: Number(item.amountDrink),
                bookingDate: bookingDate,
                bookingCost: Number((Number(item.amountDrink) * selectedDrink.sellingPrice).toFixed(2))
            }

            await onSave(savedBwBooking)
        }

        onClose();
    }

    return (
        <div className={"fixed inset-0 bg-black/50 flex items-center justify-center z-50"}>
            <div className={"bg-white rounded-2xl shadow-xl p-8 w-full max-w-xl"}>

                <h2 className={"text-2xl font-bold text-gray-800 mb-6"}>
                    {booking ? "Edit Booking" : "Add Booking"}
                </h2>

                <form
                    className={"grid grid-cols-[auto_1fr] items-center gap-x-4 gap-y-4"}
                    onSubmit={handleSubmit}>

                    <label className={"text-sm font-medium text-gray-600 justify-self-start mr-2"}>
                        Mitglied:
                    </label>
                    <select
                        className={"w-full border border-gray-300 rounded-xl px-4 py-2 text-gray-800 shadow-sm cursor-pointer hover:bg-white focus:bg-white focus:outline-none focus:ring-2 focus:ring-green-400"}
                        value={memberId}
                        onChange={e => setMemberId(e.target.value)}>
                        <option value={""}>Bitte Mitglied auswählen</option>
                        {[...members]
                            .sort((a, b) => a.lastName.localeCompare(b.lastName))
                            .map(member => (
                                <option key={member.id} value={member.id}>
                                    {member.firstName} {member.lastName}
                                </option>
                            ))}
                    </select>

                    <label className={"text-sm font-medium text-gray-600 justify-self-start mr-2 self-start mt-2"}>
                        Getränke:
                    </label>
                    <div className={"flex flex-col gap-2"}>
                        {items.map((item, index) => (
                            <div key={index} className={"flex gap-2"}>
                                <select
                                    className={"flex-1 border border-gray-300 rounded-xl px-4 py-2 text-gray-800 shadow-sm cursor-pointer hover:bg-white focus:bg-white focus:outline-none focus:ring-2 focus:ring-green-400"}
                                    value={item.drinkId} onChange={e => updateItem(index, "drinkId", e.target.value)}>
                                    <option value={""}>Bitte Getränk auswählen</option>
                                    {[...drinks].sort((a,b) => a.name.localeCompare(b.name)).map(drink => (
                                        <option key={drink.id} value={drink.id}>{drink.name}</option>
                                        ))}
                                </select>
                                <input className={"w-20 border border-gray-300 rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-400"} type={"number"} placeholder={"Anz."} value={item.amountDrink} onChange={e => updateItem(index, "amountDrink", e.target.value)}/>
                            </div>
                        ))}
                    </div>

                    <label className={"text-sm font-medium text-gray-600 justify-self-start mr-2"}>
                        Buchungsdatum:
                    </label>
                    <input
                        className={"border border-gray-300 rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-400"}
                        type={"datetime-local"}
                        value={bookingDate}
                        onChange={event => setBookingDate(event.target.value)}
                    />

                    <div className={"col-span-2 flex justify-end gap-3 mt-2"}>
                        <button
                            className={"hover:bg-green-500 hover:scale-105 bg-green-300 text-black shadow-md rounded px-6 py-2 m-1 transition"}
                            type={"submit"}>
                            Save
                        </button>
                        <button
                            className={"hover:bg-green-500 hover:scale-105 bg-green-300 text-black shadow-md rounded px-6 py-2 m-1 transition"}
                            type={"button"} onClick={onClose}>
                            Close
                        </button>
                    </div>
                </form>
            </div>
        </div>
    )
}

export default BwBookingAddEdit;