import {useEffect, useState} from "react";
import {FiEdit3, FiPlusCircle, FiTrash2} from "react-icons/fi";
import SnapshotAddEdit from "../../components/bierwart/SnapshotAddEdit.jsx";
import {Tooltip} from "react-tooltip";
import {loadSnapshots} from "../../utils/loadUtils.jsx";
import {deleteEntity, saveEntity} from "../../utils/crudUtils.jsx";

function SnapshotsPage() {
    const [snapshots, setSnapshots] = useState([])
    const [selectedSnapshot, setSelectedSnapshot] = useState(null)
    const [isModalOpen, setIsModalOpen] = useState(false)

    useEffect(() => {
        loadSnapshots(setSnapshots);
    }, [])

    const latestSnapshot = snapshots.length > 0 ? snapshots.reduce((latest, snapshot) => snapshot.id > latest.id ? snapshot : latest) : null;

    async function handleSaveSnapshot(snapshot) {
        await saveEntity(snapshot, "/bwsnapshots", loadSnapshots, setSnapshots)
    }

    async function handleDeleteSnapshot(id) {
        await deleteEntity(id, "/bwsnapshots", loadSnapshots, setSnapshots)
    }

    function handleEditClick(snapshot) {
        setSelectedSnapshot(snapshot);
        setIsModalOpen(true);
    }

    function handleAddClick() {
        setSelectedSnapshot(null);
        setIsModalOpen(true);
    }

    return (
        <div>
            <h3 className={"text-3xl font-bold text-gray-800 text-center"}>
                Kassenstände
            </h3>

            <div className="flex justify-center gap-4 my-6">
                <div className={`px-4 py-2 rounded-full shadow ${
                    latestSnapshot?.bankAccount > 0
                        ? "bg-green-100 text-green-800"
                        : latestSnapshot?.bankAccount < 0
                            ? "bg-red-100 text.red-800"
                            : "bg-gray-100 bg-gray-800"}`}>
                    Bierkonto: {latestSnapshot?.bankAccount ?? "0.00"} €
                </div>

                <div className={`px-4 py-2 rounded-full shadow ${
                    latestSnapshot?.cashRegister > 0
                        ? "bg-green-100 text-green-800"
                        : latestSnapshot?.cashRegister < 0
                            ? "bg-red-100 text.red-800"
                            : "bg-gray-100 bg-gray-800"}`}>
                    Bierkasse: {latestSnapshot?.cashRegister ?? "0.00"} €
                </div>

                <div className={`px-4 py-2 rounded-full shadow ${
                    latestSnapshot?.inventoryValue > 0
                        ? "bg-green-100 text-green-800"
                        : latestSnapshot?.inventoryValue < 0
                            ? "bg-red-100 text.red-800"
                            : "bg-gray-100 bg-gray-800"}`}>
                    Inventar: {latestSnapshot?.inventoryValue ?? "0.00"} €
                </div>
            </div>

            <button
                className={"hover:bg-green-500 hover:scale-105 bg-green-300 text-black shadow-md justify-self-start rounded px-6 py-2 m-3 transition"}
                data-tooltip-id={"add-tip"}
                data-tooltip-content={"Add a new drink"}
                onClick={handleAddClick}>
                <FiPlusCircle/>
            </button>

            {
                isModalOpen && (
                    <SnapshotAddEdit
                        snapshot={selectedSnapshot}
                        onClose={() => setIsModalOpen(false)}
                        onSave={handleSaveSnapshot}/>
                )}

            <div className={"overflow-x-auto rounded-xl shadow"}>

                <table className={"min-w-full bg-white text-sm text-left"}>
                    <thead className={"bg-gray-200 text-gray-600 uppercase text-xs"}>
                    <tr>
                        <th className={"px-6 py-3"}>Id</th>
                        <th className={"px-6 py-3"}>Datum</th>
                        <th className={"px-6 py-3"}>Bierkonto</th>
                        <th className={"px-6 py-3"}>Bierkasse</th>
                        <th className={"px-6 py-3"}>Inventar</th>
                        <th className={"px-6 py-3"}>Kommantar</th>
                    </tr>
                    </thead>
                    <tbody className={"divide-y divide-gray-100"}>
                    {snapshots.map(snapshot => (
                        <tr key={snapshot.id} className={"hover:bg-gray-50 transition"}>
                            <td className={"px-6 py-3"}>{snapshot.id}</td>
                            <td className={"px-6 py-3"}>{snapshot.snapshotDate}</td>
                            <td className={"px-6 py-3"}>{snapshot.bankAccount}</td>
                            <td className={"px-6 py-3"}>{snapshot.cashRegister}</td>
                            <td className={"px-6 py-3"}>{snapshot.inventoryValue}</td>
                            <td className={"px-6 py-3"}>{snapshot.note}</td>
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

export default SnapshotsPage;