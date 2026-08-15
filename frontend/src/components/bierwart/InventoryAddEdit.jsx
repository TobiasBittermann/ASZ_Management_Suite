import {useEffect, useState} from "react";
import {getCurrentLocalDateTime} from "../../utils/dateUtils.jsx";

function InventoryAddEdit({inventory, members, onClose, onSave}) {
    const [inventoryDate, setInventoryDate] = useState("");
    const [memberId, setMemberId] = useState("")
    const [finished, setFinished] = useState(false)
    const [note, setNote] = useState("")

    useEffect(() => {
        if (inventory) {
            setInventoryDate(inventory.date)
            setMemberId(inventory.memberId)
            setFinished(inventory.finished)
            setNote(inventory.note)
        } else {
            setInventoryDate(getCurrentLocalDateTime())
            setMemberId("")
            setFinished(false)
            setNote("")
        }
    }, [inventory]);

    async function handleSubmit(event) {
        event.preventDefault();

        const savedInventory = {
            id: inventory ? inventory.id : 0,
            date: inventoryDate,
            memberId: Number(memberId),
            finished: finished,
            note: note
        }

        await onSave(savedInventory);
        onClose();
    }

    return (
        <div className={"fixed inset-0 bg-black/50 flex items-center justify-center z-50"}>
            <div className={"bg-white rounded-2xl shadow-xl p-8 w-full max-w-md"}>

                <h2 className={"text-2xl font-bold text-gray-800 mb-6"}>
                    {inventory ? "Edit Inventory" : "Add Inventory"}
                </h2>

                <form
                    className={"grid grid-cols-[auto_1fr] items-center gap-x-4 gap-y-4"}
                    onSubmit={handleSubmit}>

                    <label className={"text-sm font-medium text-gray-600 justify-self-start mr-2"}>
                        Inventurdaatum:
                    </label>
                    <input
                        className={"border border-gray-300 rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-400"}
                        type={"datetime-local"}
                        value={inventoryDate}
                        onChange={event => setInventoryDate(event.target.value)}
                        disabled={true}
                    />

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

export default InventoryAddEdit;