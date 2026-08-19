import {useEffect, useState} from "react";
import {loadInventories, loadInventoryEntries, loadMembers} from "../../utils/loadUtils.jsx";
import {deleteEntity, saveEntity} from "../../utils/crudUtils.jsx";
import {FiEdit3, FiPlusCircle, FiTrash2} from "react-icons/fi";
import {Tooltip} from "react-tooltip";
import InventoryAdd from "../../components/bierwart/InventoryAdd.jsx";
import {getMemberName} from "../../utils/namingUtils.jsx";
import InventoryEdit from "../../components/bierwart/InventoryEdit.jsx";
import InventoryShow from "../../components/bierwart/InventoryShow.jsx";
import {SlMagnifier} from "react-icons/sl";

function InventoryPage() {
    const [inventories, setInventories] = useState([])
    const [inventoryEntries, setInventoryEntries] = useState([])
    const [members, setMembers] = useState([])
    const [selectedInventory, setSelectedInventory] = useState(null)
    const [isModalAddOpen, setIsModalAddOpen] = useState(false)
    const [isModalEditOpen, setIsModalEditOpen] = useState(false)
    const [isModalShowOpen, setIsModalShowOpen] = useState(false)

    useEffect(() => {
        loadInventories(setInventories);
        loadMembers(setMembers);
    }, [])

    async function handleSaveInventory(inventory) {
        await saveEntity(inventory, "/inventories", loadInventories, setInventories);
    }

    async function handleSaveInventoryEntry(entry) {

        console.log("handleSaveInventoryEntry:", entry);
        await saveEntity(entry, "/inventoryentries", loadInventoryEntries, setInventoryEntries)
    }

    async function handleDeleteInventory(id) {
        await deleteEntity(id, "/inventories", loadInventories, setInventories);
    }

    function handleEditClick(inventory) {
        setSelectedInventory(inventory);
        setIsModalEditOpen(true);
    }

    function handleAddClick() {
        setSelectedInventory(null);
        setIsModalAddOpen(true);
    }

    function handleShowClick(inventory) {
        setSelectedInventory(inventory);
        setIsModalShowOpen(true);
    }

    return (
        <div>
            <h3 className={"text-3xl font-bold text-gray-800 text-center"}>
                Inventuren
            </h3>

            <button
                className={"hover:bg-green-500 hover:scale-105 bg-green-300 text-black shadow-md justify-self-start rounded px-6 py-2 m-3 transition"}
                data-tooltip-id={"add-tip"}
                data-tooltip-content={"Start a new inventory"}
                onClick={handleAddClick}>
                <FiPlusCircle/>
            </button>
            <Tooltip id={"add-tip"}/>

            {
                isModalAddOpen && (
                    <InventoryAdd
                        inventory={selectedInventory}
                        members={members}
                        onClose={() => setIsModalAddOpen(false)}
                        onSave={handleSaveInventory}/>
                )
            }

            {
                isModalEditOpen && (
                    <InventoryEdit
                        inventory={selectedInventory}
                        onClose={() => setIsModalEditOpen(false)}
                        onSave={handleSaveInventoryEntry}
                    />
                )
            }

            {
                isModalShowOpen && (
                    <InventoryShow
                        inventory={selectedInventory}
                        onClose={() => setIsModalShowOpen(false)}
                    />
                )
            }

            <div className={"overflow-x-auto rounded-xl shadow"}>

                <table className={"min-w-full bg-white text-sm text-left"}>
                    <thead className={"bg-gray-200 text-gray-600 uppercase text-xs"}>
                    <tr>
                        <th className={"px-6 py-3"}>Id</th>
                        <th className={"px-6 py-3"}>Datum</th>
                        <th className={"px-6 py-3"}>Member</th>
                        <th className={"px-6 py-3"}>Bemerkung</th>
                        <th className={"px-6 py-3"}>Beendet</th>
                        <th className={"px-6 py-3"}>Aktionen</th>
                    </tr>
                    </thead>
                    <tbody className={"divide-y divide-gray-100"}>
                    {inventories.map(inventory => (
                        <tr key={inventory.id} className={"hover:bg-gray-50 transition"}>
                            <td className={"px-6 py-3"}>{inventory.id}</td>
                            <td className={"px-6 py-3"}>{inventory.date}</td>
                            <td className={"px-6 py-3"}>{getMemberName(members, inventory.memberId)}</td>
                            <td className={"px-6 py-3"}>{inventory.note}</td>
                            <td className={"px-6 py-3"}>{inventory.finished ? "Ja" : "Nein"}</td>
                            <td>
                                <button
                                    className={"hover:bg-green-500 hover:scale-105 bg-green-300 text-black shadow-md rounded px-3 py-1 m-1 transition"}
                                    data-tooltip-id={"edit-tip"}
                                    data-tooltip-content={"Edit an inventory"}
                                    onClick={() => handleShowClick(inventory)}>
                                    <SlMagnifier/>
                                </button>
                                <button
                                    className={"hover:bg-green-500 hover:scale-105 bg-green-300 disabled:bg-gray-300 disabled:text-gray-500 disabled:hover:scale-100 disabled:hover:bg-gray-300 text-black shadow-md rounded px-3 py-1 m-1 transition"}
                                    data-tooltip-id={"edit-tip"}
                                    data-tooltip-content={"Edit an inventory"}
                                    onClick={() => handleEditClick(inventory)}
                                    disabled={inventory.finished}>
                                    <FiEdit3/>
                                </button>
                                <button
                                    className={"hover:bg-green-500 hover:scale-105 bg-green-300 text-black shadow-md rounded px-3 py-1 m-1 transition"}
                                    data-tooltip-id={"delete-tip"}
                                    data-tooltip-content={"Delete an inventory"}
                                    onClick={() => handleDeleteInventory(inventory.id)}>
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

export default InventoryPage;