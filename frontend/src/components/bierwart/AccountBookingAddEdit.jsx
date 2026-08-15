import {useEffect, useState} from "react";
import {getAccountTypeLable} from "../../utils/namingUtils.jsx";
import {getCurrentLocalDateTime} from "../../utils/dateUtils.jsx";

function AccountBookingAddEdit({booking, vendors, accountTypes, onClose, onSave}) {
    const [vendorId, setVendorId] = useState("");
    const [amount, setAmount] = useState("");
    const [invoiceNumber, setInvoiceNumber] = useState("");
    const [accountType, setAccountType] = useState("")
    const [date, setDate] = useState("");
    const [note, setNote] = useState("");

    useEffect(() => {
        if (booking) {
            setVendorId(booking.vendorId)
            setAmount(booking.amount)
            setInvoiceNumber(booking.invoiceNumber)
            setAccountType(booking.accountType)
            setDate(booking.date)
            setNote(booking.note)
        } else {
            setVendorId("")
            setAmount("")
            setInvoiceNumber("")
            setAccountType("")
            setDate(getCurrentLocalDateTime())
            setNote("")
        }
    }, [booking]);

    async function handleSubmit(event) {
        event.preventDefault();

        const savedAccountBooking = {
            id: booking ? booking.id : 0,
            vendorId: Number(vendorId),
            amount: Number(amount),
            invoiceNumber: invoiceNumber,
            accountType: accountType,
            date: date,
            note: note
        }

        await onSave(savedAccountBooking)
        onClose();
    }

    return (
        <div className={"fixed inset-0 bg-black/50 flex items-center justify-center z-50"}>
            <div className={"bg-white rounded-2xl shadow-xl p-8 w-full max-w-md"}>

                <h2 className={"text-2xl font-bold text-gray-800 mb-6"}>
                    {booking ? "Edit Booking" : "Add Booking"}
                </h2>

                <form
                    className={"grid grid-cols-[auto_1fr] items-center gap-x-4 gap-y-4"}
                    onSubmit={handleSubmit}>

                    <label className={"text-sm font-medium text-gray-600 justify-self-start mr-2"}>
                        Buchungsdatum:
                    </label>
                    <input
                        className={"border border-gray-300 rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-400"}
                        type={"datetime-local"}
                        value={date}
                        onChange={event => setDate(event.target.value)}
                    />

                    <label className={"text-sm font-medium text-gray-600 justify-self-start mr-2"}>
                        Lieferant:
                    </label>
                    <select
                        className={"w-full border border-gray-300 rounded-xl px-4 py-2 text-gray-800 shadow-sm cursor-pointer hover:bg-white focus:bg-white focus:outline-none focus:ring-2 focus:ring-green-400"}
                        value={vendorId}
                        onChange={e => setVendorId(e.target.value)}>
                        <option value={""}>Bitte Lieferant auswählen</option>
                        {[...vendors]
                            .sort((a,b) => a.name.localeCompare(b.name))
                            .map(vendor => (
                            <option key={vendor.id} value={vendor.id}>
                                {vendor.name}
                            </option>
                        ))}
                    </select>

                    <label className={"text-sm font-medium text-gray-600 justify-self-start mr-2"}>
                        Betrag:
                    </label>
                    <input
                        className={"border border-gray-300 rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-400"}
                        type={"number"}
                        value={amount}
                        onChange={event => setAmount(event.target.value)}
                    />

                    <label className={"text-sm font-medium text-gray-600 justify-self-start mr-2"}>
                        Rechnungsnummer:
                    </label>
                    <input
                        className={"border border-gray-300 rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-400"}
                        type={"text"}
                        value={invoiceNumber}
                        onChange={event => setInvoiceNumber(event.target.value)}
                    />

                    <label className={"text-sm font-medium text-gray-600 justify-self-start mr-2"}>
                        Einzahlung auf:
                    </label>
                    <select
                        className={"w-full border border-gray-300 rounded-xl px-4 py-2 text-gray-800 shadow-sm cursor-pointer hover:bg-white focus:bg-white focus:outline-none focus:ring-2 focus:ring-green-400"}
                        value={accountType}
                        onChange={e => setAccountType(e.target.value)}>
                        <option value={""}>Bitte Kontotyp auswählen</option>
                        {accountTypes
                            .filter(type => type !== "INVENTORY")
                            .map(type => (
                            <option key={type} value={type}>
                                {getAccountTypeLable(type)}
                            </option>
                        ))}
                    </select>

                    <label className={"text-sm font-medium text-gray-600 justify-self-start mr-2"}>
                        Notiz:
                    </label>
                    <input
                        className={"border border-gray-300 rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-400"}
                        type={"text"}
                        value={note}
                        onChange={event => setNote(event.target.value)}
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

export default AccountBookingAddEdit;